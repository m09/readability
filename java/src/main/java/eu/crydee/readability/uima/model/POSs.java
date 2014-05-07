package eu.crydee.readability.uima.model;

import com.google.common.collect.ImmutableList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class POSs implements Iterable<String> {

    List<String> POS;

    public POSs(List<String> POS) {
        this.POS = new ImmutableList.Builder<String>()
                .addAll(POS)
                .build();
    }

    @Override
    public Iterator<String> iterator() {
        return POS.iterator();
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 13 * hash + Objects.hashCode(this.POS);
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
        final POSs other = (POSs) obj;
        return Objects.equals(this.POS, other.POS);
    }
}
