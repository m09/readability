package eu.crydee.readability.uima;

import eu.crydee.readability.uima.ae.CoveredCopierAE;
import eu.crydee.readability.uima.ae.FilterDictAE;
import eu.crydee.readability.uima.ae.MediaWikiConverterAE;
import eu.crydee.readability.uima.ae.ResourcePopulatorAE;
import eu.crydee.readability.uima.ae.ResourceWriterAE;
import eu.crydee.readability.uima.ae.RevisionsFilterAE;
import eu.crydee.readability.uima.ae.RevisionsGetterAE;
import eu.crydee.readability.uima.ae.ScorerAE;
import eu.crydee.readability.uima.ae.SentenceDiffAE;
import eu.crydee.readability.uima.ae.WordDiffAE;
import eu.crydee.readability.uima.ae.XmiSerializerCreationAE;
import eu.crydee.readability.uima.cr.RevisionsCR;
import eu.crydee.readability.uima.res.ReadabilityDict_Impl;
import eu.crydee.readability.uima.ts.Chunk;
import eu.crydee.readability.uima.ts.OriginalSentence;
import eu.crydee.readability.uima.ts.OriginalSentences;
import eu.crydee.readability.uima.ts.OriginalWords;
import eu.crydee.readability.uima.ts.RevisedSentence;
import eu.crydee.readability.uima.ts.RevisedSentences;
import eu.crydee.readability.uima.ts.RevisedWords;
import eu.crydee.readability.uima.ts.Sentence;
import eu.crydee.readability.uima.ts.Token;
import java.util.Optional;
import opennlp.uima.parser.Parser;
import opennlp.uima.parser.ParserModelResourceImpl;
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

public class DictCreationPipeline {

    static private String DB_URL = null,
            DB_USR = null,
            DB_PW = null;
    static private Optional<Integer> LIMIT = Optional.empty();

    public static void main(String[] args) throws Exception {
        parseArguments(args);

        /* View names */
        final String textRevised = "txtRevised",
                textOriginal = "txtOriginal",
                htmlRevised = "htmlRevised",
                htmlOriginal = "htmlOriginal",
                rawRevised = "revised",
                rawOriginal = "original";

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

        ExternalResourceDescription parserModel
                = ExternalResourceFactory.createExternalResourceDescription(
                        ParserModelResourceImpl.class,
                        "file:opennlp/uima/models/en-parser-chunking.bin");

        ExternalResourceDescription fullPos
                = ExternalResourceFactory.createExternalResourceDescription(
                        ReadabilityDict_Impl.class,
                        "");

        ExternalResourceDescription filteredPos
                = ExternalResourceFactory.createExternalResourceDescription(
                        ReadabilityDict_Impl.class,
                        "");

        ExternalResourceDescription fullTxt
                = ExternalResourceFactory.createExternalResourceDescription(
                        ReadabilityDict_Impl.class,
                        "");

        ExternalResourceDescription filteredTxt
                = ExternalResourceFactory.createExternalResourceDescription(
                        ReadabilityDict_Impl.class,
                        "");

        /* Collection reader */
        CollectionReaderDescription crd;
        if (LIMIT.isPresent()) {
            crd = CollectionReaderFactory.createReaderDescription(
                    RevisionsCR.class,
                    RevisionsCR.PARAM_DB_URL,
                    DB_URL,
                    RevisionsCR.PARAM_DB_USER,
                    DB_USR,
                    RevisionsCR.PARAM_DB_PASSWORD,
                    DB_PW,
                    RevisionsCR.PARAM_LIMIT,
                    LIMIT.get());
        } else {
            crd = CollectionReaderFactory.createReaderDescription(
                    RevisionsCR.class,
                    RevisionsCR.PARAM_DB_URL,
                    DB_URL,
                    RevisionsCR.PARAM_DB_USER,
                    DB_USR,
                    RevisionsCR.PARAM_DB_PASSWORD,
                    DB_PW);
        }

        /* Analysis engines */
        AnalysisEngineDescription filter
                = AnalysisEngineFactory.createEngineDescription(
                        RevisionsFilterAE.class);

        AnalysisEngineDescription mw2txtRevised
                = AnalysisEngineFactory.createEngineDescription(
                        MediaWikiConverterAE.class,
                        MediaWikiConverterAE.PARAM_OUT_VIEW_TXT,
                        textRevised,
                        MediaWikiConverterAE.PARAM_OUT_VIEW_HTML,
                        htmlRevised,
                        MediaWikiConverterAE.PARAM_LOWERCASE,
                        true);

        AnalysisEngineDescription mw2txtOriginal
                = AnalysisEngineFactory.createEngineDescription(
                        MediaWikiConverterAE.class,
                        MediaWikiConverterAE.PARAM_OUT_VIEW_TXT,
                        textOriginal,
                        MediaWikiConverterAE.PARAM_OUT_VIEW_HTML,
                        htmlOriginal,
                        MediaWikiConverterAE.PARAM_LOWERCASE,
                        true);

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
                        Sentence.class.getCanonicalName());

