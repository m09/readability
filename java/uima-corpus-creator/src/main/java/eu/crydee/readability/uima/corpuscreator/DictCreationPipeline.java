package eu.crydee.readability.uima.corpuscreator;

import eu.crydee.readability.uima.corpuscreator.ae.FilterDictAE;
import eu.crydee.readability.uima.core.ae.MediaWikiConverterAE;
import eu.crydee.readability.uima.corpuscreator.ae.ResourcePopulatorAE;
import eu.crydee.readability.uima.core.ae.SaveableWriterAE;
import eu.crydee.readability.uima.corpuscreator.ae.RevisionsFilterAE;
import eu.crydee.readability.uima.corpuscreator.ae.RevisionsGetterAE;
import eu.crydee.readability.uima.corpuscreator.ae.SentenceDiffAE;
import eu.crydee.readability.uima.corpuscreator.ae.WordDiffAE;
import eu.crydee.readability.uima.corpuscreator.ae.XmiSerializerCreationAE;
import eu.crydee.readability.uima.corpuscreator.cr.RevisionsCR;
import eu.crydee.uima.opennlp.resources.EnTokenModel;
import eu.crydee.uima.opennlp.resources.EnPosMaxentModel;
import eu.crydee.uima.opennlp.resources.EnSentModel;
import eu.crydee.readability.uima.core.res.ReadabilityDict_Impl;
import eu.crydee.readability.uima.corpuscreator.ts.OriginalSentences;
import eu.crydee.readability.uima.corpuscreator.ts.OriginalWords;
import eu.crydee.readability.uima.corpuscreator.ts.RevisedSentences;
import eu.crydee.readability.uima.corpuscreator.ts.RevisedWords;
import eu.crydee.readability.uima.core.ts.Sentence;
import eu.crydee.readability.uima.core.ts.Token;
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

        /* Create output dirs */
        new File("out/cas").mkdirs();
        new File("out/res").mkdirs();

        /* View names */
        final String txtRevised = "txtRevised",
                txtOriginal = "txtOriginal",
                htmlRevised = "htmlRevised",
                htmlOriginal = "htmlOriginal",
                rawRevised = "revised",
                rawOriginal = "original";

        /* Resources descriptions */
        ExternalResourceDescription tokenM = createExternalResourceDescription(
                TokenizerModelResourceImpl.class,
                "file:" + EnTokenModel.path),
                sentenceM = createExternalResourceDescription(
                        SentenceModelResourceImpl.class,
                        "file:" + EnSentModel.path),
                posM = createExternalResourceDescription(
                        POSModelResourceImpl.class,
                        "file:" + EnPosMaxentModel.path),
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
                MediaWikiConverterAE.PARAM_OUT_VIEW_TXT, txtRevised,
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
                UimaUtil.MODEL_PARAMETER, sentenceM,
                UimaUtil.SENTENCE_TYPE_PARAMETER, Sentence.class.getCanonicalName());

        AnalysisEngineDescription tokenizer = createEngineDescription(
                Tokenizer.class,
                UimaUtil.MODEL_PARAMETER, tokenM,
                UimaUtil.SENTENCE_TYPE_PARAMETER, Sentence.class.getCanonicalName(),
                UimaUtil.TOKEN_TYPE_PARAMETER, Token.class.getCanonicalName());

        AnalysisEngineDescription sentenceDiffer = createEngineDescription(
                SentenceDiffAE.class,
                SentenceDiffAE.PARAM_SENTENCE_TYPE, Sentence.class.getName(),
                SentenceDiffAE.PARAM_ORIGINAL_SENTENCES_TYPE, OriginalSentences.class.getName(),
                SentenceDiffAE.PARAM_REVISED_SENTENCES_TYPE, RevisedSentences.class.getName(),
                SentenceDiffAE.PARAM_ORIGINAL_SENTENCES_FEATURE, "originalSentences",
                SentenceDiffAE.PARAM_REVISED_SENTENCES_FEATURE, "revisedSentences");

        AnalysisEngineDescription taggerRevised = createEngineDescription(
                POSTagger.class,
                UimaUtil.MODEL_PARAMETER, posM,
                UimaUtil.SENTENCE_TYPE_PARAMETER, RevisedSentences.class.getName(),
                UimaUtil.TOKEN_TYPE_PARAMETER, Token.class.getName(),
                UimaUtil.POS_FEATURE_PARAMETER, "POS");

        AnalysisEngineDescription taggerOriginal = createEngineDescription(
                POSTagger.class,
                UimaUtil.MODEL_PARAMETER, posM,
                UimaUtil.SENTENCE_TYPE_PARAMETER, OriginalSentences.class.getName(),
                UimaUtil.TOKEN_TYPE_PARAMETER, Token.class.getName(),
                UimaUtil.POS_FEATURE_PARAMETER, "POS");

        AnalysisEngineDescription wordDiffer = createEngineDescription(
                WordDiffAE.class,
                WordDiffAE.PARAM_SENTENCE_TYPE, Sentence.class.getName(),
                WordDiffAE.PARAM_TOKEN_TYPE, Token.class.getName(),
                WordDiffAE.PARAM_ORIGINAL_SENTENCES_TYPE, OriginalSentences.class.getName(),
                WordDiffAE.PARAM_REVISED_SENTENCES_TYPE, RevisedSentences.class.getName(),
                WordDiffAE.PARAM_ORIGINAL_WORDS_TYPE, OriginalWords.class.getName(),
                WordDiffAE.PARAM_REVISED_WORDS_TYPE, RevisedWords.class.getName(),
                WordDiffAE.PARAM_ORIGINAL_SENTENCES_FEATURE, "originalSentences",
                WordDiffAE.PARAM_REVISED_SENTENCES_FEATURE, "revisedSentences",
                WordDiffAE.PARAM_ORIGINAL_WORDS_FEATURE, "originalWords",
                WordDiffAE.PARAM_REVISED_WORDS_FEATURE, "revisedWords");

        AnalysisEngineDescription consumerXmi = createEngineDescription(
                XmiSerializerCreationAE.class,
                XmiSerializerCreationAE.PARAM_OUT_FOLDER, "out/cas");

        AnalysisEngineDescription resourcePopulator = createEngineDescription(
                ResourcePopulatorAE.class,
                ResourcePopulatorAE.RES_KEY, fullTxt);

        AnalysisEngineDescription filterer = createEngineDescription(
                FilterDictAE.class,
                FilterDictAE.RES_INPUT_KEY, fullTxt,
                FilterDictAE.RES_OUTPUT_KEY, filteredTxt);

        AnalysisEngineDescription writer = createEngineDescription(
                SaveableWriterAE.class,
                SaveableWriterAE.PARAM_FILENAME, "out/res/corpus.xml",
                SaveableWriterAE.RES_KEY, filteredTxt);

        /* The type priority is important especially to retrieve tokens. The
         rest of the order is not accurate but it does not matter.*/
        AggregateBuilder b = new AggregateBuilder(
                null,
                TypePrioritiesFactory.createTypePriorities(
                        OriginalSentences.class,
                        OriginalWords.class,
                        RevisedSentences.class,
                        RevisedWords.class,
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
        b.add(sentenceDetector, CAS.NAME_DEFAULT_SOFA, txtRevised);
        b.add(sentenceDetector, CAS.NAME_DEFAULT_SOFA, txtOriginal);
        b.add(tokenizer, CAS.NAME_DEFAULT_SOFA, txtRevised);
        b.add(tokenizer, CAS.NAME_DEFAULT_SOFA, txtOriginal);
        b.add(sentenceDiffer, SentenceDiffAE.ORIGINAL_VIEW, txtOriginal,
                SentenceDiffAE.REVISED_VIEW, txtRevised);
        b.add(taggerRevised, CAS.NAME_DEFAULT_SOFA, txtRevised);
        b.add(taggerOriginal, CAS.NAME_DEFAULT_SOFA, txtOriginal);
        b.add(wordDiffer, WordDiffAE.ORIGINAL_VIEW, txtOriginal,
                WordDiffAE.REVISED_VIEW, txtRevised);
        b.add(consumerXmi);
        b.add(resourcePopulator, ResourcePopulatorAE.ORIGINAL_VIEW, txtOriginal,
                ResourcePopulatorAE.REVISED_VIEW, txtRevised);
        b.add(filterer);
        b.add(writer);
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
