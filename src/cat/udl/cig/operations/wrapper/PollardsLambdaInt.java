package cat.udl.cig.operations.wrapper;

import cat.udl.cig.exceptions.ConstructionException;
import cat.udl.cig.fields.GroupElement;

import java.math.BigInteger;
import java.util.Optional;

public interface PollardsLambdaInt {

    Optional<BigInteger> algorithm(GroupElement beta) throws ArithmeticException;
}
