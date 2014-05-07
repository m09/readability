package eu.crydee.readability.uima.model;

import com.google.common.collect.ImmutableList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;

public class Tokens implements List<String> {

    List<String> pos;

    public Tokens(List<String> tokens) {
        this.pos = new ImmutableList.Builder<String>()
                .addAll(tokens)
                .build();
    }

    @Override
    public Iterator<String> iterator() {
        return pos.iterator();
    }

    @Override
    public int size() {
        return pos.size();
    }

    @Override
    public String get(int index) {
        return pos.get(index);
    }

    @Override
    public boolean isEmpty() {
        return pos.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return pos.contains(o);
    }

    @Override
    public Object[] toArray() {
        return pos.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return pos.toArray(a);
    }

    @Override
    public boolean add(String e) {
        return pos.add(e);
    }

    @Override
    public boolean remove(Object o) {
        return pos.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return pos.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends String> c) {
        return pos.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends String> c) {
        return pos.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return pos.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return pos.retainAll(c);
    }

    @Override
    public void clear() {
        pos.clear();
    }

    @Override
    public String set(int index, String element) {
        return pos.set(index, element);
    }

    @Override
    public void add(int index, String element) {
        pos.add(index, element);
    }

    @Override
    public String remove(int index) {
        return pos.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return pos.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return pos.lastIndexOf(o);
    }

    @Override
    public ListIterator<String> listIterator() {
        return pos.listIterator();
    }

    @Override
    public ListIterator<String> listIterator(int index) {
        return pos.listIterator(index);
    }

    @Override
    public List<String> subList(int fromIndex, int toIndex) {
        return pos.subList(fromIndex, toIndex);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.pos);
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
        final Tokens other = (Tokens) obj;
        return Objects.equals(this.pos, other.pos);
    }
}
