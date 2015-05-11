package eu.crydee.readability.uima.scorer;

import eu.crydee.readability.uima.core.ae.MediaWikiConverterAE;
import eu.crydee.readability.uima.core.ts.Sentence;
import eu.crydee.readability.uima.core.ts.Token;
import eu.crydee.readability.uima.scorer.ae.LanguageModelMakerAE;
import eu.crydee.readability.uima.scorer.cr.CurrentCR;
import eu.crydee.uima.opennlp.resources.EnPosMaxentModel;
import eu.crydee.uima.opennlp.resources.EnSentModel;
import eu.crydee.uima.opennlp.resources.EnTokenModel;
import java.io.File;
import java.util.Optional;
import opennlp.uima.postag.POSModelResourceImpl;
import opennlp.uima.postag.POSTagger;
import opennlp.uima.sentdetect.SentenceDetector;
import opennlp.uima.sentdetect.SentenceModelResourceImpl;
import opennlp.uima.tokenize.Tokenizer;
import opennlp.uima.tokenize.TokenizerModelResourceImpl;
import opennlp.uima.util.UimaUtil;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.cas.CAS;
import org.apache.uima.collection.CollectionReaderDescription;
import org.apache.uima.fit.factory.AggregateBuilder;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;
import org.apache.uima.fit.factory.CollectionReaderFactory;
import static org.apache.uima.fit.factory.ExternalResourceFactory.createExternalResourceDescription;
import org.apache.uima.fit.factory.TypePrioritiesFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.resource.ExternalResourceDescription;

public class FrequencyCreationPipeline {

    final static public String lmFile = "out/lm/lm.bin",
            lmTmpFolder = "out/lm/tmp";
    static private String DB_URL = null,
            DB_USR = null,
            DB_PW = null;
    static private Optional<Integer> LIMIT = Optional.empty();
    static private boolean SKIP = false;

    public static void main(String[] args) throws Exception {
        parseArguments(args);

        /* Create output dirs */
        new File("out/lm/tmp/").mkdirs();

        if (SKIP) {
            AnalysisEngineFactory.createEngine(
                    LanguageModelMakerAE.class,
                    null,
                    TypePrioritiesFactory.createTypePriorities(
                            Sentence.class,
                            Token.class),
                    LanguageModelMakerAE.PARAM_TMP_FOLDER, lmTmpFolder,
                    LanguageModelMakerAE.PARAM_OUT_FILE, lmFile,
                    LanguageModelMakerAE.PARAM_SKIP_EXTRACTION, true)
                    .collectionProcessComplete();
            return;
        }

        /* View names */
        final String text = "text",
                html = "html";

        /* Resources descriptions */
        ExternalResourceDescription tokenM = createExternalResourceDescription(
                TokenizerModelResourceImpl.class,
                EnTokenModel.url);

        ExternalResourceDescription sentM = createExternalResourceDescription(
                SentenceModelResourceImpl.class,
                EnSentModel.url);

        ExternalResourceDescription taggerM = createExternalResourceDescription(
                POSModelResourceImpl.class,
                EnPosMaxentModel.url);

        /* Collection reader */
        CollectionReaderDescription crd;
        if (LIMIT.isPresent()) {
            crd = CollectionReaderFactory.createReaderDescription(
                    CurrentCR.class,
                    CurrentCR.PARAM_DB_URL, DB_URL,
                    CurrentCR.PARAM_DB_USER, DB_USR,
                    CurrentCR.PARAM_DB_PASSWORD, DB_PW,
                    CurrentCR.PARAM_LIMIT, LIMIT.get());
        } else {
            crd = CollectionReaderFactory.createReaderDescription(
                    CurrentCR.class,
                    CurrentCR.PARAM_DB_URL, DB_URL,
                    CurrentCR.PARAM_DB_USER, DB_USR,
                    CurrentCR.PARAM_DB_PASSWORD, DB_PW);
        }

        /* Analysis engines */
        AnalysisEngineDescription mw2txt = createEngineDescription(
                MediaWikiConverterAE.class,
                MediaWikiConverterAE.PARAM_OUT_VIEW_TXT, text,
                MediaWikiConverterAE.PARAM_OUT_VIEW_HTML, html);

        AnalysisEngineDescription sentenceDetector = createEngineDescription(
                SentenceDetector.class,
                UimaUtil.MODEL_PARAMETER, sentM,
                UimaUtil.SENTENCE_TYPE_PARAMETER, Sentence.class.getName());

        AnalysisEngineDescription tokenizer = createEngineDescription(
                Tokenizer.class,
                UimaUtil.MODEL_PARAMETER, tokenM,
                UimaUtil.SENTENCE_TYPE_PARAMETER, Sentence.class.getName(),
                UimaUtil.TOKEN_TYPE_PARAMETER, Token.class.getName());

        AnalysisEngineDescription tagger = createEngineDescription(
                POSTagger.class,
                UimaUtil.MODEL_PARAMETER, taggerM,
                UimaUtil.SENTENCE_TYPE_PARAMETER, Sentence.class.getName(),
                UimaUtil.TOKEN_TYPE_PARAMETER, Token.class.getName(),
                UimaUtil.POS_FEATURE_PARAMETER, "POS");

        AnalysisEngineDescription lmMakerTxt = createEngineDescription(
                LanguageModelMakerAE.class,
                LanguageModelMakerAE.PARAM_TMP_FOLDER, lmTmpFolder,
                LanguageModelMakerAE.PARAM_OUT_FILE, lmFile);

        AggregateBuilder b = new AggregateBuilder(
                null,
                TypePrioritiesFactory.createTypePriorities(
                        Sentence.class,
                        Token.class),
                null);

        b.add(mw2txt);
        b.add(sentenceDetector, CAS.NAME_DEFAULT_SOFA, text);
        b.add(tokenizer, CAS.NAME_DEFAULT_SOFA, text);
        b.add(tagger, CAS.NAME_DEFAULT_SOFA, text);
        b.add(lmMakerTxt, CAS.NAME_DEFAULT_SOFA, text);

        SimplePipeline.runPipeline(crd, b.createAggregateDescription());
    }

