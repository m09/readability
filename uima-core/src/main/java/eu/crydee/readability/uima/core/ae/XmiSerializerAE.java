package eu.crydee.readability.uima.core.ae;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.TypeSystem;
import org.apache.uima.cas.impl.XmiCasSerializer;
import org.apache.uima.fit.component.CasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.util.TypeSystemUtil;
import org.xml.sax.SAXException;

public class XmiSerializerAE extends CasAnnotator_ImplBase {

    final public static String PARAM_OUT_FOLDER = "PARAM_OUT_FOLDER";
    @ConfigurationParameter(name = PARAM_OUT_FOLDER, mandatory = true)
    private String outputFolder;

    @Override
    public void typeSystemInit(TypeSystem typeSystem)
            throws AnalysisEngineProcessException {
        super.typeSystemInit(typeSystem);
        try (OutputStream os = new FileOutputStream(
                new File(outputFolder, "ts.xml"))) {
            TypeSystemUtil.typeSystem2TypeSystemDescription(typeSystem)
                    .toXML(os);
        } catch (IOException | SAXException ex) {
            throw new AnalysisEngineProcessException(ex);
        }
    }

    @Override
    public void process(CAS cas) throws AnalysisEngineProcessException {
        try (OutputStream os = new FileOutputStream(new File(
                outputFolder,
                UUID.randomUUID().toString() + ".xmi"))) {
            XmiCasSerializer.serialize(cas, os);
        } catch (IOException | SAXException ex) {
            throw new AnalysisEngineProcessException(ex);
        }
    }
}
