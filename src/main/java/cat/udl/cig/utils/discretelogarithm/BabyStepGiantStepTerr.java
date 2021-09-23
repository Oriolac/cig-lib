package cat.udl.cig.utils.discretelogarithm;

import cat.udl.cig.structures.GroupElement;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Optional;

public class BabyStepGiantStepTerr implements LogarithmAlgorithm {


    private final GroupElement gen;
    private final BigInteger totalBabySteps;

    public BabyStepGiantStepTerr(GroupElement gen, BigInteger totalBabySteps) {
        this.gen = gen;
        this.totalBabySteps = totalBabySteps;
    }

    @Override
    public Optional<BigInteger> algorithm(GroupElement beta) throws ArithmeticException {
        GroupElement inf = gen.getGroup().getMultiplicativeIdentity();
        if (gen.equals(inf)) {
            return Optional.of(BigInteger.ONE);
        }
        BigInteger n = BigInteger.ZERO;
        BigInteger i = BigInteger.ONE;
        HashMap<GroupElement, BigInteger> R = new HashMap<>();
        R.put(inf, BigInteger.ZERO);
        R.put(gen, BigInteger.ONE);
        GroupElement alpha = this.gen;
        while (i.compareTo(totalBabySteps) < 0) {
            alpha = gen.multiply(alpha);
            i = i.add(BigInteger.ONE);
            if (alpha.equals(inf)) {
                n = i;
                return Optional.of(n);
            } else {
                R.put(alpha, i);
            }
        }
        BigInteger j = BigInteger.ZERO;
        GroupElement newB = alpha.multiply(alpha);
        BigInteger t = totalBabySteps.multiply(BigInteger.TWO);
        while (true) {
            try {
                if (R.containsKey(newB)) {
                    BigInteger newBValue = R.get(newB);
                    if (newBValue != null && newBValue.equals(i)) {
                        n = t.subtract(i);
                        return Optional.of(n);
                    }
                    alpha = gen.multiply(alpha);
                    j = j.add(BigInteger.ONE);
                    R.put(alpha, j.add(totalBabySteps));
                    newB = alpha.multiply(newB);
                    t = t.add(j).add(totalBabySteps);
                } else {
                    alpha = gen.multiply(alpha);
                    j = j.add(BigInteger.ONE);
                    R.put(alpha, j.add(totalBabySteps));
                    newB = alpha.multiply(newB);
                    t = t.add(j).add(totalBabySteps);
                }
            } catch (NullPointerException ex) {
                ex.printStackTrace();
                System.out.println(R.containsKey(newB));
            }

        }
    }

    @Override
    public GroupElement getAlpha() {
        return gen;
    }
}
