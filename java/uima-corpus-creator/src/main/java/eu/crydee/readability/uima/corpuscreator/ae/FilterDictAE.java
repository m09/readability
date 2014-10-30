package eu.crydee.readability.uima.corpuscreator.ae;

import eu.crydee.readability.uima.core.model.Mapped;
import eu.crydee.readability.uima.core.model.Metadata;
import eu.crydee.readability.uima.core.res.ReadabilityDict;
import java.util.Map;
import java.util.regex.Pattern;
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

    final private Pattern word = Pattern.compile("\\p{L}+");

    @Override
    public void collectionProcessComplete()
            throws AnalysisEngineProcessException {
        originals:
        for (Mapped o : inputDict.keySet()) {
            if (o.getTokens().size() > 5) {
                continue;
            }
            for (String token : o.getTokens()) {
                if (!word.matcher(token).matches()) {
                    continue originals;
                }
            }
            Map<Mapped, Metadata> revisedMap = inputDict.getRevisions(o).get();
            revisions:
            for (Mapped r : revisedMap.keySet()) {
                if (r.getTokens().size() > 5) {
                    continue;
                }
                for (String token : r.getTokens()) {
                    if (!word.matcher(token).matches()) {
                        continue revisions;
                    }
                }
                outputDict.add(o, r, revisedMap.get(r).getContexts());
            }
        }
    }

    @Override
    public void process(JCas jcas) throws AnalysisEngineProcessException {
        // We're only interested on collectionProcessComplete
    }
}
