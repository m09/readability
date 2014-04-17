package eu.crydee.readability.uima.misc;

import java.util.regex.Pattern;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;

/**
 * HTML to plain-text. Based on the example by Jonathan Hedley in Jsoup.
 *
 * @author Jonathan Hedley <jonathan@hedley.net>
 * @author Hugo Mougard <mog@crydee.eu>
 */
public class HtmlToPlainText {

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
