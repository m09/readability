package eu.crydee.readability.uima.res;

import com.google.common.collect.Lists;
import eu.crydee.readability.misc.XMLUtils;
import eu.crydee.readability.uima.model.Mapped;
import eu.crydee.readability.uima.model.Metadata;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import org.apache.commons.lang3.tuple.Pair;
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

    final private Map<Mapped, Map<Mapped, Metadata>> dict = new HashMap<>();
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
        Double scoreOcc = Double.parseDouble(
                xsr.getAttributeValue(null, "scoreOcc")),
                scoreLM = Double.parseDouble(
                        xsr.getAttributeValue(null, "scoreLM")),
                scoreLMW = Double.parseDouble(
                        xsr.getAttributeValue(null, "scoreLMW")),
                scoreLMN = Double.parseDouble(
                        xsr.getAttributeValue(null, "scoreLMN")),
                scoreLMWN = Double.parseDouble(
                        xsr.getAttributeValue(null, "scoreLMWN"));
        Mapped revised = parseRevision(xsr);
        List<Pair<String, String>> contexts = parseContexts(xsr);
        add(rev, revised, contexts);
        Metadata m = dict.get(rev).get(revised);
        m.addContexts(contexts);
        m.setScoreOcc(scoreOcc);
        m.setScoreLM(scoreLM);
        m.setScoreLMW(scoreLMW);
        m.setScoreLMN(scoreLMN);
        m.setScoreLMWN(scoreLMWN);
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
        List<String> pos = new ArrayList<>();
        while (XMLUtils.goToNextXBeforeY(xsr, "pos", "pos-list")) {
            pos.add(xsr.getElementText());
        }
        return new Mapped(originalText, tokens, pos);
    }

    private List<Pair<String, String>> parseContexts(XMLStreamReader xsr)
            throws XMLStreamException {
        List<Pair<String, String>> contexts = new ArrayList<>();
        while (XMLUtils.goToNextXBeforeY(xsr, "context", "context-list")) {
            XMLUtils.nextTag(xsr);
            String originalContext = xsr.getElementText();
            XMLUtils.nextTag(xsr);
            String revisedContext = xsr.getElementText();
            contexts.add(Pair.of(originalContext, revisedContext));
        }
        return contexts;
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
            Map<Mapped, Metadata> revisedMap = dict.get(original);
            for (Mapped revised : revisedMap.keySet()) {
                Metadata m = revisedMap.get(revised);
                xsw.writeStartElement("revised");
                xsw.writeAttribute(
                        "count",
                        String.valueOf(m.getCount()));
                xsw.writeAttribute(
                        "scoreOcc",
                        String.valueOf(m.getScoreOcc()));
                xsw.writeAttribute(
                        "scoreLM",
                        String.valueOf(m.getScoreLM()));
                xsw.writeAttribute(
                        "scoreLMN",
                        String.valueOf(m.getScoreLMN()));
                xsw.writeAttribute(
                        "scoreLMW",
                        String.valueOf(m.getScoreLMW()));
                xsw.writeAttribute(
                        "scoreLMWN",
                        String.valueOf(m.getScoreLMWN()));
                saveRevision(xsw, revised);
                saveContextList(xsw, m.getContexts());
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
        xsw.writeStartElement("pos-list");
        for (String token : revision.getPos()) {
            xsw.writeStartElement("pos");
            xsw.writeCharacters(token);
            xsw.writeEndElement();
        }
        xsw.writeEndElement();
    }

    private void saveContextList(
            XMLStreamWriter xsw,
            List<Pair<String, String>> contextsList)
            throws XMLStreamException {
        xsw.writeStartElement("context-list");
        for (Pair<String, String> contexts : contextsList) {
            xsw.writeStartElement("context");
            xsw.writeStartElement("original");
            xsw.writeCharacters(contexts.getLeft());
            xsw.writeEndElement();
            xsw.writeStartElement("revised");
            xsw.writeCharacters(contexts.getRight());
            xsw.writeEndElement();
            xsw.writeEndElement();
        }
        xsw.writeEndElement();
    }

    @Override
    public void add(
            Mapped original,
            Mapped revised,
            List<Pair<String, String>> revisedContexts) {
        Map<Mapped, Metadata> revisions = dict.get(original);
        if (revisions == null) {
            revisions = new HashMap<>();
            dict.put(original, revisions);
        }
        Metadata metadata = revisions.get(revised);
        if (metadata == null) {
            metadata = new Metadata();
            revisions.put(revised, metadata);
        }
        metadata.addContexts(revisedContexts);
        totalCount += revisedContexts.size();
    }

    @Override
    public void add(
            Mapped original,
            Mapped revised,
            String originalContext,
            String revisedContext) {
        add(
                original,
                revised,
                Lists.newArrayList(Pair.of(originalContext, revisedContext)));
    }

    @Override
    public void addAll(ReadabilityDict o) {
        for (Entry<Mapped, Map<Mapped, Metadata>> e1 : o.entrySet()) {
            for (Entry<Mapped, Metadata> e2
                    : e1.getValue().entrySet()) {
                add(
                        e1.getKey(),
                        e2.getKey(),
                        e2.getValue().getContexts());

            }
        }
    }

    @Override
    public int getTotalCount() {
        return totalCount;
    }

    @Override
    public Optional<Map<Mapped, Metadata>> getRevisions(Mapped original) {
        Map<Mapped, Metadata> revisions = dict.get(original);
        if (revisions == null) {
            return Optional.empty();
        }
        return Optional.of(revisions);
    }

    @Override
    public Set<Mapped> keySet() {
        return Collections.unmodifiableSet(dict.keySet());
    }

    @Override
    public Set<Entry<Mapped, Map<Mapped, Metadata>>> entrySet() {
        return Collections.unmodifiableSet(dict.entrySet());
    }
}
