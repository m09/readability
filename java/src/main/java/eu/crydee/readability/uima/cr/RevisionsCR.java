package eu.crydee.readability.uima.cr;

import eu.crydee.readability.uima.ts.Revision;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.uima.UIMAFramework;
import org.apache.uima.UIMA_IllegalStateException;
import org.apache.uima.UimaContext;
import org.apache.uima.collection.CollectionException;
import org.apache.uima.fit.component.JCasCollectionReader_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Level;
import org.apache.uima.util.Progress;
import org.apache.uima.util.ProgressImpl;

public class RevisionsCR extends JCasCollectionReader_ImplBase {

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
            String query = "SELECT id, parent_id, comment, minor, timestamp "
                    + "FROM rev";
            if (limit != null) {
                query += " LIMIT " + limit;
            }
            PreparedStatement ps = connection.prepareStatement(query);
            cursor = ps.executeQuery();
        } catch (SQLException ex) {
            throw new ResourceInitializationException(ex);
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        try {
            connection.close();
        } catch (SQLException ex) {
            UIMAFramework.getLogger(RevisionsCR.class).log(
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
            String comment = cursor.getString("comment");
            if (comment == null) {
                comment = "";
            }
            jcas.setDocumentText(comment);
            Revision revision = new Revision(
                    jcas,
                    0,
                    jcas.getDocumentText().length());
            revision.setId(cursor.getLong("id"));
            revision.setParentId(cursor.getLong("parent_id"));
            revision.setMinor(cursor.getBoolean("minor"));
            revision.setTimestamp(cursor.getString("timestamp"));
            revision.addToIndexes();
            if (++i % 10000 == 0) {
                UIMAFramework.getLogger(RevisionsCR.class).log(
                        Level.INFO,
                        "processing revision n°"
                        + i
                        + ": id is "
                        + cursor.getLong("id"));
            }
        } catch (SQLException ex) {
            UIMAFramework.getLogger(RevisionsCR.class).log(
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
            UIMAFramework.getLogger(RevisionsCR.class).log(
                    Level.SEVERE,
                    "couldn't access the database to determine if "
                    + "more results are available");
            throw new CollectionException(ex);
        }
    }

    @Override
    public Progress[] getProgress() {
        return new Progress[]{new ProgressImpl(i, -1, Progress.ENTITIES)};
    }
}
