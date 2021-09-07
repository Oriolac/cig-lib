package cat.udl.cig.structures;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

class PrimeFieldElementTest {

    PrimeField field;

    @BeforeEach
    void setUp() {
        field = new PrimeField(BigInteger.valueOf(11));
    }

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
}