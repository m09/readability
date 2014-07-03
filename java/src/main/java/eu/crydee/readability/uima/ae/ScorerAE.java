package eu.crydee.readability.uima.ae;

import edu.berkeley.nlp.lm.NgramLanguageModel;
import edu.berkeley.nlp.lm.io.LmReaders;
import eu.crydee.readability.uima.model.Mapped;
import eu.crydee.readability.uima.model.Metadata;
import eu.crydee.readability.uima.model.Score;
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
        double lowestScoreLM = Double.POSITIVE_INFINITY,
                highestScoreLM = Double.NEGATIVE_INFINITY;
        double lowestScoreLMW = Double.POSITIVE_INFINITY,
                highestScoreLMW = Double.NEGATIVE_INFINITY;
        double minLMProba = Double.POSITIVE_INFINITY;
        double minLMWProba = Double.POSITIVE_INFINITY;
        double logTotalCount = Math.log(dict.getTotalCount());
        for (Mapped original : dict.keySet()) {
            Map<Mapped, Metadata> revs = dict.getRevisions(original).get();
            int originalCount
                    = revs.values().stream().mapToInt(Metadata::getCount).sum();
            List<String> tokens = new ArrayList<>();
            tokens.add("<s>");
            tokens.addAll(original.getTokens());
            tokens.add("</s>");
            float lmProba = nlm.scoreSentence(tokens),
                    lmWProba = lmProba / original.getTokens().size();
            minLMProba = Math.min(minLMProba, lmProba);
            minLMWProba = Math.min(minLMWProba, lmWProba);
            for (Mapped rev : revs.keySet()) {
                Metadata metric = revs.get(rev);
                double scoreOcc = Math.log(metric.getCount())
                        - logTotalCount,
                        scoreLM = Math.log(metric.getCount())
                        - logTotalCount
                        - lmProba,
                        scoreLMW = Math.log(metric.getCount())
                        - logTotalCount
                        - lmWProba,
                        scoreLMC = Math.log(metric.getCount())
                        - Math.log(originalCount)
                        - lmProba,
                        scoreLMCW = Math.log(metric.getCount())
                        - Math.log(originalCount)
                        - lmWProba;
                lowestScoreLM = Math.min(scoreLMC, lowestScoreLM);
                highestScoreLM = Math.max(scoreLMC, highestScoreLM);
                lowestScoreLMW = Math.min(scoreLMCW, lowestScoreLMW);
                highestScoreLMW = Math.max(scoreLMCW, highestScoreLMW);
                metric.setScore(Score.OCC, scoreOcc);
                metric.setScore(Score.LMN, scoreLM);
                metric.setScore(Score.LMWN, scoreLMW);
                metric.setScore(Score.LMCN, scoreLMC);
                metric.setScore(Score.LMCWN, scoreLMCW);
            }
        }
        for (Mapped original : dict.keySet()) {
            Map<Mapped, Metadata> revs = dict.getRevisions(original).get();
            for (Mapped rev : revs.keySet()) {
                Metadata metric = revs.get(rev);
                metric.setScore(
                        Score.LMN,
                        metric.getScore(Score.LMN) + minLMProba);
                metric.setScore(
                        Score.LMWN,
                        metric.getScore(Score.LMWN) + minLMWProba);
                metric.setScore(
                        Score.LMCN,
                        metric.getScore(Score.LMCN) + minLMProba);
                metric.setScore(
                        Score.LMCWN,
                        metric.getScore(Score.LMCWN) + minLMWProba);
            }
        }
    }
}
