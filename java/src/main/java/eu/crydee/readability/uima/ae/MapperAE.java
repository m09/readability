package eu.crydee.readability.uima.ae;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import eu.crydee.readability.uima.model.Mapped;
import eu.crydee.readability.uima.model.Metrics;
import eu.crydee.readability.uima.res.Mappings;
import eu.crydee.readability.uima.res.ReadabilityDict;
import eu.crydee.readability.uima.ts.PosSuggestion;
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

    final static public String RES_MAPPINGS = "RES_MAPPINGS";
    @ExternalResource(key = RES_MAPPINGS)
    private Mappings mappings;

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
            UUID id = mappings.freeId();
            for (Integer i : getSublistIndices(tokens, suggestionTokens)) {
                int begin = positions.get(i).getLeft(),
                        end = positions.get(i + width - 1).getRight();
                Set<Pair<Mapped, Metrics>> revisedSet = m.get(suggestionTokens);
                if (!revisedSet.isEmpty()) {
                    try {
                        Suggestion suggestion;
                        suggestion = suggestionClass.getConstructor(
                                JCas.class,
                                int.class,
                                int.class)
                                .newInstance(jcas, begin, end);
                        suggestion.setId(id.toString());
                        jcas.addFsToIndexes(suggestion);
                        for (Pair<Mapped, Metrics> pair : revisedSet) {
                            mappings.putRevision(
                                    id,
                                    pair.getKey(),
                                    pair.getValue());
                        }
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
