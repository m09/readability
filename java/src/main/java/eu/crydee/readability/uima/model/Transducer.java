package eu.crydee.readability.uima.model;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Ordering;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.SortedSetMultimap;
import com.google.common.collect.TreeMultimap;
import eu.crydee.readability.uima.ts.Revision;
import eu.crydee.readability.uima.ts.Revisions;
import java.util.Arrays;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.UUID;
import java.util.function.Function;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.uima.UIMAFramework;
import org.apache.uima.util.Logger;

public class Transducer {

    private static final Logger logger = UIMAFramework.getLogger(
            Transducer.class);

    public class Span {

        public final UUID id;
        public final int begin, end, index;

        public Span(UUID id, int begin, int end, int index) {
            this.id = id;
            this.begin = begin;
            this.end = end;
            this.index = index;
        }
    }

    private class Cell implements Comparable<Cell> {

        public final Optional<Span> id;
        public final double score;

        public Cell(double score, Span id) {
            this.score = score;
            this.id = Optional.of(id);
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
            Revisions revs,
            Function<Revision, Double> scoreGetter) {
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
        for (int i = 0, s = revs.getRevisions().size(); i < s; i++) {
            Revision rev = revs.getRevisions(i);
            ends.put(end, new Cell(
                    scoreGetter.apply(rev),
                    new Span(UUID.fromString(revs.getId()), start, end, i)));
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

    public TreeMultimap<Double, Span[]> top(int n) {
        SortedMap<Integer, TreeMultimap<Double, Cell[]>> bests
                = new TreeMap<>();
        TreeMultimap<Double, Cell[]> initial
                = TreeMultimap.create(weight.reversed(), Ordering.allEqual());
        initial.put(weight.getUnit(), new Cell[0]);
        bests.put(0, initial);
        int current = 1;
        SortedMap<Integer, SortedSetMultimap<Integer, Cell>> tail
                = byStarts.tailMap(current);
        while (!tail.isEmpty()) {
            current = tail.firstKey();
            TreeMultimap<Double, Cell[]> currentBests
                    = TreeMultimap.create(
                            weight.reversed(),
                            (Cell[] c1, Cell[] c2) -> {
                                return Double.compare(
                                        Arrays.stream(c1)
                                        .mapToDouble(c -> c.score)
                                        .sum(),
                                        Arrays.stream(c2)
                                        .mapToDouble(c -> c.score)
                                        .sum());
                            });
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
                    for (Entry<Double, Cell[]> start
                            : bests.get(leadingToCurrent).entries()) {
                        if (trans.id.isPresent()) {
                            currentBests.put(
                                    weight.mul(start.getKey(), trans.score),
                                    ArrayUtils.add(
                                            start.getValue(),
                                            trans));
                        } else {
                            currentBests.put(
                                    weight.mul(start.getKey(), trans.score),
                                    ArrayUtils.clone(start.getValue()));
                        }
                    }
                }
            }
            int i = 0;
            TreeMultimap<Double, Cell[]> toPut
                    = TreeMultimap.create(weight.reversed(),
                            Ordering.allEqual());
            outer:
            for (Double w : currentBests.keySet()) {
                for (Cell[] revisions : currentBests.get(w)) {
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
        TreeMultimap<Double, Span[]> result
                = TreeMultimap.create(weight.reversed(),
                        Ordering.allEqual());
        int i = 0;
        TreeMultimap<Double, Cell[]> last
                = bests.get(bests.lastKey());
        for (Double score : last.keySet()) {
            for (Cell[] revisions : last.get(score)) {
                Span[] spans = new Span[revisions.length];
                Arrays.setAll(spans, k -> revisions[k].id.get());
                result.put(score, spans);
            }
        }
        return result;
    }
}
