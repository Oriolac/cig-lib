package cat.udl.cig.operations.wrapper;

import cat.udl.cig.fields.GroupElement;

import java.math.BigInteger;
import java.util.Optional;

/**
 * Constructor should get alpha for algorithm.
 * α^x=β, where x will be returned from algorithm method.
 */
public interface LogarithmAlgorithm {


    Optional<BigInteger> algorithm(GroupElement beta) throws ArithmeticException;
}
