package cat.udl.cig.operations.wrapper.data;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public class Pair<E, K> implements Comparable<Pair<E, K>> {

    private final E e;
    private final K k;

    public Pair(E e, K k) {
        this.e = e;
        this.k = k;
    }

    public E getKey() {
        return e;
    }

    public K getValue() {
        return k;
    }

    @Override
    public int compareTo(@NotNull Pair<E, K> o) {
        Comparable newK = (Comparable) this.k;
        return newK.compareTo(o.getValue());
    }
}
