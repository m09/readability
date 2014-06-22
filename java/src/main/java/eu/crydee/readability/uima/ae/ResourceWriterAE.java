package eu.crydee.readability.uima.ae;

import eu.crydee.readability.uima.res.ReadabilityDict;
import java.io.PrintStream;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.descriptor.ExternalResource;
import org.apache.uima.jcas.JCas;

public class ResourceWriterAE extends JCasAnnotator_ImplBase {

    final static public String PARAM_FILENAME = "FILENAME";
    @ConfigurationParameter(name = PARAM_FILENAME, mandatory = true)
    String fileName;

    final static public String RES_KEY = "KEY";
    @ExternalResource(key = RES_KEY)
    ReadabilityDict res;

    @Override
    public void process(JCas jcas) throws AnalysisEngineProcessException {
    }

    @Override
    public void collectionProcessComplete()
            throws AnalysisEngineProcessException {
        super.collectionProcessComplete();
        try (PrintStream ps = new PrintStream(fileName, "UTF8")) {
            res.save(ps);
        } catch (Exception ex) {
            throw new AnalysisEngineProcessException(ex);
        }
    }
}
