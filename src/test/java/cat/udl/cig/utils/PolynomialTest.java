package cat.udl.cig.utils;

import cat.udl.cig.operations.wrapper.data.Pair;
import cat.udl.cig.structures.PrimeField;
import cat.udl.cig.structures.PrimeFieldElement;
import cat.udl.cig.structures.builder.PrimeFieldElementBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PolynomialTest {

    Polynomial polynomial;
    Polynomial.PolynomialBuilder builder;
    PrimeField field;
    private PrimeFieldElementBuilder primeBuilder;

    @BeforeEach
    void setUp() {
        field = new PrimeField(BigInteger.valueOf(11L));
        builder = new Polynomial.PolynomialBuilder()
                .addTerm(2, new PrimeFieldElement(field, BigInteger.valueOf(1)));
        polynomial = builder.build();
        primeBuilder = field.buildElement();
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
        assertEquals((11 - 1) / 2, quadraticNonResidues.size());
    }

    @Test
    void testPolynomial1() {
        Polynomial zero = new Polynomial.PolynomialBuilder().setField(field).build();
        for (int i = 0; i < 3; i++) {
            for (int el = 0; el < 11; el++) {
                for (int el2 = 0; el2 < 11; el2++) {
                    for (int el3 = 0; el2 < 11; el2++) {
                        Polynomial polynomial = new Polynomial.PolynomialBuilder().addTerm(0, primeBuilder.setValue(el).build().orElseThrow())
                                .addTerm(1, primeBuilder.setValue(el2).build().orElseThrow())
                                .addTerm(2, primeBuilder.setValue(el3).build().orElseThrow())
                                .build();
                        assertEquals(zero, polynomial.euclideanMultiplication(zero), "Multiplication : (" + polynomial + ") * (" + zero + ")");
                        assertEquals(polynomial, polynomial.add(zero), "Addition : (" + polynomial + ") * (" + zero + ")");
                    }
                }
            }
        }
    }

    @Test
    void testEqualsZEROPolynomial() {
        Polynomial zero = new Polynomial.PolynomialBuilder().setField(field).build();
        Polynomial zero2 = new Polynomial.PolynomialBuilder().addTerm(0, primeBuilder.setValue(0).build().orElseThrow()).build();
        assertEquals(zero, zero2);
    }

    @Test
    void testEuclideanDivison() {
        Polynomial zero = new Polynomial.PolynomialBuilder().addTerm(0, primeBuilder.setValue(0).build().orElseThrow()).build();
        Polynomial one = new Polynomial.PolynomialBuilder().addTerm(0, primeBuilder.setValue(1).build().orElseThrow()).build();
        Polynomial opA = new Polynomial.PolynomialBuilder()
                .addTerm(0, primeBuilder.setValue(3).build().orElseThrow())
                .addTerm(1, primeBuilder.setValue(5).build().orElseThrow())
                .build();
        Polynomial opB = new Polynomial.PolynomialBuilder()
                .addTerm(0, primeBuilder.setValue(3).build().orElseThrow())
                .addTerm(1, primeBuilder.setValue(5).build().orElseThrow())
                .build();
        Pair<Polynomial, Polynomial> actual = opA.euclideanDivision(opB, field);
        assertEquals(one, actual.getKey());
        assertEquals(zero, actual.getValue());
    }

    @Test
    void testEuclideanMultiplication() {
        Polynomial opA = new Polynomial.PolynomialBuilder()
                .addTerm(0, primeBuilder.setValue(3).build().orElseThrow())
                .addTerm(1, primeBuilder.setValue(5).build().orElseThrow())
                .build();
        Polynomial opB = new Polynomial.PolynomialBuilder()
                .addTerm(0, primeBuilder.setValue(3).build().orElseThrow())
                .addTerm(1, primeBuilder.setValue(5).build().orElseThrow())
                .build();
        Polynomial expected = new Polynomial.PolynomialBuilder()
                .addTerm(0, primeBuilder.setValue(9).build().orElseThrow())
                .addTerm(1, primeBuilder.setValue(8).build().orElseThrow())
                .addTerm(2, primeBuilder.setValue(3).build().orElseThrow())
                .build();
        assertEquals(expected, opA.euclideanMultiplication(opB));
    }

    @Test
    void testHyperEuclideanMultiplication() {
        Polynomial opA = new Polynomial.PolynomialBuilder()
                .addTerm(5, primeBuilder.setValue(7).build().orElseThrow())
                .addTerm(4, primeBuilder.setValue(3).build().orElseThrow())
                .addTerm(3, primeBuilder.setValue(3).build().orElseThrow())
                .addTerm(2, primeBuilder.setValue(6).build().orElseThrow())
                .addTerm(1, primeBuilder.setValue(5).build().orElseThrow())
                .addTerm(0, primeBuilder.setValue(9).build().orElseThrow())
                .build();
        Polynomial opB = new Polynomial.PolynomialBuilder()
                .addTerm(4, primeBuilder.setValue(2).build().orElseThrow())
                .addTerm(3, primeBuilder.setValue(1).build().orElseThrow())
                .addTerm(2, primeBuilder.setValue(10).build().orElseThrow())
                .addTerm(1, primeBuilder.setValue(4).build().orElseThrow())
                .addTerm(0, primeBuilder.setValue(3).build().orElseThrow())
                .build();
        Polynomial expected = new Polynomial.PolynomialBuilder()
                .addTerm(9, primeBuilder.setValue(3).build().orElseThrow())
                .addTerm(8, primeBuilder.setValue(2).build().orElseThrow())
                .addTerm(7, primeBuilder.setValue(2).build().orElseThrow())
                .addTerm(6, primeBuilder.setValue(7).build().orElseThrow())
                .addTerm(5, primeBuilder.setValue(2).build().orElseThrow())
                .addTerm(4, primeBuilder.setValue(5).build().orElseThrow())
                .addTerm(3, primeBuilder.setValue(4).build().orElseThrow())
                .addTerm(2, primeBuilder.setValue(7).build().orElseThrow())
                .addTerm(1, primeBuilder.setValue(7).build().orElseThrow())
                .addTerm(0, primeBuilder.setValue(5).build().orElseThrow())
                .build();
        assertEquals(expected, opA.euclideanMultiplication(opB));
    }

    @Test
    void testSquaresRoots() {
        field = new PrimeField(BigInteger.valueOf(3));

    }

}