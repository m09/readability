package eu.crydee.readability.uima.ae;

import eu.crydee.readability.uima.ts.RevisionInfo;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.uima.UIMAFramework;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.component.ViewCreatorAnnotator;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Level;

public class RevisionsGetterAE extends JCasAnnotator_ImplBase {

    public static final String PARAM_DB_URL = "REVISIONSCR_DB_URL";
    @ConfigurationParameter(name = PARAM_DB_URL, mandatory = true)
    private String dbUrl;

    public static final String PARAM_DB_USER = "REVISIONSCR_DB_USER";
    @ConfigurationParameter(name = PARAM_DB_USER, mandatory = true)
    private String dbUser;

    public static final String PARAM_DB_PASSWORD = "REVISIONSCR_DB_PASSWORD";
    @ConfigurationParameter(name = PARAM_DB_PASSWORD, mandatory = true)
    private String dbPassword;

    private PreparedStatement ps;
    private Connection connection;

    @Override
    public void initialize(UimaContext context)
            throws ResourceInitializationException {
        super.initialize(context);
        try {
            connection = DriverManager.getConnection(
                    dbUrl,
                    dbUser,
                    dbPassword);
            ps = connection.prepareStatement(
                    "SELECT id, text "
                    + "FROM rev "
                    + "WHERE id = ? OR id = ?");
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
            UIMAFramework.getLogger(RevisionsGetterAE.class).log(
                    Level.SEVERE,
                    "couldn't close the connection to the database");
            ex.printStackTrace(System.err);
            System.exit(1);
        }
    }

    @Override
    public void process(JCas jcas) throws AnalysisEngineProcessException {
        RevisionInfo revisionInfo
                = JCasUtil.selectSingle(jcas, RevisionInfo.class);
        try {
            ps.setLong(1, revisionInfo.getId());
            ps.setLong(2, revisionInfo.getParentId());
            ResultSet rs = ps.executeQuery();
            JCas original = ViewCreatorAnnotator.createViewSafely(
                    jcas,
                    "original"),
                    revised = ViewCreatorAnnotator.createViewSafely(
                            jcas,
                            "revised");
            boolean revisedF = false, originalF = false;
            for (int i = 0; i < 2; i++) {
                if (!rs.next()) {
                    throw new AnalysisEngineProcessException();
                }
                if (rs.getLong("id") == revisionInfo.getId()) {
                    revised.setDocumentText(rs.getString("text"));
                    revisedF = true;
                } else if (rs.getLong("id") == revisionInfo.getParentId()) {
                    original.setDocumentText(rs.getString("text"));
                    originalF = true;
                }
            }
            if (!originalF || !revisedF) {
                throw new AnalysisEngineProcessException();
            }
        } catch (SQLException ex) {
            UIMAFramework.getLogger(RevisionsGetterAE.class).log(
                    Level.SEVERE,
                    "couldn't access the database");
            ex.printStackTrace(System.err);
            System.exit(1);
        } catch (AnalysisEngineProcessException ex) {
            UIMAFramework.getLogger(RevisionsGetterAE.class).log(
                    Level.SEVERE,
                    "couldn't retrieve both required revisions");
            ex.printStackTrace(System.err);
            System.exit(1);
        }
    }
}
