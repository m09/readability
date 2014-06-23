package eu.crydee.readability.uima.ae;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import eu.crydee.readability.uima.model.Mapped;
import eu.crydee.readability.uima.model.Metrics;
import eu.crydee.readability.uima.res.ReadabilityDict;
import eu.crydee.readability.uima.ts.PosRevisions;
import eu.crydee.readability.uima.ts.PosSuggestion;
import eu.crydee.readability.uima.ts.Revision;
import eu.crydee.readability.uima.ts.Revisions;
import eu.crydee.readability.uima.ts.Suggestion;
import eu.crydee.readability.uima.ts.Token;
import eu.crydee.readability.uima.ts.TxtRevisions;
import eu.crydee.readability.uima.ts.TxtSuggestion;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;
import java.util.stream.Collectors;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.uima.UIMAFramework;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.descriptor.ExternalResource;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.DoubleArray;
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

    final static public String PARAM_LIMIT = "LIMIT";
    @ConfigurationParameter(name = PARAM_LIMIT, mandatory = false)
    private Integer limit;

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
                TxtRevisions.class,
                positions,
                txtTokens,
                byTxt);
        annotateFromMap(
                jcas,
                PosSuggestion.class,
                PosRevisions.class,
                positions,
                posTokens,
                byPos);
    }

    private void annotateFromMap(
            JCas jcas,
            Class<? extends Suggestion> suggestionClass,
            Class<? extends Revisions> revisionsClass,
            List<Pair<Integer, Integer>> positions,
            List<String> tokens,
            SetMultimap<List<String>, Pair<Mapped, Metrics>> m)
            throws AnalysisEngineProcessException {
        for (List<String> suggestionTokens : m.keySet()) {
            int width = suggestionTokens.size();
            Set<Pair<Mapped, Metrics>> revisedSet = m.get(suggestionTokens);
            List<Integer> starts = getSublistIndices(tokens, suggestionTokens);
            if (starts.isEmpty()) {
                continue;
            }
            if (limit != null) {
                SortedSet<Pair<Mapped, Metrics>> revisedSortedOcc
                        = getTop(revisedSet,
                                (Pair<Mapped, Metrics> o1,
                                        Pair<Mapped, Metrics> o2)
                                -> Double.compare(
                                        o2.getValue().getScoreOcc(),
                                        o1.getValue().getScoreOcc()),
                                limit == null ? Integer.MAX_VALUE : limit),
                        revisedSortedLM
                        = getTop(revisedSet,
                                (Pair<Mapped, Metrics> o1,
                                        Pair<Mapped, Metrics> o2)
                                -> Double.compare(
                                        o2.getValue().getScoreLM(),
                                        o1.getValue().getScoreLM()),
                                limit == null ? Integer.MAX_VALUE : limit),
                        revisedSortedLMN
                        = getTop(revisedSet,
                                (Pair<Mapped, Metrics> o1,
                                        Pair<Mapped, Metrics> o2)
                                -> Double.compare(
                                        o2.getValue().getScoreLMN(),
                                        o1.getValue().getScoreLMN()),
                                limit == null ? Integer.MAX_VALUE : limit),
                        revisedSortedLMW
                        = getTop(revisedSet,
                                (Pair<Mapped, Metrics> o1,
                                        Pair<Mapped, Metrics> o2)
                                -> Double.compare(
                                        o2.getValue().getScoreLMW(),
                                        o1.getValue().getScoreLMW()),
                                limit == null ? Integer.MAX_VALUE : limit),
                        revisedSortedLMWN
                        = getTop(revisedSet,
                                (Pair<Mapped, Metrics> o1,
                                        Pair<Mapped, Metrics> o2)
                                -> Double.compare(
                                        o2.getValue().getScoreLMWN(),
                                        o1.getValue().getScoreLMWN()),
                                limit == null ? Integer.MAX_VALUE : limit);
                revisedSet = new HashSet<>(revisedSortedOcc);
                revisedSet.addAll(revisedSortedLM);
                revisedSet.addAll(revisedSortedLMN);
                revisedSet.addAll(revisedSortedLMW);
                revisedSet.addAll(revisedSortedLMWN);
            }
            Revisions revisions;
            try {
                revisions = revisionsClass.getConstructor(JCas.class)
                        .newInstance(jcas);
            } catch (InstantiationException |
                    IllegalAccessException |
                    IllegalArgumentException |
                    InvocationTargetException |
                    NoSuchMethodException |
                    SecurityException ex) {
                logger.log(
                        Level.SEVERE,
                        "couldn't create a revisions annotation.",
                        ex);
                throw new AnalysisEngineProcessException(ex);
            }
            revisions.setId(UUID.randomUUID().toString());
            revisions.setRevisions(new FSArray(jcas, revisedSet.size()));
            int k = -1;
            for (Pair<Mapped, Metrics> pair : revisedSet) {
                ++k;
                Mapped mapped = pair.getKey();
                Metrics metrics = pair.getRight();
                String[] toks = mapped.getTokens().toArray(new String[0]);
                StringArray saToks = new StringArray(jcas, toks.length);
                saToks.copyFromArray(toks, 0, 0, toks.length);
                Revision revision = new Revision(jcas);
                revision.setTokens(saToks);
                revision.setCount(metrics.getCount());
                revision.setScore(new DoubleArray(jcas, 5));
                revision.setScore(0, metrics.getScoreOcc());
                revision.setScore(1, metrics.getScoreLM());
                revision.setScore(2, metrics.getScoreLMN());
                revision.setScore(3, metrics.getScoreLMW());
                revision.setScore(4, metrics.getScoreLMWN());
                revision.setText(mapped.getText());
                revisions.setRevisions(k, revision);
            }
            revisions.addToIndexes();
            for (Integer i : starts) {
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

    private static <T> SortedSet<T> getTop(Set<T> s, Comparator<T> c, int n) {
        return s.stream()
                .limit(n)
                .collect(Collectors.toCollection(() -> new TreeSet<>(c)));
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
