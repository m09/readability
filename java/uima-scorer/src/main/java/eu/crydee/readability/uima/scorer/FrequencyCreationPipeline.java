package eu.crydee.readability.uima.scorer;

import eu.crydee.readability.uima.core.ae.MediaWikiConverterAE;
import eu.crydee.readability.uima.core.ts.Sentence;
import eu.crydee.readability.uima.core.ts.Token;
import eu.crydee.readability.uima.models.SentenceSplitterModelPath;
import eu.crydee.readability.uima.models.TaggerModelPath;
import eu.crydee.readability.uima.models.TokenizerModelPath;
import eu.crydee.readability.uima.scorer.ae.LanguageModelMakerAE;
import eu.crydee.readability.uima.scorer.cr.CurrentCR;
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
import org.apache.uima.fit.factory.CollectionReaderFactory;
import org.apache.uima.fit.factory.ExternalResourceFactory;
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

        if (SKIP) {
            AnalysisEngineFactory.createEngine(
                    LanguageModelMakerAE.class,
                    null,
                    TypePrioritiesFactory.createTypePriorities(
                            Sentence.class,
                            Token.class),
                    LanguageModelMakerAE.PARAM_TMP_FOLDER,
                    lmTmpFolder,
                    LanguageModelMakerAE.PARAM_OUT_FILE,
                    lmFile,
                    LanguageModelMakerAE.PARAM_SKIP_EXTRACTION,
                    true)
                    .collectionProcessComplete();
            return;
        }

        /* View names */
        final String text = "text",
                html = "html";

        /* Resources descriptions */
        ExternalResourceDescription tokenModel
                = ExternalResourceFactory.createExternalResourceDescription(
                        TokenizerModelResourceImpl.class,
                        "file:" + TokenizerModelPath.path);

        ExternalResourceDescription sentenceModel
                = ExternalResourceFactory.createExternalResourceDescription(
                        SentenceModelResourceImpl.class,
                        "file:" + SentenceSplitterModelPath.path);

        ExternalResourceDescription taggerModel
                = ExternalResourceFactory.createExternalResourceDescription(
                        POSModelResourceImpl.class,
                        "file:" + TaggerModelPath.path);

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
        AnalysisEngineDescription mw2txt
                = AnalysisEngineFactory.createEngineDescription(
                        MediaWikiConverterAE.class,
                        MediaWikiConverterAE.PARAM_OUT_VIEW_TXT,
                        text,
                        MediaWikiConverterAE.PARAM_OUT_VIEW_HTML,
                        html);

        AnalysisEngineDescription sentenceDetector
                = AnalysisEngineFactory.createEngineDescription(
                        SentenceDetector.class,
                        UimaUtil.MODEL_PARAMETER,
                        sentenceModel,
                        UimaUtil.SENTENCE_TYPE_PARAMETER,
                        Sentence.class.getCanonicalName());

        AnalysisEngineDescription tokenizer
                = AnalysisEngineFactory.createEngineDescription(
                        Tokenizer.class,
                        UimaUtil.MODEL_PARAMETER,
                        tokenModel,
                        UimaUtil.SENTENCE_TYPE_PARAMETER,
                        Sentence.class.getCanonicalName(),
                        UimaUtil.TOKEN_TYPE_PARAMETER,
                        Token.class.getCanonicalName());

        AnalysisEngineDescription tagger
                = AnalysisEngineFactory.createEngineDescription(
                        POSTagger.class,
                        UimaUtil.MODEL_PARAMETER, taggerModel,
                        UimaUtil.SENTENCE_TYPE_PARAMETER, Sentence.class.getName(),
                        UimaUtil.TOKEN_TYPE_PARAMETER, Token.class.getName(),
                        UimaUtil.POS_FEATURE_PARAMETER, "POS");

        AnalysisEngineDescription lmMakerTxt
                = AnalysisEngineFactory.createEngineDescription(
                        LanguageModelMakerAE.class,
                        LanguageModelMakerAE.PARAM_TMP_FOLDER,
                        lmTmpFolder,
                        LanguageModelMakerAE.PARAM_OUT_FILE,
                        lmFile);

        AggregateBuilder builder = new AggregateBuilder(
                null,
                TypePrioritiesFactory.createTypePriorities(
                        Sentence.class,
                        Token.class),
                null);

        builder.add(mw2txt);
        builder.add(sentenceDetector,
                CAS.NAME_DEFAULT_SOFA, text);
        builder.add(tokenizer,
                CAS.NAME_DEFAULT_SOFA, text);
        builder.add(tagger,
                CAS.NAME_DEFAULT_SOFA, text);
        builder.add(lmMakerTxt,
                CAS.NAME_DEFAULT_SOFA, text);

        SimplePipeline.runPipeline(crd, builder.createAggregateDescription());
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
