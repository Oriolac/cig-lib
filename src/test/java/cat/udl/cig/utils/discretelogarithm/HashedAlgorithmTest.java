package cat.udl.cig.utils.discretelogarithm;

import cat.udl.cig.structures.GroupElement;
import cat.udl.cig.structures.RingElement;

import java.math.BigInteger;

public class HashedAlgorithmTest extends LogarithmAlgorithmTest {
    @Override
    protected LogarithmAlgorithm returnAlgorithm() {
        HashedAlgorithm.loadHashedInstance(returnGenerator(), PrimeFieldGenerator.primeField.getSize(), BigInteger.ONE);
        return HashedAlgorithm.getHashedInstance();
    }

    @Override
    protected RingElement returnGenerator() {
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
