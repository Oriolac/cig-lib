import cat.udl.cig.exceptions.ConstructionException;
import cat.udl.cig.fields.IntegerPrimeOrderSubgroup;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.IntStream;

@RunWith(Parameterized.class)
public class CardinalityTest {
    @Parameterized.Parameters
    public static Collection<Integer> data() {
        IntStream intStream = IntStream.range(1, 9);
        for (int i = 1; i < 9; ++i) {
           IntStream tmp = IntStream.range(i*10+1, (i+1)*10 - 1);
           intStream = IntStream.concat(intStream, tmp);
        }
        int[] ints = intStream.toArray();
        Collection<Integer> alist = new ArrayList<>(ints.length);
        for (int i : ints) {
            alist.add(i);
        }
        return alist;
    }

    @Parameterized.Parameter
    public Integer i;

    @Test(expected = ConstructionException.class)
    public void test() {
        final BigInteger GENERATOR = BigInteger.valueOf(6);
        final BigInteger MODULE = BigInteger.valueOf(11);
        BigInteger exponent = BigInteger.valueOf(i);
        IntegerPrimeOrderSubgroup group = new IntegerPrimeOrderSubgroup(MODULE, exponent, GENERATOR);
    }
}
