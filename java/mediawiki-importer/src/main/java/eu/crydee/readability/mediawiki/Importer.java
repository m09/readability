package eu.crydee.readability.mediawiki;

import eu.crydee.readability.utils.XMLUtils;
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

public class Importer {

    static private class Params {

        public static int batchSizeDefault = 5000;

        public String dbUrl = null,
                dbPw = null,
                dbUser = null,
                dumpFilepath = null;
        public int batchSize = batchSizeDefault,
                from = 0,
                to = Integer.MAX_VALUE;
    }

    public static void main(String[] args)
            throws FileNotFoundException, XMLStreamException {
        Params params = new Params();
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
                        + "them to the database. Defaults to "
                        + Params.batchSizeDefault
                        + ".")
                .create("b"));
        options.addOption(OptionBuilder
                .withLongOpt("from")
                .hasArg()
                .withArgName("i")
                .withDescription("From which page we should import "
                        + "(inclusive). Defaults to the first page.")
                .create("f"));
        options.addOption(OptionBuilder
                .withLongOpt("to")
                .hasArg()
                .withArgName("i")
                .withDescription("To which page we should import (inclusive). "
                        + "Defaults to the last page.")
                .create("t"));
        CommandLineParser parser = new PosixParser();
        @SuppressWarnings("UnusedAssignment")
        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args);
            if (cmd.hasOption('h')) {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("importer [OPTIONS] DUMP_FILE", options);
                System.exit(0);
            } else {
                if (!cmd.hasOption('d')
                        || !cmd.hasOption('u')
                        || !cmd.hasOption('p')
                        || cmd.getArgList().isEmpty()) {
                    throw new ParseException("Option d, u or p missing, or "
                            + "input mediawiki dump not specified.\n"
                            + "Please use -h to learn more about the expected "
                            + "arguments.");
                }
            }
            params.dbUrl = cmd.getOptionValue('d');
            params.dbPw = cmd.getOptionValue('p');
            params.dbUser = cmd.getOptionValue('u');
            params.dumpFilepath = (String) cmd.getArgList().get(0);
            try {
                if (cmd.hasOption('f')) {
                    params.from = Integer.parseInt(cmd.getOptionValue('f'));
                }
                if (cmd.hasOption('t')) {
                    params.to = Integer.parseInt(cmd.getOptionValue('t'));
                }
                if (cmd.hasOption('b')) {
                    params.batchSize
                            = Integer.parseInt(cmd.getOptionValue('b'));
                }
            } catch (NumberFormatException e) {
                throw new ParseException("Couldn't parse one of f, t or b "
                        + "as integers. Please check your command or see "
                        + "the help (-h).");
            }
        } catch (ParseException ex) {
            System.err.println("The CLI args could not be parsed.");
            System.err.println("The error message was:");
            System.err.println(" " + ex.getMessage());
            System.exit(1);
        }

        XMLInputFactory factory = XMLInputFactory.newInstance();
        try (Connection connection = DriverManager.getConnection(
                params.dbUrl,
                params.dbUser,
                params.dbPw)) {
            PreparedStatement psRevisionsInfo = connection.prepareStatement(
                    "INSERT INTO rev ("
                    + "id, parent_id, comment, minor, timestamp, text, page_id"
                    + ") "
                    + "VALUES ("
                    + "?, ?, ?, ?, ?, ?, ?"
                    + ")");
            XMLStreamReader reader = factory.createXMLStreamReader(
                    new FileInputStream(params.dumpFilepath),
                    "utf8");
            int i = 0, k = 0;
            while (XMLUtils.goToNextXBeforeY(reader, "page", "mediawiki")
                    && i < params.to) {
                if (i++ < params.from) {
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
                    if (++k % params.batchSize == 0) {
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
                do {
                    tag = XMLUtils.nextTag(reader).get();
                } while (!tag.equals("comment")
                        && !tag.equals("text"));
            }
            final Optional<String> comment = tag.equals("comment")
                    ? Optional.of(reader.getElementText())
                    : Optional.empty();
            if (comment.isPresent()) {
                XMLUtils.goToNextXBeforeY(reader, "text", "revision");
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
