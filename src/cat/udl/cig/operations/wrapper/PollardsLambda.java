package cat.udl.cig.operations.wrapper;

import cat.udl.cig.fields.Group;
import cat.udl.cig.fields.GroupElement;
import javafx.util.Pair;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;

import static java.lang.Math.abs;

public class PollardsLambda implements PollardsLambdaInt {

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
            res = getExponent(pair, beta);
        }
        return res;
    }

    private void getHashMap() {
        this.hashMap = new HashMap<>();
        for (BigInteger k = BigInteger.ZERO; k.compareTo(N.sqrt()) < 0; k = k.add(BigInteger.ONE)) {
            BigInteger r = new BigInteger(group.getSize().bitLength(), new SecureRandom()).remainder(N).add(BigInteger.ONE);
            hashMap.put(k, new Pair<>(r, alpha.pow(r)));
        }
    }

    private Optional<BigInteger> getExponent(Pair<GroupElement, BigInteger> pair, GroupElement beta) {
        Group group = alpha.getGroup();
        BigInteger d = pair.getValue();
        GroupElement xn = pair.getKey();
        GroupElement yn = beta;
        BigInteger acc = BigInteger.ZERO;
        while (acc.compareTo(b.add(d)) <= 0) {
            Pair<BigInteger, GroupElement> tmp = hashMap.get(BigInteger.valueOf(abs(yn.hashCode()) % hashMap.size()));
            acc = acc.add(tmp.getKey());
            yn = yn.multiply(tmp.getValue());
            if (xn.equals(yn))
                return Optional.of(b.add(d).subtract(acc).remainder(group.getSize()).add(group.getSize())
                        .remainder(group.getSize()));
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
