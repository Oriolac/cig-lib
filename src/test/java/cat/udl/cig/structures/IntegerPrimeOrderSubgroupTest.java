package cat.udl.cig.structures;

import java.math.BigInteger;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class IntegerPrimeOrderSubgroupTest extends GroupTemplateTest{
    @Override
    public void setUpRing() {
        IntegerPrimeOrderSubgroup group = new IntegerPrimeOrderSubgroup(BigInteger.valueOf(11L), BigInteger.valueOf(11L - 1), BigInteger.valueOf(3L));
        this.group = group.getField();

        Optional<? extends PrimeFieldElement> ringElementOptionalA = group.buildElement().setValue(BigInteger.valueOf(5L)).buildElement();
        if (ringElementOptionalA.isEmpty())
            fail();
        operandA = ringElementOptionalA.get();
        Optional<? extends PrimeFieldElement> ringElementOptionalB = group.buildElement().setValue(BigInteger.valueOf(3L)).buildElement();
        if (ringElementOptionalB.isEmpty())
            fail();
        operandB = ringElementOptionalB.get();

        power = BigInteger.valueOf(3L);


    }
}
