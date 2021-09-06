package cat.udl.cig.fields;

import cat.udl.cig.structures.PrimeField;
import cat.udl.cig.structures.PrimeFieldElement;
import cat.udl.cig.structures.Ring;
import cat.udl.cig.structures.RingElement;

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