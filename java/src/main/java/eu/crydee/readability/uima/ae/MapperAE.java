package eu.crydee.readability.uima.ae;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import eu.crydee.readability.uima.model.Mapped;
import eu.crydee.readability.uima.model.Metrics;
import eu.crydee.readability.uima.res.ReadabilityDict;
import eu.crydee.readability.uima.ts.Original;
import eu.crydee.readability.uima.ts.PosSuggestion;
import eu.crydee.readability.uima.ts.Revised;
import eu.crydee.readability.uima.ts.Suggestion;
import eu.crydee.readability.uima.ts.Token;
import eu.crydee.readability.uima.ts.TxtSuggestion;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
        Token[] txtTokensArray = tokens.stream()
                .map(t -> new Token(jcas,
                                t.getBegin(),
                                t.getEnd()))
                .collect(Collectors.toList())
                .toArray(new Token[0]),
                posTokensArray = tokens.toArray(new Token[0]);
        List<Pair<Integer, Integer>> positions = tokens.stream()
                .map(t -> Pair.of(t.getBegin(), t.getEnd()))
                .collect(Collectors.toList());
        String text = jcas.getDocumentText();
        annotateFromMap(
                text,
                jcas,
                TxtSuggestion.class,
                positions,
                txtTokens,
                txtTokensArray,
                byTxt);
        annotateFromMap(
                text,
                jcas,
                PosSuggestion.class,
                positions,
                posTokens,
                posTokensArray,
                byPos);
    }

    private void annotateFromMap(
            String text,
            JCas jcas,
            Class<? extends Suggestion> suggestionClass,
            List<Pair<Integer, Integer>> positions,
            List<String> tokens,
            Token[] annotations,
            SetMultimap<List<String>, Pair<Mapped, Metrics>> m)
            throws AnalysisEngineProcessException {
        for (List<String> suggestionTokens : m.keySet()) {
            int width = suggestionTokens.size();
            for (Integer i : getSublistIndices(tokens, suggestionTokens)) {
                int begin = positions.get(i).getLeft(),
                        end = positions.get(i + width - 1).getRight();

                Suggestion suggestion = null;
                try {
                    suggestion = suggestionClass
                            .getConstructor(JCas.class, int.class, int.class)
                            .newInstance(jcas, begin, end);
                } catch (NoSuchMethodException |
                        SecurityException |
                        InstantiationException |
                        IllegalAccessException |
                        IllegalArgumentException |
                        InvocationTargetException ex) {
                    logger.log(
                            Level.SEVERE,
                            "coudln't instantiate a suggestion annotation",
                            ex);
                    throw new AnalysisEngineProcessException(ex);
                }
                Original original = new Original(jcas, begin, end);
                FSArray fsa = new FSArray(jcas, width);
                fsa.copyFromArray(annotations, i, 0, width);
                original.setTokens(fsa);
                original.setText(text.substring(begin, end));
                suggestion.setOriginal(original);
                Set<Pair<Mapped, Metrics>> revisedSet = m.get(suggestionTokens);
                suggestion.setRevised(new FSArray(jcas, revisedSet.size()));
                int s = 0;
                for (Pair<Mapped, Metrics> pair : revisedSet) {
                    Mapped rev = pair.getKey();
                    int revisedWidth = rev.getTokens().size();
                    Revised revised = new Revised(jcas, begin, end);
                    revised.setCount(pair.getValue().count);
                    revised.setScore(pair.getValue().score);
                    StringArray sa
                            = new StringArray(jcas, revisedWidth);
                    sa.copyFromArray(
                            rev.getTokens().toArray(new String[0]),
                            0,
                            0,
                            revisedWidth);
                    revised.setTokens(sa);
                    revised.setText(rev.getText());
                    suggestion.setRevised(s++, revised);
                }
                jcas.addFsToIndexes(suggestion);
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
