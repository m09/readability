package eu.crydee.readability.uima.ae;

import eu.crydee.readability.uima.core.model.Mapped;
import eu.crydee.readability.uima.core.res.ReadabilityDict;
import eu.crydee.readability.uima.ts.OriginalSentence;
import eu.crydee.readability.uima.ts.OriginalSentences;
import eu.crydee.readability.uima.ts.OriginalWords;
import eu.crydee.readability.uima.ts.RevisedSentence;
import eu.crydee.readability.uima.ts.RevisedSentences;
import eu.crydee.readability.uima.ts.RevisedWords;
import eu.crydee.readability.uima.ts.Sentence;
import eu.crydee.readability.uima.ts.Token;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import org.apache.uima.UIMAFramework;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.CASException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ExternalResource;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.util.Logger;

public class ResourcePopulatorAE extends JCasAnnotator_ImplBase {

    final static private Logger logger = UIMAFramework.getLogger(
            ResourcePopulatorAE.class);

    final static public String RES_KEY = "KEY";
    @ExternalResource(key = RES_KEY)
    ReadabilityDict dictTxt;

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
        String txtOri = original.getDocumentText(),
                txtRev = revised.getDocumentText();
        Map<RevisedSentences, Collection<RevisedWords>> indexWords
                = JCasUtil.indexCovered(
                        revised,
                        RevisedSentences.class,
                        RevisedWords.class);
        Map<RevisedSentences, Collection<RevisedSentence>> indexRevSentence
                = JCasUtil.indexCovered(
                        revised,
                        RevisedSentences.class,
                        RevisedSentence.class);
        Map<OriginalSentences, Collection<OriginalSentence>> indexOriSentence
                = JCasUtil.indexCovered(
                        original,
                        OriginalSentences.class,
                        OriginalSentence.class);
        for (Entry<RevisedSentences, Collection<RevisedWords>> e
                : indexWords.entrySet()) {
            Collection<RevisedSentence> allSentsRev
                    = indexRevSentence.get(e.getKey());
            Collection<OriginalSentence> allSentsOri
                    = indexOriSentence.get(
                            e.getKey().getOriginalSentences());
            for (RevisedWords rw : e.getValue()) {
                OriginalWords ow = rw.getOriginalWords();
                List<Sentence> sentsRev = allSentsRev.stream()
                        .filter(rs -> overlap(rs, rw))
                        .collect(Collectors.toList());
                List<Sentence> sentsOri = allSentsOri.stream()
                        .filter(s -> overlap(s, ow))
                        .collect(Collectors.toList());
                String sentRev = txtRev.substring(
                        sentsRev.get(0).getBegin(),
                        sentsRev.get(sentsRev.size() - 1).getEnd()),
                        sentOri = txtOri.substring(
                                sentsOri.get(0).getBegin(),
                                sentsOri.get(sentsOri.size() - 1).getEnd());
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
                                originalTokens,
                                originalPOS),
                        new Mapped(
                                rw.getCoveredText(),
                                revisedTokens,
                                revisedPOS),
                        sentOri,
                        sentRev
                );
            }
        }
    }

    private static boolean overlap(Annotation a1, Annotation a2) {
        return a1.getBegin() < a2.getEnd() && a1.getEnd() > a2.getBegin();
    }
}
