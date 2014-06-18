package eu.crydee.readability.uima.res;

import eu.crydee.readability.uima.model.Mapped;
import eu.crydee.readability.uima.model.Metrics;
import java.io.PrintStream;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface ReadabilityDict {

    public void add(Mapped original, Mapped revised);

    public void add(Mapped original, Mapped revised, Integer count);

    public void set(
            Mapped original,
            Mapped revised,
            Integer count,
            Double score);

    public Optional<Map<Mapped, Metrics>> getRevisions(Mapped original);

    public Set<Mapped> keySet();

    public int getTotalCount();

    public void save(PrintStream ps) throws Exception;
}
