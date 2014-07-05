package eu.crydee.readability.uima.corpuscreator.ae;

import edu.berkeley.nlp.lm.ConfigOptions;
import edu.berkeley.nlp.lm.NgramLanguageModel;
import edu.berkeley.nlp.lm.StringWordIndexer;
import edu.berkeley.nlp.lm.WordIndexer;
import edu.berkeley.nlp.lm.io.LmReaders;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.apache.uima.UIMAFramework;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.Feature;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.TypeSystem;
import org.apache.uima.cas.text.AnnotationFS;
import org.apache.uima.fit.component.CasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.util.CasUtil;
import org.apache.uima.util.Level;
import org.apache.uima.util.Logger;

public class LanguageModelMakerAE extends CasAnnotator_ImplBase {

    private static final Logger logger = UIMAFramework.getLogger(
            LanguageModelMakerAE.class);

    public static final String PARAM_TOKEN_TYPE = "TOKEN_TYPE";
    @ConfigurationParameter(name = PARAM_TOKEN_TYPE, mandatory = false)
    private String tokenTName;

    public static final String PARAM_TOKEN_FEATURE = "TOKEN_FEATURE";
    @ConfigurationParameter(name = PARAM_TOKEN_FEATURE, mandatory = false)
    private String tokenFName;

    public static final String PARAM_SENTENCE_TYPE = "SENTENCE_TYPE";
    @ConfigurationParameter(name = PARAM_SENTENCE_TYPE, mandatory = false)
    private String sentenceTName;

    public static final String PARAM_TMP_FOLDER = "TMP_FOLDER";
    @ConfigurationParameter(name = PARAM_TMP_FOLDER, mandatory = true)
    private File tmpFolder;

    public static final String PARAM_OUT_FILE = "OUT_FILE";
    @ConfigurationParameter(name = PARAM_OUT_FILE, mandatory = true)
    private String outFile;

    public static final String PARAM_SKIP_EXTRACTION = "SKIP_EXTRACTION";
    @ConfigurationParameter(
            name = PARAM_SKIP_EXTRACTION,
            mandatory = false,
            defaultValue = "false")
    private boolean skip;

    private Type tokenT = null,
            sentenceT = null;

    private Feature tokenF = null;

    private final Pattern spaces = Pattern.compile(" "),
            newLines = Pattern.compile("\n");

    @Override
    public void typeSystemInit(TypeSystem aTypeSystem)
            throws AnalysisEngineProcessException {
        super.typeSystemInit(aTypeSystem);
        tokenT = aTypeSystem.getType(tokenTName);
        if (tokenFName != null) {
            tokenF = tokenT.getFeatureByBaseName(tokenFName);
        }
        sentenceT = aTypeSystem.getType(sentenceTName);
    }

    @Override
    public void process(CAS aCas) throws AnalysisEngineProcessException {
        if (skip) {
            return;
        }
        File tmp = null;
        try {
            tmp = File.createTempFile("tx-", null, tmpFolder);
        } catch (IOException ex) {
            logger.log(
                    Level.SEVERE,
                    "Couldn't create tmp file to create LM",
                    ex);
            throw new AnalysisEngineProcessException(ex);
        }
        try (PrintWriter pw = new PrintWriter(tmp)) {
            Map<AnnotationFS, Collection<AnnotationFS>> index
                    = CasUtil.indexCovered(aCas, sentenceT, tokenT);
            for (AnnotationFS sentence : index.keySet()) {
                pw.println("<s> "
                        + index.get(sentence).stream()
                        .map(t -> {
                            return tokenF == null
                            ? t.getCoveredText()
                            : t.getFeatureValueAsString(tokenF);
                        })
                        .map(s -> s.toLowerCase(Locale.ENGLISH))
                        .map(s -> spaces.matcher(s).replaceAll("<spc>"))
                        .map(s -> newLines.matcher(s).replaceAll("<nl>"))
                        .collect(Collectors.joining(" "))
                        + " </s>");
            }
        } catch (FileNotFoundException ex) {
            logger.log(
                    Level.SEVERE,
                    "Couldn't read tmp file to create LM",
                    ex);
            throw new AnalysisEngineProcessException(ex);
        }
    }

    @Override
    public void collectionProcessComplete()
            throws AnalysisEngineProcessException {
        super.collectionProcessComplete();
        List<String> files = Arrays.stream(tmpFolder.listFiles())
                .map(File::getAbsolutePath)
                .collect(Collectors.toList());
        WordIndexer<String> wi = new StringWordIndexer();
        wi.setStartSymbol("<s>");
        wi.setEndSymbol("</s>");
        wi.setUnkSymbol("<unk>");
        NgramLanguageModel<String> nlm = LmReaders.readKneserNeyLmFromTextFile(
                files,
                wi,
                2,
                new ConfigOptions(),
                true);
        LmReaders.writeLmBinary(nlm, outFile);
    }
}
