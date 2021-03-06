package eu.crydee.readability.uima.core.ae;

import java.util.Locale;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.component.ViewCreatorAnnotator;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.jcas.JCas;
import org.eclipse.mylyn.wikitext.parser.MarkupParser;
import org.eclipse.mylyn.wikitext.mediawiki.MediaWikiLanguage;
import org.jsoup.Jsoup;
import java.util.regex.Pattern;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;

public class MediaWikiConverterAE extends JCasAnnotator_ImplBase {

    /**
     * HTML to plain-text. Based on the example by Jonathan Hedley in Jsoup.
     *
     * @author Jonathan Hedley (jonathan@hedley.net)
     * @author Hugo Mougard (mog@crydee.eu)
     */
    static public class HtmlToPlainText {

        final static private Pattern spaces = Pattern.compile("\\p{Zs}+");

        /**
         * Format an Element to plain-text
         *
         * @param element the root element to format
         * @return formatted text
         */
        static public String getPlainText(Element element) {
            FormattingVisitor formatter = new FormattingVisitor();
            NodeTraversor traversor = new NodeTraversor(formatter);
            traversor.traverse(element);
            String text = spaces.matcher(formatter.get()).replaceAll(" ");
            StringBuilder sb = new StringBuilder();
            for (String line : text.split("\n")) {
                String clean = line.trim();
                if (clean.length() != 0) {
                    sb.append(clean);
                    if (Character.getType(clean.charAt(clean.length() - 1))
                            != Character.OTHER_PUNCTUATION) {
                        sb.append('.');
                    }
                }
                sb.append('\n');
            }
            return sb.toString();
        }

        static private class FormattingVisitor implements NodeVisitor {

            private final StringBuilder accum = new StringBuilder();

            public StringBuilder get() {
                return accum;
            }

            @Override
            public void head(Node node, int depth) {
                String name = node.nodeName();
                if (node instanceof TextNode) {
                    accum.append(((TextNode) node).text());
                } else if (name.equals("li")) {
                    accum.append("\n * ");
                }
            }

            @Override
            public void tail(Node node, int depth) {
                switch (node.nodeName()) {
                    case "p":
                    case "h1":
                    case "h2":
                    case "h3":
                    case "h4":
                    case "h5":
                    case "h6":
                        accum.append("\n");
                    case "br":
                    case "li":
                        accum.append("\n");
                        break;
                }
            }
        }
    }

    final public static String PARAM_OUT_VIEW_TXT = "PARAM_OUT_VIEW_TXT";
    @ConfigurationParameter(name = PARAM_OUT_VIEW_TXT, mandatory = true)
    private String outputTxtViewName;

    final public static String PARAM_OUT_VIEW_HTML = "PARAM_OUT_VIEW_HTML";
    @ConfigurationParameter(name = PARAM_OUT_VIEW_HTML, mandatory = true)
    private String outputHtmlViewName;

    final public static String PARAM_LOWERCASE = "PARAM_LOWERCASE";
    @ConfigurationParameter(
            name = PARAM_LOWERCASE,
            mandatory = false,
            defaultValue = "false")
    private boolean lowercase;

    final private Pattern comments = Pattern.compile("<!--.*?-->"),
            refs = Pattern.compile("<ref[^>]*?>.*?</ref>"),
            brackets = Pattern.compile("\\{\\{.*?}}"),
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
        text = brackets.matcher(text).replaceAll("");
        text = math.matcher(text).replaceAll("");

        String html = new MarkupParser(new MediaWikiLanguage())
                .parseToHtml(text);
        outputViewHtml.setDocumentText(html);
        text = new HtmlToPlainText().getPlainText(Jsoup.parse(html));
        if (lowercase) {
            text = text.toLowerCase(Locale.ENGLISH);
        }
        outputViewTxt.setDocumentText(text);
    }
}
