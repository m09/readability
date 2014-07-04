package eu.crydee.readability.uima.ae;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.impl.XmiCasSerializer;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.jcas.JCas;
import org.apache.uima.util.TypeSystemUtil;
import org.xml.sax.SAXException;

public class XmiSerializerUsageAE extends JCasAnnotator_ImplBase {

    final public static String PARAM_OUT_FOLDER = "PARAM_OUT_FOLDER";
    @ConfigurationParameter(name = PARAM_OUT_FOLDER, mandatory = true)
    private String outputFolder;

    private int i = 0;

    private boolean tsWritten = false;

    @Override
    public void process(JCas jcas) throws AnalysisEngineProcessException {
        if (!tsWritten) {
            writeTs(jcas);
        }
        try (OutputStream os = new FileOutputStream(
                new File(outputFolder, ++i + ".xmi"))) {
            XmiCasSerializer.serialize(jcas.getCas(), os);
        } catch (IOException | SAXException ex) {
            throw new AnalysisEngineProcessException(ex);
        }
    }

    private void writeTs(JCas jcas) throws AnalysisEngineProcessException {
        try (OutputStream os = new FileOutputStream(
                new File(outputFolder, "ts.xml"))) {
            TypeSystemUtil.typeSystem2TypeSystemDescription(
                    jcas.getTypeSystem())
                    .toXML(os);
        } catch (IOException | SAXException ex) {
            throw new AnalysisEngineProcessException(ex);
        }
        tsWritten = true;
    }
}
