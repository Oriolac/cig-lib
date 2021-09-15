package cat.udl.cig.utils.discretelogarithm;

import cat.udl.cig.operations.wrapper.data.Pair;
import cat.udl.cig.structures.Group;
import cat.udl.cig.structures.GroupElement;
import cat.udl.cig.structures.RingElement;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
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
        BigInteger iterator = BigInteger.ZERO;
        BigInteger size = alpha.getGroup().getSize();
        for(; !iterator.equals(m); iterator = iterator.add(BigInteger.ONE)) {
            /*babies.add(new Pair<>(iterator, alpha.pow(iterator)));
            giants.add(new Pair<>(iterator, beta.multiply(alpha.pow(iterator.multiply(m)).inverse())));*/
            babies.add(new Pair<>(iterator, beta.multiply(alpha.pow(iterator))));
            giants.add(new Pair<>(iterator, alpha.pow(m.multiply(iterator))));
        }
        babies.sort(Pair::compareTo);
        giants.sort(Pair::compareTo);
        for (int jiterator = 0; jiterator < Math.min(babies.size(), giants.size()); jiterator++) {
            if (babies.get(jiterator).getValue().equals(giants.get(jiterator).getValue())) {
                BigInteger i = babies.get(jiterator).getKey();
                BigInteger j = giants.get(jiterator).getKey();
                BigInteger value = babies.get(jiterator).getValue().getIntValue();
                //return Optional.of(i.add(j.multiply(m)).mod(size));
                System.out.println("result " + j.multiply(value).subtract(i));
                return Optional.of(j.multiply(value).subtract(i));
            }
        }
        return Optional.empty();
    }

    @Override
    public GroupElement getAlpha() {
        return alpha;
    }
}
