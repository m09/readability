package eu.crydee.readability.uima.ae;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import eu.crydee.readability.uima.model.POSs;
import eu.crydee.readability.uima.model.Revision;
import eu.crydee.readability.uima.model.Tokens;
import eu.crydee.readability.uima.res.ReadabilityDict;
import eu.crydee.readability.uima.ts.Suggestion;
import eu.crydee.readability.uima.ts.Token;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ExternalResource;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.jcas.cas.StringArray;
import org.apache.uima.resource.ResourceInitializationException;

public class MapperAE extends JCasAnnotator_ImplBase {

    final static public String RES_KEY = "RES_KEY";
    @ExternalResource(key = RES_KEY)
    private ReadabilityDict dict;

    final private SetMultimap<Tokens, Pair<Revision, Integer>> byTokens
            = HashMultimap.create();

    final private SetMultimap<POSs, Pair<Revision, Integer>> byPOS
            = HashMultimap.create();

    @Override
    public void initialize(UimaContext context)
            throws ResourceInitializationException {
        super.initialize(context);
        for (Revision original : dict.keySet()) {
            POSs pos = original.getPOS();
            Tokens tokens = original.getTokens();
            Map<Revision, Integer> revisedMap
                    = dict.getRevisions(original).get();
            for (Revision revised : revisedMap.keySet()) {
                Pair<Revision, Integer> pair
                        = Pair.of(revised, revisedMap.get(revised));
                byTokens.put(tokens, pair);
                byPOS.put(pos, pair);
            }
        }
    }

    @Override
    public void process(JCas jcas) throws AnalysisEngineProcessException {
        List<Token> tokens
                = new ArrayList<>(JCasUtil.select(jcas, Token.class));
        List<String> tokensText = tokens.stream()
                .map(t -> t.getCoveredText())
                .collect(Collectors.toList()),
                POSsText = tokens.stream()
                .map(t -> t.getPOS())
                .collect(Collectors.toList());
        String[] tokensArray = tokensText.toArray(new String[0]),
                POSsArray = POSsText.toArray(new String[0]);
        String text = jcas.getDocumentText();
        for (Tokens suggestionTokens : byTokens.keySet()) {
            int width = suggestionTokens.size();
            for (Integer i : getSublistIndices(tokensText, suggestionTokens)) {
                int begin = tokens.get(i).getBegin(),
                        end = tokens.get(i + width - 1).getEnd();
                Suggestion suggestion = new Suggestion(jcas, begin, end);
                eu.crydee.readability.uima.ts.Revision original
                        = new eu.crydee.readability.uima.ts.Revision(
                                jcas,
                                begin,
                                end);
                StringArray sa = new StringArray(jcas, width);
                sa.copyFromArray(tokensArray, i, 0, width);
                original.setTokens(sa);
                sa = new StringArray(jcas, width);
                sa.copyFromArray(POSsArray, i, 0, width);
                original.setPos(sa);
                original.setText(text.substring(begin, end));
                suggestion.setOriginal(original);
                Set<Pair<Revision, Integer>> revisedSet
                        = byTokens.get(suggestionTokens);
                FSArray revisedArray = new FSArray(jcas, revisedSet.size());
                int s = 0;
                for (Pair<Revision, Integer> pair : revisedSet) {
                    Revision rev = pair.getKey();
                    int revisedWidth = rev.getTokens().size();
                    eu.crydee.readability.uima.ts.Revision revised
                            = new eu.crydee.readability.uima.ts.Revision(
                                    jcas,
                                    begin,
                                    end);
                    sa = new StringArray(jcas, revisedWidth);
                    sa.copyFromArray(
                            rev.getPOS().toArray(new String[0]),
                            0,
                            0,
                            revisedWidth);
                    revised.setPos(sa);
                    sa = new StringArray(jcas, revisedWidth);
                    sa.copyFromArray(
                            rev.getTokens().toArray(new String[0]),
                            0,
                            0,
                            revisedWidth);
                    revised.setTokens(sa);
                    revised.setText(rev.getText());
                    revisedArray.set(s++, revised);
                }
                suggestion.setRevised(revisedArray);
                jcas.addFsToIndexes(suggestion);
            }
        }
    }

    private List<Integer> getSublistIndices(
            List<String> source,
            Tokens target) {
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
