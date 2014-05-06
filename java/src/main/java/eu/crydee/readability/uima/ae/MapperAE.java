package eu.crydee.readability.uima.ae;

import eu.crydee.readability.uima.res.ReadabilityDict;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ExternalResource;
import org.apache.uima.jcas.JCas;

public class MapperAE extends JCasAnnotator_ImplBase {

    final static public String RES_KEY = "RES_KEY";
    @ExternalResource(key = RES_KEY)
    private ReadabilityDict dict;

    @Override
    public void process(JCas jcas) throws AnalysisEngineProcessException {

    }
}
