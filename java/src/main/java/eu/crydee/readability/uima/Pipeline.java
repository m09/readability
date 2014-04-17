package eu.crydee.readability.uima;

import eu.crydee.readability.uima.ae.MediaWikiConverterAE;
import eu.crydee.readability.uima.ae.RevisionsFilterAE;
import eu.crydee.readability.uima.ae.RevisionsGetterAE;
import eu.crydee.readability.uima.ae.XmiSerializerAE;
import eu.crydee.readability.uima.cr.RevisionsCR;
import opennlp.uima.sentdetect.SentenceDetector;
import opennlp.uima.sentdetect.SentenceModelResourceImpl;
import opennlp.uima.tokenize.Tokenizer;
import opennlp.uima.tokenize.TokenizerModelResourceImpl;
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
import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;
import static org.apache.uima.fit.factory.CollectionReaderFactory.createReaderDescription;
import static org.apache.uima.fit.factory.ExternalResourceFactory.createExternalResourceDescription;
import org.apache.uima.fit.factory.FlowControllerFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.flow.impl.FixedFlowController;

public class Pipeline {

    static private String DB_URL = null,
            DB_USR = null,
            DB_PW = null;

    public static void main(String[] args) throws Exception {
        parseArguments(args);
        CollectionReaderDescription crd = createReaderDescription(
                RevisionsCR.class,
                RevisionsCR.PARAM_DB_URL,
                DB_URL,
                RevisionsCR.PARAM_DB_USER,
                DB_USR,
                RevisionsCR.PARAM_DB_PASSWORD,
                DB_PW);

        AnalysisEngineDescription filter = createEngineDescription(
                RevisionsFilterAE.class);

        AnalysisEngineDescription mw2txtRevised = createEngineDescription(
                MediaWikiConverterAE.class,
                MediaWikiConverterAE.PARAM_OUT_VIEW_TXT,
                "txtRevised",
                MediaWikiConverterAE.PARAM_OUT_VIEW_HTML,
                "htmlRevised");

        AnalysisEngineDescription mw2txtOriginal = createEngineDescription(
                MediaWikiConverterAE.class,
                MediaWikiConverterAE.PARAM_OUT_VIEW_TXT,
                "txtOriginal",
                MediaWikiConverterAE.PARAM_OUT_VIEW_HTML,
                "htmlOriginal");

        AnalysisEngineDescription getter = createEngineDescription(
                RevisionsGetterAE.class,
                RevisionsGetterAE.PARAM_DB_URL,
                DB_URL,
                RevisionsGetterAE.PARAM_DB_USER,
                DB_USR,
                RevisionsGetterAE.PARAM_DB_PASSWORD,
                DB_PW);

        AnalysisEngineDescription sentenceDetector = createEngineDescription(
                SentenceDetector.class,
                "opennlp.uima.ModelName",
                createExternalResourceDescription(
                        SentenceModelResourceImpl.class,
                        "file:opennlp/uima/models/en-sent.bin"),
                "opennlp.uima.SentenceType",
                "eu.crydee.readability.uima.ts.Sentence");

        AnalysisEngineDescription tokenizer = createEngineDescription(
                Tokenizer.class,
                "opennlp.uima.ModelName",
                createExternalResourceDescription(
                        TokenizerModelResourceImpl.class,
                        "file:opennlp/uima/models/en-token.bin"),
                "opennlp.uima.SentenceType",
                "eu.crydee.readability.uima.ts.Sentence",
                "opennlp.uima.TokenType",
                "eu.crydee.readability.uima.ts.Token");

        AnalysisEngineDescription consumer = createEngineDescription(
                XmiSerializerAE.class,
                XmiSerializerAE.PARAM_OUT_FOLDER, "out");

        AggregateBuilder builder = new AggregateBuilder();
        builder.add(getter);
        builder.add(mw2txtRevised);
        builder.add(mw2txtOriginal, CAS.NAME_DEFAULT_SOFA, "original");
        builder.add(sentenceDetector, CAS.NAME_DEFAULT_SOFA, "txtRevised");
        builder.add(sentenceDetector, CAS.NAME_DEFAULT_SOFA, "txtOriginal");
        builder.add(tokenizer, CAS.NAME_DEFAULT_SOFA, "txtRevised");
        builder.add(tokenizer, CAS.NAME_DEFAULT_SOFA, "txtOriginal");
        builder.add(consumer);

        AnalysisEngineDescription aae = createEngineDescription(
                FlowControllerFactory.createFlowControllerDescription(
                        FixedFlowController.class,
                        FixedFlowController.PARAM_ACTION_AFTER_CAS_MULTIPLIER,
                        "drop"),
                filter,
                builder.createAggregateDescription());

        SimplePipeline.runPipeline(crd, aae);
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
    }
}
