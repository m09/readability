package eu.crydee.readability.uima.ae;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import eu.crydee.readability.uima.model.Mapped;
import eu.crydee.readability.uima.model.Metrics;
import eu.crydee.readability.uima.res.ReadabilityDict;
import eu.crydee.readability.uima.ts.PosSuggestion;
import eu.crydee.readability.uima.ts.Revision;
import eu.crydee.readability.uima.ts.Revisions;
import eu.crydee.readability.uima.ts.Suggestion;
import eu.crydee.readability.uima.ts.Token;
import eu.crydee.readability.uima.ts.TxtSuggestion;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.uima.UIMAFramework;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ExternalResource;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.jcas.cas.StringArray;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Level;
import org.apache.uima.util.Logger;

public class MapperAE extends JCasAnnotator_ImplBase {

    final static private Logger logger = UIMAFramework.getLogger(
            MapperAE.class);

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
    public void process(JCas jcas) throws AnalysisEngineProcessException {
        List<Token> tokens
                = new ArrayList<>(JCasUtil.select(jcas, Token.class));
        List<String> txtTokens = tokens.stream()
                .map(Token::getCoveredText)
                .collect(Collectors.toList()),
                posTokens = tokens.stream()
                .map(Token::getPOS)
                .collect(Collectors.toList());
        List<Pair<Integer, Integer>> positions = tokens.stream()
                .map(t -> Pair.of(t.getBegin(), t.getEnd()))
                .collect(Collectors.toList());
        annotateFromMap(
                jcas,
                TxtSuggestion.class,
                positions,
                txtTokens,
                byTxt);
        annotateFromMap(
                jcas,
                PosSuggestion.class,
                positions,
                posTokens,
                byPos);
    }

    private void annotateFromMap(
            JCas jcas,
            Class<? extends Suggestion> suggestionClass,
            List<Pair<Integer, Integer>> positions,
            List<String> tokens,
            SetMultimap<List<String>, Pair<Mapped, Metrics>> m)
            throws AnalysisEngineProcessException {
        for (List<String> suggestionTokens : m.keySet()) {
            int width = suggestionTokens.size();
            Set<Pair<Mapped, Metrics>> revisedSet = m.get(suggestionTokens);
            Revisions revisions = new Revisions(jcas);
            revisions.setRevisions(new FSArray(jcas, revisedSet.size()));
            int k = 0;
            for (Pair<Mapped, Metrics> pair : revisedSet) {
                Mapped mapped = pair.getKey();
                Metrics metrics = pair.getRight();
                String[] toks = mapped.getTokens().toArray(new String[0]);
                StringArray saToks = new StringArray(jcas, toks.length);
                saToks.copyFromArray(toks, 0, 0, toks.length);
                Revision revision = new Revision(jcas);
                revision.setId(UUID.randomUUID().toString());
                revision.setTokens(saToks);
                revision.setCount(metrics.getCount());
                revision.setScore(metrics.getScore());
                revision.setText(mapped.getText());
                revisions.setRevisions(k++, revision);
            }
            for (Integer i : getSublistIndices(tokens, suggestionTokens)) {
                int begin = positions.get(i).getLeft(),
                        end = positions.get(i + width - 1).getRight();
                try {
                    Suggestion suggestion;
                    suggestion = suggestionClass.getConstructor(
                            JCas.class,
                            int.class,
                            int.class)
                            .newInstance(jcas, begin, end);
                    suggestion.setRevisions(revisions);
                    jcas.addFsToIndexes(suggestion);

                } catch (NoSuchMethodException |
                        SecurityException |
                        InstantiationException |
                        IllegalAccessException |
                        IllegalArgumentException |
                        InvocationTargetException ex) {
                    logger.log(
                            Level.SEVERE,
                            "couldn't create a suggestion annotation.",
                            ex);
                    throw new AnalysisEngineProcessException(ex);
                }
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
