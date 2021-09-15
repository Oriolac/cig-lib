package cat.udl.cig.structures;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.print.attribute.HashAttributeSet;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class RingElementTemplateTest {

    Ring ring;
    private RingElement op1;
    private RingElement op2;
    private BigInteger power;
    private RingElement expectedOpposite1;
    private RingElement expectedInverse1;
    private RingElement expectedAddition;
    private RingElement expectedSubstraction;
    private RingElement expectedMultiplication;
    private RingElement expectedDivision;
    private RingElement expectedPower;
    private ArrayList<RingElement> expectedSquaresRootOfOp1;

    @BeforeEach
    void setUp() {
        ring = returnRing();
        op1 = returnOperand1();
        op2 = returnOperand2();
        power = returnPower();
        expectedOpposite1 = returnExpectedOpposite1();
        expectedInverse1 = returnExpectedInverse1();
        expectedAddition = returnResultAddition();
        expectedSubstraction = returnResultSubtraction();
        expectedPower = returnExpectedPower();
        expectedSquaresRootOfOp1 = returnSquaresRootOfOp1();
        expectedMultiplication = expectedResultMultiplication();
        expectedDivision = expectedResultDivision();
    }

    protected abstract Ring returnRing();

    protected abstract RingElement returnOperand1();

    protected abstract RingElement returnOperand2();

    protected abstract RingElement returnResultAddition();

    protected abstract RingElement returnResultSubtraction();

    protected abstract RingElement returnExpectedOpposite1();

    protected abstract RingElement returnExpectedInverse1();

    protected abstract BigInteger returnPower();

    protected abstract RingElement returnExpectedPower();

    protected abstract ArrayList<RingElement> returnSquaresRootOfOp1();

    protected abstract RingElement expectedResultMultiplication();

    protected abstract RingElement expectedResultDivision();

    @Test
    void testAdditionWithIdentity() {
        RingElement result = op1.add(ring.getAdditiveIdentity());
        assertEquals(op1, result);
    }

    @Test
    void testAdditionTwoOps() {
        RingElement result = op1.add(op2);
        assertEquals(expectedAddition, result);
    }

    @Test
    void testSubstractTwoOps() {
        RingElement result = op1.subtract(op2);
        assertEquals(expectedSubstraction, result);
    }

    @Test
    void testsubtractIdentity() {
        RingElement result = op1.subtract(ring.getAdditiveIdentity());
        assertEquals(op1, result);
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
    void testInverse() {
        assertEquals(expectedInverse1, op1.inverse());
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
        RingElement result = op1.opposite();
        assertEquals(expectedOpposite1, result);
    }

    @Test
    void getRingOfOneOperand() {
        assertEquals(ring, op1.getGroup());
        assertEquals(ring, op2.getGroup());
    }

    @Test
    void testPow() {
        assertEquals(expectedPower, op1.pow(power));
    }

    @Test
    void testSquareRoot() {
        assertEquals(new HashSet<>(expectedSquaresRootOfOp1), new HashSet<>(op1.squareRoot()));
    }
}