    static private void parseArguments(String[] args) {
        Options options = new Options();
        options.addOption("h", "help", false, "print this message");
        options.addOption(OptionBuilder
                .isRequired()
                .withLongOpt("db-url")
                .hasArg()
                .withArgName("jdbc:...")
                .withDescription("The database to use to retrieve revisions "
                        + "information. JDBC format.")
                .create("d"));
        options.addOption(OptionBuilder
                .isRequired()
                .withLongOpt("username")
                .hasArg()
                .withArgName("dbuser")
                .withDescription("User to use to access the database of "
                        + "revisions.")
                .create("u"));
        options.addOption(OptionBuilder
                .isRequired()
                .withLongOpt("password")
                .hasArg()
                .withArgName("pw")
                .withDescription("Password of the user account to connect to "
                        + "the database.")
                .create("p"));
        options.addOption(OptionBuilder
                .withLongOpt("limit")
                .hasArg()
                .withArgName("n")
                .withDescription("How many revisions to use. No limit means "
                        + "all the revisions available.")
                .create('l'));
        options.addOption(OptionBuilder
                .withLongOpt("skip-extraction")
                .withDescription("Skip temp files creation for the LM creation")
                .create("s"));
        CommandLineParser parser = new PosixParser();
        @SuppressWarnings("UnusedAssignment")
        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException ex) {
            System.err.println("The CLI args could not be parsed.");
            System.err.println("The error message was:");
            System.err.println(" " + ex.getMessage());
            System.exit(1);
        }

        // jdbc:postgresql://localhost:5432/readability
        DB_URL = cmd.getOptionValue('d');
        DB_USR = cmd.getOptionValue('u');
        DB_PW = cmd.getOptionValue('p');
        SKIP = cmd.hasOption('s');

        if (cmd.hasOption('l')) {
            LIMIT = Optional.of(Integer.parseInt(cmd.getOptionValue('l')));
        }
    }
}
