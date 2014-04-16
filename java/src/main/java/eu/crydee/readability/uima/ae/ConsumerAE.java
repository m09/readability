package eu.crydee.readability.uima.ae;

import eu.crydee.readability.uima.ts.Revision;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

public class ConsumerAE extends JCasAnnotator_ImplBase {

    @Override
    public void process(JCas jcas) throws AnalysisEngineProcessException {
        Revision revision = JCasUtil.selectSingle(jcas, Revision.class);
        System.out.println(revision.getId());
    }
}
