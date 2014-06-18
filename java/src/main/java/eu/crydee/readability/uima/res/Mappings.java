package eu.crydee.readability.uima.res;

import com.google.common.collect.TreeMultimap;
import eu.crydee.readability.uima.model.Mapped;
import eu.crydee.readability.uima.model.Metrics;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface Mappings {

    UUID freeId();

    void putRevision(UUID id, Mapped revision, Metrics metrics);

    Optional<Map<Mapped, Metrics>> getRevisions(UUID id);

    Optional<Map<Mapped, Metrics>> getRevisions(String id);

    void addRewriting(UUID[] ids, Double score);

    TreeMultimap<Double, UUID[]> getRewritings();
}
