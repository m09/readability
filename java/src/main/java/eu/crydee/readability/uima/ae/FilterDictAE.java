package eu.crydee.readability.uima.ae;

import eu.crydee.readability.uima.model.Mapped;
import eu.crydee.readability.uima.model.Metadata;
import eu.crydee.readability.uima.res.ReadabilityDict;
import java.util.Map;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ExternalResource;
import org.apache.uima.jcas.JCas;

public class FilterDictAE extends JCasAnnotator_ImplBase {

    final static public String RES_INPUT_KEY = "INPUT_KEY";
    @ExternalResource(key = RES_INPUT_KEY)
    ReadabilityDict inputDict;

    final static public String RES_OUTPUT_KEY = "OUTPUT_KEY";
    @ExternalResource(key = RES_OUTPUT_KEY)
    ReadabilityDict outputDict;

    @Override
    public void collectionProcessComplete()
            throws AnalysisEngineProcessException {
        for (Mapped o : inputDict.keySet()) {
            Map<Mapped, Metadata> revisedMap = inputDict.getRevisions(o).get();
            for (Mapped r : revisedMap.keySet()) {
                if (r.getTokens().size() <= o.getTokens().size()
                        && o.getTokens().size() < 4) {
                    outputDict.add(o, r, revisedMap.get(r).getContexts());
                }
            }
        }
    }

    @Override
    public void process(JCas jcas) throws AnalysisEngineProcessException {
        // We're only interested on collectionProcessComplete
    }
}
