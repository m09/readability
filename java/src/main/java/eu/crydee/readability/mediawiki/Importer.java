package eu.crydee.readability.mediawiki;

import eu.crydee.readability.App;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentNavigableMap;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.mapdb.DB;
import org.mapdb.DBMaker;

public class Importer {

    private static final org.apache.log4j.Logger logger
            = org.apache.log4j.Logger.getLogger(App.class.getCanonicalName());

    public static void main(String[] args)
            throws JAXBException, FileNotFoundException, XMLStreamException {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        try (PrintWriter pw = new PrintWriter("history.txt")) {
            DB db = DBMaker.newFileDB(new File("db/simplewiki.db"))
                    .transactionDisable()
                    .closeOnJvmShutdown()
                    .make();

            ConcurrentNavigableMap<Long, String> revisions
                    = db.getTreeMap("revisions");
            XMLStreamReader reader = factory.createXMLStreamReader(
                    new FileInputStream("simplewiki-20140325-pages-meta-history.xml"),
                    "utf8");
            pw.println("<?xml "
                    + "version=\"1.0\" "
                    + "encoding=\"UTF-8\" "
                    + "standalone=\"yes\""
                    + "?>");
            pw.print("<pages>");
            int i = 0;
            while (goToNextXBeforeY(reader, "page", "mediawiki")) {
                System.out.print("\r" + i++);
                Page page = parsePageInfo(reader);
                if (page.isRedirect() || page.getNs() != 0) {
                    continue;
                }
                for (Revision revisionInfo : parseRevisions(reader)) {
                    page.addRevision(revisionInfo);
                    revisions.put(revisionInfo.getId(), revisionInfo.getText());
                }
                JAXBContext jc = JAXBContext.newInstance(Page.class);
                Marshaller marshaller = jc.createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
                marshaller.marshal(page, pw);
            }
            System.out.println();
            pw.println("</pages>");
            db.close();
        }
    }

    static private List<Revision> parseRevisions(XMLStreamReader reader)
            throws XMLStreamException {
        List<Revision> result = new ArrayList<>();
        while (reader.getEventType() == XMLStreamReader.START_ELEMENT
                && reader.getName().getLocalPart().equals("revision")) {
            goToNextXBeforeY(reader, "id", "revision");
            final long id = Long.parseLong(reader.getElementText());
            final Optional<Long> parentId;
            if (nextTag(reader).get().equals("parentid")) {
                parentId = Optional.of(Long.parseLong(reader.getElementText()));
                goToNextXBeforeY(reader, "timestamp", "revision");
            } else {
                parentId = Optional.empty();
            }
            final ZonedDateTime timeStamp
                    = ZonedDateTime.parse(reader.getElementText());
            String tag = nextTag(reader).get();
            while (!tag.equals("minor")
                    && !tag.equals("comment")
                    && !tag.equals("text")) {
                tag = nextTag(reader).get();
            }
            final boolean minor = tag.equals("minor");
            if (minor) {
                tag = nextTag(reader).get();
            }
            final Optional<String> comment = tag.equals("comment")
                    ? Optional.of(reader.getElementText())
                    : Optional.empty();
            if (comment.isPresent()) {
                nextTag(reader);
            }
            final String text = reader.getElementText();
            result.add(new Revision(
                    parentId,
                    id,
                    timeStamp,
                    minor,
                    comment,
                    text));
            goToNextXBeforeY(reader, "revision", "page");
        }
        return result;
    }

    static private Page parsePageInfo(XMLStreamReader reader)
            throws XMLStreamException {
        Page result = new Page();
        nextTag(reader);
        result.setTitle(reader.getElementText());
        nextTag(reader);
        result.setNs(Integer.parseInt(reader.getElementText()));
        nextTag(reader);
        result.setId(Long.parseLong(reader.getElementText()));
        result.setRedirect(nextTag(reader).get().equals("redirect"));
        if (result.isRedirect()) {
            nextTag(reader);
        }
        return result;
    }

    static private boolean goToNextXBeforeY(
            XMLStreamReader reader,
            String X,
            String Y)
            throws XMLStreamException {
        while (reader.hasNext()) {
            int code = reader.next();
            if (code == XMLStreamReader.END_ELEMENT
                    && reader.getLocalName().equals(Y)) {
                return false;
            }
            if (code == XMLStreamReader.START_ELEMENT
                    && reader.getLocalName().equals(X)) {
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("empty-statement")
    static private Optional<String> nextTag(
            XMLStreamReader reader)
            throws XMLStreamException {
        while (reader.hasNext()
                && reader.next() != XMLStreamReader.START_ELEMENT);
        if (reader.getEventType() == XMLStreamReader.END_DOCUMENT) {
            return Optional.empty();
        } else {
            return Optional.of(getTag(reader));
        }
    }

    static private String getTag(
            XMLStreamReader reader)
            throws XMLStreamException {
        return reader.getName().getLocalPart();
    }
}
