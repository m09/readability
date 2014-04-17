package eu.crydee.readability.uima.ae;

import eu.crydee.readability.uima.ts.Revision;
import java.util.HashSet;
import java.util.Set;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.AbstractCas;
import org.apache.uima.fit.component.JCasMultiplier_ImplBase;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;

public class RevisionsFilterAE extends JCasMultiplier_ImplBase {

    final private Set<String> validComments = new HashSet<>();
    private JCas current = null;

    @Override
    public void initialize(UimaContext context) throws ResourceInitializationException {
        super.initialize(context);
        validComments.add("simplify");
        validComments.add("simplifying");
        validComments.add("simplified");
        validComments.add("simplification");
        validComments.add("simpler");
    }

    @Override
    public void process(JCas jcas) throws AnalysisEngineProcessException {
        Revision revision = JCasUtil.selectSingle(jcas, Revision.class);
        if (revision.getComment() != null
                && validComments.contains(revision.getComment())
                && revision.getParentId() != 0) {
            current = jcas;
        } else {
            current = null;
        }
    }

    @Override
    public boolean hasNext() throws AnalysisEngineProcessException {
        return current != null;
    }

    @Override
    public AbstractCas next() throws AnalysisEngineProcessException {
        JCas result = current;
        current = null;
        return result;
    }
}