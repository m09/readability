package eu.crydee.readability.uima;

import de.tudarmstadt.ukp.dkpro.core.frequency.resources.Web1TFrequencyCountResource;
import eu.crydee.readability.uima.ae.MapperAE;
import eu.crydee.readability.uima.ae.ScorerAE;
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
import org.apache.uima.fit.factory.AggregateBuilder;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.factory.ExternalResourceFactory;
import org.apache.uima.fit.factory.TypePrioritiesFactory;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ExternalResourceDescription;
import org.apache.uima.resource.ResourceInitializationException;

public class DictUsagePipeline {

    public static void main(String[] args)
            throws ResourceInitializationException,
            AnalysisEngineProcessException {
        AnalysisEngine ae = buildAe("file:out/res/dict.xml", true);

        JCas jcas = ae.newJCas();
        jcas.setDocumentText("Hello there, and how do you do?");

        ae.process(jcas);
    }

    public static AnalysisEngine buildAe(String fileURI, boolean serialize)
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

        ExternalResourceDescription dict
                = ExternalResourceFactory.createExternalResourceDescription(
                        ReadabilityDict_Impl.class,
                        fileURI);

        ExternalResourceDescription counts
                = ExternalResourceFactory.createExternalResourceDescription(
                        Web1TFrequencyCountResource.class,
                        Web1TFrequencyCountResource.PARAM_MIN_NGRAM_LEVEL,
                        "1",
                        Web1TFrequencyCountResource.PARAM_MAX_NGRAM_LEVEL,
                        "3",
                        Web1TFrequencyCountResource.PARAM_INDEX_PATH,
                        "out/lm/text");

        /* Analysis engines */
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

        AnalysisEngineDescription tagger
                = AnalysisEngineFactory.createEngineDescription(
                        POSTagger.class,
                        "opennlp.uima.ModelName",
                        posModel,
                        "opennlp.uima.SentenceType",
                        "eu.crydee.readability.uima.ts.Sentence",
                        "opennlp.uima.TokenType",
                        "eu.crydee.readability.uima.ts.Token",
                        "opennlp.uima.POSFeature",
                        "POS");
        
        AnalysisEngineDescription scorer
                = AnalysisEngineFactory.createEngineDescription(
                        ScorerAE.class,
                        ScorerAE.RES_KEY,
                        counts);

        AnalysisEngineDescription mapper
                = AnalysisEngineFactory.createEngineDescription(
                        MapperAE.class,
                        MapperAE.RES_KEY,
                        dict);

        AnalysisEngineDescription consumerXmi = null;
        if (serialize) {
            consumerXmi = AnalysisEngineFactory.createEngineDescription(
                    XmiSerializerUsageAE.class,
                    XmiSerializerUsageAE.PARAM_OUT_FOLDER,
                    "out/cas-usage");
        }

        AggregateBuilder builder = new AggregateBuilder(
                null,
                TypePrioritiesFactory.createTypePriorities(
                        Sentence.class,
                        Token.class),
                null);
//        builder.add(sentenceDetector);
//        builder.add(tokenizer);
//        builder.add(tagger);
        builder.add(scorer);
//        builder.add(mapper);
//        if (serialize) {
//            builder.add(consumerXmi);
//        }
//
        return builder.createAggregate();
    }
}
