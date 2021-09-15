package cat.udl.cig.utils.discretelogarithm;

import cat.udl.cig.structures.GroupElement;
import cat.udl.cig.structures.PrimeField;
import cat.udl.cig.structures.PrimeFieldElement;
import cat.udl.cig.structures.builder.PrimeFieldElementBuilder;
import cat.udl.cig.structures.ecc.GeneralEC;
import cat.udl.cig.structures.ecc.GeneralECPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

abstract class LogarithmAlgorithmTest {

    ArrayList<LogarithmAlgorithm> logarithmAlgorithm;
    ArrayList<GroupElement> generators;
    ArrayList<GroupElement> betas;
    ArrayList<BigInteger> expectedPowers;

    @BeforeEach
    void setUp() {
        logarithmAlgorithm = returnAlgorithm();
        generators = returnGenerator();
        assertEquals(generators, logarithmAlgorithm.stream().map(LogarithmAlgorithm::getAlpha).collect(Collectors.toCollection(ArrayList::new)));
        betas = returnBeta();
        expectedPowers = returnExpectedPower();
    }

    protected abstract ArrayList<LogarithmAlgorithm> returnAlgorithm();

    static protected ArrayList<GroupElement> returnGenerator() {
        return new ArrayList<>(List.of(
                new PrimeFieldGenerator().returnGenerator(),
                new EllipticCurvePrimeFieldGenerator().returnGenerator()
        ));
    }

    static protected ArrayList<GroupElement> returnBeta() {
        return new ArrayList<>(List.of(
                new PrimeFieldGenerator().returnBeta(),
                new EllipticCurvePrimeFieldGenerator().returnBeta()
        ));
    }

    static protected ArrayList<BigInteger> returnExpectedPower() {
        return new ArrayList<>(List.of(
                new PrimeFieldGenerator().returnExpectedPower(),
                new EllipticCurvePrimeFieldGenerator().returnExpectedPower()
        ));
    }

    int getNumTries() {
        return 100;
    }

    @Test
    void testTrySomeManyPowers() {
        for (int j = 0; j < expectedPowers.size(); j++) {
            GroupElement generator = generators.get(j);
            LogarithmAlgorithm logarithm = logarithmAlgorithm.get(j);
            for (int i = 1; i < getNumTries(); i++) {
                GroupElement beta = generator.pow(BigInteger.valueOf(i));
                Optional<BigInteger> actual = logarithm.algorithm(beta);
                assertTrue(actual.isPresent(), "It has failed at " + i);
                assertEquals(BigInteger.valueOf(i), actual.get(), "It has failed at " + logarithm.getAlpha() + " ** " + i);
            }
        }
    }

    @Test
    void testAlgorithm() {
        for (int j = 0; j < expectedPowers.size(); j++) {
            GroupElement beta = betas.get(j);
            LogarithmAlgorithm logarithm = logarithmAlgorithm.get(j);
            Optional<BigInteger> power = logarithm.algorithm(beta);
            assertTrue(power.isPresent(), "Power not found");
            assertEquals(expectedPowers.get(j), power.get(), "Failed at " + beta);
        }
    }

    static interface FieldGenerator {
        GroupElement returnGenerator();

        GroupElement returnBeta();

        BigInteger returnExpectedPower();
    }

    static class PrimeFieldGenerator implements FieldGenerator {

        static BigInteger P = BigInteger.valueOf(101);
        static PrimeField primeField = new PrimeField(P);

        @Override
        public PrimeFieldElement returnGenerator() {
            return primeField.buildElement().setValue(7).build().orElseThrow();
        }

        @Override
        public PrimeFieldElement returnBeta() {
            return primeField.buildElement().setValue(85).build().orElseThrow();
        }

        @Override
        public BigInteger returnExpectedPower() {
            return BigInteger.valueOf(6);
        }

    }

    static class EllipticCurvePrimeFieldGenerator implements FieldGenerator {

        static PrimeField primeField = new PrimeField(BigInteger.valueOf(2213));
        static PrimeFieldElementBuilder builder = primeField.buildElement();
        static ArrayList<BigInteger> cardFactor = new ArrayList<>(List.of(BigInteger.valueOf(1093)));
        static GeneralEC curve = new GeneralEC(
                primeField,
                builder.setValue(1).build().orElseThrow(),
                builder.setValue(49).build().orElseThrow(),
                cardFactor);

        public GeneralECPoint returnGenerator() {
            return new GeneralECPoint(curve, builder.setValue(1583).build().orElseThrow(), builder.setValue(1734).build().orElseThrow());
        }

        public GeneralECPoint returnBeta() {
            return new GeneralECPoint(curve, builder.setValue(165).build().orElseThrow(), builder.setValue(917).build().orElseThrow());
        }

        public BigInteger returnExpectedPower() {
            return BigInteger.valueOf(30);
        }

    }
}