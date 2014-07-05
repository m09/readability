package eu.crydee.readability.uima.evaluator;

import eu.crydee.readability.uima.evaluator.ae.DictSplitterAE;
import eu.crydee.readability.uima.core.res.ReadabilityDict_Impl;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.factory.AggregateBuilder;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.factory.ExternalResourceFactory;
import org.apache.uima.resource.ResourceAccessException;
import org.apache.uima.resource.ResourceInitializationException;

public class DictPartsCreationPipeline {

    public static final int nParts = 10;

    public static void main(String[] args)
            throws ResourceInitializationException,
            AnalysisEngineProcessException,
            ResourceAccessException {
        AggregateBuilder b = new AggregateBuilder();
        addAE(b, "file:out/res/fullTxt.xml", "out/res/parts/full");
        addAE(b, "file:out/res/filteredTxt.xml", "out/res/parts/filtered");
        b.createAggregate().collectionProcessComplete();
    }

    static private void addAE(AggregateBuilder builder, String in, String out)
            throws ResourceInitializationException {
        builder.add(AnalysisEngineFactory.createEngineDescription(
                DictSplitterAE.class,
                DictSplitterAE.KEY_DICT,
                ExternalResourceFactory.createExternalResourceDescription(
                        ReadabilityDict_Impl.class,
                        in),
                DictSplitterAE.PARAM_N_PARTS, nParts,
                DictSplitterAE.PARAM_OUT_FOLDER, out));
    }
}
