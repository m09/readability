package eu.crydee.readability.uima.scorer.cr;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.uima.UIMAFramework;
import org.apache.uima.UIMA_IllegalStateException;
import org.apache.uima.UimaContext;
import org.apache.uima.collection.CollectionException;
import org.apache.uima.fit.component.JCasCollectionReader_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Level;
import org.apache.uima.util.Logger;
import org.apache.uima.util.Progress;
import org.apache.uima.util.ProgressImpl;

public class CurrentCR extends JCasCollectionReader_ImplBase {

    private static final Logger logger = UIMAFramework.getLogger(
            CurrentCR.class);

    public static final String PARAM_DB_URL = "REVISIONSCR_DB_URL";
    @ConfigurationParameter(name = PARAM_DB_URL, mandatory = true)
    private String dbUrl;

    public static final String PARAM_DB_USER = "REVISIONSCR_DB_USER";
    @ConfigurationParameter(name = PARAM_DB_USER, mandatory = true)
    private String dbUser;

    public static final String PARAM_DB_PASSWORD = "REVISIONSCR_DB_PASSWORD";
    @ConfigurationParameter(name = PARAM_DB_PASSWORD, mandatory = true)
    private String dbPassword;

    public static final String PARAM_LIMIT = "LIMIT";
    @ConfigurationParameter(name = PARAM_LIMIT, mandatory = false)
    private Integer limit;

    private Connection connection = null;
    private ResultSet cursor = null;
    private int i = 0;

    @Override
    public void initialize(UimaContext context)
            throws ResourceInitializationException {
        super.initialize(context);
        try {
            connection = DriverManager.getConnection(
                    dbUrl,
                    dbUser,
                    dbPassword);
            StringBuilder querySb = new StringBuilder();
            querySb.append("SELECT * FROM rev_current");
            if (limit != null) {
                querySb.append(" LIMIT ").append(limit);
                logger.log(
                        Level.INFO,
                        "limit on the revisions to fetch set to: " + limit);
            } else {
                logger.log(
                        Level.INFO,
                        "limit on the revisions to fetch not set");
            }
            connection.setAutoCommit(false);
            Statement s = connection.createStatement();
            s.setFetchSize(5000);
            cursor = s.executeQuery(querySb.toString());
        } catch (SQLException ex) {
            throw new ResourceInitializationException(ex);
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        try {
            cursor.close();
            connection.close();
        } catch (SQLException ex) {
            logger.log(
                    Level.SEVERE,
                    "couldn't close the connection to the database");
            ex.printStackTrace(System.err);
            System.exit(1);
        }
    }

    @Override
    public void getNext(JCas jcas)
            throws IOException, CollectionException {
        try {
            if (!cursor.next()) {
                throw new UIMA_IllegalStateException();
            }
            String text = cursor.getString("text");
            if (text == null) {
                text = "";
            }
            jcas.setDocumentText(text);
            if (++i % 10 == 0) {
                logger.log(Level.INFO, "processing page n°" + i);
            }
        } catch (SQLException ex) {
            logger.log(
                    Level.SEVERE,
                    "couldn't access the database to retrieve the "
                    + "next revision");
            throw new CollectionException(ex);
        }
    }

    @Override
    public boolean hasNext()
            throws IOException, CollectionException {
        try {
            return cursor.isBeforeFirst()
                    || (cursor.getRow() != 0 && !cursor.isLast());
        } catch (SQLException ex) {
            logger.log(
                    Level.SEVERE,
                    "couldn't access the database to determine if more results "
                    + "are available");
            throw new CollectionException(ex);
        }
    }

    @Override
    public Progress[] getProgress() {
        return new Progress[]{new ProgressImpl(i, -1, Progress.ENTITIES)};
    }
}
