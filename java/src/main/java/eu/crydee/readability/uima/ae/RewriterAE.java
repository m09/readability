package eu.crydee.readability.uima.ae;

import com.google.common.collect.TreeMultimap;
import eu.crydee.readability.uima.model.LogWeight;
import eu.crydee.readability.uima.model.Transducer;
import eu.crydee.readability.uima.model.Weight;
import eu.crydee.readability.uima.ts.Original;
import eu.crydee.readability.uima.ts.Revised;
import eu.crydee.readability.uima.ts.Rewriting;
import eu.crydee.readability.uima.ts.Rewritings;
import eu.crydee.readability.uima.ts.Token;
import eu.crydee.readability.uima.ts.TxtSuggestion;
import java.util.Map.Entry;
import org.apache.uima.UIMAFramework;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.text.AnnotationFS;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.util.Logger;

public class RewriterAE extends JCasAnnotator_ImplBase {

    private static final Logger logger = UIMAFramework.getLogger(
            RewriterAE.class);

    public static final int LIMIT = 20;

    private final Weight weight = new LogWeight();

    @Override
    public void process(JCas jcas) throws AnalysisEngineProcessException {
        Transducer transitions = new Transducer(weight);
        int previousEnd = 0, txtLength = jcas.getDocumentText().length();
        for (AnnotationFS token : JCasUtil.select(jcas, Token.class)) {
            int begin = token.getBegin(),
                    end = token.getEnd();
            if (previousEnd < begin) {
                transitions.put(
                        previousEnd,
                        begin,
                        weight.getUnit(),
                        jcas.getDocumentText().substring(previousEnd, begin));
            }
            transitions.put(
                    begin,
                    end,
                    weight.getUnit(),
                    token.getCoveredText());
            previousEnd = end;
        }
        if (previousEnd < txtLength) {
            transitions.put(
                    previousEnd,
                    txtLength,
                    weight.getUnit(),
                    jcas.getDocumentText().substring(previousEnd, txtLength));
        }
        for (TxtSuggestion sugg : JCasUtil.select(jcas, TxtSuggestion.class)) {
            Original original = sugg.getOriginal();
            int begin = original.getBegin(),
                    end = original.getEnd();
            for (int i = 0, s = sugg.getRevised().size();
                    i < s && i < LIMIT;
                    i++) {
                Revised revised = sugg.getRevised(i);
                transitions.put(
                        begin,
                        end,
                        revised.getScore(),
                        revised.getText());
            }
        }
        TreeMultimap<Double, String> rewritings = transitions.top(LIMIT);
        if (!rewritings.isEmpty()) {
            Rewritings rewritingsAnn = new Rewritings(
                    jcas,
                    0,
                    txtLength);
            FSArray rewritingsA = new FSArray(
                    jcas,
                    rewritings.size());
            rewritingsAnn.setRewritings(rewritingsA);
            int i = 0;
            for (Entry<Double, String> rewriting : rewritings.entries()) {
                Rewriting rewritingAnn = new Rewriting(jcas);
                rewritingAnn.setRewriting(rewriting.getValue());
                rewritingAnn.setScore(rewriting.getKey());
                rewritingsAnn.setRewritings(i, rewritingAnn);
                ++i;
            }
            jcas.addFsToIndexes(rewritingsAnn);
        }
    }
}
