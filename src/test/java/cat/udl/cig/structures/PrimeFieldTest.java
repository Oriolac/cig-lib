package cat.udl.cig.structures;

import java.math.BigInteger;

class PrimeFieldTest extends RingTemplateTest {


    @Override
    public void setUpRing() {
        PrimeField field = new PrimeField(BigInteger.valueOf(37L));
        ring = field;
        operandA = new PrimeFieldElement(field, BigInteger.valueOf(4L));
        operandB = new PrimeFieldElement(field, BigInteger.valueOf(5L));;
        power = BigInteger.valueOf(3L);
    }
}