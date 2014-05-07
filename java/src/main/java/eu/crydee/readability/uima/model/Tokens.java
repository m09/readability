package eu.crydee.readability.uima.model;

import com.google.common.collect.ImmutableList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class Tokens implements Iterable<String> {

    List<String> tokens;

    public Tokens(List<String> tokens) {
        this.tokens = new ImmutableList.Builder<String>()
                .addAll(tokens)
                .build();
    }

    @Override
    public Iterator<String> iterator() {
        return tokens.iterator();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.tokens);
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
        final Tokens other = (Tokens) obj;
        return Objects.equals(this.tokens, other.tokens);
    }
}
