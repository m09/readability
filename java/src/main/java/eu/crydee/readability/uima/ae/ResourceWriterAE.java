package eu.crydee.readability.uima.ae;

import eu.crydee.readability.uima.res.ReadabilityDict;
import java.io.PrintStream;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.descriptor.ExternalResource;
import org.apache.uima.jcas.JCas;

public class ResourceWriterAE extends JCasAnnotator_ImplBase {

    final static public String PARAM_OUT_POS_FILENAME = "OUT_POS_FILENAME";
    @ConfigurationParameter(name = PARAM_OUT_POS_FILENAME, mandatory = true)
    String fileNamePos;

    final static public String PARAM_OUT_TXT_FILENAME = "OUT_TXT_FILENAME";
    @ConfigurationParameter(name = PARAM_OUT_TXT_FILENAME, mandatory = true)
    String fileNameTxt;

    final static public String RES_TXT_KEY = "TXT_KEY";
    @ExternalResource(key = RES_TXT_KEY)
    ReadabilityDict dictTxt;

    final static public String RES_POS_KEY = "POS_KEY";
    @ExternalResource(key = RES_POS_KEY)
    ReadabilityDict dictPos;

    @Override
    public void process(JCas jcas) throws AnalysisEngineProcessException {
    }

    @Override
    public void collectionProcessComplete()
            throws AnalysisEngineProcessException {
        super.collectionProcessComplete();
        try (PrintStream psTxt = new PrintStream(fileNameTxt, "UTF8");
                PrintStream psPos = new PrintStream(fileNamePos, "UTF8")) {
            dictPos.save(psPos);
            dictTxt.save(psTxt);
        } catch (Exception ex) {
            throw new AnalysisEngineProcessException(ex);
        }
    }
}
