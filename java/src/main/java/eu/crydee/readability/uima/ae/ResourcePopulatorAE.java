package eu.crydee.readability.uima.ae;

import eu.crydee.readability.uima.model.Mapped;
import eu.crydee.readability.uima.res.ReadabilityDict;
import eu.crydee.readability.uima.ts.OriginalWords;
import eu.crydee.readability.uima.ts.RevisedWords;
import eu.crydee.readability.uima.ts.Token;
import java.util.ArrayList;
import java.util.List;
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
        for (RevisedWords rw : JCasUtil.select(revised, RevisedWords.class)) {
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
                            originalTokens),
                    new Mapped(
                            rw.getCoveredText(),
                            revisedTokens));
            dictPos.add(
                    new Mapped(
                            ow.getCoveredText(),
                            originalPOS),
                    new Mapped(
                            rw.getCoveredText(),
                            revisedPOS));
        }
    }
}
