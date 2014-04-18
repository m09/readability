package eu.crydee.readability.uima.ae;

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
import org.eclipse.jgit.diff.DiffAlgorithm;
import org.eclipse.jgit.diff.Edit;
import org.eclipse.jgit.diff.EditList;
import org.eclipse.jgit.diff.RawText;
import org.eclipse.jgit.diff.RawTextComparator;

public class SentenceDiffAE extends JCasAnnotator_ImplBase {

    final static public String ORIGINAL_VIEW = "ORIGINAL_VIEW";
    final static public String REVISED_VIEW = "REVISED_VIEW";
    final private DiffAlgorithm da = DiffAlgorithm.getAlgorithm(
            DiffAlgorithm.SupportedAlgorithm.MYERS);

    @Override
    public void process(JCas jcas) throws AnalysisEngineProcessException {
        JCas revisedView = null, originalView = null;
        try {
            originalView = jcas.getView(ORIGINAL_VIEW);
            revisedView = jcas.getView(REVISED_VIEW);
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
        EditList editList = da.diff(
                RawTextComparator.DEFAULT,
                new RawText(originalText.toString().getBytes()),
                new RawText(revisedText.toString().getBytes()));
        for (Edit edit : editList) {
            if (edit.getType().equals(Edit.Type.REPLACE)) {
                int fromA = original.get(edit.getBeginA()).getBegin(),
                        toA = original.get(edit.getEndA() - 1).getEnd(),
                        fromB = revised.get(edit.getBeginB()).getBegin(),
                        toB = revised.get(edit.getEndB() - 1).getEnd();
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
}
