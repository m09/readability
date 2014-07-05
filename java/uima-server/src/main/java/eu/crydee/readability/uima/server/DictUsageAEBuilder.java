package eu.crydee.readability.uima.server;

import eu.crydee.readability.uima.server.ae.MapperAE;
import eu.crydee.readability.uima.server.ae.RewriterAE;
import eu.crydee.readability.uima.core.ae.XmiSerializerAE;
import eu.crydee.readability.uima.models.SentenceSplitterModelPath;
import eu.crydee.readability.uima.models.TaggerModelPath;
import eu.crydee.readability.uima.models.TokenizerModelPath;
import eu.crydee.readability.uima.core.res.ReadabilityDict_Impl;
import eu.crydee.readability.uima.core.ts.Sentence;
import eu.crydee.readability.uima.core.ts.Token;
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
            String corpusPath,
            boolean serialize)
            throws ResourceInitializationException {
        return AnalysisEngineFactory.createEngine(
                buildAed(corpusPath, serialize));
    }

    public static AnalysisEngineDescription buildAed(
            String corpusPath,
            boolean serialize)
            throws ResourceInitializationException {
        /* Resources descriptions */
        ExternalResourceDescription tokenModel
                = ExternalResourceFactory.createExternalResourceDescription(
                        TokenizerModelResourceImpl.class,
                        "file:" + TokenizerModelPath.path);

        ExternalResourceDescription sentenceModel
                = ExternalResourceFactory.createExternalResourceDescription(
                        SentenceModelResourceImpl.class,
                        "file:" + SentenceSplitterModelPath.path);

        ExternalResourceDescription posModel
                = ExternalResourceFactory.createExternalResourceDescription(
                        POSModelResourceImpl.class,
                        "file:" + TaggerModelPath.path);

        ExternalResourceDescription dictTxt
                = ExternalResourceFactory.createExternalResourceDescription(
                        ReadabilityDict_Impl.class,
                        "file:" + corpusPath);

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
