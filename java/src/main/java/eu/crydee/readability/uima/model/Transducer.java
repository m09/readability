package eu.crydee.readability.uima.model;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Ordering;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.SortedSetMultimap;
import com.google.common.collect.TreeMultimap;
import eu.crydee.readability.uima.ts.Revision;
import eu.crydee.readability.uima.ts.Revisions;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.UUID;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.uima.UIMAFramework;
import org.apache.uima.util.Level;
import org.apache.uima.util.Logger;

public class Transducer {

    private static final Logger logger = UIMAFramework.getLogger(
            Transducer.class);

    private class Cell implements Comparable<Cell> {

        public final Optional<Pair<UUID, Integer>> id;
        public final double score;

        public Cell(double score, UUID revisionsID, Integer revisionsIndex) {
            this.score = score;
            this.id = Optional.of(Pair.of(revisionsID, revisionsIndex));
        }

        public Cell(double score) {
            this.score = score;
            this.id = Optional.empty();
        }

        @Override
        public int compareTo(Cell o) {
            if (o == null) {
                return 1;
            }
            return Double.compare(score, o.score);
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 29 * hash + Objects.hashCode(this.id);
            hash = 29 * hash + (int) (Double.doubleToLongBits(this.score)
                    ^ (Double.doubleToLongBits(this.score) >>> 32));
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Cell other = (Cell) obj;
            if (!Objects.equals(this.id, other.id)) {
                return false;
            }
            return Double.doubleToLongBits(this.score)
                    == Double.doubleToLongBits(other.score);
        }
    }

    private final SortedMap<Integer, SortedSetMultimap<Integer, Cell>> byStarts
            = new TreeMap<>();
    private final SetMultimap<Integer, Integer> byEnds = HashMultimap.create();
    private final Weight weight;

    public Transducer(Weight weight) {
        this.weight = weight;
    }

    public void put(
            Integer start,
            Integer end,
            Revisions revisions) {
        byEnds.put(end, start);
        SortedSetMultimap<Integer, Cell> ends;
        if (!byStarts.containsKey(start)) {
            ends = TreeMultimap.create(
                    Ordering.natural(),
                    Ordering.natural().reverse());
            byStarts.put(start, ends);
        } else {
            ends = byStarts.get(start);
        }
        for (int i = 0, s = revisions.getRevisions().size(); i < s; i++) {
            Revision revision = revisions.getRevisions(i);
            ends.put(end, new Cell(
                    revision.getScore(),
                    UUID.fromString(revisions.getId()),
                    i));
        }
    }

    public void putEmptyTransition(Integer start, Integer end) {
        byEnds.put(end, start);
        SortedSetMultimap<Integer, Cell> ends;
        if (!byStarts.containsKey(start)) {
            ends = TreeMultimap.create(
                    Ordering.natural(),
                    Ordering.natural().reverse());
            byStarts.put(start, ends);
        } else {
            ends = byStarts.get(start);
        }
        ends.put(end, new Cell(weight.getUnit()));
    }

    public TreeMultimap<Double, Pair<UUID, Integer>[]> top(int n) {
        SortedMap<Integer, TreeMultimap<Double, Pair<UUID, Integer>[]>> bests
                = new TreeMap<>();
        TreeMultimap<Double, Pair<UUID, Integer>[]> initial
                = TreeMultimap.create(weight.reversed(), Ordering.allEqual());
        initial.put(weight.getUnit(), new Pair[0]);
        bests.put(0, initial);
        int current = 1;
        SortedMap<Integer, SortedSetMultimap<Integer, Cell>> tail
                = byStarts.tailMap(current);
        while (!tail.isEmpty()) {
            current = tail.firstKey();
            TreeMultimap<Double, Pair<UUID, Integer>[]> currentBests
                    = TreeMultimap.create(
                            weight.reversed(),
                            Ordering.allEqual());
            for (Integer leadingToCurrent : byEnds.get(current)) {
                if (!bests.containsKey(leadingToCurrent)) {
                    throw new IllegalStateException(
                            "the table of best paths doesn't contain "
                            + leadingToCurrent
                            + " while it should");
                }
                SortedSetMultimap<Integer, Cell> starts
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
                SortedSet<Cell> transitions
                        = starts.get(current);
                for (Cell trans : transitions) {
                    for (Entry<Double, Pair<UUID, Integer>[]> start
                            : bests.get(leadingToCurrent).entries()) {
                        if (trans.id.isPresent()) {
                            currentBests.put(
                                    weight.mul(start.getKey(), trans.score),
                                    ArrayUtils.add(
                                            start.getValue(),
                                            trans.id.get()));
                        } else {
                            currentBests.put(
                                    weight.mul(start.getKey(), trans.score),
                                    ArrayUtils.clone(start.getValue()));
                        }
                    }
                }
                logger.log(Level.INFO, transitions.toString());

            }
            int i = 0;
            TreeMultimap<Double, Pair<UUID, Integer>[]> toPut
                    = TreeMultimap.create(weight.reversed(),
                            Ordering.allEqual());
            outer:
            for (Double w : currentBests.keySet()) {
                for (Pair<UUID, Integer>[] revisions : currentBests.get(w)) {
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
        TreeMultimap<Double, Pair<UUID, Integer>[]> result
                = TreeMultimap.create(weight.reversed(),
                        Ordering.allEqual());
        int i = 0;
        TreeMultimap<Double, Pair<UUID, Integer>[]> last
                = bests.get(bests.lastKey());
        for (Double score : last.keySet()) {
            for (Pair<UUID, Integer>[] revisions : last.get(score)) {
                result.put(score, revisions);
            }
        }
        return result;
    }
}
