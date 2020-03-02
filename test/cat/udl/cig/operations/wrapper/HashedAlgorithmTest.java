package cat.udl.cig.operations.wrapper;

import cat.udl.cig.AbstractSetUpP192;
import cat.udl.cig.ecc.GeneralECPoint;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class HashedAlgorithmTest extends AbstractSetUpP192 {

    static HashedAlgorithm hashedForce;

    @BeforeAll
    static void setUp() {
        HashedAlgorithm.loadHashedInstance(alpha, BigInteger.valueOf(1024), BigInteger.valueOf(32));
        hashedForce = (HashedAlgorithm) HashedAlgorithm.getHashedInstance();
    }

    @Test
    void algorithmEcc() {
        BigInteger bigInici = BigInteger.ZERO;
        BigInteger bigFinal = hashedForce.getOrder();
        for(BigInteger xi = bigInici; xi.compareTo(bigFinal) < 0; xi = xi.add(BigInteger.valueOf(512))) {
            testValue(xi);
        }
    }

    @Test
    void algorithmTestCompromisedValues() {
        testValue(BigInteger.valueOf(9));
    }


    void testValue(BigInteger xi) {
        GeneralECPoint beta = alpha.pow(xi);
        Optional<BigInteger> res = hashedForce.algorithm(beta);
        assertEquals(Optional.of(xi), res);
    }
}
