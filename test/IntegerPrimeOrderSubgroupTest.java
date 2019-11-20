import cat.udl.cig.exceptions.ConstructionException;
import cat.udl.cig.fields.IntegerPrimeOrderSubgroup;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.math.BigInteger;

public class IntegerPrimeOrderSubgroupTest {

    private static final BigInteger EXPONENT = BigInteger.valueOf(10);
    private static final BigInteger GENERATOR = BigInteger.valueOf(6);
    private static final BigInteger MODULE = BigInteger.valueOf(11);

    @Test(expected = ConstructionException.class)
    public void constructionExceptionTest() {
        final BigInteger EXPONENT = BigInteger.valueOf(1);
        final BigInteger GENERATOR = BigInteger.valueOf(6);
        final BigInteger MODULE = BigInteger.valueOf(11);

        IntegerPrimeOrderSubgroup group = new IntegerPrimeOrderSubgroup(MODULE, EXPONENT, GENERATOR);
    }

    @Test(expected = ArithmeticException.class)
    public void constructionArithmeticExceptionTest(){
        final BigInteger EXPONENT = BigInteger.valueOf(10);
        final BigInteger GENERATOR = BigInteger.valueOf(6);
        final BigInteger MODULE = BigInteger.valueOf(-11);

        IntegerPrimeOrderSubgroup group = new IntegerPrimeOrderSubgroup(MODULE, EXPONENT, GENERATOR);
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

    @Test(expected = ConstructionException.class)
    public void testingCardinality(int e) {
        final BigInteger GENERATOR = BigInteger.valueOf(6);
        final BigInteger MODULE = BigInteger.valueOf(11);
        BigInteger exponent = BigInteger.valueOf(e);
        IntegerPrimeOrderSubgroup group = new IntegerPrimeOrderSubgroup(MODULE, exponent, GENERATOR);
    }
}
