package cat.udl.cig.structures;

import cat.udl.cig.utils.Polynomial;

import java.math.BigInteger;
import java.util.ArrayList;

public class ExtensionFieldPow2ElementTest extends RingElementTemplateTest {

    ExtensionField extensionField;
    BigInteger prime;
    PrimeField primeField;

    @Override
    protected Ring returnRing() {
        prime = BigInteger.valueOf(11);
        primeField = new PrimeField(prime);
        extensionField = ExtensionField.ExtensionFieldP2(prime);
        return extensionField;
    }

    @Override
    protected RingElement returnOperand1() {
        Polynomial polynomial = new Polynomial.PolynomialBuilder()
                .addTerm(0, primeField.buildElement().setValue(7).build().orElseThrow())
                .addTerm(1, primeField.buildElement().setValue(1).build().orElseThrow())
                .build();
        return extensionField.buildElement().setPolynomial(polynomial).build().orElseThrow();
    }

    @Override
    protected RingElement returnOperand2() {
        Polynomial polynomial = new Polynomial.PolynomialBuilder()
                .addTerm(0, primeField.buildElement().setValue(2).build().orElseThrow())
                .addTerm(1, primeField.buildElement().setValue(5).build().orElseThrow())
                .build();
        return extensionField.buildElement().setPolynomial(polynomial).build().orElseThrow();
    }

    @Override
    protected RingElement returnResultAddition() {
        Polynomial polynomial = new Polynomial.PolynomialBuilder()
                .addTerm(0, primeField.buildElement().setValue(9).build().orElseThrow())
                .addTerm(1, primeField.buildElement().setValue(6).build().orElseThrow())
                .build();
        return extensionField.buildElement().setPolynomial(polynomial).build().orElseThrow();
    }

    @Override
    protected RingElement returnResultSubtraction() {
        Polynomial polynomial = new Polynomial.PolynomialBuilder()
                .addTerm(0, primeField.buildElement().setValue(5).build().orElseThrow())
                .addTerm(1, primeField.buildElement().setValue(7).build().orElseThrow())
                .build();
        return extensionField.buildElement().setPolynomial(polynomial).build().orElseThrow();
    }

    @Override
    protected RingElement returnExpectedOpposite1() {
        Polynomial polynomial = new Polynomial.PolynomialBuilder()
                .addTerm(0, primeField.buildElement().setValue(4).build().orElseThrow())
                .addTerm(1, primeField.buildElement().setValue(10).build().orElseThrow())
                .build();
        return extensionField.buildElement().setPolynomial(polynomial).build().orElseThrow();
    }

    @Override
    protected RingElement returnExpectedInverse1() {
        Polynomial polynomial = new Polynomial.PolynomialBuilder()
                .addTerm(0, primeField.buildElement().setValue(3).build().orElseThrow())
                .addTerm(1, primeField.buildElement().setValue(9).build().orElseThrow())
                .build();
        return extensionField.buildElement().setPolynomial(polynomial).build().orElseThrow();
    }

    @Override
    protected BigInteger returnPower() {
        return BigInteger.valueOf(6);
    }

    @Override
    protected RingElement returnExpectedPower() {
        Polynomial polynomial = new Polynomial.PolynomialBuilder()
                .addTerm(1, primeField.buildElement().setValue(7).build().orElseThrow())
                .build();
        return extensionField.buildElement().setPolynomial(polynomial).build().orElseThrow();
    }

    @Override
    protected ArrayList<RingElement> returnSquaresRootOfOp1() {
        return new ArrayList<>();
    }

    @Override
    protected RingElement expectedResultMultiplication() {
        Polynomial polynomial = new Polynomial.PolynomialBuilder()
                .addTerm(0, primeField.buildElement().setValue(9).build().orElseThrow())
                .addTerm(1, primeField.buildElement().setValue(4).build().orElseThrow())
                .build();
        return extensionField.buildElement().setPolynomial(polynomial).build().orElseThrow();
    }

    @Override
    protected RingElement expectedResultDivision() {
        Polynomial polynomial = new Polynomial.PolynomialBuilder()
                .addTerm(0, primeField.buildElement().setValue(9).build().orElseThrow())
                .build();
        return extensionField.buildElement().setPolynomial(polynomial).build().orElseThrow();

    }
}
