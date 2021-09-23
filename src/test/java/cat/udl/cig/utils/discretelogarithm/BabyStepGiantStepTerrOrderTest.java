package cat.udl.cig.utils.discretelogarithm;

import cat.udl.cig.structures.ExtensionField;
import cat.udl.cig.structures.ExtensionFieldElement;
import cat.udl.cig.structures.PrimeField;
import cat.udl.cig.structures.ecc.EllipticCurve;
import cat.udl.cig.structures.ecc.EllipticCurvePoint;
import cat.udl.cig.utils.Polynomial;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.ArrayList;

public class BabyStepGiantStepTerrOrderTest {

    private BigInteger p;
    private PrimeField primeField;
    private ExtensionField field;
    private ExtensionFieldElement A;
    private ExtensionFieldElement B;
    private EllipticCurve ec;

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
    void testGetSuborderGroup() {
        ExtensionFieldElement xCoord = field.buildElement().setPolynomial(new Polynomial.PolynomialBuilder()
                .addTerm(1, primeField.buildElement().setValue(2).build().orElseThrow())
                .addTerm(0, primeField.buildElement().setValue(1).build().orElseThrow())
                .build()).build().orElseThrow();
        ArrayList<? extends EllipticCurvePoint> point = ec.liftX(xCoord);
        point.get(0).getOrder();
    }

}
