package eu.crydee.readability.uima.evaluator.cr;

import eu.crydee.readability.uima.core.model.Mapped;
import eu.crydee.readability.uima.core.model.Metadata;
import eu.crydee.readability.uima.core.res.ReadabilityDict;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.uima.UIMAFramework;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.collection.CollectionException;
import org.apache.uima.fit.component.JCasCollectionReader_ImplBase;
import org.apache.uima.fit.component.ViewCreatorAnnotator;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.descriptor.ExternalResource;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Level;
import org.apache.uima.util.Logger;
import org.apache.uima.util.Progress;
import org.apache.uima.util.ProgressImpl;

public class ReadabilityDictCR extends JCasCollectionReader_ImplBase {

    private static final Logger logger = UIMAFramework.getLogger(
            ReadabilityDictCR.class);

    public static final String KEY_RES = "RES";
    @ExternalResource(key = KEY_RES)
    private ReadabilityDict dict;

    public static final String PARAM_LIMIT = "LIMIT";
    @ConfigurationParameter(name = PARAM_LIMIT, mandatory = false)
    Integer limit;

    private Iterator<Map<Mapped, Metadata>> originalIterator;
    private Iterator<Metadata> revisedIterator;
    private Iterator<Pair<String, String>> contextIterator;
    private int n, total;

    @Override
    public void initialize(UimaContext context)
            throws ResourceInitializationException {
        super.initialize(context);
        originalIterator = dict.values().iterator();
        revisedIterator = null;
        contextIterator = null;
        n = 0;
        total = dict.getTotalCount();
    }

    @Override
    public void getNext(JCas jcas) throws IOException, CollectionException {
        if (revisedIterator == null && contextIterator == null
                || !contextIterator.hasNext()) {
            if (revisedIterator == null || !revisedIterator.hasNext()) {
                revisedIterator = originalIterator.next().values().iterator();
            }
            contextIterator = revisedIterator.next().getContexts().iterator();
        }
        Pair<String, String> pair = contextIterator.next();
        JCas gold;
        try {
            gold = ViewCreatorAnnotator.createViewSafely(jcas, "gold");
        } catch (AnalysisEngineProcessException ex) {
            logger.log(Level.SEVERE, "Coudln't create the gold view.", ex);
            throw new CollectionException(ex);
        }
        jcas.setDocumentText(pair.getLeft());
        gold.setDocumentText(pair.getRight());
        ++n;
    }

    @Override
    public boolean hasNext()
            throws IOException, CollectionException {
        if (limit != null && n > limit) {
            return false;
        }
        if (contextIterator != null) {
            return contextIterator.hasNext()
                    || revisedIterator.hasNext()
                    || originalIterator.hasNext();
        }
        return originalIterator.hasNext();
    }

    @Override
    public Progress[] getProgress() {
        return new Progress[]{new ProgressImpl(n, total, Progress.ENTITIES)};
    }
}
