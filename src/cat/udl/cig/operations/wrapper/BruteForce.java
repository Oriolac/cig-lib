package cat.udl.cig.operations.wrapper;

import cat.udl.cig.fields.GroupElement;

import java.math.BigInteger;
import java.util.Objects;
import java.util.Optional;

public class BruteForce implements LogarithmAlgorithm {
    GroupElement alpha;

    public BruteForce(GroupElement alpha) {
        this.alpha = alpha;
    }

    @Override
    public Optional<BigInteger> algorithm(GroupElement beta) throws ArithmeticException {
        for (int i = 0; BigInteger.valueOf(i).compareTo(beta.getGroup().getSize()) < 0; i++) {
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
}
