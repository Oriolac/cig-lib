package cat.udl.cig.structures;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PrimeFieldElementTest extends RingElementTemplateTest {

    PrimeField field;

    @Test
    void testEqualsExactField() {
        PrimeFieldElement el = new PrimeFieldElement(field, BigInteger.TWO);
        PrimeFieldElement el2 = new PrimeFieldElement(field, BigInteger.TWO);
        assertEquals(el, el2);
    }

    @Test
    void testEqualsNotExactField() {
        PrimeField field2 = new PrimeField(BigInteger.valueOf(11));
        PrimeFieldElement el = new PrimeFieldElement(field, BigInteger.TWO);
        PrimeFieldElement el2 = new PrimeFieldElement(field2, BigInteger.TWO);
        assertEquals(el, el2);
    }

    @Override
    protected Ring returnRing() {
        field = new PrimeField(BigInteger.valueOf(11));
        return field;
    }

    @Override
    protected RingElement returnOperand1() {
        return field.buildElement().setValue(10L).build().orElseThrow();
    }

    @Override
    protected RingElement returnOperand2() {
        return field.buildElement().setValue(7L).build().orElseThrow();
    }

    @Override
    protected RingElement returnResultAddition() {
        return field.buildElement().setValue(6L).build().orElseThrow();

    }

    @Override
    protected RingElement returnResultSubtraction() {
        return field.buildElement().setValue(3L).build().orElseThrow();
    }

    @Override
    protected RingElement returnExpectedOpposite1() {
        return field.buildElement().setValue(1L).build().orElseThrow();
    }

    @Override
    protected RingElement returnExpectedInverse1() {
        return field.buildElement().setValue(10L).build().orElseThrow();
    }

    @Override
    protected BigInteger returnPower() {
        return BigInteger.valueOf(7);
    }

    @Override
    protected RingElement returnExpectedPower() {
        return field.buildElement().setValue(10L).build().orElseThrow();
    }

    @Override
    protected ArrayList<RingElement> returnSquaresRootOfOp1() {
        return new ArrayList<>();
    }

    @Override
    protected RingElement expectedResultMultiplication() {
        return field.buildElement().setValue(4L).build().orElseThrow();
    }

    @Override
    protected RingElement expectedResultDivision() {
        return field.buildElement().setValue(3L).build().orElseThrow();
    }

    @Test
    void isQuadraticNonResidueTest() {
        assertTrue(field.buildElement().setValue(10).build().orElseThrow().isQuadraticNonResidue());
    }
}