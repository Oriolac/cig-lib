package cat.udl.cig.utils.discretelogarithm;

import cat.udl.cig.structures.GroupElement;
import cat.udl.cig.structures.RingElement;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Optional;

public class HashedAlgorithm implements LogarithmAlgorithm {

    private final GroupElement alpha;
    private final BigInteger order;
    private final BigInteger times;
    private HashMap<GroupElement, BigInteger> hash;
    private static LogarithmAlgorithm hashedInstance;

    private HashedAlgorithm(GroupElement alpha, BigInteger order, BigInteger times) {
        this.alpha = alpha;
        hash = new HashMap<>();
        this.order = order;
        this.times = times;
        generateHash();
    }

    private void generateHash() {
        GroupElement actual = alpha.getGroup().getMultiplicativeIdentity();
        hash.putIfAbsent(actual, BigInteger.ZERO);
        hash.putIfAbsent(alpha, BigInteger.ONE);
        GroupElement gen = alpha.pow(times);
        for (BigInteger x = times; x.compareTo(order) <= 0; x = x.add(times)) {
            actual = actual.multiply(gen);
            hash.putIfAbsent(actual, x);
        }
    }

    @Override
    public Optional<BigInteger> algorithm(GroupElement beta) throws ArithmeticException {
        GroupElement actual = beta.multiply(beta.getGroup().getMultiplicativeIdentity());
        Optional<BigInteger> res = Optional.ofNullable(hash.get(actual));
        for (BigInteger x = BigInteger.ONE; res.isEmpty() && x.compareTo(times) <= 0; x = x.add(BigInteger.ONE)) {
            actual = actual.multiply(alpha);
            BigInteger finalX = x;
            res = Optional.ofNullable(hash.get(actual)).map((y) -> y.subtract(finalX));
        }
        return res;
    }

    @Override
    public GroupElement getAlpha() {
        return this.alpha;
    }

    public static void loadHashedInstance(RingElement alpha, BigInteger order, BigInteger times) {
        hashedInstance = new HashedAlgorithm(alpha, order, times);
    }

    public static LogarithmAlgorithm getHashedInstance() {
        return hashedInstance;
    }

    public BigInteger getOrder() {
        return order;
    }
}
