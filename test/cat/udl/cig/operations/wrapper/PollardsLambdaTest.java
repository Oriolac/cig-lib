package cat.udl.cig.operations.wrapper;

import cat.udl.cig.ecc.ECPrimeOrderSubgroup;
import cat.udl.cig.ecc.GeneralEC;
import cat.udl.cig.ecc.GeneralECPoint;
import cat.udl.cig.fields.*;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class PollardsLambdaTest {

    static BigInteger MODULE = new BigInteger("11");
    static BigInteger n = new BigInteger("14");
    static BigInteger b = new BigInteger("1", 16);
    static BigInteger gx = new BigInteger("1", 16);
    static BigInteger gy = new BigInteger("5"
            .replaceAll("\\s", ""), 16);
    static RingElement[] COEF = new RingElement[2];
    static ArrayList<BigInteger> card = new ArrayList<>();
    static GeneralECPoint gen;

    @Test
    void algorithm() {
        IntegerPrimeOrderSubgroup g = new IntegerPrimeOrderSubgroup(MODULE, MODULE.subtract(BigInteger.ONE), BigInteger.valueOf(7));
        GroupElement alpha = g.getGenerator();
        BigInteger x = g.getRandomExponent();
        GroupElement beta = alpha.pow(x);
        System.out.println("Alpha: " + alpha.getIntValue().toString() + "   x: " + x.toString() + "    beta: " + beta.getIntValue().toString());
        PollardsLambda lambda = new PollardsLambda(alpha, beta);
        Optional<BigInteger> res = lambda.algorithm();
        assertEquals(Optional.of(x), res);//.map(y -> y.subtract(BigInteger.ONE)));
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
        assertTrue(curve.isOnCurve(alpha));
        for(int xi = 1; xi < g.getSize().intValue(); xi++) {
            BigInteger x = BigInteger.valueOf(xi);
            GeneralECPoint beta = alpha.pow(x);
            System.out.println("Alpha: " + alpha.toString() + "   x: " + x.toString() + "    beta: " + beta.toString());
            PollardsLambda lambda = new PollardsLambda(alpha, beta);
            Optional<BigInteger> res = lambda.algorithm();
            assertEquals(Optional.of(x), res);
        }
    }
}