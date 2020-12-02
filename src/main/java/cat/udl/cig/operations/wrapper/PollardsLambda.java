package cat.udl.cig.operations.wrapper;

import cat.udl.cig.fields.GroupElement;
import cat.udl.cig.fields.Group;
import cat.udl.cig.operations.wrapper.data.Pair;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;

import static java.lang.Math.abs;

public class PollardsLambda implements LogarithmAlgorithm {

    private final GroupElement alpha;
    private final Group group;
    private final BigInteger b;
    private final BigInteger N;
    private HashMap<BigInteger, Pair<BigInteger, GroupElement>> hashMap;


    public PollardsLambda(GroupElement alpha) {
        group = alpha.getGroup();
        b = BigInteger.TWO.pow(20);
        N = b.sqrt().add(BigInteger.ONE);
        this.alpha = alpha;
    }


    @Override
    public Optional<BigInteger> algorithm(GroupElement beta) throws ArithmeticException {
        if (!alpha.belongsToSameGroup(beta))
            throw new ArithmeticException("Alpha and beta don't belong to the same group");
        Optional<BigInteger> res = Optional.empty();
        Pair<GroupElement, BigInteger> pair;
        for (int i = 0; i < 30 && res.isEmpty(); i++) {
            getHashMap();
            pair = getD();
            res = getExponent(pair.getKey(), pair.getValue(), beta);
        }
        return res;
    }

    @Override
    public GroupElement getAlpha() {
        return this.alpha;
    }

    private void getHashMap() {
        this.hashMap = new HashMap<>();
        for (BigInteger k = BigInteger.ZERO; k.compareTo(N.sqrt()) < 0; k = k.add(BigInteger.ONE)) {
            BigInteger r = new BigInteger(group.getSize().bitLength(), new SecureRandom()).remainder(N).add(BigInteger.ONE);
            hashMap.put(k, new Pair<>(r, alpha.pow(r)));
        }
    }

    private Optional<BigInteger> getExponent(GroupElement xn, BigInteger d, GroupElement beta) {
        Group group = alpha.getGroup();
        GroupElement yj = beta;
        BigInteger dj = BigInteger.ZERO;
        while (dj.compareTo(b.add(d)) <= 0) {
            Pair<BigInteger, GroupElement> tmp = hashMap.get(BigInteger.valueOf(abs(yj.hashCode()) % hashMap.size()));
            dj = dj.add(tmp.getKey());
            yj = yj.multiply(tmp.getValue());
            if (xn.equals(yj))
                return Optional.of(b.add(d).subtract(dj).remainder(group.getSize()));
        }
        return Optional.empty();
    }

    private Pair<GroupElement, BigInteger> getD() {
        GroupElement xn = alpha.pow(b);
        BigInteger d = BigInteger.ZERO;
        for (long i = 0; i < N.intValue(); i++) {
            Pair<BigInteger, GroupElement> tmp = hashMap.get(BigInteger.valueOf(abs(xn.hashCode()) % hashMap.size()));
            d = d.add(tmp.getKey());
            xn = xn.multiply(tmp.getValue());
        }
        return new Pair<>(xn, d);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PollardsLambda that = (PollardsLambda) o;
        return Objects.equals(alpha, that.alpha) &&
                Objects.equals(group, that.group) &&
                Objects.equals(b, that.b) &&
                Objects.equals(N, that.N);
    }

    @Override
    public int hashCode() {
        return Objects.hash(alpha, group, b, N);
    }

}
