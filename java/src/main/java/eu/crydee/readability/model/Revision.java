package eu.crydee.readability.model;

import info.bliki.wiki.filter.PlainTextConverter;
import info.bliki.wiki.model.WikiModel;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class Revision {

    private static final Logger logger
            = Logger.getLogger(Revision.class.getCanonicalName());

    private static final Tokenizer tokenizer;

    static {
        try (InputStream is = Revision.class.getResourceAsStream(
                "/opennlp/tools/tokenize/en-token.bin")) {
            TokenizerModel model = new TokenizerModel(is);
            tokenizer = new TokenizerME(model);
        } catch (IOException ex) {
            logger.error("couldn't load tokenizer model: "
                    + ex.getLocalizedMessage());
            throw new RuntimeException("aborting. couldn't load tokenizer.");
        }
    }
    public final int revId,
            parentId;

    public final long size;

    public final LocalDateTime timeStamp;

    public final String comment,
            parsedComment,
            cleanedComment;

    public final boolean minor,
            noisy;

    public Revision(
            int revId,
            int parentId,
            long size,
            LocalDateTime timeStamp,
            String comment,
            String parsedComment,
            boolean minor) {
        this.revId = revId;
        this.parentId = parentId;
        this.size = size;
        this.timeStamp = timeStamp;
        this.comment = comment;
        this.parsedComment = parsedComment;
        this.minor = minor;
        WikiModel wikiModel = new WikiModel("${image}", "${title}");
        String cleaned = wikiModel.render(new PlainTextConverter(), comment);
        cleaned = cleaned.replaceFirst("^/\\*.+?\\*/", "");
//        cleaned = StringUtils.join(
//                tokenizer.tokenize(cleaned),
//                " ");
        this.cleanedComment = cleaned;
        noisy = false;
    }
}
