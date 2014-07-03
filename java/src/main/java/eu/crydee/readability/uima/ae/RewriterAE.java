package eu.crydee.readability.uima.ae;

import eu.crydee.readability.uima.model.LogWeight;
import eu.crydee.readability.uima.model.ProbaWeight;
import eu.crydee.readability.uima.model.Transducer;
import eu.crydee.readability.uima.model.Transducer.Span;
import eu.crydee.readability.uima.model.Weight;
import eu.crydee.readability.uima.ts.Revision;
import eu.crydee.readability.uima.ts.Rewriting;
import eu.crydee.readability.uima.ts.RewritingSpan;
import eu.crydee.readability.uima.ts.Rewritings;
import eu.crydee.readability.uima.ts.RewritingsLM;
import eu.crydee.readability.uima.ts.RewritingsLMN;
import eu.crydee.readability.uima.ts.RewritingsLMW;
import eu.crydee.readability.uima.ts.RewritingsLMWN;
import eu.crydee.readability.uima.ts.RewritingsOcc;
import eu.crydee.readability.uima.ts.Token;
import eu.crydee.readability.uima.ts.TxtSuggestion;
import java.lang.reflect.InvocationTargetException;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.uima.UIMAFramework;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.util.Level;
import org.apache.uima.util.Logger;

public class RewriterAE extends JCasAnnotator_ImplBase {

    private static final Logger logger = UIMAFramework.getLogger(
            RewriterAE.class);

    public static final int LIMIT = 20;

    @Override
    public void process(JCas jcas) throws AnalysisEngineProcessException {
        process(jcas, RewritingsOcc.class, 0, new LogWeight());
        process(jcas, RewritingsLM.class, 1, new LogWeight());
        process(jcas, RewritingsLMN.class, 2, new LogWeight());
        process(jcas, RewritingsLMW.class, 3, new LogWeight());
        process(jcas, RewritingsLMWN.class, 4, new LogWeight());
    }

    private void process(
            JCas jcas,
            Class<? extends Rewritings> rewritingsClass,
            int scoreIndex,
            Weight weight)
            throws AnalysisEngineProcessException {
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
        Rewritings rewritings;
        try {
            rewritings = rewritingsClass
                    .getConstructor(JCas.class, int.class, int.class)
                    .newInstance(jcas, 0, txtLength);
        } catch (NoSuchMethodException |
                SecurityException |
                InstantiationException |
                IllegalAccessException |
                IllegalArgumentException |
                InvocationTargetException ex) {
            logger.log(
                    Level.SEVERE,
                    "couldn't create Rewritings annotation.",
                    ex);
            throw new AnalysisEngineProcessException(ex);
        }
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
        rewritings.addToIndexes();
    }
}
