package eu.crydee.readability.uima.ae;

import eu.crydee.readability.Diff;
import eu.crydee.readability.Edit;
import eu.crydee.readability.uima.ts.Area;
import eu.crydee.readability.uima.ts.SentenceDiff;
import eu.crydee.readability.uima.ts.Token;
import eu.crydee.readability.uima.ts.WordDiff;
import java.util.List;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.CASException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

public class WordDiffAE extends JCasAnnotator_ImplBase {

    @Override
    public void process(JCas jcas) throws AnalysisEngineProcessException {
        JCas revisedView = null, originalView = null;
        try {
            revisedView = jcas.getView("txtRevised");
            originalView = jcas.getView("txtOriginal");
        } catch (CASException ex) {
            throw new AnalysisEngineProcessException(ex);
        }
        for (SentenceDiff sentence : JCasUtil.select(
                revisedView,
                SentenceDiff.class)) {
            List<Token> revised = JCasUtil.selectCovered(
                    revisedView,
                    Token.class,
                    sentence);
            StringBuilder revisedText = new StringBuilder();
            for (Token token : revised) {
                revisedText.append(token.getCoveredText()
                        .replaceAll("\n", "\u0000"))
                        .append("\n");
            }
            List<Token> original = JCasUtil.selectCovered(
                    originalView,
                    Token.class,
                    sentence.getOriginal());
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
                int fromA = original.get(lineEdit.getBeginA()).getBegin(),
                        toA = original.get(lineEdit.getEndA() - 1).getEnd(),
                        fromB = revised.get(lineEdit.getBeginB()).getBegin(),
                        toB = revised.get(lineEdit.getEndB() - 1).getEnd();
                Area area = new Area(originalView, fromA, toA);
                area.addToIndexes();
                WordDiff diff = new WordDiff(revisedView, fromB, toB);
                diff.setOriginal(area);
                diff.addToIndexes();
            }
        }
    }
}
