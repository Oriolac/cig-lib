package cat.udl.cig.structures.ecc;

import cat.udl.cig.structures.*;
import cat.udl.cig.structures.builder.ExtensionFieldElementBuilder;
import cat.udl.cig.utils.Polynomial;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExtensionFieldECTest extends GeneralECTest {
    GeneralEC curve;
    ExtensionField extensionField;
    BigInteger p = BigInteger.valueOf(2213);
    PrimeField primeField;

    @Override
    protected GeneralEC returnGeneralEC() {
        extensionField = ExtensionField.ExtensionFieldP2(p);
        Polynomial.PolynomialBuilder pBuilder = new Polynomial.PolynomialBuilder();
        primeField = new PrimeField(p);
        ExtensionFieldElementBuilder builder = extensionField.buildElement();
        ArrayList<BigInteger> cardFactor = new ArrayList<>(List.of(BigInteger.valueOf(306332)));
        curve = new GeneralEC(
                extensionField,
                builder.setPolynomial(
                        pBuilder
                                .addTerm(0, new PrimeFieldElement(primeField, BigInteger.valueOf(3)))
                                .build())
                        .build().orElseThrow(),
                builder.setPolynomial(
                        pBuilder
                                .addTerm(0, new PrimeFieldElement(primeField, BigInteger.valueOf(49)))
                                .build())
                        .build().orElseThrow(),
                cardFactor);
        return curve;
    }

    @Override
    protected Ring returnRingOfEC() {
        return curve.getRing();
    }

    @Override
    protected RingElement returnBadCoordinateX() {
        Polynomial.PolynomialBuilder pBuilder = new Polynomial.PolynomialBuilder();
        return extensionField.buildElement().setPolynomial(
                pBuilder.addTerm(1, primeField.getMultiplicativeIdentity())
                        .addTerm(0, primeField.buildElement().setValue(5).build().orElseThrow())
                        .build())
                .build().orElseThrow();
    }

    @Override
    protected GeneralECPoint returnGeneralECPoint1() {
        Polynomial.PolynomialBuilder pBuilder = new Polynomial.PolynomialBuilder();
        ExtensionFieldElement x = extensionField.buildElement().setPolynomial(
                pBuilder.addTerm(1, primeField.getMultiplicativeIdentity())
                        .build())
                .build().orElseThrow();
        ExtensionFieldElement y = extensionField.buildElement().setPolynomial(
                pBuilder.addTerm(1, primeField.buildElement().setValue(606).build().orElseThrow())
                        .addTerm(0, primeField.buildElement().setValue(451).build().orElseThrow())
                        .build())
                .build().orElseThrow();
        return new GeneralECPoint(curve, x, y);
    }

    @Override
    protected GeneralECPoint returnGeneralECPoint2() {
        GeneralECPoint point2 = returnGeneralECPoint1().pow(BigInteger.TWO);
        Polynomial.PolynomialBuilder pBuilder = new Polynomial.PolynomialBuilder();
        ExtensionFieldElement x = extensionField.buildElement().setPolynomial(
                pBuilder.addTerm(1, primeField.buildElement().setValue(1550).build().orElseThrow())
                        .addTerm(0, primeField.buildElement().setValue(1407).build().orElseThrow())
                        .build())
                .build().orElseThrow();
        ExtensionFieldElement y = extensionField.buildElement().setPolynomial(
                pBuilder.addTerm(1, primeField.buildElement().setValue(1415).build().orElseThrow())
                        .addTerm(0, primeField.buildElement().setValue(2210).build().orElseThrow())
                        .build())
                .build().orElseThrow();
        GeneralECPoint expected = new GeneralECPoint(curve, x, y);
        assertEquals(expected, point2);
        return point2;
    }

    @Override
    protected GeneralECPoint returnExpectedResultPlusOperation() {
        Polynomial.PolynomialBuilder pBuilder = new Polynomial.PolynomialBuilder();
        ExtensionFieldElement x = extensionField.buildElement().setPolynomial(
                pBuilder.addTerm(1, primeField.buildElement().setValue(1667).build().orElseThrow())
                        .addTerm(0, primeField.buildElement().setValue(1802).build().orElseThrow())
                        .build())
                .build().orElseThrow();
        ExtensionFieldElement y = extensionField.buildElement().setPolynomial(
                pBuilder.addTerm(1, primeField.buildElement().setValue(305).build().orElseThrow())
                        .addTerm(0, primeField.buildElement().setValue(1166).build().orElseThrow())
                        .build())
                .build().orElseThrow();
        return new GeneralECPoint(curve, x, y);
    }

    @Override
    protected GeneralECPoint returnsExpectedElementMultByTHREE() {
        Polynomial.PolynomialBuilder pBuilder = new Polynomial.PolynomialBuilder();
        ExtensionFieldElement x = extensionField.buildElement().setPolynomial(
                pBuilder.addTerm(1, primeField.buildElement().setValue(1667).build().orElseThrow())
                        .addTerm(0, primeField.buildElement().setValue(1802).build().orElseThrow())
                        .build())
                .build().orElseThrow();
        ExtensionFieldElement y = extensionField.buildElement().setPolynomial(
                pBuilder.addTerm(1, primeField.buildElement().setValue(305).build().orElseThrow())
                        .addTerm(0, primeField.buildElement().setValue(1166).build().orElseThrow())
                        .build())
                .build().orElseThrow();
        return new GeneralECPoint(curve, x, y);
    }

    @Override
    protected BigInteger returnExpectedOrderOfPoint1() {
        return BigInteger.valueOf(306332);
    }


    @Test
    void testPolynomial() {
        Polynomial polynomial = new Polynomial.PolynomialBuilder()
                .addTerm(2, primeField.buildElement().setValue(1).build().orElseThrow())
                .addTerm(0, primeField.buildElement().setValue(2).build().orElseThrow())
                .build();
        assertEquals(polynomial, this.extensionField.getReducingPolynomial());
    }

}
