package cat.udl.cig.structures;

import java.math.BigInteger;
import java.util.ArrayList;

public class PairGroupElementTest extends RingElementTemplateTest{

    PrimeField groupA = new PrimeField(BigInteger.valueOf(17L));
    PrimeField groupB = new PrimeField(BigInteger.valueOf(11L));

    @Override
    protected Ring returnRing() {
        return new PairGroup(groupA, groupB);
    }

    @Override
    protected RingElement returnOperand1() {
        RingElement firstElementOp1 = new PrimeFieldElement(groupA, BigInteger.valueOf(13L));
        RingElement secondElementOp1 = new PrimeFieldElement(groupB, BigInteger.valueOf(9L));
        return new PairGroupElement(firstElementOp1, secondElementOp1);
    }

    @Override
    protected RingElement returnOperand2() {
        RingElement firstElementOp2 = new PrimeFieldElement(groupA, BigInteger.valueOf(9L));
        RingElement secondElementOp2 = new PrimeFieldElement(groupB, BigInteger.valueOf(5L));
        return new PairGroupElement(firstElementOp2, secondElementOp2);
    }

    @Override
    protected RingElement returnResultAddition() {
        RingElement op1 = new PrimeFieldElement(groupA, BigInteger.valueOf(5L));
        RingElement op2 = new PrimeFieldElement(groupB, BigInteger.valueOf(3L));
        return new PairGroupElement(op1, op2);
    }

    @Override
    protected RingElement returnResultSubtraction() {
        RingElement op1 = new PrimeFieldElement(groupA, BigInteger.valueOf(4L));
        RingElement op2 = new PrimeFieldElement(groupB, BigInteger.valueOf(2L));
        return new PairGroupElement(op1, op2);
    }

    @Override
    protected RingElement returnExpectedOpposite1() {
        return new PrimeFieldElement(groupA, BigInteger.valueOf(-13L));
    }

    @Override
    protected BigInteger returnPower() {
        return BigInteger.valueOf(3L);
    }

    @Override
    protected RingElement returnExpectedPower() {
        return null;
    }

    @Override
    protected ArrayList<RingElement> returnSquaresRootOfOp1() {
        return null;
    }

    @Override
    protected RingElement expectedResultMultiplication() {
        RingElement op1 = new PrimeFieldElement(groupA, BigInteger.valueOf(6L));
        RingElement op2 = new PrimeFieldElement(groupB, BigInteger.valueOf(6L));
        return new PairGroupElement(op1, op2);
    }

    @Override
    protected RingElement expectedResultDivision() {
        return null;
    }
}
