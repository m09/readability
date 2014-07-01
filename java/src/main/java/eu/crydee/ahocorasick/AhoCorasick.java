package eu.crydee.ahocorasick;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.SetMultimap;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang.ArrayUtils;

public class AhoCorasick<T> {

    private final FSM<T> fsm;

    public AhoCorasick(List<T[]> patterns) {
        fsm = new FSM<>(patterns);
    }

    /*
     * Performs the Aho-Corasick search on the text. Normal result is a
     * multimap:
     *    text indeces marking the end of matches => indeces of matched patterns
     * both indeces are calculated starting from 0.
     * 
     * Reversed result is the reversed multimap (from patterns to end of match
     * indeces)
     */
    public SetMultimap<Integer, Integer> search(
            T[] text,
            boolean reversedResult) {
        SetMultimap<Integer, Integer> result = HashMultimap.create();
        FSM<T>.State state = fsm.root;
        for (int i = 0, s = text.length; i < s; ++i) {
            T symbol = text[i];
            while (!state.canGoTo(symbol)) {
                state = state.fail();
            }
            state = state.goTo(symbol);
            if (!reversedResult) {
                result.putAll(i, state.getOutputs());
            } else {
                for (Integer patternIndex : state.getOutputs()) {
                    result.put(patternIndex, i);
                }
            }
        }
        return result;
    }

    public static void main(String[] args) {
        List<Character[]> patterns = Lists.newArrayList(
                "he", "us", "she", "ushers").stream()
                .map(s -> ArrayUtils.toObject(s.toCharArray()))
                .collect(Collectors.toList());
        AhoCorasick<Character> ac = new AhoCorasick<>(patterns);
        System.out.println(
                ac.search(ArrayUtils.toObject("ushers".toCharArray()), true));
    }
}
