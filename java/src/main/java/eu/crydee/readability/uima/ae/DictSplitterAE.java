package eu.crydee.readability.uima.ae;

import eu.crydee.readability.uima.res.ReadabilityDict;
import java.io.File;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.descriptor.ExternalResource;
import org.apache.uima.jcas.JCas;

public class DictSplitterAE extends JCasAnnotator_ImplBase {

    static public final String KEY_DICT = "DICT";
    @ExternalResource(key = KEY_DICT)
    ReadabilityDict dict;

    static public final String PARAM_N_PARTS = "N_PARTS";
    @ConfigurationParameter(name = PARAM_N_PARTS, mandatory = true)
    int parts;

    static public final String PARAM_OUT_FOLDER = "OUT_FOLDER";
    @ConfigurationParameter(name = PARAM_OUT_FOLDER, mandatory = true)
    File outFolder;

    @Override
    public void process(JCas jcas) throws AnalysisEngineProcessException {

    }
}
