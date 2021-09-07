package cat.udl.cig.utils;

import cat.udl.cig.structures.PrimeField;
import cat.udl.cig.structures.PrimeFieldElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class PolynomialTest {

    Polynomial polynomial;
    Polynomial.PolynomialBuilder builder;
    PrimeField field;

    @BeforeEach
    void setUp() {
        field = new PrimeField(BigInteger.valueOf(11L));
        builder = new Polynomial.PolynomialBuilder()
                .addTerm(2, new PrimeFieldElement(field, BigInteger.valueOf(1)));
        polynomial = builder.build();
    }

    @Test
    void testNonResidue() {
        HashMap<PrimeFieldElement, Boolean> quadraticNonResidues = new HashMap<>();
        quadraticNonResidues.put(new PrimeFieldElement(field, BigInteger.TWO), true);
        quadraticNonResidues.put(new PrimeFieldElement(field, new BigInteger("6")), true);
        quadraticNonResidues.put(new PrimeFieldElement(field, new BigInteger("7")), true);
        quadraticNonResidues.put(new PrimeFieldElement(field, new BigInteger("8")), true);
        quadraticNonResidues.put(new PrimeFieldElement(field, new BigInteger("10")), true);
        PrimeFieldElement e = field.getAdditiveIdentity().add(field.getMultiplicativeIdentity());
        for (; !e.equals(field.getAdditiveIdentity()); e = e.add(field.getMultiplicativeIdentity())) {
            Boolean expected = quadraticNonResidues.getOrDefault(e, false);
            assertEquals(expected, e.isQuadraticNonResidue());
        }
    }

}