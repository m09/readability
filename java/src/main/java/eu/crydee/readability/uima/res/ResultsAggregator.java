package eu.crydee.readability.uima.res;

import eu.crydee.readability.uima.model.Mapped;
import eu.crydee.readability.uima.model.Metadata;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface ResultsAggregator extends Saveable {

    public void add(Mapped original, Mapped revised);

    public void add(Mapped original, Mapped revised, Integer count);

    public void addAll(ResultsAggregator o);

    public Optional<Map<Mapped, Metadata>> getRevisions(Mapped original);

    public Set<Mapped> keySet();

    public Set<Map.Entry<Mapped, Map<Mapped, Metadata>>> entrySet();

    public int getTotalCount();
}
