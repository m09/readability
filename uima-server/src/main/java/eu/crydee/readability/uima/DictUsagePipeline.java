package eu.crydee.readability.uima;

import eu.crydee.readability.uima.ae.MapperAE;
import eu.crydee.readability.uima.ae.RewriterAE;
import eu.crydee.readability.uima.ae.XmiSerializerUsageAE;
import eu.crydee.readability.uima.res.ReadabilityDict_Impl;
import eu.crydee.readability.uima.ts.Sentence;
import eu.crydee.readability.uima.ts.Token;
import opennlp.uima.postag.POSModelResourceImpl;
import opennlp.uima.postag.POSTagger;
import opennlp.uima.sentdetect.SentenceDetector;
import opennlp.uima.sentdetect.SentenceModelResourceImpl;
import opennlp.uima.tokenize.Tokenizer;
import opennlp.uima.tokenize.TokenizerModelResourceImpl;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.CAS;
import org.apache.uima.fit.factory.AggregateBuilder;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.factory.ExternalResourceFactory;
import org.apache.uima.fit.factory.TypePrioritiesFactory;
import org.apache.uima.resource.ExternalResourceDescription;
import org.apache.uima.resource.ResourceAccessException;
import org.apache.uima.resource.ResourceInitializationException;

public class DictUsagePipeline {

    public static void main(String[] args)
            throws ResourceInitializationException,
            AnalysisEngineProcessException,
            ResourceAccessException {
        AnalysisEngine ae = buildAe(true, true);
        CAS aCas = ae.newCAS();
        aCas.setDocumentText("this, and that!");
        ae.process(aCas);
    }

    public static AnalysisEngine buildAe(
            boolean filtered,
            boolean serialize)
            throws ResourceInitializationException {
        return AnalysisEngineFactory.createEngine(
                buildAed(filtered, serialize));
    }

    public static AnalysisEngineDescription buildAed(
            boolean filtered,
            boolean serialize)
            throws ResourceInitializationException {
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

        ExternalResourceDescription dictTxt
                = ExternalResourceFactory.createExternalResourceDescription(
                        ReadabilityDict_Impl.class,
                        "file:" + (filtered ? "filtered" : "full") + "Txt.xml");

        /* Analysis engines */
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

        AnalysisEngineDescription tagger
                = AnalysisEngineFactory.createEngineDescription(
                        POSTagger.class,
                        "opennlp.uima.ModelName",
                        posModel,
                        "opennlp.uima.SentenceType",
                        Sentence.class.getCanonicalName(),
                        "opennlp.uima.TokenType",
                        Token.class.getCanonicalName(),
                        "opennlp.uima.POSFeature",
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
                    XmiSerializerUsageAE.class,
                    XmiSerializerUsageAE.PARAM_OUT_FOLDER,
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
