package cat.udl.cig.structures;

import cat.udl.cig.utils.bfarithmetic.BitSetManipulation;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.BitSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

    @Test
    public void testCompareTo() {
        BinaryFieldElement op1 = (BinaryFieldElement) returnOperand1(); // x^2
        BinaryFieldElement op2 = (BinaryFieldElement) returnOperand2(); // x^2 + x
        int expected = -1; // Because the degree is the same, so as 2 <= 2 -> expected = -1
        assertEquals(expected, op1.compareTo(op2));

    }

    @Test
    public void testGetSubstitutionMaxSize() {
        BinaryFieldElement element1 = (BinaryFieldElement) returnOperand1();
        BigInteger expected1 = (BigInteger.TWO).pow(2);
        assertEquals(expected1, element1.getSubstitutionMaxSizeValue());
        BinaryFieldElement element2 = (BinaryFieldElement) returnOperand2();
        BigInteger expected2 = ((BigInteger.TWO).pow(2)).add(BigInteger.TWO);
        assertEquals(expected2, element2.getSubstitutionMaxSizeValue());
    }
}
