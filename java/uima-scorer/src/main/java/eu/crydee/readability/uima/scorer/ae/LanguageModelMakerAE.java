package eu.crydee.readability.uima.scorer.ae;

import edu.berkeley.nlp.lm.ConfigOptions;
import edu.berkeley.nlp.lm.NgramLanguageModel;
import edu.berkeley.nlp.lm.StringWordIndexer;
import edu.berkeley.nlp.lm.WordIndexer;
import edu.berkeley.nlp.lm.io.LmReaders;
import eu.crydee.readability.uima.core.ts.Sentence;
import eu.crydee.readability.uima.core.ts.Token;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.apache.uima.UIMAFramework;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.util.Level;
import org.apache.uima.util.Logger;

public class LanguageModelMakerAE extends JCasAnnotator_ImplBase {

    private static final Logger logger = UIMAFramework.getLogger(
            LanguageModelMakerAE.class);

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

    public static class LmFormatter<T> {

        private final Pattern spaces = Pattern.compile(" "),
                newLines = Pattern.compile("\n");
        private final Function<T, String> posGetter;
        private final Function<T, String> textGetter;

        public LmFormatter(
                Function<T, String> textGetter,
                Function<T, String> posGetter) {
            this.posGetter = posGetter;
            this.textGetter = textGetter;
        }

        public List<String> apply(Collection<T> tokens) {
            return tokens.stream()
                    .map(t -> textGetter.apply(t) + "/" + posGetter.apply(t))
                    .map(s -> s.toLowerCase(Locale.ENGLISH))
                    .map(s -> spaces.matcher(s).replaceAll("<spc>"))
                    .map(s -> newLines.matcher(s).replaceAll("<nl>"))
                    .collect(Collectors.toList());
        }
    }

    @Override
    public void process(JCas jcas) throws AnalysisEngineProcessException {
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
            Map<Sentence, Collection<Token>> m
                    = JCasUtil.indexCovered(jcas, Sentence.class, Token.class);
            LmFormatter<Token> lf = new LmFormatter<>(
                    Token::getCoveredText,
                    Token::getPOS);
            m.values().forEach(v -> pw.println(
                    "<s> "
                    + lf.apply(v).stream().collect(Collectors.joining(" "))
                    + " </s>"));
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
