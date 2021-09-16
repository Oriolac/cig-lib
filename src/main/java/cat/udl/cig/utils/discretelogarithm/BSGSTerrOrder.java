package cat.udl.cig.utils.discretelogarithm;

import cat.udl.cig.structures.GroupElement;
import cat.udl.cig.utils.bfarithmetic.GreaterCommonDivisor;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

public class BSGSTerrOrder implements LogarithmAlgorithm {

    private final GroupElement alpha;
    private final ArrayList<BigInteger> babySteps;

    public BSGSTerrOrder(GroupElement alpha) {
        this.alpha = alpha;
        this.babySteps = new ArrayList<>();
        babySteps.add(BigInteger.valueOf(200));
        babySteps.add(BigInteger.valueOf(201));
        babySteps.add(BigInteger.valueOf(203));
    }

    public BSGSTerrOrder(GroupElement alpha, ArrayList<BigInteger> babySteps) {
        this.alpha = alpha;
        this.babySteps = babySteps;
    }

    public BSGSTerrOrder(GroupElement alpha, BigInteger... babySteps) {
        this.alpha = alpha;
        this.babySteps = new ArrayList<>(Arrays.asList(babySteps));
    }

    public BSGSTerrOrder(GroupElement alpha, String... babySteps) {
        this.alpha = alpha;
        this.babySteps = Arrays.stream(babySteps).map(BigInteger::new).collect(Collectors.toCollection(ArrayList::new));
    }


    @Override
    public Optional<BigInteger> algorithm(GroupElement beta) throws ArithmeticException {
        return babySteps.stream()
                .map(babyStep -> new BabyStepGiantStepTerr(alpha, babyStep).algorithm(beta))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .reduce(GreaterCommonDivisor::gcd);
    }

    @Override
    public GroupElement getAlpha() {
        return alpha;
    }
}
