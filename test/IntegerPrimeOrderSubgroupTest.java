import cat.udl.cig.exceptions.ConstructionException;
import cat.udl.cig.fields.groups.IntegerPrimeOrderSubgroup;
import org.junit.Test;
import static org.junit.Assert.*;

import java.math.BigInteger;

public class IntegerPrimeOrderSubgroupTest {

    private static final BigInteger EXPONENT = BigInteger.valueOf(10);
    private static final BigInteger GENERATOR = BigInteger.valueOf(6);
    private static final BigInteger MODULE = BigInteger.valueOf(11);

    @Test(expected = ConstructionException.class)
    public void ConstructionExceptionTest() {
        final BigInteger EXPONENT = BigInteger.valueOf(1);
        final BigInteger GENERATOR = BigInteger.valueOf(6);
        final BigInteger MODULE = BigInteger.valueOf(11);

        IntegerPrimeOrderSubgroup group = new IntegerPrimeOrderSubgroup(MODULE, EXPONENT, GENERATOR);
    }

    @Test(expected = ArithmeticException.class)
    public void ConstructionArithmeticExceptionTest(){
        final BigInteger EXPONENT = BigInteger.valueOf(10);
        final BigInteger GENERATOR = BigInteger.valueOf(6);
        final BigInteger MODULE = BigInteger.valueOf(-11);

        IntegerPrimeOrderSubgroup group = new IntegerPrimeOrderSubgroup(MODULE, EXPONENT, GENERATOR);
    }

    @Test
    public void ConstructionTest() {
        final BigInteger EXPONENT = BigInteger.valueOf(10);
        final BigInteger GENERATOR = BigInteger.valueOf(6);
        final BigInteger MODULE = BigInteger.valueOf(11);

        IntegerPrimeOrderSubgroup group = new IntegerPrimeOrderSubgroup(MODULE, EXPONENT, GENERATOR);
        assertEquals(EXPONENT, group.getSize());
        assertEquals(GENERATOR, group.getGenerator().getValue());
        assertEquals(BigInteger.ONE, group.getNeuterElement().getValue());
    }
}
