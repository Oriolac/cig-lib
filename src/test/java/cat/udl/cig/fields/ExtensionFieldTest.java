package cat.udl.cig.fields;

import cat.udl.cig.structures.*;
import cat.udl.cig.utils.Polynomial;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        ArrayList<PrimeFieldElement> coefficients = new ArrayList<>(5);
        coefficients.add(new PrimeFieldElement(primeField, BigInteger.valueOf(2)));
        coefficients.add(neuter);
        coefficients.add(neuter);
        coefficients.add(new PrimeFieldElement(primeField, BigInteger.valueOf(2)));
        coefficients.add(new PrimeFieldElement(primeField, BigInteger.valueOf(1)));
        Polynomial polynomial = new Polynomial(coefficients);
        extensionField = new ExtensionField(p, m.intValue(), polynomial);
        ring = extensionField;
        setUpOperandA(primeField, extensionField);
        setUpOperandB(primeField, extensionField);
        power = BigInteger.valueOf(3);
    }

    private void setUpOperandA(PrimeField field, ExtensionField extensionField) {
        ArrayList<PrimeFieldElement> coefficients = new ArrayList<>(5);
        coefficients.add(new PrimeFieldElement(field, BigInteger.valueOf(2)));
        coefficients.add(new PrimeFieldElement(field, BigInteger.valueOf(1)));
        coefficients.add(neuter);
        coefficients.add(new PrimeFieldElement(field, BigInteger.valueOf(2)));
        Polynomial polynomialA = new Polynomial(coefficients);
        operandA = new ExtensionFieldElement(extensionField, polynomialA);
    }

    private void setUpOperandB(PrimeField field, ExtensionField extensionField) {
        ArrayList<PrimeFieldElement> coefficients = new ArrayList<>(5);
        coefficients.add(new PrimeFieldElement(field, BigInteger.valueOf(1)));
        coefficients.add(neuter);
        coefficients.add(new PrimeFieldElement(field, BigInteger.valueOf(2)));
        coefficients.add(new PrimeFieldElement(field, BigInteger.valueOf(1)));
        Polynomial polynomialA = new Polynomial(coefficients);
        operandB = new ExtensionFieldElement(extensionField, polynomialA);
    }

    @Test
    public void additiveTest() {
        ArrayList<PrimeFieldElement> coefficients = new ArrayList<>(5);
        coefficients.add(neuter);
        coefficients.add(new PrimeFieldElement(primeField, BigInteger.valueOf(1)));
        coefficients.add(new PrimeFieldElement(primeField, BigInteger.valueOf(2)));
        Polynomial polynomial = new Polynomial(coefficients);
        ExtensionFieldElement expected = new ExtensionFieldElement(extensionField, polynomial);
        assertEquals(expected, operandA.add(operandB));
    }

    @Test
    public void multiplicativeTest() {
        ArrayList<PrimeFieldElement> coefficients = new ArrayList<>(5);
        coefficients.add(neuter);
        coefficients.add(new PrimeFieldElement(primeField, BigInteger.valueOf(1)));
        coefficients.add(neuter);
        coefficients.add(new PrimeFieldElement(primeField, BigInteger.valueOf(1)));
        Polynomial polynomial = new Polynomial(coefficients);
        ExtensionFieldElement expected = new ExtensionFieldElement(extensionField, polynomial);
        assertEquals(expected, operandA.multiply(operandB));
    }
}