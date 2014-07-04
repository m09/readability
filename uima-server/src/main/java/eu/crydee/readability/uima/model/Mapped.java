package eu.crydee.readability.uima.model;

import com.google.common.collect.ImmutableList;
import java.util.List;
import java.util.Objects;

public class Mapped {

    final private String text;
    final private List<String> tokens;
    final private List<String> pos;

    public Mapped(
            String text,
            List<String> tokens,
            List<String> pos) {
        this.text = text;
        this.tokens = new ImmutableList.Builder<String>()
                .addAll(tokens)
                .build();
        this.pos = pos;
    }

    public String getText() {
        return text;
    }

    public List<String> getTokens() {
        return tokens;
    }

    public List<String> getPos() {
        return pos;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 71 * hash + Objects.hashCode(this.tokens);
        hash = 71 * hash + Objects.hashCode(this.pos);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Mapped other = (Mapped) obj;
        return Objects.equals(this.tokens, other.tokens)
                && Objects.equals(this.pos, other.pos);
    }
}
