package cat.udl.cig.operations.wrapper;

import cat.udl.cig.AbstractSetUpP192;
import cat.udl.cig.ecc.GeneralECPoint;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PollardsLambdaECCTest extends AbstractSetUpP192 {


    @Test
    void algorithm_ecc() {
        for(BigInteger xi = BigInteger.TEN.pow(2); xi.compareTo(BigInteger.TEN.pow(2).multiply(BigInteger.TWO)) < 0; xi = xi.add(BigInteger.TEN)) {
            test_value(xi);
        }
    }

    @Test
    void algorithm_test_compromised_values() {
        test_value(BigInteger.valueOf(4));
    }


    void test_value(BigInteger xi) {
        GeneralECPoint beta = alpha.pow(xi);
        PollardsLambda lambda = new PollardsLambda(alpha);
        Optional<BigInteger> res = lambda.algorithm(beta);
        assertEquals(Optional.of(xi), res);
    }


}
