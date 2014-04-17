package eu.crydee.readability.uima.ae;

import eu.crydee.readability.uima.misc.HtmlToPlainText;
import java.io.StringWriter;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.component.ViewCreatorAnnotator;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.jcas.JCas;
import org.eclipse.mylyn.wikitext.core.parser.DocumentBuilder;
import org.eclipse.mylyn.wikitext.core.parser.MarkupParser;
import org.eclipse.mylyn.wikitext.core.parser.builder.HtmlDocumentBuilder;
import org.eclipse.mylyn.wikitext.mediawiki.core.MediaWikiLanguage;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

public class MediaWikiConverterAE extends JCasAnnotator_ImplBase {

    final public static String PARAM_OUT_VIEW_TXT = "PARAM_OUT_VIEW_TXT";
    @ConfigurationParameter(name = PARAM_OUT_VIEW_TXT, mandatory = true)
    private String outputTxtViewName;

    final public static String PARAM_OUT_VIEW_HTML = "PARAM_OUT_VIEW_HTML";
    @ConfigurationParameter(name = PARAM_OUT_VIEW_HTML, mandatory = true)
    private String outputHtmlViewName;

    @Override
    public void process(JCas jcas) throws AnalysisEngineProcessException {
        JCas outputViewTxt = ViewCreatorAnnotator.createViewSafely(
                jcas,
                outputTxtViewName),
                outputViewHtml = ViewCreatorAnnotator.createViewSafely(
                        jcas,
                        outputHtmlViewName);
        StringWriter sw = new StringWriter();
        DocumentBuilder db = new HtmlDocumentBuilder(sw, true);
        MarkupParser markupParser = new MarkupParser(
                new MediaWikiLanguage(),
                db);
        markupParser.parse(jcas.getDocumentText());
        String html = Jsoup.clean(sw.toString(), Whitelist.relaxed());
        outputViewHtml.setDocumentText(html);
        outputViewTxt.setDocumentText(new HtmlToPlainText().getPlainText(
                Jsoup.parse(html)));
    }
}
