package eu.crydee.readability.uima;

import eu.crydee.readability.uima.ae.ConsumerAE;
import eu.crydee.readability.uima.ae.RevisionsFilterAE;
import eu.crydee.readability.uima.ae.RevisionsGetterAE;
import eu.crydee.readability.uima.cr.RevisionsCR;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.collection.CollectionReaderDescription;
import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;
import static org.apache.uima.fit.factory.CollectionReaderFactory.createReaderDescription;
import org.apache.uima.fit.factory.FlowControllerFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.flow.impl.FixedFlowController;

public class Pipeline {

    public static void main(String[] args) throws Exception {
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
        final String DB_URL = cmd.getOptionValue('d'),
                DB_USER = cmd.getOptionValue('u'),
                DB_PW = cmd.getOptionValue('p');

        CollectionReaderDescription crd = createReaderDescription(
                RevisionsCR.class,
                RevisionsCR.PARAM_DB_URL,
                DB_URL,
                RevisionsCR.PARAM_DB_USER,
                DB_USER,
                RevisionsCR.PARAM_DB_PASSWORD,
                DB_PW);

        AnalysisEngineDescription filter = createEngineDescription(
                RevisionsFilterAE.class);

        AnalysisEngineDescription getter = createEngineDescription(
                RevisionsGetterAE.class,
                RevisionsGetterAE.PARAM_DB_URL,
                DB_URL,
                RevisionsGetterAE.PARAM_DB_USER,
                DB_USER,
                RevisionsGetterAE.PARAM_DB_PASSWORD,
                DB_PW);

        AnalysisEngineDescription consumer = createEngineDescription(
                ConsumerAE.class);

        AnalysisEngineDescription aae = createEngineDescription(
                FlowControllerFactory.createFlowControllerDescription(
                        FixedFlowController.class,
                        FixedFlowController.PARAM_ACTION_AFTER_CAS_MULTIPLIER,
                        "drop"),
                filter,
                getter,
                consumer);

        SimplePipeline.runPipeline(crd, aae);
    }
}
