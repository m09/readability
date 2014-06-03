package eu.crydee.readability.uima.ae;

import edu.berkeley.nlp.lm.NgramLanguageModel;
import edu.berkeley.nlp.lm.io.LmReaders;
import eu.crydee.readability.uima.model.Mapped;
import eu.crydee.readability.uima.model.Metrics;
import eu.crydee.readability.uima.res.ReadabilityDict;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.uima.UIMAFramework;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.descriptor.ExternalResource;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Logger;

public class ScorerAE extends JCasAnnotator_ImplBase {

    public static final String PARAM_LM_FILENAME = "LM_FILENAME_TXT";
    @ConfigurationParameter(name = PARAM_LM_FILENAME, mandatory = true)
    String lmFilename;

    final static public String RES_KEY = "KEY";
    @ExternalResource(key = RES_KEY)
    ReadabilityDict dict;

    private NgramLanguageModel<String> nlm;

    @Override
    public void initialize(UimaContext context)
            throws ResourceInitializationException {
        super.initialize(context);
        nlm = LmReaders.readLmBinary(lmFilename);
    }

    private static final Logger logger = UIMAFramework.getLogger(
            ScorerAE.class);

    @Override
    public void process(JCas jcas) throws AnalysisEngineProcessException {
    }

    @Override
    public void collectionProcessComplete()
            throws AnalysisEngineProcessException {
        int totalCount = dict.getTotalCount();
        for (Mapped original : dict.keySet()) {
            Map<Mapped, Metrics> revisions = dict.getRevisions(original).get();
            List<String> tokens = new ArrayList<>();
            tokens.add("<s>");
            tokens.addAll(original.getTokens());
            tokens.add("</s>");
            float lmProba = nlm.scoreSentence(tokens);
            for (Mapped revision : revisions.keySet()) {
                Metrics metric = revisions.get(revision);
                metric.score = Math.log(metric.count)
                        - lmProba
                        - Math.log(totalCount);
            }
        }
    }
}
