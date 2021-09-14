package cat.udl.cig.utils.discretelogarithm;

import cat.udl.cig.structures.GroupElement;

import java.math.BigInteger;

public class PollardsLambdaTest extends LogarithmAlgorithmTest {

    @Override
    protected LogarithmAlgorithm returnAlgorithm() {
        return new PollardsLambda(returnGenerator());
    }

    @Override
    protected GroupElement returnGenerator() {
        return LogarithmAlgorithmTest.PrimeFieldGenerator.returnGenerator();
    }

    @Override
    protected GroupElement returnBeta() {
        return LogarithmAlgorithmTest.PrimeFieldGenerator.returnBeta();
    }

    @Override
    protected BigInteger returnExpectedPower() {
        return LogarithmAlgorithmTest.PrimeFieldGenerator.returnExpectedPower();
    }
}
