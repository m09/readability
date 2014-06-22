package eu.crydee.readability.uima.ae;

import eu.crydee.readability.uima.model.LogWeight;
import eu.crydee.readability.uima.model.Transducer;
import eu.crydee.readability.uima.model.Transducer.Span;
import eu.crydee.readability.uima.model.Weight;
import eu.crydee.readability.uima.ts.Rewriting;
import eu.crydee.readability.uima.ts.RewritingSpan;
import eu.crydee.readability.uima.ts.Rewritings;
import eu.crydee.readability.uima.ts.Token;
import eu.crydee.readability.uima.ts.TxtSuggestion;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.uima.UIMAFramework;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
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
        for (Token token : JCasUtil.select(jcas, Token.class)) {
            int begin = token.getBegin(),
                    end = token.getEnd();
            if (previousEnd < begin) {
                transitions.putEmptyTransition(previousEnd, begin);
            }
            transitions.putEmptyTransition(begin, end);
            previousEnd = end;
        }
        if (previousEnd < txtLength) {
            transitions.putEmptyTransition(previousEnd, txtLength);
        }
        for (TxtSuggestion sugg : JCasUtil.select(jcas, TxtSuggestion.class)) {
            int begin = sugg.getBegin(),
                    end = sugg.getEnd();
            transitions.put(begin, end, sugg.getRevisions());
        }
        Set<Entry<Double, Span[]>> topRewritings
                = transitions.top(LIMIT).entries();
        Rewritings rewritings = new Rewritings(jcas, 0, txtLength);
        rewritings.setRewritings(new FSArray(jcas, topRewritings.size()));
        int j = 0;
        for (Entry<Double, Span[]> e
                : topRewritings) {
            int length = e.getValue().length;
            Rewriting r = new Rewriting(jcas);
            r.setScore(e.getKey());
            r.setRevisions(new FSArray(jcas, length));
            for (int i = 0; i < length; i++) {
                Span s = e.getValue()[i];
                RewritingSpan rs = new RewritingSpan(jcas, s.begin, s.end);
                rs.setRevisionsId(s.id.toString());
                rs.setRevisionsIndex(s.index);
                r.setRevisions(i, rs);
            }
            rewritings.setRewritings(j++, r);
        }
        rewritings.addToIndexes();
    }
}
