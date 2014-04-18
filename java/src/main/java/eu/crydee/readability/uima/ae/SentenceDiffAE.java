package eu.crydee.readability.uima.ae;

import eu.crydee.readability.Diff;
import eu.crydee.readability.Edit;
import eu.crydee.readability.uima.ts.OriginalSentences;
import eu.crydee.readability.uima.ts.RevisedSentences;
import eu.crydee.readability.uima.ts.Sentence;
import java.util.ArrayList;
import java.util.List;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.CASException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

public class SentenceDiffAE extends JCasAnnotator_ImplBase {

    @Override
    public void process(JCas jcas) throws AnalysisEngineProcessException {
        JCas revisedView = null, originalView = null;
        try {
            originalView = jcas.getView("txtOriginal");
            revisedView = jcas.getView("txtRevised");
        } catch (CASException ex) {
            throw new AnalysisEngineProcessException(ex);
        }
        List<Sentence> revised = new ArrayList<>(JCasUtil.select(
                revisedView,
                Sentence.class));
        StringBuilder revisedText = new StringBuilder();
        for (Sentence sentence : revised) {
            revisedText.append(sentence.getCoveredText()
                    .replaceAll("\n", "\u0000"))
                    .append('\n');
        }
        List<Sentence> original = new ArrayList<>(JCasUtil.select(
                originalView,
                Sentence.class));
        StringBuilder originalText = new StringBuilder();
        for (Sentence sentence : original) {
            originalText.append(sentence.getCoveredText()
                    .replaceAll("\n", "\u0000"))
                    .append('\n');
        }
        Diff edits = new Diff(
                originalText.toString(),
                revisedText.toString());
        for (Edit lineEdit : edits.getLineEdits()) {
            int fromA = original.get(lineEdit.getBeginA()).getBegin(),
                    toA = original.get(lineEdit.getEndA() - 1).getEnd(),
                    fromB = revised.get(lineEdit.getBeginB()).getBegin(),
                    toB = revised.get(lineEdit.getEndB() - 1).getEnd();
            OriginalSentences originalSentences = new OriginalSentences(
                    originalView,
                    fromA,
                    toA);
            originalSentences.addToIndexes();
            RevisedSentences revisedSentences = new RevisedSentences(
                    revisedView,
                    fromB,
                    toB);
            revisedSentences.setOriginalSentences(originalSentences);
            revisedSentences.addToIndexes();
        }
    }
}
