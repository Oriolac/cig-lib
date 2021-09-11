package cat.udl.cig.utils.discretelogarithm;

import cat.udl.cig.structures.GroupElement;
import cat.udl.cig.structures.RingElement;

import java.math.BigInteger;
import java.util.Optional;

/**
 * Constructor should get alpha for algorithm.
 * α^x=β, where x will be returned from algorithm method.
 */
public interface LogarithmAlgorithm {


    Optional<BigInteger> algorithm(GroupElement beta) throws ArithmeticException;

    GroupElement getAlpha();
}
