package cat.udl.cig.structures.ecc;

import cat.udl.cig.structures.ExtensionField;
import cat.udl.cig.structures.ExtensionFieldElement;
import cat.udl.cig.structures.PrimeField;
import cat.udl.cig.structures.RingElement;
import cat.udl.cig.utils.Polynomial;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class LittleExtensionFieldECTest {

    private BigInteger p;
    private PrimeField primeField;
    private ExtensionField field;
    private EllipticCurve curve;
    private EllipticCurve ec;
    private ExtensionFieldElement A;
    private ExtensionFieldElement B;

    @BeforeEach
    void setUp() {
        p = BigInteger.valueOf(3);
        primeField = new PrimeField(p);
        field = ExtensionField.ExtensionFieldP2(p);
        A = field.buildElement().setPolynomial(new Polynomial.PolynomialBuilder()
                .addTerm(1, primeField.buildElement().setValue(2).build().orElseThrow())
                .addTerm(0, primeField.getMultiplicativeIdentity())
                .build()).build().orElseThrow();
        B = field.buildElement().setPolynomial(new Polynomial.PolynomialBuilder()
                .addTerm(1, primeField.buildElement().setValue(2).build().orElseThrow())
                .addTerm(0, primeField.buildElement().setValue(2).build().orElseThrow())
                .build()).build().orElseThrow();
        ec = new EllipticCurve(field, A, B);
    }

    @Test
    void testAllLiftX() {
        HashMap<ExtensionFieldElement, Set<ExtensionFieldElement>> points = new HashMap<>();
        points.put(field.buildElement().setPolynomial(new Polynomial.PolynomialBuilder().addTerm(0, primeField.buildElement().setValue(2).build().orElseThrow()).build()).build().orElseThrow(),
                new HashSet<>(List.of(
                        field.buildElement().setPolynomial(new Polynomial.PolynomialBuilder().addTerm(0, primeField.buildElement().setValue(0).build().orElseThrow()).build()).build().orElseThrow()
                )));
        points.put(field.buildElement().setPolynomial(new Polynomial.PolynomialBuilder().addTerm(1, primeField.getMultiplicativeIdentity()).build()).build().orElseThrow(),
                new HashSet<>(List.of(
                        field.buildElement().setPolynomial(new Polynomial.PolynomialBuilder()
                                .addTerm(1, primeField.getMultiplicativeIdentity())
                                .addTerm(0, primeField.getMultiplicativeIdentity())
                                .build())
                                .build().orElseThrow(),
                        field.buildElement().setPolynomial(new Polynomial.PolynomialBuilder()
                                .addTerm(1, primeField.buildElement().setValue(2).build().orElseThrow())
                                .addTerm(0, primeField.buildElement().setValue(2).build().orElseThrow()).build()).build().orElseThrow()
                )));
        points.put(field.buildElement().setPolynomial(new Polynomial.PolynomialBuilder()
                        .addTerm(1, primeField.getMultiplicativeIdentity())
                        .addTerm(0, primeField.buildElement().setValue(2).build().orElseThrow())
                        .build()).build().orElseThrow(),
                new HashSet<>(List.of(
                        field.buildElement().setPolynomial(new Polynomial.PolynomialBuilder()
                                .addTerm(0, primeField.getMultiplicativeIdentity())
                                .build()).build().orElseThrow(),
                        field.buildElement().setPolynomial(new Polynomial.PolynomialBuilder()
                                .addTerm(0, primeField.buildElement().setValue(2).build().orElseThrow())
                                .build()).build().orElseThrow()
                )));
        points.put(field.buildElement().setPolynomial(new Polynomial.PolynomialBuilder()
                        .addTerm(1, primeField.buildElement().setValue(2).build().orElseThrow())
                        .addTerm(0, primeField.buildElement().setValue(2).build().orElseThrow())
                        .build()).build().orElseThrow(),
                new HashSet<>(List.of(
                        field.buildElement().setPolynomial(new Polynomial.PolynomialBuilder()
                                .addTerm(1, primeField.buildElement().setValue(1).build().orElseThrow())
                                .build()).build().orElseThrow(),
                        field.buildElement().setPolynomial(new Polynomial.PolynomialBuilder()
                                .addTerm(1, primeField.buildElement().setValue(2).build().orElseThrow())
                                .build()).build().orElseThrow()
                )));


        points.forEach((a, b) -> {
            ArrayList<? extends GeneralECPoint> actualPoints = ec.liftX(a);
            for (GeneralECPoint actualPoint : actualPoints) {
                RingElement y = actualPoint.getY();
                assertTrue(y instanceof ExtensionFieldElement);
                assertTrue(b.contains((ExtensionFieldElement) actualPoint.getY()));
            }

        });
    }
}
