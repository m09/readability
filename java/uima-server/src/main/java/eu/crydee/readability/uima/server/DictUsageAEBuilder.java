package eu.crydee.readability.uima.server;

import eu.crydee.readability.uima.server.ae.MapperAE;
import eu.crydee.readability.uima.server.ae.RewriterAE;
import eu.crydee.readability.uima.core.ae.XmiSerializerAE;
import eu.crydee.readability.uima.core.res.ReadabilityDict_Impl;
import eu.crydee.readability.uima.core.ts.Sentence;
import eu.crydee.readability.uima.core.ts.Token;
import eu.crydee.uima.opennlp.resources.EnPosMaxentModel;
import eu.crydee.uima.opennlp.resources.EnSentModel;
import eu.crydee.uima.opennlp.resources.EnTokenModel;
import java.net.URL;
import opennlp.uima.postag.POSModelResourceImpl;
import opennlp.uima.postag.POSTagger;
import opennlp.uima.sentdetect.SentenceDetector;
import opennlp.uima.sentdetect.SentenceModelResourceImpl;
import opennlp.uima.tokenize.Tokenizer;
import opennlp.uima.tokenize.TokenizerModelResourceImpl;
import opennlp.uima.util.UimaUtil;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.fit.factory.AggregateBuilder;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.factory.ExternalResourceFactory;
import org.apache.uima.fit.factory.TypePrioritiesFactory;
import org.apache.uima.resource.ExternalResourceDescription;
import org.apache.uima.resource.ResourceInitializationException;

public class DictUsageAEBuilder {

    public static AnalysisEngine buildAe(
            URL corpusURL,
            boolean serialize)
            throws ResourceInitializationException {
        return AnalysisEngineFactory.createEngine(
                buildAed(corpusURL, serialize));
    }

    public static AnalysisEngineDescription buildAed(
            URL corpusURL,
            boolean serialize)
            throws ResourceInitializationException {
        /* Resources descriptions */
        ExternalResourceDescription tokenModel
                = ExternalResourceFactory.createExternalResourceDescription(
                        TokenizerModelResourceImpl.class,
                        EnTokenModel.url);

        ExternalResourceDescription sentenceModel
                = ExternalResourceFactory.createExternalResourceDescription(
                        SentenceModelResourceImpl.class,
                        EnSentModel.url);

        ExternalResourceDescription posModel
                = ExternalResourceFactory.createExternalResourceDescription(
                        POSModelResourceImpl.class,
                        EnPosMaxentModel.url);

        ExternalResourceDescription dictTxt
                = ExternalResourceFactory.createExternalResourceDescription(
                        ReadabilityDict_Impl.class,
                        corpusURL);

        /* Analysis engines */
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
                        UimaUtil.MODEL_PARAMETER,
                        posModel,
                        UimaUtil.SENTENCE_TYPE_PARAMETER,
                        Sentence.class.getCanonicalName(),
                        UimaUtil.TOKEN_TYPE_PARAMETER,
                        Token.class.getCanonicalName(),
                        UimaUtil.POS_FEATURE_PARAMETER,
                        "POS");

        AnalysisEngineDescription mapper
                = AnalysisEngineFactory.createEngineDescription(
                        MapperAE.class,
                        MapperAE.RES_KEY,
                        dictTxt,
                        MapperAE.PARAM_LIMIT,
                        10);

        AnalysisEngineDescription rewriter
                = AnalysisEngineFactory.createEngineDescription(
                        RewriterAE.class);

        AnalysisEngineDescription consumerXmi = null;
        if (serialize) {
            consumerXmi = AnalysisEngineFactory.createEngineDescription(
                    XmiSerializerAE.class,
                    XmiSerializerAE.PARAM_OUT_FOLDER,
                    "out");
        }

        AggregateBuilder builder = new AggregateBuilder(
                null,
                TypePrioritiesFactory.createTypePriorities(
                        Sentence.class,
                        Token.class),
                null);
        builder.add(sentenceDetector);
        builder.add(tokenizer);
        builder.add(tagger);
        builder.add(mapper);
        builder.add(rewriter);
        if (serialize) {
            builder.add(consumerXmi);
        }

        return builder.createAggregateDescription();
    }
}
