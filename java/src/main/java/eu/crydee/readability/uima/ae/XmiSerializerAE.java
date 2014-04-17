package eu.crydee.readability.uima.ae;

import eu.crydee.readability.uima.ts.Revision;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.impl.XmiCasSerializer;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.util.TypeSystemUtil;
import org.xml.sax.SAXException;

public class XmiSerializerAE extends JCasAnnotator_ImplBase {

    final public static String PARAM_OUT_FOLDER = "PARAM_OUT_FOLDER";
    @ConfigurationParameter(name = PARAM_OUT_FOLDER, mandatory = true)
    private String outputFolder;

    private boolean tsWritten = false;

    @Override
    public void process(JCas jcas) throws AnalysisEngineProcessException {
        Revision revision = JCasUtil.selectSingle(jcas, Revision.class);
        if (!tsWritten) {
            writeTs(jcas);
        }
        try (OutputStream os = new FileOutputStream(
                new File(outputFolder, revision.getId() + ".xmi"))) {
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
