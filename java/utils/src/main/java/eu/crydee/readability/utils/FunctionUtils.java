package eu.crydee.readability.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang3.tuple.Pair;

public class FunctionUtils {

    static public <A, B> List<Pair<A, B>> zip(Collection<A> a, Collection<B> b) {
        List<Pair<A, B>> result = new ArrayList<>();
        Iterator<A> itA = a.iterator();
        Iterator<B> itB = b.iterator();
        while (itA.hasNext() && itB.hasNext()) {
            result.add(Pair.of(itA.next(), itB.next()));
        }
        return result;
    }
}
