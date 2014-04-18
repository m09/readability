package eu.crydee.readability.uima.ae;

import eu.crydee.readability.Diff;
import eu.crydee.readability.Edit;
import eu.crydee.readability.uima.ts.OriginalWords;
import eu.crydee.readability.uima.ts.RevisedSentences;
import eu.crydee.readability.uima.ts.RevisedWords;
import eu.crydee.readability.uima.ts.Token;
import java.util.List;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.CASException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

public class WordDiffAE extends JCasAnnotator_ImplBase {

    final static public String ORIGINAL_VIEW = "ORIGINAL_VIEW";
    final static public String REVISED_VIEW = "REVISED_VIEW";

    @Override
    public void process(JCas jcas) throws AnalysisEngineProcessException {
        JCas revisedView = null, originalView = null;
        try {
            originalView = jcas.getView(ORIGINAL_VIEW);
            revisedView = jcas.getView(REVISED_VIEW);
        } catch (CASException ex) {
            throw new AnalysisEngineProcessException(ex);
        }
        for (RevisedSentences revisedSentences : JCasUtil.select(
                revisedView,
                RevisedSentences.class)) {
            List<Token> revised = JCasUtil.selectCovered(
                    revisedView,
                    Token.class,
                    revisedSentences);
            StringBuilder revisedText = new StringBuilder();
            for (Token token : revised) {
                revisedText.append(token.getCoveredText()
                        .replaceAll("\n", "\u0000"))
                        .append("\n");
            }
            List<Token> original = JCasUtil.selectCovered(
                    originalView,
                    Token.class,
                    revisedSentences.getOriginalSentences());
            StringBuilder originalText = new StringBuilder();
            for (Token token : original) {
                originalText.append(token.getCoveredText()
                        .replaceAll("\n", "\u0000"))
                        .append("\n");
            }
            Diff edits = new Diff(
                    originalText.toString(),
                    revisedText.toString());
            for (Edit lineEdit : edits.getLineEdits()) {
                OriginalWords originalWords = new OriginalWords(
                        originalView,
                        original.get(lineEdit.getBeginA()).getBegin(),
                        original.get(lineEdit.getEndA() - 1).getEnd());
                originalWords.addToIndexes();
                RevisedWords revisedWords = new RevisedWords(
                        revisedView,
                        revised.get(lineEdit.getBeginB()).getBegin(),
                        revised.get(lineEdit.getEndB() - 1).getEnd());
                revisedWords.setOriginalWords(originalWords);
                revisedWords.addToIndexes();
            }
        }
    }
}
