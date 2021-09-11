package cat.udl.cig.utils.discretelogarithm;

import cat.udl.cig.operations.wrapper.data.Pair;
import cat.udl.cig.structures.Group;
import cat.udl.cig.structures.GroupElement;
import cat.udl.cig.structures.RingElement;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Optional;

public class BabyStepGiantStep implements LogarithmAlgorithm {


    private final BigInteger m;
    private final GroupElement alpha;

    public BabyStepGiantStep(GroupElement alpha) {
        BigInteger size = alpha.getGroup().getSize();
        this.m = size.sqrt().add(BigInteger.ONE);
        this.alpha = alpha;
    }

    @Override
    public Optional<BigInteger> algorithm(GroupElement beta) throws ArithmeticException {
        ArrayList<Pair<BigInteger, GroupElement>> babies = new ArrayList<>();
        ArrayList<Pair<BigInteger, GroupElement>> giants = new ArrayList<>();
        BigInteger i = BigInteger.ZERO;
        for(; !i.equals(m); i = i.add(BigInteger.ONE)) {
            babies.add(new Pair<>(i, alpha.pow(i)));
            giants.add(new Pair<>(i, beta.multiply(alpha.pow(i.multiply(m)).inverse())));
        }
        for(Pair<BigInteger, GroupElement> baby : babies) {
            for(Pair<BigInteger, GroupElement> giant : giants) {

            }
        }
        return Optional.empty();
    }

    @Override
    public GroupElement getAlpha() {
        return alpha;
    }
}
