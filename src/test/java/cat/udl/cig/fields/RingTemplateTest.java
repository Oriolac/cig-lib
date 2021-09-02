package cat.udl.cig.fields;

import cat.udl.cig.structures.Ring;
import cat.udl.cig.structures.RingElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class RingTemplateTest {

    Ring ring;

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

    }

}
