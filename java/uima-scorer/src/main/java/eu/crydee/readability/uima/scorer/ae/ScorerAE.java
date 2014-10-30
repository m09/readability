package eu.crydee.readability.uima.scorer.ae;

import edu.berkeley.nlp.lm.NgramLanguageModel;
import edu.berkeley.nlp.lm.io.LmReaders;
import eu.crydee.gridsearch.Gridsearch;
import eu.crydee.readability.uima.core.model.Mapped;
import eu.crydee.readability.uima.core.model.Metadata;
import eu.crydee.readability.uima.core.model.Score;
import eu.crydee.readability.uima.core.res.ReadabilityDict;
import eu.crydee.readability.uima.scorer.ae.LanguageModelMakerAE.LmFormatter;
import eu.crydee.readability.utils.FunctionUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.ArrayUtils;
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
    private String lmFilename;

    final static public String RES_KEY = "KEY";
    @ExternalResource(key = RES_KEY)
    private ReadabilityDict dict;

    private Map<Mapped, Double> inverseCounts;

    private NgramLanguageModel<String> nlm;

    private final LmFormatter<Pair<String, String>> lf = new LmFormatter<>(
            Pair::getLeft,
            Pair::getRight);

    @Override
    public void initialize(UimaContext context)
            throws ResourceInitializationException {
        super.initialize(context);
        nlm = LmReaders.readLmBinary(lmFilename);
        inverseCounts = dict.getInverseCounts();
    }

    @Override
    public void process(JCas jcas) throws AnalysisEngineProcessException {
    }

    private enum Features {

        lmO,
        lmR,
        lmwO,
        lmwR,
        p,
        pcRecto,
        pcVerso;
    }

    @Override
    public void collectionProcessComplete()
            throws AnalysisEngineProcessException {
        Features[] featuresValues = Features.values();
        double totalCount = Math.log(dict.getTotalCount());
        Map<Features, Double> maxFeatures = new EnumMap<>(Features.class),
                minFeatures = new EnumMap<>(Features.class);
        Arrays.stream(Features.values()).forEach(f -> {
            maxFeatures.put(f, Double.MIN_VALUE);
            minFeatures.put(f, Double.MAX_VALUE);
        });
        List<double[]> featuresList = new ArrayList<>();
        List<Pair<Mapped, Mapped>> keysList = new ArrayList<>();
        for (Mapped original : dict.keySet()) {
            Map<Mapped, Metadata> revs = dict.getRevisions(original).get();
            double originalCount = Math.log(
                    revs.values().stream().mapToInt(Metadata::getCount).sum());
            List<Pair<String, String>> oriTokens = FunctionUtils.zip(
                    original.getTokens(),
                    original.getPos());
            double originalLmProba = nlm.scoreSentence(lf.apply(oriTokens)),
                    originalLmWProba = originalLmProba
                    / original.getTokens().size();
            for (Mapped rev : revs.keySet()) {
                double revisedCount = inverseCounts.get(rev);
                List<Pair<String, String>> revTokens = FunctionUtils.zip(
                        rev.getTokens(),
                        rev.getPos());
                double revLmProba = nlm.scoreSentence(lf.apply(revTokens)),
                        revLmWProba = revLmProba / rev.getTokens().size();
                double count = Math.log(revs.get(rev).getCount());
                Map<Features, Double> features = new EnumMap<>(Features.class);
                features.put(Features.lmwO, originalLmWProba);
                features.put(Features.lmwR, revLmWProba);
                features.put(Features.lmO, originalLmProba);
                features.put(Features.lmR, revLmProba);
                features.put(Features.p, count - totalCount);
                features.put(Features.pcRecto, count - originalCount);
                features.put(Features.pcVerso, count - revisedCount);
                double[] raw = new double[Features.values().length];
                for (int i = 0, l = featuresValues.length; i < l; i++) {
                    raw[i] = features.get(featuresValues[i]);
                }
                featuresList.add(raw);
                keysList.add(Pair.of(original, rev));
                Arrays.stream(Features.values()).forEach(f -> {
                    if (maxFeatures.get(f) < features.get(f)) {
                        maxFeatures.put(f, features.get(f));
                    }
                    if (minFeatures.get(f) > features.get(f)) {
                        minFeatures.put(f, features.get(f));
                    }
                });
            }
        }
        double[][] featuresArray = featuresList.toArray(new double[0][0]);
        Pair<Mapped, Mapped>[] keysArray = keysList.toArray(new Pair[0]);
        for (int i = 0, l = featuresValues.length; i < l; i++) {
            double min = minFeatures.get(featuresValues[i]),
                    max = maxFeatures.get(featuresValues[i]),
                    scaler = max - min;
            if (min == max) {
                for (double[] raw : featuresArray) {
                    raw[i] = 0d;
                }
            } else {
                for (double[] raw : featuresArray) {
                    raw[i] = (raw[i] - min) / scaler;
                }
            }
        }

        Gridsearch g = new Gridsearch();
        double[] max = new double[Features.values().length],
                min = new double[Features.values().length];
        Arrays.fill(max, 1d);
        Arrays.fill(min, -1d);
        double[] coeffs = g.estimate(
                max,
                min,
                3,
                10,
                c -> {
                    Double result = 0d;
                    for (double[] raw : featuresArray) {
                        double sumNormal = c[0] * raw[0]
                        + c[1] * raw[1]
                        + c[2] * raw[2]
                        + c[3] * raw[3]
                        + c[4] * raw[4]
                        + c[5] * raw[5]
                        + c[6] * raw[6];
                        double sumReversed = c[1] * raw[0]
                        + c[0] * raw[1]
                        + c[3] * raw[2]
                        + c[2] * raw[3]
                        + c[4] * raw[4]
                        + c[6] * raw[5]
                        + c[5] * raw[6];
                        if (sumNormal > sumReversed) {
                            result++;
                        }
                    }
                    return result;
                },
                Gridsearch.Objective.MAXIMIZE);
        System.out.println(ArrayUtils.toString(coeffs));

        for (int i = 0, l = keysArray.length; i < l; i++) {
            Pair<Mapped, Mapped> key = keysArray[i];
            double[] raw = featuresArray[i];
            Metadata metadata = dict.getRevisions(key.getLeft()).get()
                    .get(key.getRight());
            // raw[0] = lmO
            // raw[1] = lmR
            // raw[2] = lmwO
            // raw[3] = lmwR
            // raw[4] = p
            // raw[5] = pcRecto
            // raw[6] = pcVerso
            double s = raw[4] - raw[2],
                    sc = raw[5] - raw[2],
                    sd = raw[4] + raw[1] - raw[2],
                    sdc = raw[5] + raw[1] - raw[2],
                    swd = raw[4] + raw[3] - raw[2],
                    swdc = raw[5] + raw[3] - raw[2],
                    sall = coeffs[0] * raw[0]
                    + coeffs[1] * raw[1]
                    + coeffs[2] * raw[2]
                    + coeffs[3] * raw[3]
                    + coeffs[4] * raw[4]
                    + coeffs[5] * raw[5]
                    + coeffs[6] * raw[6];
            metadata.setScore(Score.S, s);
            metadata.setScore(Score.Sc, sc);
            metadata.setScore(Score.Sd, sd);
            metadata.setScore(Score.Sdc, sdc);
            metadata.setScore(Score.Swd, swd);
            metadata.setScore(Score.Swdc, swdc);
            metadata.setScore(Score.Sall, sall);
        }
    }
}
