package cat.udl.cig.operations.wrapper;

import cat.udl.cig.config.LoadCurve;
import cat.udl.cig.fields.GroupElement;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Optional;

public class HashedAlgorithm implements LogarithmAlgorithm {

    private final GroupElement alpha;
    private final BigInteger order;
    HashMap<GroupElement, BigInteger> hash;
    static final LogarithmAlgorithm hashedInstance =
            new HashedAlgorithm(LoadCurve.P192().getGroup().getGenerator(), BigInteger.valueOf(1024 * 1024));

    public HashedAlgorithm(GroupElement alpha, BigInteger order) {
        this.alpha = alpha;
        hash = new HashMap<>();
        this.order = order;
        generateHash(alpha, order);
    }

    private void generateHash(GroupElement alpha, BigInteger order) {
        GroupElement actual = alpha.getGroup().getNeuterElement();
        hash.putIfAbsent(actual, BigInteger.ZERO);
        for (BigInteger x = BigInteger.ONE; x.compareTo(order) < 0; x = x.add(BigInteger.ONE)) {
            actual = actual.multiply(alpha);
            hash.putIfAbsent(actual, x);
        }
    }

    @Override
    public Optional<BigInteger> algorithm(GroupElement beta) throws ArithmeticException {
        return Optional.of(hash.get(beta));
    }

    @Override
    public GroupElement getAlpha() {
        return this.alpha;
    }

    public static LogarithmAlgorithm getHashedInstance() {
        return hashedInstance;
    }

    public BigInteger getOrder() {
        return order;
    }
}
