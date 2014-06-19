package eu.crydee.readability.uima.ae;

import eu.crydee.readability.uima.model.LogWeight;
import eu.crydee.readability.uima.model.Transducer;
import eu.crydee.readability.uima.model.Weight;
import eu.crydee.readability.uima.ts.Revision;
import eu.crydee.readability.uima.ts.Revisions;
import eu.crydee.readability.uima.ts.Rewriting;
import eu.crydee.readability.uima.ts.Rewritings;
import eu.crydee.readability.uima.ts.Token;
import eu.crydee.readability.uima.ts.TxtSuggestion;
import java.util.Map.Entry;
import java.util.Optional;
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
                transitions.put(
                        previousEnd,
                        begin,
                        Optional.empty());
            }
            transitions.put(
                    begin,
                    end,
                    Optional.empty());
            previousEnd = end;
        }
        if (previousEnd < txtLength) {
            transitions.put(
                    previousEnd,
                    txtLength,
                    Optional.empty());
        }
        for (TxtSuggestion sugg : JCasUtil.select(jcas, TxtSuggestion.class)) {
            int begin = sugg.getBegin(),
                    end = sugg.getEnd();
            Revisions revisions = sugg.getRevisions();
            for (int i = 0; i < revisions.getRevisions().size(); i++) {
                Revision revision = revisions.getRevisions(i);
                transitions.put(
                        begin,
                        end,
                        Optional.of(revision));
            }
        }
        Set<Entry<Double, Revision[]>> topRewritings
                = transitions.top(LIMIT).entries();
        Rewritings rewritings = new Rewritings(jcas);
        rewritings.setRewritings(new FSArray(jcas, topRewritings.size()));
        int j = 0;
        for (Entry<Double, Revision[]> e
                : topRewritings) {
            int length = e.getValue().length;
            FSArray fsaRevisions = new FSArray(jcas, length);
            fsaRevisions.copyFromArray(e.getValue(), 0, 0, length);
            Rewriting r = new Rewriting(jcas);
            r.setRevisions(fsaRevisions);
            rewritings.setRewritings(j, r);
        }
        jcas.addFsToIndexes(rewritings);
    }
}
