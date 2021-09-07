package cat.udl.cig.structures;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public abstract class RingElementTemplateTest {

    private Ring ring;
    private RingElement op1;
    private RingElement op2;
    private RingElement resultAddition;

    @BeforeEach
    void setUp() {
        ring = returnRing();
        op1 = returnOperand1();
        op2 = returnOperand2();
        resultAddition = returnResultAddition();
    }

    protected abstract Ring returnRing();

    protected abstract RingElement returnOperand1();

    protected abstract RingElement returnOperand2();

    protected abstract RingElement returnResultAddition();

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

    }

    @Test
    void testMultiplicationTwoOps() {

    }

    @Test
    void testDivisionTwoOps() {

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
