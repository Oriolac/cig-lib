package cat.udl.cig.structures;

import cat.udl.cig.structures.Group;
import cat.udl.cig.structures.GroupElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class GroupTemplateTest {

    Group group;
    GroupElement operandA;
    GroupElement operandB;
    BigInteger power;

    @BeforeEach
    public void setUp() {
        setUpRing();
    }

    abstract public void setUpRing();

    @Test
    public void isRandomElementOnRingTest() {
        GroupElement randomElement = group.getRandomElement();
        assertEquals(randomElement.getGroup(), group);
    }

    @Test
    public void getCorrectlyIdentityElement() {
        GroupElement randomElement = group.getRandomElement();
        GroupElement identity = group.getMultiplicativeIdentity();
        GroupElement result = randomElement.multiply(identity);
        assertEquals(randomElement, result);
    }

    @Test
    public void multiplicationTest() {
        GroupElement result = group.multiply(operandA, operandB);
        assertEquals(operandA.multiply(operandB), result);
    }

    @Test
    public void powTest() {
        GroupElement result = group.pow(operandA, power);
        assertEquals(operandA.pow(power), result);
    }

}
