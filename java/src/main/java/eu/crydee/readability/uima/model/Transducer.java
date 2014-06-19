package eu.crydee.readability.uima.model;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Ordering;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.TreeMultimap;
import eu.crydee.readability.uima.ts.Revision;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.SortedMap;
import java.util.TreeMap;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.uima.UIMAFramework;
import org.apache.uima.util.Level;
import org.apache.uima.util.Logger;

public class Transducer {

    private static final Logger logger = UIMAFramework.getLogger(
            Transducer.class);

    private final SortedMap<Integer, Map<Integer, TreeMultimap<Double, Optional<Revision>>>> byStarts;
    private final SetMultimap<Integer, Integer> byEnds = HashMultimap.create();
    private final Weight weight;

    public Transducer(Weight weight) {
        this.weight = weight;
        byStarts = new TreeMap<>();
    }

    public void put(
            Integer start,
            Integer end,
            Optional<Revision> revision) {
        byEnds.put(end, start);
        Map<Integer, TreeMultimap<Double, Optional<Revision>>> ends;
        if (!byStarts.containsKey(start)) {
            ends = new HashMap<>();
            byStarts.put(start, ends);
        } else {
            ends = byStarts.get(start);
        }
        TreeMultimap<Double, Optional<Revision>> scores;
        if (!ends.containsKey(end)) {
            scores = TreeMultimap.create(
                    weight.comparator().reversed(),
                    Ordering.allEqual());
            ends.put(end, scores);
        } else {
            scores = ends.get(end);
        }
        if (revision.isPresent()) {
            scores.put(revision.get().getScore(), revision);
        } else {
            scores.put(weight.getUnit(), Optional.empty());
        }
    }

    public TreeMultimap<Double, Revision[]> top(int n) {
        logger.log(Level.INFO, "byEnds  : " + byEnds.toString());
        logger.log(Level.INFO, "byStarts: " + byStarts.toString());
        SortedMap<Integer, TreeMultimap<Double, Revision[]>> bests
                = new TreeMap<>();
        TreeMultimap<Double, Revision[]> initial = TreeMultimap.create(
                weight.comparator().reversed(),
                Ordering.allEqual());
        initial.put(weight.getUnit(), new Revision[0]);
        bests.put(0, initial);
        int current = 1;
        SortedMap<Integer, Map<Integer, TreeMultimap<Double, Optional<Revision>>>> tail
                = byStarts.tailMap(current);
        while (!tail.isEmpty()) {
            current = tail.firstKey();
            TreeMultimap<Double, Revision[]> currentBests
                    = TreeMultimap.create(
                            weight.comparator().reversed(),
                            Ordering.allEqual());
            for (Integer leadingToCurrent : byEnds.get(current)) {
                if (!bests.containsKey(leadingToCurrent)) {
                    throw new IllegalStateException(
                            "the table of best paths doesn't contain "
                            + leadingToCurrent
                            + " while it should");
                }
                Map<Integer, TreeMultimap<Double, Optional<Revision>>> starts
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
                TreeMultimap<Double, Optional<Revision>> transitions
                        = starts.get(current);
                for (Entry<Double, Optional<Revision>> trans
                        : transitions.entries()) {
                    for (Entry<Double, Revision[]> start
                            : bests.get(leadingToCurrent).entries()) {
                        if (trans.getValue().isPresent()) {
                            currentBests.put(
                                    weight.mul(start.getKey(), trans.getKey()),
                                    ArrayUtils.add(
                                            start.getValue(),
                                            trans.getValue().get()));
                        } else {
                            currentBests.put(
                                    weight.mul(start.getKey(), trans.getKey()),
                                    ArrayUtils.clone(start.getValue()));
                        }
                    }
                }
                logger.log(Level.INFO, transitions.toString());

            }
            int i = 0;
            TreeMultimap<Double, Revision[]> toPut
                    = TreeMultimap.create(weight.comparator().reversed(),
                            Ordering.allEqual());
            outer:
            for (Double w : currentBests.keySet()) {
                for (Revision[] revisions : currentBests.get(w)) {
                    if (i >= n) {
                        break outer;
                    }
                    toPut.put(w, revisions);
                    ++i;
                }
            }
            bests.put(current, toPut);
            // while increment
            tail = tail.tailMap(current + 1);
        }
        TreeMultimap<Double, Revision[]> result
                = TreeMultimap.create(weight.comparator().reversed(),
                        Ordering.allEqual());
        int i = 0;
        TreeMultimap<Double, Revision[]> last = bests.get(bests.lastKey());
        for (Double score : last.keySet()) {
            for (Revision[] revisions : last.get(score)) {
                result.put(score, revisions);
            }
        }
        return result;
    }
}
