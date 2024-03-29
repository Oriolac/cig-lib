package cat.udl.cig.structures;

import cat.udl.cig.utils.Polynomial;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ExtensionFieldTest extends RingTemplateTest {

    BigInteger p = BigInteger.valueOf(3L);
    BigInteger m = BigInteger.valueOf(4L);
    PrimeField primeField;
    PrimeFieldElement neuter;
    ExtensionField extensionField;


    @Override
    public void setUpRing() {
        primeField = new PrimeField(p);
        neuter = primeField.getAdditiveIdentity();
        Polynomial.PolynomialBuilder polyBuilder = new Polynomial.PolynomialBuilder()
                .addTerm(0, new PrimeFieldElement(primeField, BigInteger.valueOf(2)))
                .addTerm(3, new PrimeFieldElement(primeField, BigInteger.valueOf(2)))
                .addTerm(4, new PrimeFieldElement(primeField, BigInteger.valueOf(1)));
        extensionField = new ExtensionField(p, m.intValue(), polyBuilder.build());
        ring = extensionField;
        operandA = setUpOperandA(primeField, extensionField);
        operandB = setUpOperandB(primeField, extensionField);
        power = BigInteger.valueOf(3);
    }

    private ExtensionFieldElement setUpOperandA(PrimeField field, ExtensionField extensionField) {
        Polynomial polynomialA = new Polynomial.PolynomialBuilder()
                .addTerm(0, new PrimeFieldElement(field, BigInteger.valueOf(2)))
                .addTerm(1, new PrimeFieldElement(field, BigInteger.valueOf(1)))
                .addTerm(3, new PrimeFieldElement(field, BigInteger.valueOf(2)))
                .build();
        return new ExtensionFieldElement(extensionField, polynomialA);
    }

    private ExtensionFieldElement setUpOperandB(PrimeField field, ExtensionField extensionField) {
        Polynomial polynomialA = new Polynomial.PolynomialBuilder()
                .addTerm(0, new PrimeFieldElement(field, BigInteger.ONE))
                .addTerm(2, new PrimeFieldElement(field, BigInteger.TWO))
                .addTerm(3, new PrimeFieldElement(field, BigInteger.ONE))
                .build();
        return new ExtensionFieldElement(extensionField, polynomialA);
    }

    @Test
    public void additiveTest() {
        Polynomial polynomial = new Polynomial.PolynomialBuilder()
                .addTerm(1, new PrimeFieldElement(primeField, BigInteger.ONE))
                .addTerm(2, new PrimeFieldElement(primeField, BigInteger.TWO))
                .build();
        ExtensionFieldElement expected = new ExtensionFieldElement(extensionField, polynomial);
        assertEquals(expected, operandA.add(operandB));
    }

    @Test
    public void multiplicativeTest() {
        Polynomial polynomial = new Polynomial.PolynomialBuilder()
                .addTerm(1, new PrimeFieldElement(primeField, BigInteger.ONE))
                .addTerm(3, new PrimeFieldElement(primeField, BigInteger.ONE))
                .build();
        ExtensionFieldElement expected = new ExtensionFieldElement(extensionField, polynomial);
        assertEquals(expected, operandA.multiply(operandB));
    }

    @Test
    public void irreduciblePolynomialCreationP2Test() {
        BigInteger p = BigInteger.valueOf(11);
        PrimeField primeField = new PrimeField(p);
        ExtensionField field = ExtensionField.ExtensionFieldP2(p);
        assertEquals(p.pow(2), field.getSize());
        Polynomial expected = new Polynomial.PolynomialBuilder()
                .addTerm(2, new PrimeFieldElement(primeField, BigInteger.ONE))
                .addTerm(0, new PrimeFieldElement(primeField, BigInteger.ONE))
                .build();
        assertEquals(expected, field.getReducingPolynomial());
    }

    @Test
    public void irreduciblePolynomialCreationP2TestWithMorePrimes() {
        int[] numbers = new int[]{3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41};
        for (int number : numbers) {
            PrimeField primeField = new PrimeField(BigInteger.valueOf(number));
            BigInteger p = BigInteger.valueOf(number);
            ExtensionField field = ExtensionField.ExtensionFieldP2(p);
            assertNotNull(field.getReducingPolynomial());
            Optional<PrimeFieldElement> optElement = primeField.buildElement().setValue(BigInteger.ONE).build();
            assertTrue(optElement.isPresent());
            assertEquals(optElement.get(), field.getReducingPolynomial().getCoefficient(2));
            System.out.println("P: " + number + "; Polynomial: " + field.getReducingPolynomial());
            if (BigInteger.valueOf(number).mod(BigInteger.valueOf(4)).equals(BigInteger.valueOf(3))) {
                assertEquals(field.getReducingPolynomial().getCoefficient(0), primeField.getMultiplicativeIdentity());
            }
        }
    }

    @Test
    public void irreduciblePolynomialOfLargePrime() {
        BigInteger prime = new BigInteger("26745071");
        ExtensionField field = ExtensionField.ExtensionFieldP2(prime);
        PrimeField primeField = new PrimeField(prime);
        Optional<PrimeFieldElement> optElement = primeField.buildElement().setValue(BigInteger.ONE).build();
        assertTrue(optElement.isPresent());
        assertEquals(optElement.get(), field.getReducingPolynomial().getCoefficient(2));
        System.out.println("Polynomial: " + field.getReducingPolynomial());
    }

    @Test
    public void squareRoots() {
        BigInteger p = BigInteger.valueOf(3);
        PrimeField primeField = new PrimeField(p);
        ExtensionField field = ExtensionField.ExtensionFieldP2(p);
        ExtensionFieldElement x = field.buildElement()
                .setPolynomial(new Polynomial.PolynomialBuilder()
                        .addTerm(0, primeField.getMultiplicativeIdentity())
                        .build())
                .build()
                .orElseThrow();
        ExtensionFieldElement y = field.buildElement()
                .setPolynomial(new Polynomial.PolynomialBuilder()
                        .addTerm(0, primeField.getMultiplicativeIdentity())
                        .addTerm(1, primeField.getMultiplicativeIdentity())
                        .build())
                .build()
                .orElseThrow();
        ArrayList<RingElement> roots = y.squareRoot();
        assertTrue(roots.size() > 0);
    }
}