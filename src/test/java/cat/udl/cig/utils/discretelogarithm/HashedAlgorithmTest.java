package cat.udl.cig.utils.discretelogarithm;

import cat.udl.cig.structures.GroupElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HashedAlgorithmTest {


    private ArrayList<BigInteger> expectedPowers;
    private ArrayList<GroupElement> betas;
    private ArrayList<GroupElement> gens;
    private HashMap<Integer, Supplier<LogarithmAlgorithm>> algorithms;

    @BeforeEach
    void setUp() {
        this.expectedPowers = returnExpectedPower();
        this.betas = returnBeta();
        this.gens = returnGenerator();
        this.algorithms = new HashMap<>();
        algorithms.put(0, () -> {
           HashedAlgorithm.loadHashedInstance(gens.get(0), gens.get(0).getGroup().getSize(), BigInteger.ONE);
           return HashedAlgorithm.getHashedInstance();
        });
        algorithms.put(1, () -> {
           HashedAlgorithm.loadHashedInstance(gens.get(1), gens.get(1).getGroup().getSize(), BigInteger.ONE);
           return HashedAlgorithm.getHashedInstance();
        });
    }

    @Test
    void testTrySomeManyPowers() {
        for (int j = 0; j < expectedPowers.size(); j++) {
            GroupElement generator = gens.get(j);
            LogarithmAlgorithm logarithm = algorithms.get(j).get();
            for (int i = 1; i < 100; i++) {
                GroupElement beta = generator.pow(BigInteger.valueOf(i));
                Optional<BigInteger> actual = logarithm.algorithm(beta);
                assertTrue(actual.isPresent(), "It has failed at " + i);
                assertEquals(BigInteger.valueOf(i), actual.get(), "It has failed at " + i);
            }
        }
    }

    @Test
    void testAlgorithm() {
        for (int j = 0; j < expectedPowers.size(); j++) {
            GroupElement beta = betas.get(j);
            LogarithmAlgorithm logarithm = algorithms.get(j).get();
            Optional<BigInteger> power = logarithm.algorithm(beta);
            assertTrue(power.isPresent(), "Power not found");
            assertEquals(expectedPowers.get(j), power.get());
        }
    }

    static protected ArrayList<GroupElement> returnGenerator() {
        return new ArrayList<>(List.of(
                new LogarithmAlgorithmTest.PrimeFieldGenerator().returnGenerator(),
                new LogarithmAlgorithmTest.EllipticCurvePrimeFieldGenerator().returnGenerator()
        ));
    }

    static protected ArrayList<GroupElement> returnBeta() {
        return new ArrayList<>(List.of(
                new LogarithmAlgorithmTest.PrimeFieldGenerator().returnBeta(),
                new LogarithmAlgorithmTest.EllipticCurvePrimeFieldGenerator().returnBeta()
        ));
    }

    static protected ArrayList<BigInteger> returnExpectedPower() {
        return new ArrayList<>(List.of(
                new LogarithmAlgorithmTest.PrimeFieldGenerator().returnExpectedPower(),
                new LogarithmAlgorithmTest.EllipticCurvePrimeFieldGenerator().returnExpectedPower()
        ));
    }
}
