package eu.crydee.readability.uima.ae;

import java.util.ArrayList;
import java.util.List;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.cas.Feature;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.TypeSystem;
import org.apache.uima.cas.text.AnnotationFS;
import org.apache.uima.fit.component.CasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.util.CasUtil;
import org.apache.uima.jcas.JCas;
import org.eclipse.jgit.diff.DiffAlgorithm;
import org.eclipse.jgit.diff.Edit;
import org.eclipse.jgit.diff.EditList;
import org.eclipse.jgit.diff.RawText;
import org.eclipse.jgit.diff.RawTextComparator;

public class SentenceDiffAE extends CasAnnotator_ImplBase {

    public static final String PARAM_SENTENCE_TYPE
            = "SENTENCE_TYPE";
    @ConfigurationParameter(
            name = PARAM_SENTENCE_TYPE,
            mandatory = true)
    private String sentenceType;

    public static final String PARAM_REVISED_SENTENCES_TYPE
            = "REVISED_SENTENCES_TYPE";
    @ConfigurationParameter(
            name = PARAM_REVISED_SENTENCES_TYPE,
            mandatory = true)
    private String revisedSentencesType;

    public static final String PARAM_REVISED_SENTENCES_FEATURE
            = "REVISED_SENTENCES_FEATURE";
    @ConfigurationParameter(
            name = PARAM_REVISED_SENTENCES_FEATURE,
            mandatory = true)
    private String revisedSentencesFeature;

    public static final String PARAM_ORIGINAL_SENTENCES_TYPE
            = "ORIGINAL_SENTENCES_TYPE";
    @ConfigurationParameter(
            name = PARAM_ORIGINAL_SENTENCES_TYPE,
            mandatory = true)
    private String originalSentencesType;

    public static final String PARAM_ORIGINAL_SENTENCES_FEATURE
            = "ORIGINAL_SENTENCES_FEATURE";
    @ConfigurationParameter(
            name = PARAM_ORIGINAL_SENTENCES_FEATURE,
            mandatory = true)
    private String originalSentencesFeature;

    final static public String ORIGINAL_VIEW = "ORIGINAL_VIEW";
    final static public String REVISED_VIEW = "REVISED_VIEW";

    private Type sentenceTypeT = null,
            revisedSentencesTypeT = null,
            originalSentencesTypeT = null;
    private Feature revisedSentencesFeatureF = null,
            originalSentencesFeatureF = null;

    @Override
    public void typeSystemInit(TypeSystem aTypeSystem)
            throws AnalysisEngineProcessException {
        super.typeSystemInit(aTypeSystem);
        sentenceTypeT = aTypeSystem.getType(sentenceType);
        revisedSentencesTypeT = aTypeSystem.getType(revisedSentencesType);
        originalSentencesTypeT = aTypeSystem.getType(originalSentencesType);
        revisedSentencesFeatureF = originalSentencesTypeT.getFeatureByBaseName(
                revisedSentencesFeature);
        originalSentencesFeatureF = revisedSentencesTypeT.getFeatureByBaseName(
                originalSentencesFeature);
    }

    @Override
    public void process(CAS cas) throws AnalysisEngineProcessException {
        JCas jcas;
        try {
            jcas = cas.getJCas();
        } catch (CASException ex) {
            throw new AnalysisEngineProcessException(ex);
        }
        final JCas revisedView, originalView;
        try {
            originalView = jcas.getView(ORIGINAL_VIEW);
            revisedView = jcas.getView(REVISED_VIEW);
        } catch (CASException ex) {
            throw new AnalysisEngineProcessException(ex);
        }
        final List<AnnotationFS> revised = new ArrayList<>(CasUtil.select(
                revisedView.getCas(),
                sentenceTypeT));
        StringBuilder revisedText = new StringBuilder();
        for (AnnotationFS sentence : revised) {
            revisedText.append(sentence.getCoveredText()
                    .replaceAll("\n", "\u0000"))
                    .append('\n');
        }
        final List<AnnotationFS> original = new ArrayList<>(CasUtil.select(
                originalView.getCas(),
                sentenceTypeT));
        final StringBuilder originalText = new StringBuilder();
        for (AnnotationFS sentence : original) {
            originalText.append(sentence.getCoveredText()
                    .replaceAll("\n", "\u0000"))
                    .append('\n');
        }
        final DiffAlgorithm da = DiffAlgorithm.getAlgorithm(
                DiffAlgorithm.SupportedAlgorithm.MYERS);
        final EditList editList = da.diff(
                RawTextComparator.DEFAULT,
                new RawText(originalText.toString().getBytes()),
                new RawText(revisedText.toString().getBytes()));
        for (Edit edit : editList) {
            if (edit.getType().equals(Edit.Type.REPLACE)) {
                final int fromA = original.get(edit.getBeginA()).getBegin(),
                        toA = original.get(edit.getEndA() - 1).getEnd(),
                        fromB = revised.get(edit.getBeginB()).getBegin(),
                        toB = revised.get(edit.getEndB() - 1).getEnd();
                AnnotationFS originalSentences = originalView.getCas()
                        .createAnnotation(originalSentencesTypeT, fromA, toA);
                AnnotationFS revisedSentences = revisedView.getCas()
                        .createAnnotation(revisedSentencesTypeT, fromB, toB);
                originalSentences.setFeatureValue(
                        revisedSentencesFeatureF,
                        revisedSentences);
                revisedSentences.setFeatureValue(
                        originalSentencesFeatureF,
                        originalSentences);
                originalView.addFsToIndexes(originalSentences);
                revisedView.addFsToIndexes(revisedSentences);
            }
        }
    }
}
