package cat.udl.cig.structures.ecc;

import cat.udl.cig.structures.ExtensionField;
import cat.udl.cig.structures.ExtensionFieldElement;
import cat.udl.cig.structures.PrimeField;
import cat.udl.cig.utils.Polynomial;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class EllipticCurve2x0F32Test {


    private BigInteger p;
    private ExtensionField ring;
    private PrimeField primefield;
    private ExtensionFieldElement elA;
    private ExtensionFieldElement elB;
    private EllipticCurve ec;

    @BeforeEach
    void setUp() {
        this.p = BigInteger.valueOf(3);
        this.ring = ExtensionField.ExtensionFieldP2(p);
        this.primefield = new PrimeField(p);
        this.elA = ring.buildElement().setPolynomial(new Polynomial.PolynomialBuilder()
                .addTerm(1, primefield.buildElement().setValue(2).build().orElseThrow())
                .build()).build().orElseThrow();
        this.elB = ring.buildElement().setPolynomial(new Polynomial.PolynomialBuilder()
                .addTerm(0, primefield.buildElement().setValue(0).build().orElseThrow())
                .addTerm(1, primefield.buildElement().setValue(0).build().orElseThrow())
                .build()).build().orElseThrow();
        this.ec = new EllipticCurve(ring, elA, elB);
    }

    @Test
    void testGetOrder() {
        assertEquals(BigInteger.valueOf(4), ec.getSize());
    }


}
