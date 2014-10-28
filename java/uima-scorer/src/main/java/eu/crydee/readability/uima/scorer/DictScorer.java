package eu.crydee.readability.uima.scorer;

import eu.crydee.readability.uima.core.ae.SaveableWriterAE;
import eu.crydee.readability.uima.core.res.ReadabilityDict_Impl;
import eu.crydee.readability.uima.scorer.ae.ScorerAE;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.factory.AggregateBuilder;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.factory.ExternalResourceFactory;
import org.apache.uima.resource.ExternalResourceDescription;
import org.apache.uima.resource.ResourceInitializationException;

public class DictScorer {

    private static String lmPath, dictPath;

    public static void main(String[] args)
            throws ResourceInitializationException,
            AnalysisEngineProcessException {
        parseArguments(args);
        ExternalResourceDescription dict
                = ExternalResourceFactory.createExternalResourceDescription(
                        ReadabilityDict_Impl.class, "file:" + dictPath);
        AggregateBuilder b = new AggregateBuilder();
        b.add(getEngine(dict, lmPath));
        b.add(AnalysisEngineFactory.createEngineDescription(
                SaveableWriterAE.class,
                SaveableWriterAE.RES_KEY, dict,
                SaveableWriterAE.PARAM_FILENAME, dictPath));
        b.createAggregate().collectionProcessComplete();
    }

    public static AnalysisEngineDescription getEngine(
            ExternalResourceDescription dict,
            String lmPath)
            throws ResourceInitializationException {
        return AnalysisEngineFactory.createEngineDescription(
                ScorerAE.class,
                ScorerAE.RES_KEY, dict,
                ScorerAE.PARAM_LM_FILENAME, lmPath);
    }

    static private void parseArguments(String[] args) {
        Options optionsHelp = new Options();
        optionsHelp.addOption("h", "help", false, "print this message");
        Options optionsRequired = new Options();
        optionsRequired.addOption("h", "help", false, "print this message");
        optionsRequired.addOption(OptionBuilder
                .withLongOpt("lmPath")
                .hasArg()
                .withArgName("filepath")
                .withDescription("Location of the language model.")
                .create("l"));
        optionsRequired.addOption(OptionBuilder
                .isRequired()
                .withLongOpt("dictPath")
                .hasArg()
                .withArgName("filepath")
                .withDescription("Location of the dictionary to score.")
                .create("d"));
        Options optionsAll = new Options();
        optionsHelp.getOptions().forEach(
                o -> optionsAll.addOption((Option) o));
        optionsRequired.getOptions().forEach(
                o -> optionsAll.addOption((Option) o));
        CommandLineParser parser = new PosixParser();
        @SuppressWarnings("UnusedAssignment")
        CommandLine cmd = null;
        try {
            cmd = parser.parse(optionsHelp, args, true);
            if (cmd.hasOption('h')) {
                HelpFormatter hf = new HelpFormatter();
                hf.printHelp("uima-scorer", optionsAll);
                System.exit(0);
            }
        } catch (ParseException ex) {
            System.err.println("The CLI args could not be parsed.");
            System.err.println("The error message was:");
            System.err.println(" " + ex.getMessage());
            System.exit(1);
        }

        try {
            cmd = parser.parse(optionsAll, args);
        } catch (ParseException ex) {
            System.err.println("The CLI args could not be parsed.");
            System.err.println("The error message was:");
            System.err.println(" " + ex.getMessage());
            System.exit(1);
        }

        dictPath = cmd.getOptionValue('d');
        lmPath = cmd.hasOption('l')
                ? cmd.getOptionValue('l')
                : FrequencyCreationPipeline.lmFile;
    }
}
