package cat.udl.cig.structures;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class RingElementTemplateTest {

    private Ring ring;
    private RingElement op1;
    private RingElement op2;
    private RingElement resultAddition;
    private RingElement expectedMultiplication;
    private RingElement expectedDivision;

    @BeforeEach
    void setUp() {
        ring = returnRing();
        op1 = returnOperand1();
        op2 = returnOperand2();
        resultAddition = returnResultAddition();
        expectedMultiplication = expectedResultMultiplication();
        expectedDivision = expectedResultDivision();
    }

    protected abstract Ring returnRing();

    protected abstract RingElement returnOperand1();

    protected abstract RingElement returnOperand2();

    protected abstract RingElement returnResultAddition();

    protected abstract RingElement expectedResultMultiplication();

    protected abstract RingElement expectedResultDivision();

    @Test
    void testAdditionWithIdentity() {

    }

    @Test
    void testAdditionTwoOps() {

    }

    @Test
    void testSubstractTwoOps() {

    }

    @Test
    void testMultiplicationWithIdentity() {
        RingElement result = op1.multiply(ring.getMultiplicativeIdentity());
        assertEquals(op1, result);

    }

    @Test
    void testMultiplicationTwoOps() {
        RingElement result = op1.multiply(op2);
        assertEquals(expectedMultiplication, result);

    }

    @Test
    void testDivisionWithIdentity() {
        RingElement result = op1.divide(ring.getMultiplicativeIdentity());
        assertEquals(op1, result);
    }

    @Test
    void testDivisionTwoOps() {
        RingElement result = op1.divide(op2);
        assertEquals(expectedDivision, result);
    }

    @Test
    void getOppositeOfOneOperand() {

    }

    @Test
    void getRingOfOneOperand() {

    }

    @Test
    void testPow() {

    }

    @Test
    void testSquareRoot() {

    }
}
