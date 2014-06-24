package eu.crydee.readability.uima;

import eu.crydee.readability.uima.ae.DictSplitterAE;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.resource.ResourceInitializationException;

public class EvaluationPipeline {

    public static void main(String[] args)
            throws ResourceInitializationException {
        for (int fold = 0; fold < DictPartsCreationPipeline.nParts; ++fold) {
            for (String corpus : new String[]{"full", "filtered"}) {
                AnalysisEngineDescription aed = DictUsagePipeline.buildAed(
                        "file:out/res/parts/"
                        + corpus
                        + "/txt/"
                        + DictSplitterAE.trainNamer.apply(fold),
                        "file:out/res/parts/"
                        + corpus
                        + "/pos/"
                        + DictSplitterAE.trainNamer.apply(fold),
                        true);
            }
        }
    }
}
