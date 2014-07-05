package eu.crydee.readability.uima;

import eu.crydee.readability.uima.ae.DictSplitterAE;
import eu.crydee.readability.uima.ae.FleschAE;
import eu.crydee.readability.uima.ae.SyllableCounterAE;
import eu.crydee.readability.uima.ae.XmiSerializerUsageAE;
import eu.crydee.readability.uima.cr.ReadabilityDictCR;
import eu.crydee.readability.uima.res.ReadabilityDict_Impl;
import eu.crydee.readability.uima.res.ResultsAggregator_Impl;
import eu.crydee.readability.uima.ts.Sentence;
import eu.crydee.readability.uima.ts.Token;
import java.io.IOException;
import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.collection.CollectionReader;
import org.apache.uima.fit.factory.AggregateBuilder;
import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;
import org.apache.uima.fit.factory.CollectionReaderFactory;
import static org.apache.uima.fit.factory.ExternalResourceFactory.createExternalResourceDescription;
import org.apache.uima.fit.factory.TypePrioritiesFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.resource.ExternalResourceDescription;
import org.apache.uima.resource.ResourceInitializationException;
import org.xml.sax.SAXException;

public class EvaluationPipeline {
//
//    public static void main(String[] args)
//            throws ResourceInitializationException,
//            UIMAException,
//            IOException,
//            SAXException {
//        for (int fold = 0; fold < 1/* DictPartsCreationPipeline.nParts */; ++fold) {
////            for (String corpus : new String[]{"full/", "filtered/"}) {
//            for (String corpus : new String[]{"filtered/"}) {
//                ExternalResourceDescription dictTest
//                        = createExternalResourceDescription(
//                                "dictTest",
//                                ReadabilityDict_Impl.class,
//                                "file:out/res/parts/"
//                                + "txt/"
//                                + corpus
//                                + DictSplitterAE.testNamer.apply(fold));
//
//                ExternalResourceDescription resultsAggregator
//                        = createExternalResourceDescription(
//                                "resultsAggregator",
//                                ResultsAggregator_Impl.class,
//                                "");
//
//                CollectionReader cr
//                        = CollectionReaderFactory.createReader(
//                                ReadabilityDictCR.class,
//                                ReadabilityDictCR.KEY_RES, dictTest,
//                                ReadabilityDictCR.PARAM_LIMIT, 200);
//
//                AnalysisEngineDescription usageAed = DictUsagePipeline.buildAed(
//                        "file:out/res/parts/"
//                        + "txt/"
//                        + corpus
//                        + DictSplitterAE.trainNamer.apply(fold),
//                        true);
//
//                AnalysisEngineDescription sylAe = createEngineDescription(
//                        SyllableCounterAE.class,
//                        SyllableCounterAE.PARAM_TOKEN_TYPE,
//                        Token.class.getName(),
//                        SyllableCounterAE.PARAM_TOKEN_FEATURE,
//                        "syllablesNumber");
//
//                AnalysisEngineDescription fleschAe = createEngineDescription(
//                        FleschAE.class,
//                        FleschAE.RES_AGGREGATOR,
//                        resultsAggregator);
//
//                AnalysisEngineDescription consumerAe = createEngineDescription(
//                        XmiSerializerUsageAE.class,
//                        XmiSerializerUsageAE.PARAM_OUT_FOLDER,
//                        "out/cas-evaluation");
//
//                AggregateBuilder b = new AggregateBuilder(
//                        null,
//                        TypePrioritiesFactory.createTypePriorities(
//                                Sentence.class,
//                                Token.class),
//                        null);
//                b.add(usageAed);
//                b.add(sylAe);
//                b.add(consumerAe);
//
//                AnalysisEngine aae = b.createAggregate();
//
//                SimplePipeline.runPipeline(cr, aae);
//            }
//        }
//    }
}
