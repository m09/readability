package eu.crydee.readability.uima.res;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.apache.uima.resource.DataResource;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.SharedResourceObject;

@XmlRootElement(name = "dict")
public class ReadabilityDict_Impl
        implements SharedResourceObject, ReadabilityDict {

    final private Map<Revision, Map<Revision, Integer>> dict = new HashMap<>();

    @Override
    public void load(DataResource dr) throws ResourceInitializationException {
        try (InputStream is = dr.getInputStream()) {
        } catch (NullPointerException ex) {
            // nothing to load, go on
        } catch (IOException ex) {
            throw new ResourceInitializationException(ex);
        }
    }

    @Override
    public void save(PrintStream ps) throws XMLStreamException {
        XMLStreamWriter xsw = XMLOutputFactory.newInstance()
                .createXMLStreamWriter(ps, "UTF8");
        xsw.writeStartDocument();
        xsw.writeStartElement("dict");
        for (Revision original : dict.keySet()) {
            xsw.writeStartElement("original");
            saveRevision(xsw, original);
            xsw.writeStartElement("revised-list");
            for (Revision revised : dict.get(original).keySet()) {
                xsw.writeStartElement("revised");
                saveRevision(xsw, revised);
                xsw.writeEndElement();
            }
            xsw.writeEndElement();
            xsw.writeEndElement();
        }
        xsw.writeEndElement();
        xsw.writeEndDocument();
        xsw.close();
    }

    private void saveRevision(XMLStreamWriter xsw, Revision revision)
            throws XMLStreamException {
        xsw.writeStartElement("text");
        xsw.writeCData(revision.getText());
        xsw.writeEndElement();
        xsw.writeStartElement("token-list");
        for (String token : revision.getTokens()) {
            xsw.writeStartElement("token");
            xsw.writeCData(token);
            xsw.writeEndElement();
        }
        xsw.writeEndElement();
        xsw.writeStartElement("pos-list");
        for (String pos : revision.getPOS()) {
            xsw.writeStartElement("pos");
            xsw.writeCharacters(pos);
            xsw.writeEndElement();
        }
        xsw.writeEndElement();
    }

    @Override
    public void add(Revision original, Revision revised) {
        Map<Revision, Integer> revisions = dict.get(original);
        if (revisions == null) {
            revisions = new HashMap<>();
            dict.put(original, revisions);
        }
        revisions.put(revised, revisions.getOrDefault(revised, 0) + 1);
    }

    @Override
    public Optional<Map<Revision, Integer>> getRevisions(Revision original) {
        Map<Revision, Integer> revisions = dict.get(original);
        if (dict == null) {
            return Optional.empty();
        }
        return Optional.of(Collections.unmodifiableMap(revisions));
    }
}
