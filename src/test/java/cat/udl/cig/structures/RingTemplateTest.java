package cat.udl.cig.structures;

import cat.udl.cig.structures.Ring;
import cat.udl.cig.structures.RingElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class RingTemplateTest {

    Ring ring;
    RingElement operandA;
    RingElement operandB;
    BigInteger power;

    @BeforeEach
    public void setUp() {
        setUpRing();
    }

    abstract public void setUpRing();

    @Test
    public void isRandomElementOnRingTest() {
        RingElement randomElement = ring.getRandomElement();
        assertEquals(randomElement.getGroup(), ring);
    }

    @Test
    public void getCorrectlyAdditiveIdentity() {
        RingElement randomElement = ring.getRandomElement();
        RingElement neuterElement = ring.getAdditiveIdentity();
        RingElement result = randomElement.add(neuterElement);
        assertEquals(randomElement, result);
    }

    @Test
    public void getCorrectlyIdentityElement() {
        RingElement randomElement = ring.getRandomElement();
        RingElement identity = ring.getMultiplicativeIdentity();
        RingElement result = randomElement.multiply(identity);
        assertEquals(randomElement, result);
    }

    @Test
    public void multiplicationTest() {
        RingElement result = ring.multiply(operandA, operandB);
        assertEquals(operandA.multiply(operandB), result);
    }

    @Test
    public void powTest() {
        RingElement result = ring.pow(operandA, power);
        assertEquals(operandA.pow(power), result);
    }

}