        AnalysisEngineDescription tokenizer
                = AnalysisEngineFactory.createEngineDescription(
                        Tokenizer.class,
                        "opennlp.uima.ModelName",
                        tokenModel,
                        "opennlp.uima.SentenceType",
                        Sentence.class.getCanonicalName(),
                        "opennlp.uima.TokenType",
                        Token.class.getCanonicalName());

        AnalysisEngineDescription sentenceDiffer
                = AnalysisEngineFactory.createEngineDescription(
                        SentenceDiffAE.class,
                        SentenceDiffAE.PARAM_SENTENCE_TYPE,
                        Sentence.class.getCanonicalName(),
                        SentenceDiffAE.PARAM_ORIGINAL_SENTENCES_TYPE,
                        OriginalSentences.class.getCanonicalName(),
                        SentenceDiffAE.PARAM_REVISED_SENTENCES_TYPE,
                        RevisedSentences.class.getCanonicalName(),
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
                        RevisedSentences.class.getCanonicalName(),
                        "opennlp.uima.TokenType",
                        Token.class.getCanonicalName(),
                        "opennlp.uima.POSFeature",
                        "POS");

        AnalysisEngineDescription taggerOriginal
                = AnalysisEngineFactory.createEngineDescription(
                        POSTagger.class,
                        "opennlp.uima.ModelName",
                        posModel,
                        "opennlp.uima.SentenceType",
                        OriginalSentences.class.getCanonicalName(),
                        "opennlp.uima.TokenType",
                        Token.class.getCanonicalName(),
                        "opennlp.uima.POSFeature",
                        "POS");

        AnalysisEngineDescription coveredCopierRevised
                = AnalysisEngineFactory.createEngineDescription(
                        CoveredCopierAE.class,
                        CoveredCopierAE.PARAM_CONTAINER_TYPE,
                        RevisedSentences.class.getCanonicalName(),
                        CoveredCopierAE.PARAM_CHILD_TYPE,
                        Sentence.class.getCanonicalName(),
                        CoveredCopierAE.PARAM_NEW_CHILD_TYPE,
                        RevisedSentence.class.getCanonicalName());

        AnalysisEngineDescription coveredCopierOriginal
                = AnalysisEngineFactory.createEngineDescription(
                        CoveredCopierAE.class,
                        CoveredCopierAE.PARAM_CONTAINER_TYPE,
                        OriginalSentences.class.getCanonicalName(),
                        CoveredCopierAE.PARAM_CHILD_TYPE,
                        Sentence.class.getCanonicalName(),
                        CoveredCopierAE.PARAM_NEW_CHILD_TYPE,
                        OriginalSentence.class.getCanonicalName());

        AnalysisEngineDescription parserRevised
                = AnalysisEngineFactory.createEngineDescription(
                        Parser.class,
                        "opennlp.uima.ModelName",
                        parserModel,
                        "opennlp.uima.SentenceType",
                        RevisedSentence.class.getCanonicalName(),
                        "opennlp.uima.TokenType",
                        Token.class.getCanonicalName(),
                        "opennlp.uima.ParseType",
                        Chunk.class.getCanonicalName(),
                        "opennlp.uima.TypeFeature",
                        "label",
                        "opennlp.uima.ChildrenFeature",
                        "children");

        AnalysisEngineDescription parserOriginal
                = AnalysisEngineFactory.createEngineDescription(
                        Parser.class,
                        "opennlp.uima.ModelName",
                        parserModel,
                        "opennlp.uima.SentenceType",
                        OriginalSentence.class.getCanonicalName(),
                        "opennlp.uima.TokenType",
                        Token.class.getCanonicalName(),
                        "opennlp.uima.ParseType",
                        Chunk.class.getCanonicalName(),
                        "opennlp.uima.TypeFeature",
                        "label",
                        "opennlp.uima.ChildrenFeature",
                        "children");

