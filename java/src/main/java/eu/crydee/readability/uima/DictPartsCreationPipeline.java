package eu.crydee.readability.uima;

import eu.crydee.readability.uima.ae.DictSplitterAE;
import eu.crydee.readability.uima.res.ReadabilityDict_Impl;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.factory.AggregateBuilder;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.factory.ExternalResourceFactory;
import org.apache.uima.resource.ResourceAccessException;
import org.apache.uima.resource.ResourceInitializationException;

public class DictPartsCreationPipeline {

    private static final int crossValidationFolds = 10;

    public static void main(String[] args)
            throws ResourceInitializationException,
            AnalysisEngineProcessException,
            ResourceAccessException {
        AggregateBuilder b = new AggregateBuilder();
        addAE(b, "file:out/res/fullPos.xml", "out/res/parts/pos/full");
        addAE(b, "file:out/res/fullTxt.xml", "out/res/parts/txt/full");
        addAE(b, "file:out/res/filteredPos.xml", "out/res/parts/pos/filtered");
        addAE(b, "file:out/res/filteredTxt.xml", "out/res/parts/txt/filtered");
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
                DictSplitterAE.PARAM_N_PARTS, crossValidationFolds,
                DictSplitterAE.PARAM_OUT_FOLDER, out));
    }
}
