package eu.crydee.ahocorasick;

import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class FSM<T> {

    public class State {

        private final Map<T, State> goToTransitions = new HashMap<>();
        private final Set<Integer> outputs = new HashSet<>();
        private State failTransition;
        private Optional<State> defaultTransition = Optional.empty();

        public Map<T, State> getGoToTransitions() {
            return Collections.unmodifiableMap(goToTransitions);
        }

        public State goTo(T symbol) {
            if (goToTransitions.containsKey(symbol)) {
                return goToTransitions.get(symbol);
            }
            return defaultTransition.orElse(null);
        }

        public boolean canGoTo(T symbol) {
            return defaultTransition.isPresent()
                    || goToTransitions.containsKey(symbol);
        }

        public void addGoTo(T symbol, State target) {
            goToTransitions.put(symbol, target);
        }

        public void addOutput(Integer patternIndex) {
            outputs.add(patternIndex);
        }

        public void addOutputs(Set<Integer> patternIndeces) {
            outputs.addAll(patternIndeces);
        }

        public Set<Integer> getOutputs() {
            return Collections.unmodifiableSet(outputs);
        }

        public State fail() {
            return failTransition;
        }

        public void setFail(State target) {
            failTransition = target;
        }

        public void setDefault(State state) {
            defaultTransition = Optional.of(state);
        }
    }

    public final State root = new State();

    public FSM(List<T[]> patterns) {
        /* First phase of the construction in the original paper.
         * Builds the goto graph and assigns some outputs.
         */
        for (int i = 0, s = patterns.size(); i < s; ++i) {
            T[] pattern = patterns.get(i);
            State current = root;
            for (T symbol : pattern) {
                if (current.canGoTo(symbol)) {
                    current = current.goTo(symbol);
                } else {
                    State newState = new State();
                    current.addGoTo(symbol, newState);
                    current = newState;
                }
            }
            current.addOutput(i);
        }
        root.setDefault(root);
        /* Second phase of the construction in the original paper.
         * Builds the fail function and assigns the rest of the outputs.
         */
        // initialization: all the depth == 1 nodes get a fail to 0.
        Deque<State> pool = root.goToTransitions.values().stream()
                .collect(Collectors.toCollection(LinkedList::new));
        pool.forEach(s -> s.setFail(root));
        while (!pool.isEmpty()) {
            State state = pool.pop();
            for (Entry<T, State> e : state.getGoToTransitions().entrySet()) {
                T symbol = e.getKey();
                State next = e.getValue();
                pool.addLast(next);
                State fail = state.fail();
                while (!fail.canGoTo(symbol)) {
                    fail = fail.fail();
                }
                fail = fail.goTo(symbol);
                next.setFail(fail);
                next.addOutputs(fail.getOutputs());
            }
        }
    }
}