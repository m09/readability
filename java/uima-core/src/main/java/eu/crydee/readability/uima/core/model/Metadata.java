package eu.crydee.readability.uima.core.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.tuple.Pair;

public class Metadata {

    private final List<Pair<String, String>> contexts = new ArrayList<>();
    private final Map<Score, Double> scores = new HashMap<>();

    public List<Pair<String, String>> getContexts() {
        return contexts;
    }

    public void addContext(String originalContext, String revisedContext) {
        this.contexts.add(Pair.of(originalContext, revisedContext));
    }

    public void addContexts(List<Pair<String, String>> contexts) {
        this.contexts.addAll(contexts);
    }

    public int getCount() {
        return contexts.size();
    }

    public Double getScore(Score score) {
        return scores.get(score);
    }

    public void setScore(Score type, Double score) {
        scores.put(type, score);
    }
}
