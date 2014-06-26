package eu.crydee.readability.uima.res;

import eu.crydee.readability.uima.model.Mapped;
import eu.crydee.readability.uima.model.Metrics;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface ResultAggregator extends Saveable {

    public void add(Mapped original, Mapped revised);

    public void add(Mapped original, Mapped revised, Integer count);

    public void addAll(ResultAggregator o);

    public Optional<Map<Mapped, Metrics>> getRevisions(Mapped original);

    public Set<Mapped> keySet();

    public Set<Map.Entry<Mapped, Map<Mapped, Metrics>>> entrySet();

    public int getTotalCount();
}
