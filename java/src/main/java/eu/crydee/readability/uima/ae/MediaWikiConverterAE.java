package eu.crydee.readability.uima.ae;

import eu.crydee.readability.uima.misc.HtmlToPlainText;
import java.util.regex.Pattern;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.component.ViewCreatorAnnotator;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.jcas.JCas;
import org.eclipse.mylyn.wikitext.core.parser.MarkupParser;
import org.eclipse.mylyn.wikitext.mediawiki.core.MediaWikiLanguage;
import org.jsoup.Jsoup;

public class MediaWikiConverterAE extends JCasAnnotator_ImplBase {

    final public static String PARAM_OUT_VIEW_TXT = "PARAM_OUT_VIEW_TXT";
    @ConfigurationParameter(name = PARAM_OUT_VIEW_TXT, mandatory = true)
    private String outputTxtViewName;

    final public static String PARAM_OUT_VIEW_HTML = "PARAM_OUT_VIEW_HTML";
    @ConfigurationParameter(name = PARAM_OUT_VIEW_HTML, mandatory = true)
    private String outputHtmlViewName;

    final private Pattern comments = Pattern.compile("<!--.*?-->"),
            refs = Pattern.compile("<ref>.*?</ref>"),
            math = Pattern.compile("<math>.*?</math>");

    @Override
    public void process(JCas jcas) throws AnalysisEngineProcessException {
        JCas outputViewTxt = ViewCreatorAnnotator.createViewSafely(
                jcas,
                outputTxtViewName),
                outputViewHtml = ViewCreatorAnnotator.createViewSafely(
                        jcas,
                        outputHtmlViewName);

        String text = jcas.getDocumentText();
        text = comments.matcher(text).replaceAll("");
        text = refs.matcher(text).replaceAll("");
        text = math.matcher(text).replaceAll("");

        String html = new MarkupParser(new MediaWikiLanguage())
                .parseToHtml(text);
        outputViewHtml.setDocumentText(html);
        outputViewTxt.setDocumentText(new HtmlToPlainText().getPlainText(
                Jsoup.parse(html)));
    }
}
