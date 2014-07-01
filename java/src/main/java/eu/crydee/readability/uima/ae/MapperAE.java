package eu.crydee.readability.uima.ae;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.SetMultimap;
import eu.crydee.ahocorasick.AhoCorasick;
import eu.crydee.readability.uima.model.Mapped;
import eu.crydee.readability.uima.model.Metadata;
import eu.crydee.readability.uima.res.ReadabilityDict;
import eu.crydee.readability.uima.ts.Revision;
import eu.crydee.readability.uima.ts.Revisions;
import eu.crydee.readability.uima.ts.Suggestion;
import eu.crydee.readability.uima.ts.Token;
import eu.crydee.readability.uima.ts.TxtRevisions;
import eu.crydee.readability.uima.ts.TxtSuggestion;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
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
import org.apache.uima.util.Logger;

public class MapperAE extends JCasAnnotator_ImplBase {

    final static private Logger logger = UIMAFramework.getLogger(
            MapperAE.class);

    final static public String RES_KEY = "KEY";
    @ExternalResource(key = RES_KEY)
    private ReadabilityDict dict;

    final static public String PARAM_LIMIT = "LIMIT";
    @ConfigurationParameter(name = PARAM_LIMIT, mandatory = false)
    private Integer limit;

//    final private SetMultimap<List<String>, Pair<Mapped, Metadata>> byTxt
//            = HashMultimap.create();
    private Entry<Mapped, Map<Mapped, Metadata>>[] mappedArray;

    private AhoCorasick<Pair<String, String>> ac;

    @Override
    public void initialize(UimaContext context)
            throws ResourceInitializationException {
        super.initialize(context);
        mappedArray = new Entry[dict.entrySet().size()];
        List<Pair<String, String>[]> patterns = new ArrayList<>();
        int k = -1;
        for (Entry<Mapped, Map<Mapped, Metadata>> e : dict.entrySet()) {
            ++k;
            Mapped original = e.getKey();
            List<String> tokens = original.getTokens(), pos = original.getPos();
            int size = tokens.size();
            if (size != pos.size()) {
                continue;
            }
            Pair<String, String>[] pattern = new Pair[size];
            for (int i = 0, s = tokens.size(); i < s; ++i) {
                pattern[i] = Pair.of(tokens.get(i), pos.get(i));
            }
            mappedArray[k] = e;
            patterns.add(pattern);
//            Map<Mapped, Metadata> revisedM = dict.getRevisions(original).get();
//            for (Mapped revised : revisedM.keySet()) {
//                Pair<Mapped, Metadata> pair
//                        = Pair.of(revised, revised.get(revised));
//                byTxt.put(tokens, pair);
//            }
        }
        ac = new AhoCorasick<>(patterns);
    }

    @Override
    public void process(JCas jcas) throws AnalysisEngineProcessException {
        List<Token> tokens
                = new ArrayList<>(JCasUtil.select(jcas, Token.class));
        List<Pair<Integer, Integer>> positions = tokens.stream()
                .map(t -> Pair.of(t.getBegin(), t.getEnd()))
                .collect(Collectors.toList());
        Pair<String, String>[] text = tokens.stream()
                .map(t -> Pair.of(t.getCoveredText(), t.getPOS()))
                .toArray(length -> new Pair[length]);
        SetMultimap<Integer, Integer> suggestions = ac.search(text, true);
        for (Integer mappedIndex : suggestions.keySet()) {
            Map<Mapped, Metadata> revisedMap
                    = mappedArray[mappedIndex].getValue();
            Set<Integer> ends = suggestions.get(mappedIndex);
            int width = mappedArray[mappedIndex].getKey().getTokens().size();
            Set<Entry<Mapped, Metadata>> revisedSet;
            if (limit != null) {
                List<Entry<Mapped, Metadata>> a
                        = getTop(revisedMap, Metadata::getScoreOcc, limit),
                        b = getTop(revisedMap, Metadata::getScoreLM, limit),
                        c = getTop(revisedMap, Metadata::getScoreLMN, limit),
                        d = getTop(revisedMap, Metadata::getScoreLMW, limit),
                        e = getTop(revisedMap, Metadata::getScoreLMWN, limit);
                revisedSet = new HashSet<>(a);
                revisedSet.addAll(b);
                revisedSet.addAll(c);
                revisedSet.addAll(d);
                revisedSet.addAll(e);
            } else {
                revisedSet = revisedMap.entrySet();
            }
            Revisions revisions = new TxtRevisions(jcas);
            revisions.setId(UUID.randomUUID().toString());
            revisions.setRevisions(new FSArray(jcas, revisedSet.size()));
            int k = -1;
            for (Entry<Mapped, Metadata> entry : revisedSet) {
                ++k;
                Mapped mapped = entry.getKey();
                Metadata metrics = entry.getValue();
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
            for (Integer i : ends) {
                int begin = positions.get(i - width + 1).getLeft(),
                        end = positions.get(i).getRight();
                Suggestion suggestion;
                suggestion = new TxtSuggestion(jcas, begin, end);
                suggestion.setRevisions(revisions);
                jcas.addFsToIndexes(suggestion);
            }
        }
    }

    private static List<Entry<Mapped, Metadata>> getTop(
            Map<Mapped, Metadata> m,
            Function<Metadata, Double> scoreGetter,
            int n) {
        return m.entrySet().stream()
                .sorted((Entry<Mapped, Metadata> o1,
                                Entry<Mapped, Metadata> o2)
                        -> Double.compare(
                                scoreGetter.apply(o2.getValue()),
                                scoreGetter.apply(o1.getValue())))
                .limit(n)
                .collect(Collectors.toList());
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
