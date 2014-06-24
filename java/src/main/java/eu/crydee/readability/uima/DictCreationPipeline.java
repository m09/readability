package eu.crydee.readability.uima;

import eu.crydee.readability.uima.ae.CoveredCopierAE;
import eu.crydee.readability.uima.ae.FilterDictAE;
import eu.crydee.readability.uima.ae.MediaWikiConverterAE;
import eu.crydee.readability.uima.ae.ResourcePopulatorAE;
import eu.crydee.readability.uima.ae.SaveableWriterAE;
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
import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;
import org.apache.uima.fit.factory.CollectionReaderFactory;
import static org.apache.uima.fit.factory.ExternalResourceFactory.createExternalResourceDescription;
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
                txtOriginal = "txtOriginal",
                htmlRevised = "htmlRevised",
                htmlOriginal = "htmlOriginal",
                rawRevised = "revised",
                rawOriginal = "original";

        /* Resources descriptions */
        ExternalResourceDescription tokenM = createExternalResourceDescription(
                TokenizerModelResourceImpl.class,
                "file:opennlp/uima/models/en-token.bin"),
                sentenceM = createExternalResourceDescription(
                        SentenceModelResourceImpl.class,
                        "file:opennlp/uima/models/en-sent.bin"),
                posM = createExternalResourceDescription(
                        POSModelResourceImpl.class,
                        "file:opennlp/uima/models/en-pos-maxent.bin"),
                parserM = createExternalResourceDescription(
                        ParserModelResourceImpl.class,
                        "file:opennlp/uima/models/en-parser-chunking.bin"),
                fullPos = createExternalResourceDescription(
                        ReadabilityDict_Impl.class, ""),
                filteredPos = createExternalResourceDescription(
                        ReadabilityDict_Impl.class, ""),
                fullTxt = createExternalResourceDescription(
                        ReadabilityDict_Impl.class, ""),
                filteredTxt = createExternalResourceDescription(
                        ReadabilityDict_Impl.class, "");

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
        AnalysisEngineDescription filter = createEngineDescription(
                RevisionsFilterAE.class);

        AnalysisEngineDescription mw2txtRevised = createEngineDescription(
                MediaWikiConverterAE.class,
                MediaWikiConverterAE.PARAM_OUT_VIEW_TXT, textRevised,
                MediaWikiConverterAE.PARAM_OUT_VIEW_HTML, htmlRevised,
                MediaWikiConverterAE.PARAM_LOWERCASE, true);

        AnalysisEngineDescription mw2txtOriginal = createEngineDescription(
                MediaWikiConverterAE.class,
                MediaWikiConverterAE.PARAM_OUT_VIEW_TXT, txtOriginal,
                MediaWikiConverterAE.PARAM_OUT_VIEW_HTML, htmlOriginal,
                MediaWikiConverterAE.PARAM_LOWERCASE, true);

        AnalysisEngineDescription getter = createEngineDescription(
                RevisionsGetterAE.class,
                RevisionsGetterAE.PARAM_DB_URL, DB_URL,
                RevisionsGetterAE.PARAM_DB_USER, DB_USR,
                RevisionsGetterAE.PARAM_DB_PASSWORD, DB_PW);

        AnalysisEngineDescription sentenceDetector = createEngineDescription(
                SentenceDetector.class,
                "opennlp.uima.ModelName", sentenceM,
                "opennlp.uima.SentenceType", Sentence.class.getCanonicalName());

        AnalysisEngineDescription tokenizer = createEngineDescription(
                Tokenizer.class,
                "opennlp.uima.ModelName", tokenM,
                "opennlp.uima.SentenceType", Sentence.class.getCanonicalName(),
                "opennlp.uima.TokenType", Token.class.getCanonicalName());

        AnalysisEngineDescription sentenceDiffer = createEngineDescription(
                SentenceDiffAE.class,
                SentenceDiffAE.PARAM_SENTENCE_TYPE,
                Sentence.class.getName(),
                SentenceDiffAE.PARAM_ORIGINAL_SENTENCES_TYPE,
                OriginalSentences.class.getName(),
                SentenceDiffAE.PARAM_REVISED_SENTENCES_TYPE,
                RevisedSentences.class.getName(),
                SentenceDiffAE.PARAM_ORIGINAL_SENTENCES_FEATURE,
                "originalSentences",
                SentenceDiffAE.PARAM_REVISED_SENTENCES_FEATURE,
                "revisedSentences");

        AnalysisEngineDescription taggerRevised = createEngineDescription(
                POSTagger.class,
                "opennlp.uima.ModelName", posM,
                "opennlp.uima.SentenceType", RevisedSentences.class.getName(),
                "opennlp.uima.TokenType", Token.class.getName(),
                "opennlp.uima.POSFeature", "POS");

        AnalysisEngineDescription taggerOriginal = createEngineDescription(
                POSTagger.class,
                "opennlp.uima.ModelName", posM,
                "opennlp.uima.SentenceType", OriginalSentences.class.getName(),
                "opennlp.uima.TokenType", Token.class.getName(),
                "opennlp.uima.POSFeature", "POS");

        AnalysisEngineDescription copierRevised = createEngineDescription(
                CoveredCopierAE.class,
                CoveredCopierAE.PARAM_CONTAINER_TYPE,
                RevisedSentences.class.getName(),
                CoveredCopierAE.PARAM_CHILD_TYPE,
                Sentence.class.getCanonicalName(),
                CoveredCopierAE.PARAM_NEW_CHILD_TYPE,
                RevisedSentence.class.getName());

        AnalysisEngineDescription copierOriginal = createEngineDescription(
                CoveredCopierAE.class,
                CoveredCopierAE.PARAM_CONTAINER_TYPE,
                OriginalSentences.class.getCanonicalName(),
                CoveredCopierAE.PARAM_CHILD_TYPE,
                Sentence.class.getCanonicalName(),
                CoveredCopierAE.PARAM_NEW_CHILD_TYPE,
                OriginalSentence.class.getCanonicalName());

        AnalysisEngineDescription parserRevised = createEngineDescription(
                Parser.class,
                "opennlp.uima.ModelName", parserM,
                "opennlp.uima.SentenceType", RevisedSentence.class.getName(),
                "opennlp.uima.TokenType", Token.class.getName(),
                "opennlp.uima.ParseType", Chunk.class.getName(),
                "opennlp.uima.TypeFeature", "label",
                "opennlp.uima.ChildrenFeature", "children");

        AnalysisEngineDescription parserOriginal = createEngineDescription(
                Parser.class,
                "opennlp.uima.ModelName", parserM,
                "opennlp.uima.SentenceType", OriginalSentence.class.getName(),
                "opennlp.uima.TokenType", Token.class.getName(),
                "opennlp.uima.ParseType", Chunk.class.getName(),
                "opennlp.uima.TypeFeature", "label",
                "opennlp.uima.ChildrenFeature", "children");

        AnalysisEngineDescription wordDiffer = createEngineDescription(
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

        AnalysisEngineDescription consumerXmi = createEngineDescription(
                XmiSerializerCreationAE.class,
                XmiSerializerCreationAE.PARAM_OUT_FOLDER, "out/cas");

        AnalysisEngineDescription resourcePopulator = createEngineDescription(
                ResourcePopulatorAE.class,
                ResourcePopulatorAE.RES_POS_KEY, fullPos,
                ResourcePopulatorAE.RES_TXT_KEY, fullTxt);

        AnalysisEngineDescription txtFilterer = createEngineDescription(
                FilterDictAE.class,
                FilterDictAE.RES_INPUT_KEY, fullTxt,
                FilterDictAE.RES_OUTPUT_KEY, filteredTxt);

        AnalysisEngineDescription posFilterer = createEngineDescription(
                FilterDictAE.class,
                FilterDictAE.RES_INPUT_KEY, fullPos,
                FilterDictAE.RES_OUTPUT_KEY, filteredPos);

        AnalysisEngineDescription fullTxtScorer = createEngineDescription(
                ScorerAE.class,
                ScorerAE.PARAM_LM_FILENAME, "out/lm/txt",
                ScorerAE.RES_KEY, fullTxt);

        AnalysisEngineDescription fullPosScorer = createEngineDescription(
                ScorerAE.class,
                ScorerAE.PARAM_LM_FILENAME, "out/lm/pos",
                ScorerAE.RES_KEY, fullPos);

        AnalysisEngineDescription filteredTxtScorer = createEngineDescription(
                ScorerAE.class,
                ScorerAE.PARAM_LM_FILENAME, "out/lm/txt",
                ScorerAE.RES_KEY, filteredTxt);

        AnalysisEngineDescription filteredPosScorer = createEngineDescription(
                ScorerAE.class,
                ScorerAE.PARAM_LM_FILENAME, "out/lm/pos",
                ScorerAE.RES_KEY, filteredPos);

        AnalysisEngineDescription fullTxtWriter = createEngineDescription(
                SaveableWriterAE.class,
                SaveableWriterAE.PARAM_FILENAME, "out/res/fullTxt.xml",
                SaveableWriterAE.RES_KEY, fullTxt);

        AnalysisEngineDescription fullPosWriter = createEngineDescription(
                SaveableWriterAE.class,
                SaveableWriterAE.PARAM_FILENAME, "out/res/fullPos.xml",
                SaveableWriterAE.RES_KEY, fullPos);

        AnalysisEngineDescription filteredTxtWriter = createEngineDescription(
                SaveableWriterAE.class,
                SaveableWriterAE.PARAM_FILENAME, "out/res/filteredTxt.xml",
                SaveableWriterAE.RES_KEY, filteredTxt);

        AnalysisEngineDescription filteredPosWriter = createEngineDescription(
                SaveableWriterAE.class,
                SaveableWriterAE.PARAM_FILENAME, "out/res/filteredPos.xml",
                SaveableWriterAE.RES_KEY, filteredPos);

        /* The type priority is important especially to retrieve tokens. The
         rest of the order is not accurate but it does not matter.*/
        AggregateBuilder b = new AggregateBuilder(
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
        b.add(filter);
        b.add(getter);
        b.add(mw2txtRevised, CAS.NAME_DEFAULT_SOFA, rawRevised);
        b.add(mw2txtOriginal, CAS.NAME_DEFAULT_SOFA, rawOriginal);
        b.add(sentenceDetector, CAS.NAME_DEFAULT_SOFA, textRevised);
        b.add(sentenceDetector, CAS.NAME_DEFAULT_SOFA, txtOriginal);
        b.add(tokenizer, CAS.NAME_DEFAULT_SOFA, textRevised);
        b.add(tokenizer, CAS.NAME_DEFAULT_SOFA, txtOriginal);
        b.add(sentenceDiffer, SentenceDiffAE.ORIGINAL_VIEW, txtOriginal,
                SentenceDiffAE.REVISED_VIEW, textRevised);
        b.add(taggerRevised, CAS.NAME_DEFAULT_SOFA, textRevised);
        b.add(taggerOriginal, CAS.NAME_DEFAULT_SOFA, txtOriginal);
        b.add(copierRevised, CAS.NAME_DEFAULT_SOFA, textRevised);
        b.add(copierOriginal, CAS.NAME_DEFAULT_SOFA, txtOriginal);
//        builder.add(parserRevised, CAS.NAME_DEFAULT_SOFA, textRevised);
//        builder.add(parserOriginal, CAS.NAME_DEFAULT_SOFA, textOriginal);
        b.add(wordDiffer, WordDiffAE.ORIGINAL_VIEW, txtOriginal,
                WordDiffAE.REVISED_VIEW, textRevised);
        b.add(consumerXmi);
        b.add(resourcePopulator, ResourcePopulatorAE.ORIGINAL_VIEW, txtOriginal,
                ResourcePopulatorAE.REVISED_VIEW, textRevised);
        b.add(txtFilterer);
        b.add(posFilterer);
        b.add(fullTxtScorer);
        b.add(fullPosScorer);
        b.add(filteredTxtScorer);
        b.add(filteredPosScorer);
        b.add(fullTxtWriter);
        b.add(fullPosWriter);
        b.add(filteredTxtWriter);
        b.add(filteredPosWriter);
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
