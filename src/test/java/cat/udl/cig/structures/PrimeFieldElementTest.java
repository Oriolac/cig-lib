package cat.udl.cig.structures;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

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
        return new PrimeField(BigInteger.valueOf(11));
    }

    @Override
    protected RingElement returnOperand1() {
        return ((PrimeField) returnRing()).buildElement().setValue(10L).buildElement().orElseThrow();
    }

    @Override
    protected RingElement returnOperand2() {
        return ((PrimeField) returnRing()).buildElement().setValue(7L).buildElement().orElseThrow();
    }

    @Override
    protected RingElement returnResultAddition() {
        return ((PrimeField) returnRing()).buildElement().setValue(6L).buildElement().orElseThrow();

    }

    @Override
    protected RingElement returnResultSubtraction() {
        return ((PrimeField) returnRing()).buildElement().setValue(3L).buildElement().orElseThrow();
    }

    @Override
    protected RingElement returnExpectedOpposite1() {
        return ((PrimeField) returnRing()).buildElement().setValue(7L).buildElement().orElseThrow();
    }

    @Override
    protected RingElement returnExpectedInverse1() {
        return ((PrimeField) returnRing()).buildElement().setValue(8L).buildElement().orElseThrow();
    }

    @Override
    protected BigInteger returnPower() {
        return BigInteger.valueOf(7);
    }

    @Override
    protected RingElement returnExpectedPower() {
        return ((PrimeField) returnRing()).buildElement().setValue(5L).buildElement().orElseThrow();
    }

    @Override
    protected ArrayList<RingElement> returnSquaresRootOfOp1() {
        return null;
    }

    @Override
    protected RingElement expectedResultMultiplication() {
        return null;
    }

    @Override
    protected RingElement expectedResultDivision() {
        return null;
    }
}