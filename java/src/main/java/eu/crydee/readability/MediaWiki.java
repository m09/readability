package eu.crydee.readability;

import eu.crydee.readability.model.Lang;
import eu.crydee.readability.model.Revision;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class MediaWiki {

    private static final Logger logger
            = Logger.getLogger(MediaWiki.class.getCanonicalName());

    private static final String prefix = "https://",
            suffix = ".wikipedia.org/w/api.php?";

    public static List<Revision> getRevisions(String title, Lang lang) {
        List<Revision> revisions = new ArrayList<>();
        Document content = parse("action=query"
                + "&prop=revisions"
                + "&titles=" + title
                + "&rvprop=timestamp|comment|parsedcomment|flags|size|ids|tags"
                + "&rvlimit=max"
                + "&format=xml",
                lang);
        NodeList revNodes = content.getElementsByTagName("rev");
        for (int i = 0; i < revNodes.getLength(); i++) {
            Element revNode = (Element) revNodes.item(i);
            revisions.add(new Revision(
                    Integer.parseInt(revNode.getAttribute("revid")),
                    Integer.parseInt(revNode.getAttribute("parentid")),
                    Integer.parseInt(revNode.getAttribute("size")),
                    LocalDateTime.parse(revNode.getAttribute("timestamp"),
                            DateTimeFormatter.ofPattern(
                                    "yyyy-MM-dd'T'HH:mm:ss'Z'")),
                    revNode.getAttribute("comment"),
                    revNode.getAttribute("parsedcomment"),
                    revNode.hasAttribute("minor")));
        }
        return revisions;
    }

    private static Document parse(String query, Lang lang) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            return db.parse(new URL(
                    prefix
                    + (lang == Lang.ENGLISH ? "en" : "simple")
                    + suffix
                    + query)
                    .openStream());
        } catch (SAXException ex) {
            logger.error("couldn't parse the document: "
                    + ex.getLocalizedMessage());
            System.exit(1);
        } catch (ParserConfigurationException ex) {
            logger.error("couldn't configure parser: "
                    + ex.getLocalizedMessage());
            System.exit(1);
        } catch (MalformedURLException ex) {
            logger.error("malformed URL: " + ex.getLocalizedMessage());
            System.exit(1);
        } catch (IOException ex) {
            logger.error("error while retrieving URL: "
                    + ex.getLocalizedMessage());
            System.exit(1);
        }
        return null;
    }
}
