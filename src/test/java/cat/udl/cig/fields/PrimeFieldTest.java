package cat.udl.cig.fields;

import cat.udl.cig.structures.PrimeField;

import java.math.BigInteger;

class PrimeFieldTest extends RingTemplateTest {


    @Override
    public void setUpRing() {
        ring = new PrimeField(BigInteger.valueOf(37L));
    }
}