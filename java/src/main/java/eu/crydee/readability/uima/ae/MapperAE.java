package eu.crydee.readability.uima.ae;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import eu.crydee.readability.uima.model.POSs;
import eu.crydee.readability.uima.model.Revision;
import eu.crydee.readability.uima.model.Tokens;
import eu.crydee.readability.uima.res.ReadabilityDict;
import java.util.Map;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ExternalResource;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;

public class MapperAE extends JCasAnnotator_ImplBase {

    final static public String RES_KEY = "RES_KEY";
    @ExternalResource(key = RES_KEY)
    private ReadabilityDict dict;

    final private SetMultimap<Tokens, Pair<Revision, Integer>> byTokens
            = HashMultimap.create();

    final private SetMultimap<POSs, Pair<Revision, Integer>> byPOS
            = HashMultimap.create();

    @Override
    public void initialize(UimaContext context) throws ResourceInitializationException {
        super.initialize(context);
        for (Revision original : dict.keySet()) {
            POSs pos = original.getPOS();
            Tokens tokens = original.getTokens();
            Map<Revision, Integer> revisedMap
                    = dict.getRevisions(original).get();
            for (Revision revised : revisedMap.keySet()) {
                Pair<Revision, Integer> pair
                        = Pair.of(revised, revisedMap.get(revised));
                byTokens.put(tokens, pair);
                byPOS.put(pos, pair);
            }
        }
    }

    @Override
    public void process(JCas jcas) throws AnalysisEngineProcessException {
        for (Revision revision : dict.keySet()) {
            //
        }
    }
}
