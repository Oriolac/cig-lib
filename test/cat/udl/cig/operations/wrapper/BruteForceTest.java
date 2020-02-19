package cat.udl.cig.operations.wrapper;

import cat.udl.cig.AbstractSetUpP192;
import cat.udl.cig.ecc.GeneralECPoint;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BruteForceTest extends AbstractSetUpP192 {

    static BruteForce bruteForce;

    @BeforeAll
    static void setUp() {
        bruteForce = new BruteForce(alpha);
    }

    @Test
    void algorithmEcc() {
        BigInteger bigInici = BigInteger.TEN.pow(2);
        BigInteger bigFinal = bigInici.multiply(BigInteger.TWO);
        for(BigInteger xi = bigInici; xi.compareTo(bigFinal) < 0; xi = xi.add(BigInteger.TEN)) {
            testValue(xi);
        }
    }

    @Test
    void algorithmTestCompromisedValues() {
        testValue(BigInteger.valueOf(9));
    }


    void testValue(BigInteger xi) {
        GeneralECPoint beta = alpha.pow(xi);
        Optional<BigInteger> res = bruteForce.algorithm(beta);
        assertEquals(Optional.of(xi), res);
    }
}