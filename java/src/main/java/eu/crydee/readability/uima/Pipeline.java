package eu.crydee.readability.uima;

import eu.crydee.readability.uima.ae.MediaWikiConverterAE;
import eu.crydee.readability.uima.ae.ResourceWriterAE;
import eu.crydee.readability.uima.ae.RevisionsFilterAE;
import eu.crydee.readability.uima.ae.RevisionsGetterAE;
import eu.crydee.readability.uima.ae.SentenceDiffAE;
import eu.crydee.readability.uima.ae.WordDiffAE;
import eu.crydee.readability.uima.ae.XmiSerializerAE;
import eu.crydee.readability.uima.cr.RevisionsCR;
import eu.crydee.readability.uima.res.ReadabilityDict_Impl;
import eu.crydee.readability.uima.ts.OriginalSentences;
import eu.crydee.readability.uima.ts.OriginalWords;
import eu.crydee.readability.uima.ts.RevisedSentences;
import eu.crydee.readability.uima.ts.RevisedWords;
import eu.crydee.readability.uima.ts.Sentence;
import eu.crydee.readability.uima.ts.Token;
import opennlp.uima.postag.POSModelResourceImpl;
import opennlp.uima.postag.POSTagger;
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
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.factory.CollectionReaderFactory;
import org.apache.uima.fit.factory.ExternalResourceFactory;
import org.apache.uima.fit.factory.FlowControllerFactory;
import org.apache.uima.fit.factory.TypePrioritiesFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.flow.impl.FixedFlowController;
import org.apache.uima.resource.ExternalResourceDescription;

public class Pipeline {

    static private String DB_URL = null,
            DB_USR = null,
            DB_PW = null;

