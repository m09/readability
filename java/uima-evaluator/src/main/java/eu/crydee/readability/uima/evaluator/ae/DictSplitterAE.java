package eu.crydee.readability.uima.evaluator.ae;

import eu.crydee.readability.uima.core.model.Mapped;
import eu.crydee.readability.uima.core.model.Metadata;
import eu.crydee.readability.uima.core.res.ReadabilityDict;
import eu.crydee.readability.uima.core.res.ReadabilityDict_Impl;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.function.Function;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.uima.UIMAFramework;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.descriptor.ExternalResource;
import org.apache.uima.jcas.JCas;
import org.apache.uima.util.Level;
import org.apache.uima.util.Logger;

public class DictSplitterAE extends JCasAnnotator_ImplBase {

    static private final Logger logger = UIMAFramework.getLogger(
            DictSplitterAE.class);
    static public final Function<Integer, String> testNamer
            = i -> "test-" + i + ".xml",
            trainNamer = i -> "train-" + i + ".xml";

    static public final String KEY_DICT = "DICT";
    @ExternalResource(key = KEY_DICT)
    ReadabilityDict dict;

    static public final String PARAM_N_PARTS = "N_PARTS";
    @ConfigurationParameter(name = PARAM_N_PARTS, mandatory = true)
    int parts;

    static public final String PARAM_OUT_FOLDER = "OUT_FOLDER";
    @ConfigurationParameter(name = PARAM_OUT_FOLDER, mandatory = true)
    File outDir;

    @Override
    public void process(JCas jcas) throws AnalysisEngineProcessException {
        // We're only interested in collectionProcessComplete
    }

    @Override
    public void collectionProcessComplete()
            throws AnalysisEngineProcessException {
        ReadabilityDict[] dicts = new ReadabilityDict[parts];
        Arrays.setAll(dicts, a -> new ReadabilityDict_Impl());
        Random random = new Random();
        for (Entry<Mapped, Map<Mapped, Metadata>> maps : dict.entrySet()) {
            for (Entry<Mapped, Metadata> e : maps.getValue().entrySet()) {
                for (Pair<String, String> contexts
                        : e.getValue().getContexts()) {
                    dicts[random.nextInt(parts)].add(
                            maps.getKey(),
                            e.getKey(),
                            contexts.getLeft(),
                            contexts.getRight());
                }
            }
        }
        for (int i = 0; i < parts; i++) {
            ReadabilityDict test = dicts[i],
                    train = new ReadabilityDict_Impl();
            for (int j = 0; j < parts; j++) {
                if (j == i) {
                    continue;
                }
                train.addAll(dicts[j]);
            }
            String cs = StandardCharsets.UTF_8.name();
            try (PrintStream psTest = new PrintStream(
                    new File(outDir, testNamer.apply(i)), cs);
                    PrintStream psTrain = new PrintStream(
                            new File(outDir, trainNamer.apply(i)), cs)) {
                test.save(psTest);
                train.save(psTrain);
            } catch (FileNotFoundException | UnsupportedEncodingException ex) {
                logger.log(
                        Level.SEVERE,
                        "couldn't create output file for part "
                        + i
                        + " of the output.",
                        ex);
                throw new AnalysisEngineProcessException(ex);
            } catch (Exception ex) {
                logger.log(
                        Level.SEVERE,
                        "couldn't save part " + i + " of the output.",
                        ex);
                throw new AnalysisEngineProcessException(ex);
            }
        }
    }
}
