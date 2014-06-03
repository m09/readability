package eu.crydee.readability.uima.ae;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import eu.crydee.readability.uima.model.Mapped;
import eu.crydee.readability.uima.model.Metrics;
import eu.crydee.readability.uima.res.ReadabilityDict;
import eu.crydee.readability.uima.ts.Original;
import eu.crydee.readability.uima.ts.PosSuggestion;
import eu.crydee.readability.uima.ts.Revised;
import eu.crydee.readability.uima.ts.SimplePosToken;
import eu.crydee.readability.uima.ts.SimpleTextToken;
import eu.crydee.readability.uima.ts.Suggestion;
import eu.crydee.readability.uima.ts.Token;
import eu.crydee.readability.uima.ts.TxtSuggestion;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.cas.Feature;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.TypeSystem;
import org.apache.uima.cas.text.AnnotationFS;
import org.apache.uima.fit.component.CasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ExternalResource;
import org.apache.uima.fit.util.CasUtil;
import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.jcas.cas.StringArray;
import org.apache.uima.resource.ResourceInitializationException;

public class MapperAE extends CasAnnotator_ImplBase {

    final static public String RES_TXT = "RES_TXT";
    @ExternalResource(key = RES_TXT)
    private ReadabilityDict dictTxt;

    final static public String RES_POS = "RES_POS";
    @ExternalResource(key = RES_POS)
    private ReadabilityDict dictPos;

    final private SetMultimap<List<String>, Pair<Mapped, Metrics>> byTxt
            = HashMultimap.create();

    final private SetMultimap<List<String>, Pair<Mapped, Metrics>> byPos
            = HashMultimap.create();

    private Type tokenT,
            simpleTextTokenT,
            simplePosTokenT,
            suggestionT,
            suggestionTxtT,
            suggestionPosT;

    private Feature tokenPosF,
            simplePosTokenF,
            suggestionOriF,
            suggestionRevF;

    @Override
    public void initialize(UimaContext context)
            throws ResourceInitializationException {
        super.initialize(context);
        initializeMapFromDict(dictTxt, byTxt);
        initializeMapFromDict(dictPos, byPos);
    }

    private void initializeMapFromDict(
            ReadabilityDict d,
            SetMultimap<List<String>, Pair<Mapped, Metrics>> m) {
        for (Mapped original : d.keySet()) {
            List<String> tokens = original.getTokens();
            Map<Mapped, Metrics> revisedMap
                    = d.getRevisions(original).get();
            for (Mapped revised : revisedMap.keySet()) {
                Pair<Mapped, Metrics> pair
                        = Pair.of(revised, revisedMap.get(revised));
                m.put(tokens, pair);
            }
        }
    }

    @Override
    public void typeSystemInit(TypeSystem aTypeSystem)
            throws AnalysisEngineProcessException {
        super.typeSystemInit(aTypeSystem);
        tokenT = aTypeSystem.getType(Token.class.getCanonicalName());
        tokenPosF = tokenT.getFeatureByBaseName("POS");
        simpleTextTokenT
                = aTypeSystem.getType(SimpleTextToken.class.getCanonicalName());
        simplePosTokenT
                = aTypeSystem.getType(SimplePosToken.class.getCanonicalName());
        simplePosTokenF = simplePosTokenT.getFeatureByBaseName("POS");
        suggestionT = aTypeSystem.getType(Suggestion.class.getCanonicalName());
        suggestionPosT
                = aTypeSystem.getType(PosSuggestion.class.getCanonicalName());
        suggestionTxtT
                = aTypeSystem.getType(TxtSuggestion.class.getCanonicalName());
        suggestionOriF = suggestionT.getFeatureByBaseName("original");
        suggestionRevF = suggestionT.getFeatureByBaseName("revised");
    }

