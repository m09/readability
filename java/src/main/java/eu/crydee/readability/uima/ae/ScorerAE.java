package eu.crydee.readability.uima.ae;

import edu.berkeley.nlp.lm.NgramLanguageModel;
import edu.berkeley.nlp.lm.io.LmReaders;
import eu.crydee.readability.uima.model.Mapped;
import eu.crydee.readability.uima.model.Metrics;
import eu.crydee.readability.uima.res.ReadabilityDict;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.uima.UIMAFramework;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.descriptor.ExternalResource;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Level;
import org.apache.uima.util.Logger;

public class ScorerAE extends JCasAnnotator_ImplBase {

    public static final String PARAM_LM_FILENAME = "LM_FILENAME_TXT";
    @ConfigurationParameter(name = PARAM_LM_FILENAME, mandatory = true)
    String lmFilename;

    final static public String RES_KEY = "KEY";
    @ExternalResource(key = RES_KEY)
    ReadabilityDict dict;

    private NgramLanguageModel<String> nlm;

    @Override
    public void initialize(UimaContext context)
            throws ResourceInitializationException {
        super.initialize(context);
        nlm = LmReaders.readLmBinary(lmFilename);
    }

    private static final Logger logger = UIMAFramework.getLogger(
            ScorerAE.class);

    @Override
    public void process(JCas jcas) throws AnalysisEngineProcessException {
    }

    @Override
    public void collectionProcessComplete()
            throws AnalysisEngineProcessException {
        double lowestScore = Double.POSITIVE_INFINITY,
                highestScore = Double.NEGATIVE_INFINITY;
        int totalCount = dict.getTotalCount();
        for (Mapped original : dict.keySet()) {
            Map<Mapped, Metrics> revs = dict.getRevisions(original).get();
            int originalCount
                    = revs.values().stream().mapToInt(Metrics::getCount).sum();
            List<String> tokens = new ArrayList<>();
            tokens.add("<s>");
            tokens.addAll(original.getTokens());
            tokens.add("</s>");
            float lmProba = nlm.scoreSentence(tokens);
            logger.log(Level.FINEST, "originalCount: " + originalCount);
            logger.log(Level.FINEST, "lmProba:       " + lmProba);
            for (Mapped rev : revs.keySet()) {
                Metrics metric = revs.get(rev);
                double score = Math.log(metric.getCount())
                        - Math.log(totalCount)
                        - Math.log(1 - Math.exp(lmProba));
                lowestScore = Math.min(score, lowestScore);
                highestScore = Math.max(score, highestScore);
                metric.setScore(score);
            }
        }
//        double ambitus = highestScore - lowestScore;
//        for (Mapped original : dict.keySet()) {
//            Map<Mapped, Metrics> revs = dict.getRevisions(original).get();
//            for (Mapped rev : revs.keySet()) {
//                Metrics metric = revs.get(rev);
//                metric.setScore(
//                        (metric.getScore() - lowestScore)
//                        / ambitus);
//            }
//        }
    }
}
