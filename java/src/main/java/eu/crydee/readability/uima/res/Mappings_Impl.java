package eu.crydee.readability.uima.res;

import com.google.common.collect.Ordering;
import com.google.common.collect.TreeMultimap;
import eu.crydee.readability.uima.model.LogWeight;
import eu.crydee.readability.uima.model.Mapped;
import eu.crydee.readability.uima.model.Metrics;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.apache.uima.resource.DataResource;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.SharedResourceObject;

public class Mappings_Impl implements SharedResourceObject, Mappings {

    private final Map<UUID, Map<Mapped, Metrics>> revisions;
    private final TreeMultimap<Double, UUID[]> rewritings;

    public Mappings_Impl() {
        revisions = new HashMap<>();
        rewritings = TreeMultimap.create(
                new LogWeight().comparator().reversed(),
                Ordering.allEqual());
    }

    @Override
    public UUID freeId() {
        UUID uuid;
        do {
            uuid = UUID.randomUUID();
        } while (revisions.containsKey(uuid));
        return uuid;
    }

    @Override
    public void putRevision(UUID id, Mapped revision, Metrics metrics) {
        Map<Mapped, Metrics> target;
        if (!revisions.containsKey(id)) {
            target = new HashMap<>();
            revisions.put(id, target);
        } else {
            target = revisions.get(id);
        }
        target.put(revision, metrics);
    }

    @Override
    public Map<UUID, Map<Mapped, Metrics>> getRevisionsMap() {
        return Collections.unmodifiableMap(revisions);
    }

    @Override
    public Optional<Map<Mapped, Metrics>> getRevisions(UUID id) {
        return Optional.ofNullable(
                Collections.unmodifiableMap(revisions.get(id)));
    }

    @Override
    public Optional<Map<Mapped, Metrics>> getRevisions(String id) {
        return getRevisions(UUID.fromString(id));
    }

    @Override
    public void addRewriting(UUID[] ids, Double score) {
        rewritings.put(score, ids);
    }

    @Override
    public TreeMultimap<Double, UUID[]> getRewritings() {
        return rewritings;
    }

    @Override
    public void load(DataResource dr) throws ResourceInitializationException {
        // We don't load anything in this resource
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("rewritings: ")
                .append(rewritings)
                .append('\n')
                .append("revisions: ")
                .append(revisions)
                .append('\n')
                .append("revisions size: ")
                .append('\n')
                .append(revisions.size());
        return sb.toString();
    }
}
