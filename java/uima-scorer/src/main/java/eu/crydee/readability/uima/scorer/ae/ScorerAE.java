package eu.crydee.readability.uima.scorer.ae;

import edu.berkeley.nlp.lm.NgramLanguageModel;
import edu.berkeley.nlp.lm.io.LmReaders;
import eu.crydee.readability.uima.core.model.Mapped;
import eu.crydee.readability.uima.core.model.Metadata;
import eu.crydee.readability.uima.core.model.Score;
import eu.crydee.readability.uima.core.res.ReadabilityDict;
import eu.crydee.readability.uima.scorer.ae.LanguageModelMakerAE.LmFormatter;
import eu.crydee.readability.utils.FunctionUtils;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.tuple.Pair;
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

    private static final Logger logger = UIMAFramework.getLogger(
            ScorerAE.class);

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

    @Override
    public void process(JCas jcas) throws AnalysisEngineProcessException {
    }

    @Override
    public void collectionProcessComplete()
            throws AnalysisEngineProcessException {
        double minLMProba = Double.POSITIVE_INFINITY;
        double minLMWProba = Double.POSITIVE_INFINITY;
        double logTotalCount = Math.log(dict.getTotalCount());
        LmFormatter<Pair<String, String>> lf = new LmFormatter<>(
                Pair::getLeft,
                Pair::getRight);
        for (Mapped original : dict.keySet()) {
            Map<Mapped, Metadata> revs = dict.getRevisions(original).get();
            int originalCount
                    = revs.values().stream().mapToInt(Metadata::getCount).sum();
            List<Pair<String, String>> oriTokens = FunctionUtils.zip(
                    original.getTokens(),
                    original.getPos());
            float originalLmProba = nlm.scoreSentence(lf.apply(oriTokens)),
                    originalLmWProba = originalLmProba
                    / original.getTokens().size();
            minLMProba = Math.min(minLMProba, originalLmProba);
            minLMWProba = Math.min(minLMWProba, originalLmWProba);
            for (Mapped rev : revs.keySet()) {
                List<Pair<String, String>> revTokens = FunctionUtils.zip(
                        rev.getTokens(),
                        rev.getPos());
                float revisedLmProba = nlm.scoreSentence(lf.apply(revTokens)),
                        revisedLmWProba = revisedLmProba
                        / rev.getTokens().size();
                Metadata metric = revs.get(rev);
                double scoreLM = Math.log(metric.getCount())
                        - logTotalCount
                        - originalLmWProba,
                        scoreLMC = Math.log(metric.getCount())
                        - Math.log(originalCount)
                        - originalLmWProba,
                        scoreDLM = Math.log(metric.getCount())
                        - logTotalCount
                        + revisedLmProba
                        - originalLmWProba,
                        scoreDLMC = Math.log(metric.getCount())
                        - Math.log(originalCount)
                        + revisedLmProba
                        - originalLmWProba,
                        scoreDLMW = Math.log(metric.getCount())
                        - logTotalCount
                        + revisedLmWProba
                        - originalLmWProba,
                        scoreDLMCW = Math.log(metric.getCount())
                        - Math.log(originalCount)
                        + revisedLmWProba
                        - originalLmWProba;
                metric.setScore(Score.S, scoreLM);
                metric.setScore(Score.Sc, scoreLMC);
                metric.setScore(Score.Sd, scoreDLM);
                metric.setScore(Score.Sdc, scoreDLMC);
                metric.setScore(Score.Swd, scoreDLMW);
                metric.setScore(Score.Swdc, scoreDLMCW);
            }
        }
        for (Mapped original : dict.keySet()) {
            Map<Mapped, Metadata> revs = dict.getRevisions(original).get();
            for (Mapped rev : revs.keySet()) {
                Metadata metric = revs.get(rev);
                for (Score score : Score.values()) {
                    metric.setScore(
                            score,
                            metric.getScore(score) + minLMWProba);
                }
            }
        }
    }
}
