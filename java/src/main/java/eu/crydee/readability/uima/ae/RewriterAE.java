package eu.crydee.readability.uima.ae;

import eu.crydee.readability.uima.model.LogWeight;
import eu.crydee.readability.uima.model.Mapped;
import eu.crydee.readability.uima.model.Metrics;
import eu.crydee.readability.uima.model.Transducer;
import eu.crydee.readability.uima.model.Weight;
import eu.crydee.readability.uima.res.Mappings;
import eu.crydee.readability.uima.ts.Token;
import eu.crydee.readability.uima.ts.TxtSuggestion;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import org.apache.uima.UIMAFramework;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ExternalResource;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.util.Logger;

public class RewriterAE extends JCasAnnotator_ImplBase {

    private static final Logger logger = UIMAFramework.getLogger(
            RewriterAE.class);

    final static public String RES_MAPPINGS = "RES_MAPPINGS";
    @ExternalResource(key = RES_MAPPINGS)
    private Mappings mappings;

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
                        weight.getUnit(),
                        new UUID[0]);
            }
            transitions.put(
                    begin,
                    end,
                    weight.getUnit(),
                    new UUID[0]);
            previousEnd = end;
        }
        if (previousEnd < txtLength) {
            transitions.put(
                    previousEnd,
                    txtLength,
                    weight.getUnit(),
                    new UUID[0]);
        }
        for (TxtSuggestion sugg : JCasUtil.select(jcas, TxtSuggestion.class)) {
            int begin = sugg.getBegin(),
                    end = sugg.getEnd();
            Map<Mapped, Metrics> revisions
                    = mappings.getRevisions(sugg.getId()).get();
            for (Mapped revision : revisions.keySet()) {
                Metrics metrics = revisions.get(revision);
                transitions.put(
                        begin,
                        end,
                        metrics.getScore(),
                        new UUID[]{UUID.fromString(sugg.getId())});
            }
        }
        for (Entry<Double, UUID[]> rewriting
                : transitions.top(LIMIT).entries()) {
            mappings.addRewriting(rewriting.getValue(), rewriting.getKey());
        }
    }
}
