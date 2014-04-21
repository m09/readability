package eu.crydee.readability.mediawiki;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.log4j.Logger;

public class Importer {

    @SuppressWarnings("UnusedDeclaration")
    private static final org.apache.log4j.Logger logger = Logger.getLogger(
            Importer.class.getCanonicalName());

    public static void main(String[] args)
            throws JAXBException, FileNotFoundException, XMLStreamException {
        Options options = new Options();
        options.addOption("h", "help", false, "print this message");
        options.addOption(OptionBuilder
                .isRequired()
                .withLongOpt("db-url")
                .hasArg()
                .withArgName("jdbc:...")
                .withDescription("The database to use to retrieve revisions "
                        + "information. JDBC format.")
                .create("d"));
        options.addOption(OptionBuilder
                .isRequired()
                .withLongOpt("username")
                .hasArg()
                .withArgName("dbuser")
                .withDescription("User to use to access the database of "
                        + "revisions.")
                .create("u"));
        options.addOption(OptionBuilder
                .isRequired()
                .withLongOpt("password")
                .hasArg()
                .withArgName("pw")
                .withDescription("Password of the user account to connect to "
                        + "the database.")
                .create("p"));
        options.addOption(OptionBuilder
                .isRequired()
                .withLongOpt("from")
                .hasArg()
                .withArgName("i")
                .withDescription("From which page we should import "
                        + "(inclusive).")
                .create("f"));
        options.addOption(OptionBuilder
                .isRequired()
                .withLongOpt("to")
                .hasArg()
                .withArgName("i")
                .withDescription("To which page we should import (exclusive).")
                .create("t"));
        CommandLineParser parser = new PosixParser();
        @SuppressWarnings("UnusedAssignment")
        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException ex) {
            System.err.println("The CLI args could not be parsed.");
            System.err.println("The error message was:");
            System.err.println(" " + ex.getMessage());
            System.exit(1);
        }

        final String DB_URL = cmd.getOptionValue('d'),
                DB_USER = cmd.getOptionValue('u'),
                DB_PW = cmd.getOptionValue('p');
        final int FROM = Integer.parseInt(cmd.getOptionValue('f')),
                TO = Integer.parseInt(cmd.getOptionValue('t'));

        XMLInputFactory factory = XMLInputFactory.newInstance();
        try (Connection connection = DriverManager.getConnection(
                DB_URL,
                DB_USER,
                DB_PW)) {
            PreparedStatement psRevisionsInfo = connection.prepareStatement(
                    "INSERT INTO revisions_info "
                    + "(id, parent_id, comment, minor, page_id, timestamp) "
                    + "VALUES (?, ?, ?, ?, ?, ?)"),
                    psPages = connection.prepareStatement(
                            "INSERT INTO pages "
                            + "(id, title) "
                            + "VALUES (?, ?)"),
                    psRevisions = connection.prepareStatement(
                            "INSERT INTO revisions "
                            + "(id, text) "
                            + "VALUES (?, ?)");
            XMLStreamReader reader = factory.createXMLStreamReader(
                    new FileInputStream(
                            "dump.xml"),
                    "utf8");
            int i = 0;
            while (goToNextXBeforeY(reader, "page", "mediawiki")
                    && i < TO) {
                if (i++ < FROM) {
                    continue;
                }
                System.out.print("\r" + i);
                Page page = parsePageInfo(reader);
                if (page.isRedirect() || page.getNs() != 0) {
                    continue;
                }
                psPages.setLong(1, page.getId());
                psPages.setString(2, page.getTitle());
                psPages.execute();
                for (Revision revisionInfo : parseRevisions(reader)) {
                    psRevisions.setLong(1, revisionInfo.getId());
                    psRevisions.setString(2, revisionInfo.getText());
                    psRevisions.execute();
                    psRevisionsInfo.setLong(1, revisionInfo.getId());
                    if (revisionInfo.getParentId().isPresent()) {
                        psRevisionsInfo.setLong(
                                2,
                                revisionInfo.getParentId().get());
                    } else {
                        psRevisionsInfo.setNull(
                                2,
                                Types.BIGINT);
                    }
                    if (revisionInfo.getComment().isPresent()) {
                        psRevisionsInfo.setString(
                                3,
                                revisionInfo.getComment().get());
                    } else {
                        psRevisionsInfo.setNull(
                                3,
                                Types.VARCHAR);
                    }
                    psRevisionsInfo.setBoolean(
                            4,
                            revisionInfo.isMinor());
                    psRevisionsInfo.setLong(
                            5,
                            page.getId());
                    psRevisionsInfo.setString(
                            6,
                            revisionInfo.getTimeStamp().format(
                                    DateTimeFormatter.ISO_DATE_TIME));
                    psRevisionsInfo.execute();
                }
            }
            System.out.println();
        } catch (SQLException ex) {
            ex.printStackTrace(System.err);
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
