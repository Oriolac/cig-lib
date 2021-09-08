package cat.udl.cig.structures;

import cat.udl.cig.utils.bfarithmetic.BitSetManipulation;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.BitSet;

public class BinaryFieldElementTest extends RingElementTemplateTest{

    BinaryField field;


    @Override
    protected Ring returnRing() {
        BitSet bitSet = BitSetManipulation.longToBitSet(8 + 2 + 1);
        field = new BinaryField(bitSet);
        return field;
    }

    @Override
    protected RingElement returnOperand1() {
        return new BinaryFieldElement(field, BitSetManipulation.longToBitSet(4)); // x^2
    }

    @Override
    protected RingElement returnOperand2() {
        return new BinaryFieldElement(field, BitSetManipulation.longToBitSet(6)); // x^2 + x
    }

    @Override
    protected RingElement returnResultAddition() {
        return new BinaryFieldElement(field, BitSetManipulation.longToBitSet(2)); // x
    }

    @Override
    protected RingElement returnResultSubtraction() {
        return new BinaryFieldElement(field, BitSetManipulation.longToBitSet(2)); // x
    }

    @Override
    protected RingElement returnExpectedOpposite1() {
        return new BinaryFieldElement(field, BitSetManipulation.longToBitSet(4)); // x^2
    }

    @Override
    protected RingElement returnExpectedInverse1() {
        return new BinaryFieldElement(field, BitSetManipulation.longToBitSet(7)); // x^2 + x + 1
    }

    @Override
    protected BigInteger returnPower() {
        return BigInteger.valueOf(5);
    }

    @Override
    protected RingElement returnExpectedPower() {
        return new BinaryFieldElement(field, BitSetManipulation.longToBitSet(3)); // x + 1
    }

    @Override
    protected ArrayList<RingElement> returnSquaresRootOfOp1() {
        ArrayList<RingElement> list = new ArrayList<>();
        list.add(new BinaryFieldElement(field, BitSetManipulation.longToBitSet( 2)));
        return list;
    }

    @Override
    protected RingElement expectedResultMultiplication() {
        return new BinaryFieldElement(field, BitSetManipulation.longToBitSet(5)); // x^2 + 1
    }

    @Override
    protected RingElement expectedResultDivision() {
        return new BinaryFieldElement(field, BitSetManipulation.longToBitSet(7)); // x^2 + x + 1
    }
}
