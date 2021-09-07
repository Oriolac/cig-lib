package cat.udl.cig.structures;

import java.math.BigInteger;

public class PairGroupTest extends RingTemplateTest{
    @Override
    public void setUpRing() {
        PrimeField groupA = new PrimeField(BigInteger.valueOf(17L));
        PrimeField groupB = new PrimeField(BigInteger.valueOf(11L));
        ring = new PairGroup(groupA, groupB);
        RingElement firstElementComponentA = new PrimeFieldElement(groupA, BigInteger.valueOf(10L));
        RingElement firstElementComponentB = new PrimeFieldElement(groupB, BigInteger.valueOf(7L));
        operandA = new PairGroupElement(firstElementComponentA, firstElementComponentB);
        RingElement secondElementComponentA = new PrimeFieldElement(groupA, BigInteger.valueOf(13L));
        RingElement secondElementComponentB = new PrimeFieldElement(groupB, BigInteger.valueOf(5L));
        operandB = new PairGroupElement(secondElementComponentA, secondElementComponentB);
        power = BigInteger.valueOf(3L);

    }
}
