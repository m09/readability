package eu.crydee.readability.uima.model;

import com.google.common.collect.ImmutableList;
import java.util.List;
import java.util.Objects;

public class Mapped {

    final private String text, context;
    final private List<String> tokens;

    public Mapped(String text, String context, List<String> tokens) {
        this.text = text;
        this.tokens = new ImmutableList.Builder<String>()
                .addAll(tokens)
                .build();
        this.context = context;
    }

    public String getText() {
        return text;
    }

    public List<String> getTokens() {
        return tokens;
    }

    public String getContext() {
        return context;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 71 * hash + Objects.hashCode(this.text);
        hash = 71 * hash + Objects.hashCode(this.tokens);
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
        if (!Objects.equals(this.text, other.text)) {
            return false;
        }
        return Objects.equals(this.tokens, other.tokens);
    }
}
