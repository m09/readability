package eu.crydee.readability.uima.server.ae;

import eu.crydee.readability.uima.core.model.Score;
import eu.crydee.readability.uima.server.model.Semiring;
import eu.crydee.readability.uima.server.model.Transducer;
import eu.crydee.readability.uima.server.model.Transducer.Span;
import eu.crydee.readability.uima.server.model.Weight;
import eu.crydee.readability.uima.server.ts.RewritingsByScoreThenSemiring;
import eu.crydee.readability.uima.server.ts.Revision;
import eu.crydee.readability.uima.server.ts.Rewriting;
import eu.crydee.readability.uima.server.ts.RewritingSpan;
import eu.crydee.readability.uima.server.ts.Rewritings;
import eu.crydee.readability.uima.server.ts.RewritingsBySemiring;
import eu.crydee.readability.uima.core.ts.Token;
import eu.crydee.readability.uima.server.ts.TxtSuggestion;
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

    @Override
    public void process(JCas jcas) throws AnalysisEngineProcessException {
        int scoreSize = Score.values().length,
                semiringSize = Semiring.values().length;
        RewritingsByScoreThenSemiring rbsts = new RewritingsByScoreThenSemiring(
                jcas,
                0,
                jcas.getDocumentText().length());
        rbsts.setRewritingsByScoreThenSemiring(new FSArray(jcas, scoreSize));
        for (int i = 0; i < scoreSize; ++i) {
            RewritingsBySemiring rbs = new RewritingsBySemiring(jcas);
            rbs.setRewritingsBySemiring(new FSArray(jcas, semiringSize));
            for (int j = 0; j < semiringSize; ++j) {
                rbs.setRewritingsBySemiring(
                        j,
                        process(jcas, i, j));
            }
            rbsts.setRewritingsByScoreThenSemiring(i, rbs);
        }
        rbsts.addToIndexes();
    }

    private Rewritings process(
            JCas jcas,
            int scoreIndex,
            int semiringIndex)
            throws AnalysisEngineProcessException {
        Weight weight = Semiring.values()[semiringIndex];
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
            transitions.put(
                    begin,
                    end,
                    sugg.getRevisions(),
                    (Revision r) -> r.getScore(scoreIndex));
        }
        Set<Entry<Double, Span[]>> topRewritings
                = transitions.top(LIMIT).entries();
        Rewritings rewritings = new Rewritings(jcas, 0, txtLength);
        rewritings.setRewritings(new FSArray(jcas, topRewritings.size()));
        int j = 0;
        for (Entry<Double, Span[]> e : topRewritings) {
            int length = e.getValue().length;
            Rewriting r = new Rewriting(jcas);
            r.setRevisions(new FSArray(jcas, length));
            r.setScore(e.getKey());
            for (int i = 0; i < length; i++) {
                Span s = e.getValue()[i];
                RewritingSpan rs = new RewritingSpan(jcas, s.begin, s.end);
                rs.setRevisionsId(s.id.toString());
                rs.setRevisionsIndex(s.index);
                r.setRevisions(i, rs);
            }
            rewritings.setRewritings(j++, r);
        }
        return rewritings;
    }
}
