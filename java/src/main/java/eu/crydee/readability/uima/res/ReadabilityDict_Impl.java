package eu.crydee.readability.uima.res;

import eu.crydee.readability.misc.XMLUtils;
import eu.crydee.readability.uima.model.Mapped;
import eu.crydee.readability.uima.model.Metrics;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import org.apache.uima.UIMAFramework;
import org.apache.uima.resource.DataResource;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.SharedResourceObject;
import org.apache.uima.util.Level;
import org.apache.uima.util.Logger;

public class ReadabilityDict_Impl
        implements SharedResourceObject, ReadabilityDict {

    final static private Logger logger = UIMAFramework.getLogger(
            ReadabilityDict_Impl.class);

    final private Map<Mapped, Map<Mapped, Metrics>> dict = new HashMap<>();
    private int totalCount = 0;

    @Override
    public void load(DataResource dr) throws ResourceInitializationException {
        XMLStreamReader xsr = null;
        try (InputStream is = dr.getInputStream()) {
            xsr = XMLInputFactory.newInstance()
                    .createXMLStreamReader(is, "UTF8");
            int mappings = 0;
            while (XMLUtils.goToNextXBeforeY(xsr, "original", "dict")) {
                Mapped rev = parseRevision(xsr);
                while (XMLUtils.goToNextXBeforeY(
                        xsr,
                        "revised",
                        "revised-list")) {
                    addRevised(xsr, rev);
                    mappings++;
                }
            }
            logger.log(Level.INFO, "loaded " + mappings + " revisions");
        } catch (NullPointerException ex) {
            logger.log(Level.INFO, "no file to load");
        } catch (IOException | XMLStreamException ex) {
            throw new ResourceInitializationException(ex);
        } finally {
            if (xsr != null) {
                try {
                    xsr.close();
                } catch (XMLStreamException ex) {
                    throw new ResourceInitializationException(ex);
                }
            }
        }
    }

    private void addRevised(XMLStreamReader xsr, Mapped rev)
            throws XMLStreamException {
        Integer count = Integer.parseInt(xsr.getAttributeValue(null, "count"));
        Double score = Double.parseDouble(xsr.getAttributeValue(null, "score"));
        Mapped revised = parseRevision(xsr);
        add(rev, revised, count);
        dict.get(rev).get(revised).setScore(score);
    }

    private Mapped parseRevision(XMLStreamReader xsr)
            throws XMLStreamException {
        XMLUtils.nextTag(xsr);
        String originalText = xsr.getElementText();
        XMLUtils.nextTag(xsr);
        List<String> tokens = new ArrayList<>();
        while (XMLUtils.goToNextXBeforeY(xsr, "token", "token-list")) {
            tokens.add(xsr.getElementText());
        }
        return new Mapped(originalText, tokens);
    }

    @Override
    public void save(PrintStream ps) throws XMLStreamException {
        XMLStreamWriter xsw = XMLOutputFactory.newInstance()
                .createXMLStreamWriter(ps, "UTF8");
        xsw.writeStartDocument("utf-8", "1.0");
        xsw.writeStartElement("dict");
        for (Mapped original : dict.keySet()) {
            xsw.writeStartElement("original");
            saveRevision(xsw, original);
            xsw.writeStartElement("revised-list");
            Map<Mapped, Metrics> revisedMap = dict.get(original);
            for (Mapped revised : revisedMap.keySet()) {
                xsw.writeStartElement("revised");
                xsw.writeAttribute(
                        "count",
                        String.valueOf(revisedMap.get(revised).getCount()));
                xsw.writeAttribute(
                        "score",
                        String.valueOf(revisedMap.get(revised).getScore()));
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

    private void saveRevision(XMLStreamWriter xsw, Mapped revision)
            throws XMLStreamException {
        xsw.writeStartElement("text");
        xsw.writeCharacters(revision.getText());
        xsw.writeEndElement();
        xsw.writeStartElement("token-list");
        for (String token : revision.getTokens()) {
            xsw.writeStartElement("token");
            xsw.writeCharacters(token);
            xsw.writeEndElement();
        }
        xsw.writeEndElement();
    }

    @Override
    public void add(Mapped original, Mapped revised, Integer count) {
        Map<Mapped, Metrics> revisions = dict.get(original);
        if (revisions == null) {
            revisions = new HashMap<>();
            dict.put(original, revisions);
        }
        Metrics scores = revisions.get(revised);
        if (scores == null) {
            scores = new Metrics(0, 0d);
            revisions.put(revised, scores);
        }
        scores.setCount(scores.getCount() + count);
        totalCount += count;
    }

    @Override
    public void add(Mapped original, Mapped revised) {
        add(original, revised, 1);
    }

    @Override
    public int getTotalCount() {
        return totalCount;
    }

    @Override
    public Optional<Map<Mapped, Metrics>> getRevisions(Mapped original) {
        Map<Mapped, Metrics> revisions = dict.get(original);
        if (dict == null) {
            return Optional.empty();
        }
        return Optional.of(Collections.unmodifiableMap(revisions));
    }

    @Override
    public Set<Mapped> keySet() {
        return Collections.unmodifiableSet(dict.keySet());
    }

    @Override
    public void set(Mapped original, Mapped revised, Integer count, Double score) {
        Map<Mapped, Metrics> revisions = dict.get(original);
        if (revisions == null) {
            revisions = new HashMap<>();
            dict.put(original, revisions);
        }
        revisions.put(revised, new Metrics(count, score));
    }
}
