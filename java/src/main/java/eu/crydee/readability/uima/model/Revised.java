package eu.crydee.readability.uima.model;

import java.util.Objects;

public class Revised {

    final private String text;
    final private Tokens tokens;
    final private POSs POS;

    public Revised(String text, Tokens tokens, POSs POS) {
        this.text = text;
        this.tokens = tokens;
        this.POS = POS;
    }

    public String getText() {
        return text;
    }

    public Tokens getTokens() {
        return tokens;
    }

    public POSs getPOS() {
        return POS;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 31 * hash + Objects.hashCode(this.text);
        hash = 31 * hash + Objects.hashCode(this.tokens);
        hash = 31 * hash + Objects.hashCode(this.POS);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final Revised other = (Revised) obj;
        return Objects.equals(this.text, other.text)
                && Objects.equals(this.tokens, other.tokens)
                && Objects.equals(this.POS, other.POS);
    }

}
