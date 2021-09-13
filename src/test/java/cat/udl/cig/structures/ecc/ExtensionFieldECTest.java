package cat.udl.cig.structures.ecc;

import cat.udl.cig.structures.*;
import cat.udl.cig.structures.builder.ExtensionFieldElementBuilder;
import cat.udl.cig.utils.Polynomial;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

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
        ArrayList<BigInteger> cardFactor = new ArrayList<>(List.of(BigInteger.valueOf(4901312)));
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
                        .addTerm(0, primeField.getMultiplicativeIdentity())
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
                pBuilder.addTerm(1, primeField.buildElement().setValue(1607).build().orElseThrow())
                        .addTerm(0, primeField.buildElement().setValue(1762).build().orElseThrow())
                        .build())
                .build().orElseThrow();
        return new GeneralECPoint(curve, x, y);
    }

    @Override
    protected GeneralECPoint returnGeneralECPoint2() {
        Polynomial.PolynomialBuilder pBuilder = new Polynomial.PolynomialBuilder();
        ExtensionFieldElement x = extensionField.buildElement().setPolynomial(pBuilder.build())
                .build().orElseThrow();
        ExtensionFieldElement y = extensionField.buildElement().setPolynomial(
                pBuilder.addTerm(0, primeField.buildElement().setValue(2206).build().orElseThrow())
                        .build())
                .build().orElseThrow();
        return new GeneralECPoint(curve, x, y);
    }

    @Override
    protected GeneralECPoint returnExpectedResultPlusOperation() {
        Polynomial.PolynomialBuilder pBuilder = new Polynomial.PolynomialBuilder();
        ExtensionFieldElement x = extensionField.buildElement().setPolynomial(
                pBuilder.addTerm(1, primeField.buildElement().setValue(921).build().orElseThrow())
                        .addTerm(0, primeField.buildElement().setValue(895).build().orElseThrow())
                        .build())
                .build().orElseThrow();
        ExtensionFieldElement y = extensionField.buildElement().setPolynomial(
                pBuilder.addTerm(1, primeField.buildElement().setValue(930).build().orElseThrow())
                        .addTerm(0, primeField.buildElement().setValue(1924).build().orElseThrow())
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
                pBuilder.addTerm(1, primeField.buildElement().setValue(1908).build().orElseThrow())
                        .addTerm(0, primeField.buildElement().setValue(1047).build().orElseThrow())
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
        System.out.println(extensionField.getReducingPolynomial());
    }
}
