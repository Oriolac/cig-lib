package cat.udl.cig.fields;

import cat.udl.cig.structures.BinaryField;
import cat.udl.cig.structures.BinaryFieldElement;
import cat.udl.cig.utils.bfarithmetic.BasicOperations;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.BitSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BinaryFieldTest extends RingTemplateTest {

    @Override
    public void setUpRing() {
        BitSet bitSet = new BitSet(5);
        bitSet.set(4, true);
        bitSet.set(3, true);
        bitSet.set(2, true);
        bitSet.set(0, true);
        BinaryField field = new BinaryField(bitSet);
        ring = field;
        BitSet bitSetA = new BitSet(4);
        bitSetA.set(3);
        bitSetA.set(2);
        bitSetA.set(1);
        operandA = new BinaryFieldElement(field, bitSetA);
        BitSet bitSetB = new BitSet(4);
        bitSetB.set(2);
        bitSetB.set(0);
        operandB = new BinaryFieldElement(field, bitSetB);
        power = BigInteger.valueOf(3L);
    }

    @Test
    public void checkAddition() {
        BinaryFieldElement element = (BinaryFieldElement) operandA.add(operandB);
        BitSet expected = new BitSet(4);
        expected.set(3);
        expected.set(1);
        expected.set(0);
        assertEquals(expected, element.getValue());
    }

    @Test
    public void checkMult() {
        BinaryFieldElement element = (BinaryFieldElement) operandA.multiply(operandB);
        BitSet expected = new BitSet(4);
        expected.set(3);
        expected.set(2);
        assertEquals(expected, element.getValue());
        assertEquals(BasicOperations.multiply(((BinaryFieldElement) operandA).getValue(), ((BinaryFieldElement) operandB).getValue()), element.getValue());
    }
}