        AnalysisEngineDescription wordDiffer
                = AnalysisEngineFactory.createEngineDescription(
                        WordDiffAE.class,
                        WordDiffAE.PARAM_SENTENCE_TYPE,
                        Sentence.class.getCanonicalName(),
                        WordDiffAE.PARAM_TOKEN_TYPE,
                        Token.class.getCanonicalName(),
                        WordDiffAE.PARAM_ORIGINAL_SENTENCES_TYPE,
                        OriginalSentences.class.getCanonicalName(),
                        WordDiffAE.PARAM_REVISED_SENTENCES_TYPE,
                        RevisedSentences.class.getCanonicalName(),
                        WordDiffAE.PARAM_ORIGINAL_WORDS_TYPE,
                        OriginalWords.class.getCanonicalName(),
                        WordDiffAE.PARAM_REVISED_WORDS_TYPE,
                        RevisedWords.class.getCanonicalName(),
                        WordDiffAE.PARAM_ORIGINAL_SENTENCES_FEATURE,
                        "originalSentences",
                        WordDiffAE.PARAM_REVISED_SENTENCES_FEATURE,
                        "revisedSentences",
                        WordDiffAE.PARAM_ORIGINAL_WORDS_FEATURE,
                        "originalWords",
                        WordDiffAE.PARAM_REVISED_WORDS_FEATURE,
                        "revisedWords");

        AnalysisEngineDescription consumerXmi
                = AnalysisEngineFactory.createEngineDescription(
                        XmiSerializerCreationAE.class,
                        XmiSerializerCreationAE.PARAM_OUT_FOLDER,
                        "out/cas");

        AnalysisEngineDescription resourcePopulator
                = AnalysisEngineFactory.createEngineDescription(
                        ResourcePopulatorAE.class,
                        ResourcePopulatorAE.RES_POS_KEY,
                        fullPos,
                        ResourcePopulatorAE.RES_TXT_KEY,
                        fullTxt);

        AnalysisEngineDescription txtFilterer
                = AnalysisEngineFactory.createEngineDescription(
                        FilterDictAE.class,
                        FilterDictAE.RES_INPUT_KEY,
                        fullTxt,
                        FilterDictAE.RES_OUTPUT_KEY,
                        filteredTxt);

        AnalysisEngineDescription posFilterer
                = AnalysisEngineFactory.createEngineDescription(
                        FilterDictAE.class,
                        FilterDictAE.RES_INPUT_KEY,
                        fullPos,
                        FilterDictAE.RES_OUTPUT_KEY,
                        filteredPos);

        AnalysisEngineDescription fullTxtScorer
                = AnalysisEngineFactory.createEngineDescription(
                        ScorerAE.class,
                        ScorerAE.PARAM_LM_FILENAME,
                        "out/lm/txt",
                        ScorerAE.RES_KEY,
                        fullTxt);

        AnalysisEngineDescription fullPosScorer
                = AnalysisEngineFactory.createEngineDescription(
                        ScorerAE.class,
                        ScorerAE.PARAM_LM_FILENAME,
                        "out/lm/pos",
                        ScorerAE.RES_KEY,
                        fullPos);

        AnalysisEngineDescription filteredTxtScorer
                = AnalysisEngineFactory.createEngineDescription(
                        ScorerAE.class,
                        ScorerAE.PARAM_LM_FILENAME,
                        "out/lm/txt",
                        ScorerAE.RES_KEY,
                        filteredTxt);

        AnalysisEngineDescription filteredPosScorer
                = AnalysisEngineFactory.createEngineDescription(
                        ScorerAE.class,
                        ScorerAE.PARAM_LM_FILENAME,
                        "out/lm/pos",
                        ScorerAE.RES_KEY,
                        filteredPos);

        AnalysisEngineDescription fullTxtWriter
                = AnalysisEngineFactory.createEngineDescription(
                        ResourceWriterAE.class,
                        ResourceWriterAE.PARAM_FILENAME,
                        "out/res/fullTxt.xml",
                        ResourceWriterAE.RES_KEY,
                        fullTxt);

        AnalysisEngineDescription fullPosWriter
                = AnalysisEngineFactory.createEngineDescription(
                        ResourceWriterAE.class,
                        ResourceWriterAE.PARAM_FILENAME,
                        "out/res/fullPos.xml",
                        ResourceWriterAE.RES_KEY,
                        fullPos);

