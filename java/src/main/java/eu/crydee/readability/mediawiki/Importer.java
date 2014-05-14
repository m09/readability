package eu.crydee.readability.mediawiki;

import eu.crydee.readability.misc.XMLUtils;
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
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
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
            throws FileNotFoundException, XMLStreamException {
        Options options = new Options();
        options.addOption("h", "help", false, "Print this message.");
        options.addOption(OptionBuilder
                .withLongOpt("db-url")
                .hasArg()
                .withArgName("jdbc:...")
                .withDescription("The database to use to store revisions "
                        + "information. JDBC format.")
                .create("d"));
        options.addOption(OptionBuilder
                .withLongOpt("username")
                .hasArg()
                .withArgName("dbuser")
                .withDescription("User to use to access the database of "
                        + "revisions.")
                .create("u"));
        options.addOption(OptionBuilder
                .withLongOpt("password")
                .hasArg()
                .withArgName("pw")
                .withDescription("Password of the user account to connect to "
                        + "the database.")
                .create("p"));
        options.addOption(OptionBuilder
                .withLongOpt("batch-size")
                .hasArg()
                .withArgName("revisions")
                .withDescription("Number of revisions to group before sending "
                        + "them to the database.")
                .create("b"));
        options.addOption(OptionBuilder
                .withLongOpt("from")
                .hasArg()
                .withArgName("i")
                .withDescription("From which page we should import "
                        + "(inclusive).")
                .create("f"));
        options.addOption(OptionBuilder
                .withLongOpt("to")
                .hasArg()
                .withArgName("i")
                .withDescription("To which page we should import (inclusive).")
                .create("t"));
        CommandLineParser parser = new PosixParser();
        @SuppressWarnings("UnusedAssignment")
        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args);
            if (cmd.hasOption('h')) {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("importer", options);
                System.exit(0);
            } else {
                if (!cmd.hasOption('d')
                        || !cmd.hasOption('u')
                        || !cmd.hasOption('p')
                        || !cmd.hasOption('b')) {
                    throw new ParseException("Argument missing among the "
                            + "required arguments d, u, p and b.\n"
                            + "Please use -h to learn more about the expected "
                            + "arguments.");
                }
            }
        } catch (ParseException ex) {
            System.err.println("The CLI args could not be parsed.");
            System.err.println("The error message was:");
            System.err.println(" " + ex.getMessage());
            System.exit(1);
        }

        final String DB_URL = cmd.getOptionValue('d'),
                DB_USER = cmd.getOptionValue('u'),
                DB_PW = cmd.getOptionValue('p');
        final int FROM;
        if (cmd.hasOption('f')) {
            FROM = Integer.parseInt(cmd.getOptionValue('f'));
        } else {
            FROM = 0;
        }
        final int TO;
        if (cmd.hasOption('t')) {
            TO = Integer.parseInt(cmd.getOptionValue('t'));
        } else {
            TO = Integer.MAX_VALUE;
        }
        final int BATCH_SIZE = Integer.parseInt(cmd.getOptionValue('b'));

        XMLInputFactory factory = XMLInputFactory.newInstance();
        try (Connection connection = DriverManager.getConnection(
                DB_URL,
                DB_USER,
                DB_PW)) {
            PreparedStatement psRevisionsInfo = connection.prepareStatement(
                    "INSERT INTO rev ("
                    + "id, parent_id, comment, minor, timestamp, text, page_id"
                    + ") "
                    + "VALUES ("
                    + "?, ?, ?, ?, ?, ?, ?"
                    + ")");
            XMLStreamReader reader = factory.createXMLStreamReader(
                    new FileInputStream(
                            "dump.xml"),
                    "utf8");
            int i = 0, k = 0;
            while (XMLUtils.goToNextXBeforeY(reader, "page", "mediawiki")
                    && i < TO) {
                if (i++ < FROM) {
                    continue;
                }
                System.out.print("\r" + i);
                Page page = parsePageInfo(reader);
                if (page.isRedirect() || page.getNs() != 0) {
                    continue;
                }
                psRevisionsInfo.setLong(7, page.getId());
                for (Revision revisionInfo : parseRevisions(reader)) {
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
                    psRevisionsInfo.setString(
                            5,
                            revisionInfo.getTimeStamp().format(
                                    DateTimeFormatter.ISO_DATE_TIME));
                    psRevisionsInfo.setString(6, revisionInfo.getText());
                    psRevisionsInfo.addBatch();
                    if (++k % BATCH_SIZE == 0) {
                        psRevisionsInfo.executeBatch();
                    }
                }
            }
            psRevisionsInfo.executeBatch();
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
            XMLUtils.goToNextXBeforeY(reader, "id", "revision");
            final long id = Long.parseLong(reader.getElementText());
            final Optional<Long> parentId;
            if (XMLUtils.nextTag(reader).get().equals("parentid")) {
                parentId = Optional.of(Long.parseLong(reader.getElementText()));
                XMLUtils.goToNextXBeforeY(reader, "timestamp", "revision");
            } else {
                parentId = Optional.empty();
            }
            final ZonedDateTime timeStamp
                    = ZonedDateTime.parse(reader.getElementText());
            String tag = XMLUtils.nextTag(reader).get();
            while (!tag.equals("minor")
                    && !tag.equals("comment")
                    && !tag.equals("text")) {
                tag = XMLUtils.nextTag(reader).get();
            }
            final boolean minor = tag.equals("minor");
            if (minor) {
                tag = XMLUtils.nextTag(reader).get();
            }
            final Optional<String> comment = tag.equals("comment")
                    ? Optional.of(reader.getElementText())
                    : Optional.empty();
            if (comment.isPresent()) {
                XMLUtils.nextTag(reader);
            }
            final String text = reader.getElementText();
            result.add(new Revision(
                    parentId,
                    id,
                    timeStamp,
                    minor,
                    comment,
                    text));
            XMLUtils.goToNextXBeforeY(reader, "revision", "page");
        }
        return result;
    }

    static private Page parsePageInfo(XMLStreamReader reader)
            throws XMLStreamException {
        Page result = new Page();
        XMLUtils.nextTag(reader);
        result.setTitle(reader.getElementText());
        XMLUtils.nextTag(reader);
        result.setNs(Integer.parseInt(reader.getElementText()));
        XMLUtils.nextTag(reader);
        result.setId(Long.parseLong(reader.getElementText()));
        result.setRedirect(XMLUtils.nextTag(reader).get().equals("redirect"));
        if (result.isRedirect()) {
            XMLUtils.nextTag(reader);
        }
        return result;
    }
}
