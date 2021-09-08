package cat.udl.cig.structures;

import java.math.BigInteger;
import java.util.ArrayList;

public class PairGroupElementTest extends RingElementTemplateTest {

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
        RingElement op2 = new PrimeFieldElement(groupB, BigInteger.valueOf(4L));
        return new PairGroupElement(op1, op2);
    }

    @Override
    protected RingElement returnExpectedOpposite1() {
        RingElement op1 = new PrimeFieldElement(groupA, BigInteger.valueOf(4L));
        RingElement op2 = new PrimeFieldElement(groupB, BigInteger.valueOf(2L));
        return new PairGroupElement(op1, op2);
    }

    @Override
    protected RingElement returnExpectedInverse1() {
        RingElement op1 = new PrimeFieldElement(groupA, BigInteger.valueOf(4L));
        RingElement op2 = new PrimeFieldElement(groupB, BigInteger.valueOf(5L));
        return new PairGroupElement(op1, op2);

    }

    @Override
    protected BigInteger returnPower() {
        return BigInteger.valueOf(3L);
    }

    @Override
    protected RingElement returnExpectedPower() {
        RingElement op1 = new PrimeFieldElement(groupA, BigInteger.valueOf(4L));
        RingElement op2 = new PrimeFieldElement(groupB, BigInteger.valueOf(3L));
        return new PairGroupElement(op1, op2);
    }

    @Override
    protected ArrayList<RingElement> returnSquaresRootOfOp1() {
        RingElement sqrt1A = new PrimeFieldElement(groupA, BigInteger.valueOf(8L));
        RingElement sqrt1B = new PrimeFieldElement(groupB, BigInteger.valueOf(3L));
        RingElement sqrt2A = new PrimeFieldElement(groupA, BigInteger.valueOf(9L));
        RingElement sqrt2B = new PrimeFieldElement(groupB, BigInteger.valueOf(8L));

        return new ArrayList<RingElement>(){{add(new PairGroupElement(sqrt1A, sqrt1B)); add(new PairGroupElement(sqrt2A, sqrt2B));}};
    }

    @Override
    protected RingElement expectedResultMultiplication() {
        RingElement op1 = new PrimeFieldElement(groupA, BigInteger.valueOf(15L));
        RingElement op2 = new PrimeFieldElement(groupB, BigInteger.ONE);
        return new PairGroupElement(op1, op2);
    }

    @Override
    protected RingElement expectedResultDivision() {
        RingElement op1 = new PrimeFieldElement(groupA, BigInteger.valueOf(9L));
        RingElement op2 = new PrimeFieldElement(groupB, BigInteger.valueOf(4L));
        return new PairGroupElement(op1, op2);
    }
}
