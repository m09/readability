package eu.crydee.readability.uima.res;

import eu.crydee.readability.uima.model.Revised;
import java.io.PrintStream;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface ReadabilityDict {

    public void add(Revised original, Revised revised);

    public void add(Revised original, Revised revised, Integer count);

    public Optional<Map<Revised, Integer>> getRevisions(Revised original);

    public Set<Revised> keySet();

    public void save(PrintStream ps) throws Exception;
}
