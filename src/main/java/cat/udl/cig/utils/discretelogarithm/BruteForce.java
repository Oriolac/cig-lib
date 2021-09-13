package cat.udl.cig.utils.discretelogarithm;

import cat.udl.cig.structures.GroupElement;

import java.math.BigInteger;
import java.util.Objects;
import java.util.Optional;

public class BruteForce implements LogarithmAlgorithm {
    GroupElement alpha;
    BigInteger last;

    public BruteForce(GroupElement alpha) {
        this.alpha = alpha;
        last = alpha.getGroup().getSize();
    }

    public BruteForce(GroupElement alpha, BigInteger last) {
        this.alpha = alpha;
        this.last = last;
    }

    @Override
    public Optional<BigInteger> algorithm(GroupElement beta) throws ArithmeticException {
        for (int i = 1; BigInteger.valueOf(i-1).compareTo(last) < 0; i++) {
            BigInteger x = BigInteger.valueOf(i);
            if (alpha.pow(x).equals(beta))
                return Optional.of(x);
        }
        return Optional.empty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BruteForce that = (BruteForce) o;
        return Objects.equals(alpha, that.alpha);
    }

    @Override
    public int hashCode() {
        return Objects.hash(alpha);
    }

    @Override
    public GroupElement getAlpha() {
        return alpha;
    }
}