        AnalysisEngineDescription filteredTxtWriter
                = AnalysisEngineFactory.createEngineDescription(
                        ResourceWriterAE.class,
                        ResourceWriterAE.PARAM_FILENAME,
                        "out/res/filteredTxt.xml",
                        ResourceWriterAE.RES_KEY,
                        filteredTxt);

        AnalysisEngineDescription filteredPosWriter
                = AnalysisEngineFactory.createEngineDescription(
                        ResourceWriterAE.class,
                        ResourceWriterAE.PARAM_FILENAME,
                        "out/res/filteredPos.xml",
                        ResourceWriterAE.RES_KEY,
                        filteredPos);

        /* The type priority is important especially to retrieve tokens. The
         rest of the order is not accurate but it does not matter.*/
        AggregateBuilder builder = new AggregateBuilder(
                null,
                TypePrioritiesFactory.createTypePriorities(
                        OriginalSentences.class,
                        RevisedSentences.class,
                        OriginalWords.class,
                        RevisedWords.class,
                        OriginalSentence.class,
                        RevisedSentence.class,
                        Sentence.class,
                        Token.class),
                FlowControllerFactory.createFlowControllerDescription(
                        FixedFlowController.class,
                        FixedFlowController.PARAM_ACTION_AFTER_CAS_MULTIPLIER,
                        "drop"));

        builder.add("filter", filter);

        builder.add("getter", getter);

        builder.add("mediawiki-importer-revised", mw2txtRevised,
                CAS.NAME_DEFAULT_SOFA, rawRevised);

        builder.add("mediawiki-importer-original", mw2txtOriginal,
                CAS.NAME_DEFAULT_SOFA, rawOriginal);

        builder.add("sentence-detector-revised", sentenceDetector,
                CAS.NAME_DEFAULT_SOFA, textRevised);

        builder.add("sentence-detector-original", sentenceDetector,
                CAS.NAME_DEFAULT_SOFA, textOriginal);

        builder.add("tokenizer-revised", tokenizer,
                CAS.NAME_DEFAULT_SOFA, textRevised);

        builder.add("tokenizer-original", tokenizer,
                CAS.NAME_DEFAULT_SOFA, textOriginal);

        builder.add("sentence-differ", sentenceDiffer,
                SentenceDiffAE.ORIGINAL_VIEW, textOriginal,
                SentenceDiffAE.REVISED_VIEW, textRevised);

        builder.add("tagger-revised", taggerRevised,
                CAS.NAME_DEFAULT_SOFA, textRevised);

        builder.add("tagger-original", taggerOriginal,
                CAS.NAME_DEFAULT_SOFA, textOriginal);

        builder.add("covered-copier-revised", coveredCopierRevised,
                CAS.NAME_DEFAULT_SOFA, textRevised);

        builder.add("covered-copier-original", coveredCopierOriginal,
                CAS.NAME_DEFAULT_SOFA, textOriginal);

//        builder.add("parser-revised", parserRevised,
//                CAS.NAME_DEFAULT_SOFA, textRevised);
//
//        builder.add("parser-original", parserOriginal,
//                CAS.NAME_DEFAULT_SOFA, textOriginal);
//
        builder.add("word-differ", wordDiffer,
                WordDiffAE.ORIGINAL_VIEW, textOriginal,
                WordDiffAE.REVISED_VIEW, textRevised);

        builder.add("xmi-consumer", consumerXmi);

        builder.add("resource-populator", resourcePopulator,
                ResourcePopulatorAE.ORIGINAL_VIEW, textOriginal,
                ResourcePopulatorAE.REVISED_VIEW, textRevised);

        builder.add("txt-filterer", txtFilterer);

        builder.add("pos-filterer", posFilterer);

        builder.add("full-txt-scorer", fullTxtScorer);

        builder.add("full-pos-scorer", fullPosScorer);

        builder.add("filtered-txt-scorer", filteredTxtScorer);

        builder.add("filtered-pos-scorer", filteredPosScorer);

        builder.add("full-txt-writer", fullTxtWriter);

        builder.add("full-pos-writer", fullPosWriter);

        builder.add("filtered-txt-writer", filteredTxtWriter);

        builder.add("filtered-pos-writer", filteredPosWriter);

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

        if (cmd.hasOption('l')) {
            LIMIT = Optional.of(Integer.parseInt(cmd.getOptionValue('l')));
        }
    }
}
