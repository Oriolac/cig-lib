package cat.udl.cig.operations.wrapper;

import cat.udl.cig.ecc.ECPrimeOrderSubgroup;
import cat.udl.cig.ecc.GeneralEC;
import cat.udl.cig.ecc.GeneralECPoint;
import cat.udl.cig.fields.PrimeField;
import cat.udl.cig.fields.PrimeFieldElement;
import cat.udl.cig.fields.RingElement;
import com.moandjiezana.toml.Toml;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
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

    @BeforeEach
    void start(){
        File file = new File("p192.toml");
        Toml toml = new Toml().read(file);
        MODULE_ECC = new BigInteger(toml.getString("p"));
        n = new BigInteger(toml.getString("n"));
        b = new BigInteger(toml.getString("b").replaceAll("\\s", ""), 16);
        gx = new BigInteger(toml.getString("gx")
                .replaceAll("\\s", ""), 16);
        gy = new BigInteger(toml.getString("gy")
                .replaceAll("\\s", ""), 16);
    }

    @Test
    void algorithm_ecc() {
        PrimeField ring = new PrimeField(MODULE_ECC);

        COEF[0] = new PrimeFieldElement(ring, BigInteger.valueOf(-3));
        COEF[1] = new PrimeFieldElement(ring, b);
        card.add(n);
        GeneralEC curve = new GeneralEC(ring, COEF, card);
        gen = new GeneralECPoint(curve, new PrimeFieldElement(ring, gx), new PrimeFieldElement(ring, gy));
        ECPrimeOrderSubgroup g = new ECPrimeOrderSubgroup(curve, n, gen);
        GeneralECPoint alpha = g.getGenerator();
        //TODO: eliminar 2^20
        for(BigInteger xi = BigInteger.TEN.pow(4); xi.compareTo(BigInteger.TEN.pow(4).multiply(BigInteger.TWO)) < 0; xi = xi.add(BigInteger.ONE)) {
            GeneralECPoint beta = alpha.pow(xi);
            PollardsLambda lambda = new PollardsLambda(alpha, beta);
            Optional<BigInteger> res = lambda.algorithm();
            assertEquals(Optional.of(xi), res);
        }
    }
}
