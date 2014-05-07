package eu.crydee.readability.uima.res;

import eu.crydee.readability.uima.model.Revision;
import java.io.PrintStream;
import java.util.Map;
import java.util.Optional;

public interface ReadabilityDict {

    public void add(Revision original, Revision revised);

    public void add(Revision original, Revision revised, Integer count);

    public Optional<Map<Revision, Integer>> getRevisions(Revision original);

    public void save(PrintStream ps) throws Exception;
}
