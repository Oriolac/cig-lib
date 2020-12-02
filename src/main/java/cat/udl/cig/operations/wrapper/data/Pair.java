package cat.udl.cig.operations.wrapper.data;

public class Pair<E, K> {

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
}
