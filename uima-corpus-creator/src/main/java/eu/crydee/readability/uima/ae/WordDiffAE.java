package eu.crydee.readability.uima.ae;

import java.util.List;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.cas.Feature;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.TypeSystem;
import org.apache.uima.cas.text.AnnotationFS;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.util.CasUtil;
import org.apache.uima.jcas.JCas;
import org.eclipse.jgit.diff.DiffAlgorithm;
import org.eclipse.jgit.diff.Edit;
import org.eclipse.jgit.diff.EditList;
import org.eclipse.jgit.diff.RawText;
import org.eclipse.jgit.diff.RawTextComparator;

public class WordDiffAE extends SentenceDiffAE {

    public static final String PARAM_TOKEN_TYPE
            = "TOKEN_TYPE";
    @ConfigurationParameter(
            name = PARAM_TOKEN_TYPE,
            mandatory = true)
    private String tokenType;

    public static final String PARAM_REVISED_WORDS_TYPE
            = "REVISED_WORDS_TYPE";
    @ConfigurationParameter(
            name = PARAM_REVISED_WORDS_TYPE,
            mandatory = true)
    private String revisedWordsType;

    public static final String PARAM_REVISED_WORDS_FEATURE
            = "REVISED_WORDS_FEATURE";
    @ConfigurationParameter(
            name = PARAM_REVISED_WORDS_FEATURE,
            mandatory = true)
    private String revisedWordsFeature;

    public static final String PARAM_ORIGINAL_WORDS_TYPE
            = "ORIGINAL_WORDS_TYPE";
    @ConfigurationParameter(
            name = PARAM_ORIGINAL_WORDS_TYPE,
            mandatory = true)
    private String originalWordsType;

    public static final String PARAM_ORIGINAL_WORDS_FEATURE
            = "ORIGINAL_WORDS_FEATURE";
    @ConfigurationParameter(
            name = PARAM_ORIGINAL_WORDS_FEATURE,
            mandatory = true)
    private String originalWordsFeature;

    protected Type tokenTypeT = null,
            revisedWordsTypeT = null,
            originalWordsTypeT = null;
    protected Feature revisedWordsFeatureF = null,
            originalWordsFeatureF = null;

    @Override
    public void typeSystemInit(TypeSystem aTypeSystem) throws AnalysisEngineProcessException {
        super.typeSystemInit(aTypeSystem);
        tokenTypeT = aTypeSystem.getType(tokenType);
        revisedWordsTypeT = aTypeSystem.getType(revisedWordsType);
        originalWordsTypeT = aTypeSystem.getType(originalWordsType);
        revisedWordsFeatureF = originalWordsTypeT.getFeatureByBaseName(
                revisedWordsFeature);
        originalWordsFeatureF = revisedWordsTypeT.getFeatureByBaseName(
                originalWordsFeature);
    }

    @Override
    public void process(CAS cas) throws AnalysisEngineProcessException {
        final JCas jcas, revisedView, originalView;
        try {
            jcas = cas.getJCas();
            originalView = jcas.getView(ORIGINAL_VIEW);
            revisedView = jcas.getView(REVISED_VIEW);
        } catch (CASException ex) {
            throw new AnalysisEngineProcessException(ex);
        }
        for (AnnotationFS revisedSentences : CasUtil.select(
                revisedView.getCas(),
                revisedSentencesTypeT)) {
            final List<AnnotationFS> revised = CasUtil.selectCovered(
                    revisedView.getCas(),
                    tokenTypeT,
                    revisedSentences),
                    original = CasUtil.selectCovered(
                            originalView.getCas(),
                            tokenTypeT,
                            (AnnotationFS) revisedSentences.getFeatureValue(
                                    originalSentencesFeatureF));
            final byte[] revisedText = getBytes(revised),
                    originalText = getBytes(original);
            final DiffAlgorithm da = DiffAlgorithm.getAlgorithm(
                    DiffAlgorithm.SupportedAlgorithm.MYERS);
            final EditList editList = da.diff(
                    RawTextComparator.DEFAULT,
                    new RawText(originalText),
                    new RawText(revisedText));
            for (Edit edit : editList) {
                if (edit.getType().equals(Edit.Type.REPLACE)) {
                    final AnnotationFS originalWords = originalView.getCas()
                            .createAnnotation(
                                    originalWordsTypeT,
                                    original.get(edit.getBeginA()).getBegin(),
                                    original.get(edit.getEndA() - 1).getEnd()),
                            revisedWords = revisedView.getCas()
                            .createAnnotation(
                                    revisedWordsTypeT,
                                    revised.get(edit.getBeginB()).getBegin(),
                                    revised.get(edit.getEndB() - 1).getEnd());
                    originalWords.setFeatureValue(
                            revisedWordsFeatureF,
                            revisedWords);
                    revisedWords.setFeatureValue(
                            originalWordsFeatureF,
                            originalWords);
                    originalView.addFsToIndexes(originalWords);
                    revisedView.addFsToIndexes(revisedWords);
                }
            }
        }
    }
}
