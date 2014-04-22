package eu.crydee.readability.uima.res;

import java.io.PrintStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public interface ReadabilityDict {

    static public class Revision {

        final private String text;
        final private List<String> tokens;
        final private List<String> POS;

        public Revision(String text, List<String> tokens, List<String> POS) {
            this.text = text;
            this.tokens = Collections.unmodifiableList(tokens);
            this.POS = Collections.unmodifiableList(POS);
        }

        public String getText() {
            return text;
        }

        public List<String> getTokens() {
            return tokens;
        }

        public List<String> getPOS() {
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
            final Revision other = (Revision) obj;
            return Objects.equals(this.text, other.text)
                    && Objects.equals(this.tokens, other.tokens)
                    && Objects.equals(this.POS, other.POS);
        }

    }

    public void add(Revision original, Revision revised);

    public Optional<Map<Revision, Integer>> getRevisions(Revision original);

    public void save(PrintStream ps) throws Exception;
}
