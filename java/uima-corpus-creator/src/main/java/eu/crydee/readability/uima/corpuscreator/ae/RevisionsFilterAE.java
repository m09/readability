package eu.crydee.readability.uima.corpuscreator.ae;

import eu.crydee.readability.uima.corpuscreator.ts.RevisionInfo;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;
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
    final private Pattern category = Pattern.compile("/\\*.*?\\*/ ");
    
    @Override
    public void initialize(UimaContext context)
            throws ResourceInitializationException {
        super.initialize(context);
        validComments.add("simplify");
        validComments.add("simplifying");
        validComments.add("simplified");
        validComments.add("simplification");
        validComments.add("simpler");
    }
    
    @Override
    public void process(JCas jcas) throws AnalysisEngineProcessException {
        String text = jcas.getDocumentText();
        text = category.matcher(text).replaceFirst("");
        if (validComments.contains(text.toLowerCase(Locale.ENGLISH))
                && JCasUtil.selectSingle(jcas, RevisionInfo.class).getParentId()
                != 0) {
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
