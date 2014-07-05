package eu.crydee.readability.uima.evaluator.ae;

import eu.crydee.readability.uima.evaluator.res.ResultsAggregator;
import org.apache.uima.UIMAFramework;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ExternalResource;
import org.apache.uima.jcas.JCas;
import org.apache.uima.util.Logger;

public class FleschAE extends JCasAnnotator_ImplBase {

    final static private Logger logger = UIMAFramework.getLogger(
            FleschAE.class);

    final static public String RES_AGGREGATOR = "AGGREGATOR";
    @ExternalResource(key = RES_AGGREGATOR)
    private ResultsAggregator ra;

    @Override
    public void process(JCas jcas) throws AnalysisEngineProcessException {
    }
}
