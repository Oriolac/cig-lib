package cat.udl.cig.operations.wrapper;

import cat.udl.cig.ecc.ECPrimeOrderSubgroup;
import cat.udl.cig.ecc.GeneralEC;
import cat.udl.cig.ecc.GeneralECPoint;
import cat.udl.cig.fields.*;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PollardsLambdaTest {
/*
    static BigInteger MODULE = new BigInteger("7");
    static BigInteger n = new BigInteger("5");
    static BigInteger b = new BigInteger("1", 16);
    static BigInteger gx = new BigInteger("0", 16);
    static BigInteger gy = new BigInteger("6"
            .replaceAll("\\s", ""), 16);
    static RingElement[] COEF = new RingElement[2];
    static ArrayList<BigInteger> card = new ArrayList<>();
    static GeneralECPoint gen;

    @Test
    void algorithm() {
        Group g = new IntegerPrimeOrderSubgroup(BigInteger.valueOf(11), BigInteger.valueOf(10), BigInteger.valueOf(7));
        GroupElement alpha = g.toElement(BigInteger.valueOf(7));
        BigInteger x = BigInteger.valueOf(4);
        GroupElement beta = alpha.pow(x);
        System.out.println("Alpha: " + alpha.getIntValue().toString() + "   x: " + x.toString() + "    beta: " + beta.getIntValue().toString());
        assertEquals(x, PollardsLambda.algorithm(alpha, beta));
    }

    @Test
    void algorithm_ecc() {
        PrimeField ring = new PrimeField(MODULE);

        COEF[0] = new PrimeFieldElement(ring, BigInteger.valueOf(1));
        COEF[1] = new PrimeFieldElement(ring, b);
        card.add(n);
        GeneralEC curve = new GeneralEC(ring, COEF, card);
        gen = new GeneralECPoint(curve, new PrimeFieldElement(ring, gx), new PrimeFieldElement(ring, gy));
        ECPrimeOrderSubgroup g = new ECPrimeOrderSubgroup(curve, n, gen);
        GeneralECPoint alpha = g.getGenerator();
        BigInteger x = g.getRandomExponent();
        GeneralECPoint beta = alpha.pow(x);
        System.out.println("Alpha: " + alpha.toString() + "   x: " + x.toString() + "    beta: " + beta.toString());
        assertEquals(x, PollardsLambda.algorithm(alpha, beta));
    }*/
}