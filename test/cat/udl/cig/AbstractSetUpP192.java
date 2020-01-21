package cat.udl.cig;

import cat.udl.cig.ecc.ECPrimeOrderSubgroup;
import cat.udl.cig.ecc.GeneralEC;
import cat.udl.cig.ecc.GeneralECPoint;
import cat.udl.cig.fields.PrimeField;
import cat.udl.cig.fields.PrimeFieldElement;
import cat.udl.cig.fields.RingElement;
import com.moandjiezana.toml.Toml;
import org.junit.jupiter.api.BeforeAll;

import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;

public abstract class AbstractSetUpP192 {


    static BigInteger MODULE_ECC;
    static BigInteger n;
    static BigInteger b;
    static BigInteger gx;
    static BigInteger gy;
    static RingElement[] COEF;
    static ArrayList<BigInteger> card = new ArrayList<>();
    static GeneralECPoint gen;
    static protected GeneralECPoint alpha;
    static protected ECPrimeOrderSubgroup g;
    static protected GeneralEC curve;

    @BeforeAll
    static void setP192Up() {
        File file = new File("p192.toml");
        Toml toml = new Toml().read(file);
        MODULE_ECC = new BigInteger(toml.getString("p"));
        n = new BigInteger(toml.getString("n"));
        b = new BigInteger(toml.getString("b").replaceAll("\\s", ""), 16);
        gx = new BigInteger(toml.getString("gx")
                .replaceAll("\\s", ""), 16);
        gy = new BigInteger(toml.getString("gy")
                .replaceAll("\\s", ""), 16);
        PrimeField ring = new PrimeField(MODULE_ECC);
        COEF = new PrimeFieldElement[2];
        COEF[0] = new PrimeFieldElement(ring, BigInteger.valueOf(-3));
        COEF[1] = new PrimeFieldElement(ring, b);
        card.add(n);
        curve = new GeneralEC(ring, COEF, card);
        gen = new GeneralECPoint(curve, new PrimeFieldElement(ring, gx), new PrimeFieldElement(ring, gy));
        g = new ECPrimeOrderSubgroup(curve, n, gen);
        alpha = g.getGenerator();
    }
}
