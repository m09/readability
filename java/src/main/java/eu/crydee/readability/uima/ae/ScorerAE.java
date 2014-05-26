package eu.crydee.readability.uima.ae;

import de.tudarmstadt.ukp.dkpro.core.api.frequency.provider.FrequencyCountProvider;
import de.tudarmstadt.ukp.dkpro.core.ngrams.util.NGramStringIterable;
import java.io.IOException;
import org.apache.uima.UIMAFramework;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ExternalResource;
import org.apache.uima.jcas.JCas;
import org.apache.uima.util.Level;
import org.apache.uima.util.Logger;

public class ScorerAE extends JCasAnnotator_ImplBase {

    public static final String RES_KEY = "RES_KEY";
    @ExternalResource(key = RES_KEY)
    FrequencyCountProvider counts;

    private static final Logger logger = UIMAFramework.getLogger(
            ScorerAE.class);

    @Override
    public void process(JCas jcas) throws AnalysisEngineProcessException {
        NGramStringIterable ngrams
                = new NGramStringIterable(new String[]{"I", "am"}, 1, 3);
        try {
            for (String ngram : ngrams) {
                logger.log(
                        Level.INFO,
                        ngram
                        + " → "
                        + String.valueOf(counts.getLogProbability(ngram)));
            }
        } catch (IOException ex) {
            throw new AnalysisEngineProcessException(ex);
        }
    }

    public double proba(String[] tokens) {
        return 0d;
    }
}
