package cat.udl.cig.utils.discretelogarithm;

import cat.udl.cig.structures.GroupElement;
import cat.udl.cig.structures.PrimeField;
import cat.udl.cig.structures.PrimeFieldElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.text.html.Option;
import java.math.BigInteger;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

abstract class LogarithmAlgorithmTest {

    LogarithmAlgorithm logarithmAlgorithm;
    GroupElement generator;
    GroupElement beta;
    BigInteger expectedPower;

    @BeforeEach
    void setUp() {
        logarithmAlgorithm = returnAlgorithm();
        generator = returnGenerator();
        assertEquals(generator, logarithmAlgorithm.getAlpha());
        beta = returnBeta();
        expectedPower = returnExpectedPower();
    }

    protected abstract LogarithmAlgorithm returnAlgorithm();

    protected abstract GroupElement returnGenerator();

    protected abstract GroupElement returnBeta();

    protected abstract BigInteger returnExpectedPower();

    @Test
    void testTrySomeManyPowers() {
        for (int i = 1; i < 100; i++) {
            GroupElement beta = generator.pow(BigInteger.valueOf(i));
            Optional<BigInteger> actual = logarithmAlgorithm.algorithm(beta);
            assertTrue(actual.isPresent(), "It has failed at " + i);
            assertEquals(BigInteger.valueOf(i), actual.get(), "It has failed at " + i);
        }
    }

    @Test
    void testAlgorithm() {
        Optional<BigInteger> power = logarithmAlgorithm.algorithm(beta);
        assertTrue(power.isPresent(), "Power not found");
        assertEquals(expectedPower, power.get());
    }

    static class PrimeFieldGenerator {

        static BigInteger P = BigInteger.valueOf(101);
        static PrimeField primeField = new PrimeField(P);

        static protected PrimeFieldElement returnGenerator() {
            return primeField.buildElement().setValue(7).build().orElseThrow();
        }

        static protected PrimeFieldElement returnBeta() {
            return primeField.buildElement().setValue(85).build().orElseThrow();
        }

        static protected BigInteger returnExpectedPower() {
            return BigInteger.valueOf(6);
        }

        static protected PrimeField returnField() {
            return primeField;
        }

    }
}