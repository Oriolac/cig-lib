import cat.udl.cig.exceptions.ConstructionException;
import cat.udl.cig.fields.IntegerPrimeOrderSubgroup;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigInteger;

public class IntegerPrimeOrderSubgroupTest {


    @Test
    public void constructionExceptionTest() {
        final BigInteger EXPONENT = BigInteger.valueOf(1);
        final BigInteger GENERATOR = BigInteger.valueOf(6);
        final BigInteger MODULE = BigInteger.valueOf(11);

        assertThrows(ConstructionException.class, () -> new IntegerPrimeOrderSubgroup(MODULE, EXPONENT, GENERATOR));
    }

    @Test
    public void constructionArithmeticExceptionTest(){
        final BigInteger EXPONENT = BigInteger.valueOf(10);
        final BigInteger GENERATOR = BigInteger.valueOf(6);
        final BigInteger MODULE = BigInteger.valueOf(-11);
        assertThrows(ArithmeticException.class, () -> new IntegerPrimeOrderSubgroup(MODULE, EXPONENT, GENERATOR));
    }

    @Test
    public void constructionTest() {
        final BigInteger EXPONENT = BigInteger.valueOf(10);
        final BigInteger GENERATOR = BigInteger.valueOf(6);
        final BigInteger MODULE = BigInteger.valueOf(11);

        IntegerPrimeOrderSubgroup group = new IntegerPrimeOrderSubgroup(MODULE, EXPONENT, GENERATOR);
        assertEquals(EXPONENT, group.getSize());
        assertEquals(GENERATOR, group.getGenerator().getValue());
        assertEquals(BigInteger.ONE, group.getNeuterElement().getValue());
    }

    @Test
    public void testingExponentialOrCardinailty() {
        for (int i = 1; i < 10; ++i) {
            BigInteger exponent = BigInteger.valueOf(i);

        }
    }

}
