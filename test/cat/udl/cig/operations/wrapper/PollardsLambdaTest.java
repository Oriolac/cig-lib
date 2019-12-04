package cat.udl.cig.operations.wrapper;

import cat.udl.cig.fields.Group;
import cat.udl.cig.fields.GroupElement;
import cat.udl.cig.fields.IntegerPrimeOrderSubgroup;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

class PollardsLambdaTest {

    @Test
    void algorithm() {
        Group g = new IntegerPrimeOrderSubgroup(BigInteger.valueOf(11), BigInteger.valueOf(10), BigInteger.valueOf(7));
        GroupElement alpha = g.toElement(BigInteger.valueOf(7));
        BigInteger x = g.getRandomExponent();
        GroupElement beta = alpha.pow(x);
        System.out.println("Alpha: " + alpha.getIntValue().toString() + "   x: " + x.toString() + "    beta: " + beta.getIntValue().toString());
        assertEquals(x, PollardsLambda.algorithm(alpha, beta));
    }
}