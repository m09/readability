package eu.crydee.readability.misc;

import java.util.Optional;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class XMLUtils {

    public static boolean goToNextXBeforeY(
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
    public static Optional<String> nextTag(
            XMLStreamReader reader)
            throws XMLStreamException {
        while (reader.hasNext()
                && reader.next() != XMLStreamReader.START_ELEMENT);
        if (reader.getEventType() == XMLStreamReader.END_DOCUMENT) {
            return Optional.empty();
        } else {
            return Optional.of(reader.getName().getLocalPart());
        }
    }
}
