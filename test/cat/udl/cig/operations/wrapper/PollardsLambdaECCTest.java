package cat.udl.cig.operations.wrapper;

import cat.udl.cig.ecc.ECPrimeOrderSubgroup;
import cat.udl.cig.ecc.GeneralEC;
import cat.udl.cig.ecc.GeneralECPoint;
import cat.udl.cig.fields.PrimeField;
import cat.udl.cig.fields.PrimeFieldElement;
import cat.udl.cig.fields.RingElement;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PollardsLambdaECCTest {

    static BigInteger MODULE = new BigInteger("1259");
    static BigInteger MODULE_ECC = new BigInteger("11");
    static BigInteger n = new BigInteger("14");
    static BigInteger b = new BigInteger("1", 16);
    static BigInteger gx = new BigInteger("1", 16);
    static BigInteger gy = new BigInteger("6"
            .replaceAll("\\s", ""), 16);
    static RingElement[] COEF = new RingElement[2];
    static ArrayList<BigInteger> card = new ArrayList<>();
    static GeneralECPoint gen;

    @Test
    void algorithm_ecc() {
        PrimeField ring = new PrimeField(MODULE_ECC);

        COEF[0] = new PrimeFieldElement(ring, BigInteger.valueOf(1));
        COEF[1] = new PrimeFieldElement(ring, b);
        card.add(n);
        GeneralEC curve = new GeneralEC(ring, COEF, card);
        gen = new GeneralECPoint(curve, new PrimeFieldElement(ring, gx), new PrimeFieldElement(ring, gy));
        ECPrimeOrderSubgroup g = new ECPrimeOrderSubgroup(curve, n, gen);
        GeneralECPoint alpha = g.getGenerator();
        assertTrue(curve.isOnCurve(alpha));
        for(int xi = 0; xi < g.getSize().intValue(); xi++) {
            BigInteger x = BigInteger.valueOf(xi);
            GeneralECPoint beta = alpha.pow(x);
            PollardsLambda lambda = new PollardsLambda(alpha, beta);
            Optional<BigInteger> res = lambda.algorithm();
            assertEquals(Optional.of(x), res);
        }
    }
}
