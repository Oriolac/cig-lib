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

public class EllipticCurveF32Test {

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
                .addTerm(0, primefield.buildElement().setValue(1).build().orElseThrow())
                .addTerm(1, primefield.buildElement().setValue(2).build().orElseThrow())
                .build()).build().orElseThrow();
        this.elB = ring.buildElement().setPolynomial(new Polynomial.PolynomialBuilder()
                .addTerm(0, primefield.buildElement().setValue(2).build().orElseThrow())
                .addTerm(1, primefield.buildElement().setValue(2).build().orElseThrow())
                .build()).build().orElseThrow();
        this.ec = new EllipticCurve(ring, elA, elB);
    }

    @Test
    void testLiftX() {
        ExtensionFieldElement x = ring.buildElement().setPolynomial(new Polynomial.PolynomialBuilder()
                .addTerm(1, primefield.getMultiplicativeIdentity()).build()).build().orElseThrow();
        ExtensionFieldElement y = ring.buildElement().setPolynomial(new Polynomial.PolynomialBuilder()
                .addTerm(0, primefield.getMultiplicativeIdentity())
                .addTerm(1, primefield.getMultiplicativeIdentity()).build()).build().orElseThrow();
        ExtensionFieldElement ysquare = ring.buildElement().setPolynomial(new Polynomial.PolynomialBuilder()
                .addTerm(1, primefield.buildElement().setValue(2).build().orElseThrow()).build())
                .build().orElseThrow();
        ArrayList<? extends EllipticCurvePoint> points = ec.liftX(x);
        assertEquals(ysquare, x.pow(BigInteger.valueOf(3)).add(elA.multiply(x)).add(elB));
        assertFalse(points.isEmpty());
        EllipticCurvePoint point = points.get(0);
        assertEquals(y, point.getY());
    }

    @Test
    void testOrder() {
        assertEquals(BigInteger.valueOf(10), this.ec.getSize());;
    }
}
