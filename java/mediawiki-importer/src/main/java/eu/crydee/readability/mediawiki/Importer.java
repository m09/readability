package eu.crydee.readability.mediawiki;

import eu.crydee.readability.mediawiki.xjb.MediaWikiType;
import eu.crydee.readability.mediawiki.xjb.PageType;
import eu.crydee.readability.mediawiki.xjb.RevisionType;
import eu.crydee.readability.mediawiki.xjb.UploadType;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
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
import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorStreamFactory;

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
            InputStream is = new FileInputStream(params.dumpFilepath);
            if (params.dumpFilepath.endsWith(".bz2")) {
                is = new CompressorStreamFactory().createCompressorInputStream(CompressorStreamFactory.BZIP2, is);
            }
            XMLStreamReader reader = factory.createXMLStreamReader(
                    is,
                    "utf8");
            JAXBContext context = JAXBContext.newInstance(MediaWikiType.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            int i = 0, k = 0;
            while (reader.hasNext() && i < params.to) {
                if (reader.getEventType() != XMLStreamReader.START_ELEMENT || reader.getLocalName() != "page") {
                    reader.next();
                    continue;
                }
                if (i++ < params.from) {
                    continue;
                }
                PageType page = unmarshaller.unmarshal(reader, PageType.class).getValue();
                System.out.print("\rParsing page " + i);
                if (page.getRedirect() != null || !page.getNs().equals(BigInteger.ZERO)) {
                    continue;
                }
                psRevisionsInfo.setLong(7, page.getId().longValue());
                for (Object o : page.getRevisionOrUpload()) {
                    if (o instanceof UploadType) {
                        continue;
                    }
                    RevisionType revisionInfo = (RevisionType) o;
                    psRevisionsInfo.setLong(1, revisionInfo.getId().longValue());
                    if (revisionInfo.getParentid() != null) {
                        psRevisionsInfo.setLong(
                                2,
                                revisionInfo.getParentid().longValue());
                    } else {
                        psRevisionsInfo.setNull(
                                2,
                                Types.BIGINT);
                    }
                    if (revisionInfo.getComment() != null) {
                        psRevisionsInfo.setString(
                                3,
                                revisionInfo.getComment().getValue());
                    } else {
                        psRevisionsInfo.setNull(
                                3,
                                Types.VARCHAR);
                    }
                    psRevisionsInfo.setBoolean(
                            4,
                            revisionInfo.getMinor() != null);
                    psRevisionsInfo.setString(
                            5,
                            dateFormat.format(revisionInfo.getTimestamp().toGregorianCalendar().getTime()));
                    psRevisionsInfo.setString(6, revisionInfo.getText().getValue());
                    psRevisionsInfo.addBatch();
                    if (++k % params.batchSize == 0) {
                        psRevisionsInfo.executeBatch();
                    }
                }
            }
            psRevisionsInfo.executeBatch();
            System.out.println();
        } catch (SQLException | CompressorException | JAXBException ex) {
            ex.printStackTrace(System.err);
        }
    }
}