    public static void main(String[] args) throws Exception {
        parseArguments(args);

        /* Resources descriptions */
        ExternalResourceDescription tokenModel
                = ExternalResourceFactory.createExternalResourceDescription(
                        TokenizerModelResourceImpl.class,
                        "file:opennlp/uima/models/en-token.bin");

        ExternalResourceDescription sentenceModel
                = ExternalResourceFactory.createExternalResourceDescription(
                        SentenceModelResourceImpl.class,
                        "file:opennlp/uima/models/en-sent.bin");

        ExternalResourceDescription posModel
                = ExternalResourceFactory.createExternalResourceDescription(
                        POSModelResourceImpl.class,
                        "file:opennlp/uima/models/en-pos-maxent.bin");

        CollectionReaderDescription crd
                = CollectionReaderFactory.createReaderDescription(
                        RevisionsCR.class,
                        RevisionsCR.PARAM_DB_URL,
                        DB_URL,
                        RevisionsCR.PARAM_DB_USER,
                        DB_USR,
                        RevisionsCR.PARAM_DB_PASSWORD,
                        DB_PW);
        ExternalResourceDescription dict
                = ExternalResourceFactory.createExternalResourceDescription(
                        ReadabilityDict_Impl.class,
                        "");

        AnalysisEngineDescription filter
                = AnalysisEngineFactory.createEngineDescription(
                        RevisionsFilterAE.class);

        AnalysisEngineDescription mw2txtRevised
                = AnalysisEngineFactory.createEngineDescription(
                        MediaWikiConverterAE.class,
                        MediaWikiConverterAE.PARAM_OUT_VIEW_TXT,
                        "txtRevised",
                        MediaWikiConverterAE.PARAM_OUT_VIEW_HTML,
                        "htmlRevised");

        AnalysisEngineDescription mw2txtOriginal
                = AnalysisEngineFactory.createEngineDescription(
                        MediaWikiConverterAE.class,
                        MediaWikiConverterAE.PARAM_OUT_VIEW_TXT,
                        "txtOriginal",
                        MediaWikiConverterAE.PARAM_OUT_VIEW_HTML,
                        "htmlOriginal");

        AnalysisEngineDescription getter
                = AnalysisEngineFactory.createEngineDescription(
                        RevisionsGetterAE.class,
                        RevisionsGetterAE.PARAM_DB_URL,
                        DB_URL,
                        RevisionsGetterAE.PARAM_DB_USER,
                        DB_USR,
                        RevisionsGetterAE.PARAM_DB_PASSWORD,
                        DB_PW);

        AnalysisEngineDescription sentenceDetector
                = AnalysisEngineFactory.createEngineDescription(
                        SentenceDetector.class,
                        "opennlp.uima.ModelName",
                        sentenceModel,
                        "opennlp.uima.SentenceType",
                        "eu.crydee.readability.uima.ts.Sentence");

        AnalysisEngineDescription tokenizer
                = AnalysisEngineFactory.createEngineDescription(
                        Tokenizer.class,
                        "opennlp.uima.ModelName",
                        tokenModel,
                        "opennlp.uima.SentenceType",
                        "eu.crydee.readability.uima.ts.Sentence",
                        "opennlp.uima.TokenType",
                        "eu.crydee.readability.uima.ts.Token");

        AnalysisEngineDescription sentenceDiffer
                = AnalysisEngineFactory.createEngineDescription(
                        SentenceDiffAE.class,
                        SentenceDiffAE.PARAM_SENTENCE_TYPE,
                        "eu.crydee.readability.uima.ts.Sentence",
                        SentenceDiffAE.PARAM_ORIGINAL_SENTENCES_TYPE,
                        "eu.crydee.readability.uima.ts.OriginalSentences",
                        SentenceDiffAE.PARAM_REVISED_SENTENCES_TYPE,
                        "eu.crydee.readability.uima.ts.RevisedSentences",
                        SentenceDiffAE.PARAM_ORIGINAL_SENTENCES_FEATURE,
                        "originalSentences",
                        SentenceDiffAE.PARAM_REVISED_SENTENCES_FEATURE,
                        "revisedSentences");

        AnalysisEngineDescription taggerRevised
                = AnalysisEngineFactory.createEngineDescription(
                        POSTagger.class,
                        "opennlp.uima.ModelName",
                        posModel,
                        "opennlp.uima.SentenceType",
                        "eu.crydee.readability.uima.ts.RevisedSentences",
                        "opennlp.uima.TokenType",
                        "eu.crydee.readability.uima.ts.Token",
                        "opennlp.uima.POSFeature",
                        "POS");

        AnalysisEngineDescription taggerOriginal
                = AnalysisEngineFactory.createEngineDescription(
                        POSTagger.class,
                        "opennlp.uima.ModelName",
                        posModel,
                        "opennlp.uima.SentenceType",
                        "eu.crydee.readability.uima.ts.OriginalSentences",
                        "opennlp.uima.TokenType",
                        "eu.crydee.readability.uima.ts.Token",
                        "opennlp.uima.POSFeature",
                        "POS");

        AnalysisEngineDescription wordDiffer
                = AnalysisEngineFactory.createEngineDescription(
                        WordDiffAE.class,
                        WordDiffAE.PARAM_SENTENCE_TYPE,
                        "eu.crydee.readability.uima.ts.Sentence",
                        WordDiffAE.PARAM_TOKEN_TYPE,
                        "eu.crydee.readability.uima.ts.Token",
                        WordDiffAE.PARAM_ORIGINAL_SENTENCES_TYPE,
                        "eu.crydee.readability.uima.ts.OriginalSentences",
                        WordDiffAE.PARAM_REVISED_SENTENCES_TYPE,
                        "eu.crydee.readability.uima.ts.RevisedSentences",
                        WordDiffAE.PARAM_ORIGINAL_WORDS_TYPE,
                        "eu.crydee.readability.uima.ts.OriginalWords",
                        WordDiffAE.PARAM_REVISED_WORDS_TYPE,
                        "eu.crydee.readability.uima.ts.RevisedWords",
                        WordDiffAE.PARAM_ORIGINAL_SENTENCES_FEATURE,
                        "originalSentences",
                        WordDiffAE.PARAM_REVISED_SENTENCES_FEATURE,
                        "revisedSentences",
                        WordDiffAE.PARAM_ORIGINAL_WORDS_FEATURE,
                        "originalWords",
                        WordDiffAE.PARAM_REVISED_WORDS_FEATURE,
                        "revisedWords");

        AnalysisEngineDescription consumer
                = AnalysisEngineFactory.createEngineDescription(
                        XmiSerializerAE.class,
                        XmiSerializerAE.PARAM_OUT_FOLDER, "out");
        AnalysisEngineDescription consumerResource
                = AnalysisEngineFactory.createEngineDescription(
                        ResourceWriterAE.class,
                        ResourceWriterAE.PARAM_OUT_PS,
                        "dict.xml",
                        ResourceWriterAE.RES_KEY,
                        dict);

        /* The type priority is important especially to retrieve tokens. The
         rest of the order is not accurate but it does not matter.*/
        AggregateBuilder builder = new AggregateBuilder(
                null,
                TypePrioritiesFactory.createTypePriorities(
                        OriginalSentences.class,
                        RevisedSentences.class,
                        OriginalWords.class,
                        RevisedWords.class,
                        Sentence.class,
                        Token.class),
                FlowControllerFactory.createFlowControllerDescription(
                        FixedFlowController.class,
                        FixedFlowController.PARAM_ACTION_AFTER_CAS_MULTIPLIER,
                        "drop"));
        builder.add(filter);
        builder.add(getter);
        builder.add(mw2txtRevised,
                CAS.NAME_DEFAULT_SOFA, "revised");
        builder.add(mw2txtOriginal,
                CAS.NAME_DEFAULT_SOFA, "original");
        builder.add(sentenceDetector,
                CAS.NAME_DEFAULT_SOFA, "txtRevised");
        builder.add(sentenceDetector,
                CAS.NAME_DEFAULT_SOFA, "txtOriginal");
        builder.add(tokenizer,
                CAS.NAME_DEFAULT_SOFA, "txtRevised");
        builder.add(tokenizer,
                CAS.NAME_DEFAULT_SOFA, "txtOriginal");
        builder.add(sentenceDiffer,
                SentenceDiffAE.ORIGINAL_VIEW, "txtOriginal",
                SentenceDiffAE.REVISED_VIEW, "txtRevised");
        builder.add(taggerRevised,
                CAS.NAME_DEFAULT_SOFA, "txtRevised");
        builder.add(taggerOriginal,
                CAS.NAME_DEFAULT_SOFA, "txtOriginal");
        builder.add(wordDiffer,
                WordDiffAE.ORIGINAL_VIEW, "txtOriginal",
                WordDiffAE.REVISED_VIEW, "txtRevised");
        builder.add(consumer);
        builder.add(consumerResource,
                ResourceWriterAE.ORIGINAL_VIEW, "txtOriginal",
                ResourceWriterAE.REVISED_VIEW, "txtRevised");

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
