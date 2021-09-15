package cat.udl.cig.utils.discretelogarithm;

import cat.udl.cig.operations.wrapper.data.Pair;
import cat.udl.cig.structures.GroupElement;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Optional;

public class BabyStepGiantStep implements LogarithmAlgorithm {


    private final BigInteger m;
    private final GroupElement alpha;

    public BabyStepGiantStep(GroupElement alpha) {
        BigInteger size = alpha.getGroup().getSize();
        this.m = size.sqrt();
        this.alpha = alpha;
    }

    @Override
    public Optional<BigInteger> algorithm(GroupElement beta) throws ArithmeticException {
        ArrayList<Pair<BigInteger, GroupElement>> babies = new ArrayList<>();
        ArrayList<Pair<BigInteger, GroupElement>> giants = new ArrayList<>();
        BigInteger iterator = BigInteger.ONE;
        BigInteger size = alpha.getGroup().getSize();
        for (; !iterator.equals(m.add(BigInteger.ONE)); iterator = iterator.add(BigInteger.ONE)) {
            babies.add(new Pair<>(iterator, beta.multiply(alpha.pow(iterator))));
            giants.add(new Pair<>(iterator, alpha.pow(m.multiply(iterator))));
        }
        babies.sort(Pair::compareTo);
        giants.sort(Pair::compareTo);
        for (Pair<BigInteger, GroupElement> baby : babies) {
            for (Pair<BigInteger, GroupElement> giant : giants) {
                if (baby.getValue().equals(giant.getValue())) {
                    BigInteger i = baby.getKey();
                    BigInteger j = giant.getKey();
                    BigInteger x = j.multiply(m).subtract(i).mod(size.subtract(BigInteger.ONE));
                    return Optional.of(x);
                }
                if (baby.getValue().compareTo(giant.getValue()) < 0) {
                    break;
                }
            }

        }
        return Optional.empty();
    }

    @Override
    public GroupElement getAlpha() {
        return alpha;
    }
}
