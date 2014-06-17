package eu.crydee.readability.uima.model;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Ordering;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.TreeMultimap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.uima.UIMAFramework;
import org.apache.uima.util.Level;
import org.apache.uima.util.Logger;

public class Transducer {

    private static final Logger logger = UIMAFramework.getLogger(
            Transducer.class);

    private final SortedMap<Integer, Map<Integer, TreeMultimap<Double, String>>> byStarts;
    private final SetMultimap<Integer, Integer> byEnds = HashMultimap.create();
    private final Weight weight;

    public Transducer(Weight weight) {
        this.weight = weight;
        byStarts = new TreeMap<>();
    }

    public void put(Integer start, Integer end, Double score, String text) {
        byEnds.put(end, start);
        Map<Integer, TreeMultimap<Double, String>> ends;
        if (!byStarts.containsKey(start)) {
            ends = new HashMap<>();
            byStarts.put(start, ends);
        } else {
            ends = byStarts.get(start);
        }
        TreeMultimap<Double, String> scores;
        if (!ends.containsKey(end)) {
            scores = TreeMultimap.create(
                    weight.comparator().reversed(),
                    Ordering.natural());
            ends.put(end, scores);
        } else {
            scores = ends.get(end);
        }
        scores.put(score, text);
    }

    public TreeMultimap<Double, String> top(int n) {
        logger.log(Level.INFO, "byEnds  : " + byEnds.toString());
        logger.log(Level.INFO, "byStarts: " + byStarts.toString());
        SortedMap<Integer, TreeMultimap<Double, String>> bests
                = new TreeMap<>();
        TreeMultimap<Double, String> initial = TreeMultimap.create();
        initial.put(weight.getUnit(), "");
        bests.put(0, initial);
        int current = 1;
        SortedMap<Integer, Map<Integer, TreeMultimap<Double, String>>> tail
                = byStarts.tailMap(current);
        while (!tail.isEmpty()) {
            current = tail.firstKey();
            TreeMultimap<Double, String> currentBests
                    = TreeMultimap.create(weight.comparator().reversed(),
                            Ordering.natural());
            for (Integer leadingToCurrent : byEnds.get(current)) {
                if (!bests.containsKey(leadingToCurrent)) {
                    throw new IllegalStateException(
                            "the table of best paths doesn't contain "
                            + leadingToCurrent
                            + " while it should");
                }
                Map<Integer, TreeMultimap<Double, String>> starts
                        = byStarts.get(leadingToCurrent);
                if (!starts.containsKey(current)) {
                    throw new IllegalStateException(
                            "the table of transitions doesn't contain a "
                            + "transition from "
                            + leadingToCurrent
                            + " to "
                            + current
                            + " while it should");
                }
                TreeMultimap<Double, String> transitions = starts.get(current);
                for (Entry<Double, String> trans : transitions.entries()) {
                    for (Entry<Double, String> start
                            : bests.get(leadingToCurrent).entries()) {
                        currentBests.put(
                                weight.mul(start.getKey(), trans.getKey()),
                                start.getValue().concat(trans.getValue()));
                    }
                }
                logger.log(Level.INFO, transitions.toString());

            }
            int i = 0;
            TreeMultimap<Double, String> toPut
                    = TreeMultimap.create(weight.comparator().reversed(),
                            Ordering.natural());
            outer:
            for (Double w : currentBests.keySet()) {
                for (String text : currentBests.get(w)) {
                    if (i >= n) {
                        break outer;
                    }
                    toPut.put(w, text);
                    ++i;
                }
            }
            bests.put(current, toPut);
            // while increment
            tail = tail.tailMap(current + 1);
        }
        TreeMultimap<Double, String> result
                = TreeMultimap.create(weight.comparator().reversed(),
                        Ordering.natural());
        int i = 0;
        TreeMultimap<Double, String> last = bests.get(bests.lastKey());
        for (Double score : last.keySet()) {
            for (String text : last.get(score)) {
                result.put(score, text);
            }
        }
        return result;
    }
}
