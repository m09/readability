package eu.crydee.readability;

public class SentenceEdit {

    final private int lineStart, lineEnd, sentenceStart, sentenceEnd;
    final private String text;

    public SentenceEdit(
            int lineStart,
            int lineEnd,
            int sentenceStart,
            int sentenceEnd,
            String text) {
        this.lineStart = lineStart;
        this.lineEnd = lineEnd;
        this.sentenceStart = sentenceStart;
        this.sentenceEnd = sentenceEnd;
        this.text = text;
    }
}
