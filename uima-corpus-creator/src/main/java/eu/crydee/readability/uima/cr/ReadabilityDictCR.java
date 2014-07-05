package eu.crydee.readability.uima.cr;

import eu.crydee.readability.uima.core.model.Mapped;
import eu.crydee.readability.uima.core.model.Metadata;
import eu.crydee.readability.uima.core.res.ReadabilityDict;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.uima.UIMAFramework;
import org.apache.uima.UimaContext;
import org.apache.uima.collection.CollectionException;
import org.apache.uima.fit.component.JCasCollectionReader_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.descriptor.ExternalResource;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
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

    private Iterator<Entry<Mapped, Map<Mapped, Metadata>>> originalIterator;
    private Iterator<Entry<Mapped, Metadata>> revisedIterator;
    private Iterator<Pair<String, String>> contextIterator;
    private Mapped original;
    private int n, total;

    @Override
    public void initialize(UimaContext context)
            throws ResourceInitializationException {
        super.initialize(context);
        originalIterator = dict.entrySet().iterator();
        revisedIterator = null;
        n = 0;
        total = dict.getTotalCount();
    }

    @Override
    public void getNext(JCas jcas) throws IOException, CollectionException {
//        if (revisedIterator == null || !revisedIterator.hasNext()) {
//            Entry<Mapped, Map<Mapped, Metadata>> e = originalIterator.next();
//            original = e.getKey();
//            revisedIterator = e.getValue().entrySet().iterator();
//        }
//        Entry<Mapped, Metadata> e = revisedIterator.next();
//        Mapped revised = e.getKey();
//        JCas gold;
//        try {
//            gold = ViewCreatorAnnotator.createViewSafely(jcas, "gold");
//        } catch (AnalysisEngineProcessException ex) {
//            logger.log(Level.SEVERE, "Coudln't create the gold view.", ex);
//            throw new CollectionException(ex);
//        }
//        jcas.setDocumentText(original.getContext());
//        gold.setDocumentText(revised.getContext());
//        ++n;
    }

    @Override
    public boolean hasNext()
            throws IOException, CollectionException {
        if (limit != null && n > limit) {
            return false;
        }
        if (revisedIterator != null) {
            return revisedIterator.hasNext() || originalIterator.hasNext();
        }
        return originalIterator.hasNext();
    }

    @Override
    public Progress[] getProgress() {
        return new Progress[]{new ProgressImpl(n, total, Progress.ENTITIES)};
    }
}
