package eu.crydee.readability.uima.cr;

import eu.crydee.readability.uima.model.Mapped;
import eu.crydee.readability.uima.model.Metrics;
import eu.crydee.readability.uima.res.ReadabilityDict;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
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

    private Iterator<Entry<Mapped, Map<Mapped, Metrics>>> outerIterator;
    private Iterator<Entry<Mapped, Metrics>> innerIterator;
    private Mapped original;
    private int n, total;

    @Override
    public void initialize(UimaContext context)
            throws ResourceInitializationException {
        super.initialize(context);
        outerIterator = dict.entrySet().iterator();
        innerIterator = null;
        n = 0;
        total = dict.getTotalCount();
    }

    @Override
    public void getNext(JCas jcas) throws IOException, CollectionException {
        if (innerIterator == null || !innerIterator.hasNext()) {
            Entry<Mapped, Map<Mapped, Metrics>> e = outerIterator.next();
            original = e.getKey();
            innerIterator = e.getValue().entrySet().iterator();
        }
        Entry<Mapped, Metrics> e = innerIterator.next();
        Mapped revised = e.getKey();
        JCas gold;
        try {
            gold = ViewCreatorAnnotator.createViewSafely(jcas, "gold");
        } catch (AnalysisEngineProcessException ex) {
            logger.log(Level.SEVERE, "Coudln't create the gold view.", ex);
            throw new CollectionException(ex);
        }
        jcas.setDocumentText(original.getContext());
        gold.setDocumentText(revised.getContext());
        ++n;
    }

    @Override
    public boolean hasNext()
            throws IOException, CollectionException {
        if (limit != null && n > limit) {
            return false;
        }
        if (innerIterator != null) {
            return innerIterator.hasNext() || outerIterator.hasNext();
        }
        return outerIterator.hasNext();
    }

    @Override
    public Progress[] getProgress() {
        return new Progress[]{new ProgressImpl(n, total, Progress.ENTITIES)};
    }
}