    @Override
    public void process(CAS aCas) throws AnalysisEngineProcessException {
        List<AnnotationFS> tokens
                = new ArrayList<>(CasUtil.select(aCas, tokenT));
        List<String> txtTokens = tokens.stream()
                .map(t -> t.getCoveredText())
                .collect(Collectors.toList()),
                posTokens = tokens.stream()
                .map(t -> t.getFeatureValueAsString(tokenPosF))
                .collect(Collectors.toList());
        AnnotationFS[] txtTokensArray = tokens.stream()
                .map(t -> aCas.createAnnotation(
                                simpleTextTokenT,
                                t.getBegin(),
                                t.getEnd()))
                .collect(Collectors.toList())
                .toArray(new AnnotationFS[0]),
                posTokensArray = tokens.stream()
                .map(t -> {
                    AnnotationFS ann = aCas.createAnnotation(
                            simplePosTokenT,
                            t.getBegin(),
                            t.getEnd());
                    ann.setFeatureValueFromString(
                            simplePosTokenF,
                            t.getFeatureValueAsString(tokenPosF));
                    return ann;
                })
                .collect(Collectors.toList())
                .toArray(new AnnotationFS[0]);
        List<Pair<Integer, Integer>> positions = tokens.stream()
                .map(t -> Pair.of(t.getBegin(), t.getEnd()))
                .collect(Collectors.toList());
        String text = aCas.getDocumentText();
        try {
            annotateFromMap(
                    text,
                    aCas,
                    suggestionTxtT,
                    positions,
                    txtTokens,
                    txtTokensArray,
                    byTxt);
            annotateFromMap(
                    text,
                    aCas,
                    suggestionPosT,
                    positions,
                    posTokens,
                    posTokensArray,
                    byPos);
        } catch (CASException ex) {
            throw new AnalysisEngineProcessException(ex);
        }
    }

    private void annotateFromMap(
            String text,
            CAS aCas,
            Type suggestionT,
            List<Pair<Integer, Integer>> positions,
            List<String> tokens,
            AnnotationFS[] annotations,
            SetMultimap<List<String>, Pair<Mapped, Metrics>> m)
            throws CASException {
        for (List<String> suggestionTokens : m.keySet()) {
            int width = suggestionTokens.size();
            for (Integer i : getSublistIndices(tokens, suggestionTokens)) {
                int begin = positions.get(i).getLeft(),
                        end = positions.get(i + width - 1).getRight();
                AnnotationFS suggestion = aCas.createAnnotation(
                        suggestionT,
                        begin,
                        end);
                Original original = new Original(aCas.getJCas(), begin, end);
                FSArray fsa = new FSArray(aCas.getJCas(), width);
                fsa.copyFromArray(annotations, i, 0, width);
                original.setTokens(fsa);
                original.setText(text.substring(begin, end));
                suggestion.setFeatureValue(suggestionOriF, original);
                Set<Pair<Mapped, Metrics>> revisedSet
                        = m.get(suggestionTokens);
                FSArray revisedArray
                        = new FSArray(aCas.getJCas(), revisedSet.size());
                int s = 0;
                for (Pair<Mapped, Metrics> pair : revisedSet) {
                    Mapped rev = pair.getKey();
                    int revisedWidth = rev.getTokens().size();
                    Revised revised = new Revised(aCas.getJCas(), begin, end);
                    revised.setCount(pair.getValue().count);
                    StringArray sa
                            = new StringArray(aCas.getJCas(), revisedWidth);
                    sa.copyFromArray(
                            rev.getTokens().toArray(new String[0]),
                            0,
                            0,
                            revisedWidth);
                    revised.setTokens(sa);
                    revised.setText(rev.getText());
                    revisedArray.set(s++, revised);
                }
                suggestion.setFeatureValue(suggestionRevF, revisedArray);
                aCas.addFsToIndexes(suggestion);
            }
        }

    }

    private List<Integer> getSublistIndices(
            List<String> source,
            List<String> target) {
        List<Integer> result = new ArrayList<>();
        for (int i = 0, s = source.size(), t = target.size(), d = s - t;
                i < d;
                i++) {
            if (source.subList(i, i + t).equals(target)) {
                result.add(i);
            }
        }
        return result;
    }
}
