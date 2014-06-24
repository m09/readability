package eu.crydee.readability.uima.ae;

import eu.crydee.readability.uima.model.Mapped;
import eu.crydee.readability.uima.res.ReadabilityDict;
import eu.crydee.readability.uima.ts.OriginalWords;
import eu.crydee.readability.uima.ts.RevisedSentences;
import eu.crydee.readability.uima.ts.RevisedWords;
import eu.crydee.readability.uima.ts.Token;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.CASException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ExternalResource;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

public class ResourcePopulatorAE extends JCasAnnotator_ImplBase {

    final static public String RES_TXT_KEY = "TXT_KEY";
    @ExternalResource(key = RES_TXT_KEY)
    ReadabilityDict dictTxt;

    final static public String RES_POS_KEY = "POS_KEY";
    @ExternalResource(key = RES_POS_KEY)
    ReadabilityDict dictPos;

    final static public String ORIGINAL_VIEW = "ORIGINAL_VIEW";
    final static public String REVISED_VIEW = "REVISED_VIEW";

    @Override
    public void process(JCas jcas) throws AnalysisEngineProcessException {
        JCas original, revised;
        try {
            original = jcas.getView(ORIGINAL_VIEW);
            revised = jcas.getView(REVISED_VIEW);
        } catch (CASException ex) {
            throw new AnalysisEngineProcessException(ex);
        }
        Map<RevisedSentences, Collection<RevisedWords>> index
                = JCasUtil.indexCovered(
                        revised,
                        RevisedSentences.class,
                        RevisedWords.class);
        for (Entry<RevisedSentences, Collection<RevisedWords>> e
                : index.entrySet()) {
            String senRev = e.getKey().getCoveredText(),
                    senOri = e.getKey().getOriginalSentences().getCoveredText();
            for (RevisedWords rw : e.getValue()) {
                OriginalWords ow = rw.getOriginalWords();
                List<String> originalTokens = new ArrayList<>(),
                        originalPOS = new ArrayList<>(),
                        revisedTokens = new ArrayList<>(),
                        revisedPOS = new ArrayList<>();
                for (Token token
                        : JCasUtil.selectCovered(revised, Token.class, rw)) {
                    revisedTokens.add(token.getCoveredText());
                    revisedPOS.add(token.getPOS());
                }
                for (Token token
                        : JCasUtil.selectCovered(original, Token.class, ow)) {
                    originalTokens.add(token.getCoveredText());
                    originalPOS.add(token.getPOS());
                }
                dictTxt.add(
                        new Mapped(
                                ow.getCoveredText(),
                                senOri,
                                originalTokens),
                        new Mapped(
                                rw.getCoveredText(),
                                senRev,
                                revisedTokens));
                dictPos.add(
                        new Mapped(
                                ow.getCoveredText(),
                                senOri,
                                originalPOS),
                        new Mapped(
                                rw.getCoveredText(),
                                senRev,
                                revisedPOS));
            }
        }
    }
}
