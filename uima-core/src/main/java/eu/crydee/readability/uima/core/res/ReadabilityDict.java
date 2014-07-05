package eu.crydee.readability.uima.core.res;

import eu.crydee.readability.uima.core.model.Mapped;
import eu.crydee.readability.uima.core.model.Metadata;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import org.apache.commons.lang3.tuple.Pair;

public interface ReadabilityDict extends Saveable {

    public void add(
            Mapped original,
            Mapped revised,
            String originalContext,
            String revisedContext);

    public void add(
            Mapped original,
            Mapped revised,
            List<Pair<String, String>> contexts);

    public void addAll(ReadabilityDict o);

    public Optional<Map<Mapped, Metadata>> getRevisions(Mapped original);

    public Set<Mapped> keySet();

    public Set<Entry<Mapped, Map<Mapped, Metadata>>> entrySet();

    public int getTotalCount();
}
