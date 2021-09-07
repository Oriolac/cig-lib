package cat.udl.cig.structures;

import cat.udl.cig.utils.Polynomial;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.ArrayList;
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
        setUpOperandA(primeField, extensionField);
        setUpOperandB(primeField, extensionField);
        power = BigInteger.valueOf(3);
    }

    private void setUpOperandA(PrimeField field, ExtensionField extensionField) {
        Polynomial polynomialA = new Polynomial.PolynomialBuilder()
                .addTerm(0, new PrimeFieldElement(field, BigInteger.valueOf(2)))
                .addTerm(1, new PrimeFieldElement(field, BigInteger.valueOf(1)))
                .addTerm(3, new PrimeFieldElement(field, BigInteger.valueOf(2))).build();
        operandA = new ExtensionFieldElement(extensionField, polynomialA);
    }

    private void setUpOperandB(PrimeField field, ExtensionField extensionField) {
        Polynomial polynomialA = new Polynomial.PolynomialBuilder()
                .addTerm(0, new PrimeFieldElement(field, BigInteger.ONE))
                .addTerm(2, new PrimeFieldElement(field, BigInteger.TWO))
                .addTerm(3, new PrimeFieldElement(field, BigInteger.ONE))
                .build();
        ArrayList<PrimeFieldElement> coefficients = new ArrayList<>(5);
        operandB = new ExtensionFieldElement(extensionField, polynomialA);
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
                .addTerm(0, new PrimeFieldElement(primeField, BigInteger.TWO))
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
            Optional<PrimeFieldElement> optElement = primeField.buildElement().setValue(BigInteger.ONE).buildElement();
            assertTrue(optElement.isPresent());
            assertEquals(optElement.get(), field.getReducingPolynomial().getCoefficient(2));
            System.out.println("P: " + number + "; Polynomial: " + field.getReducingPolynomial());
        }
    }

    @Test
    public void irreduciblePolynomialOfLargePrime() {
        BigInteger prime = new BigInteger("26745071");
        ExtensionField field = ExtensionField.ExtensionFieldP2(prime);
        PrimeField primeField = new PrimeField(prime);
        Optional<PrimeFieldElement> optElement = primeField.buildElement().setValue(BigInteger.ONE).buildElement();
        assertTrue(optElement.isPresent());
        assertEquals(optElement.get(), field.getReducingPolynomial().getCoefficient(2));
        System.out.println("Polynomial: " + field.getReducingPolynomial());
    }
}