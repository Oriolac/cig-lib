package cat.udl.cig.structures.ecc;

import cat.udl.cig.structures.PrimeField;
import cat.udl.cig.structures.PrimeFieldElement;
import cat.udl.cig.structures.Ring;
import cat.udl.cig.structures.RingElement;

import java.math.BigInteger;
import java.util.ArrayList;

public class LargePrimeFieldECTest extends GeneralECTest {

    GeneralEC generalEC;
    private PrimeField primeField;
    private BigInteger n;
    private GeneralECPoint gen;

    @Override
    protected GeneralEC returnGeneralEC() {
        String pStr = "6277101735386680763835789423207666416083908700390324961279";
        BigInteger module = new BigInteger(pStr);
        String nStr = "6277101735386680763835789423176059013767194773182842284081";
        this.n = new BigInteger(nStr);
        String bStr = "64210519 e59c80e7 0fa7e9ab 72243049 feb8deec c146b9b1".replaceAll("\\s", "");
        BigInteger b = new BigInteger(bStr, 16);
        String gxStr = "188da80e b03090f6 7cbf20eb 43a18800 f4ff0afd 82ff1012".replaceAll("\\s", "");
        BigInteger gx = new BigInteger(gxStr, 16);
        String gyStr = "07192b95 ffc8da78 631011ed 6b24cdd5 73f977a1 1e794811".replaceAll("\\s", "");
        BigInteger gy = new BigInteger(gyStr, 16);
        generalEC = curveConstruction(module, b, n);
        genConstruction(n, gx, gy, generalEC);
        return generalEC;
    }

    private GeneralEC curveConstruction(BigInteger module, BigInteger b, BigInteger order) {
        ArrayList<BigInteger> card = new ArrayList<>();
        this.primeField = new PrimeField(module);
        RingElement A = new PrimeFieldElement(primeField, BigInteger.valueOf(-3));
        RingElement B = new PrimeFieldElement(primeField, b);
        card.add(order);
        return new GeneralEC(primeField, A, B, card);
    }

    private ECPrimeOrderSubgroup genConstruction(BigInteger n, BigInteger gx, BigInteger gy, GeneralEC generalEC) {
        gen = new GeneralECPoint(generalEC, new PrimeFieldElement(primeField, gx), new PrimeFieldElement(primeField, gy));
        return new ECPrimeOrderSubgroup(generalEC, n, gen);
    }

    @Override
    protected Ring returnRingOfEC() {
        return primeField;
    }

    @Override
    protected RingElement returnBadCoordinateX() {
        return new PrimeFieldElement(primeField, BigInteger.valueOf(23));
    }

    @Override
    protected GeneralECPoint returnGeneralECPoint1() {
        return gen;
    }

    @Override
    protected GeneralECPoint returnGeneralECPoint2() {
        return gen.pow(BigInteger.TWO);
    }

    @Override
    protected GeneralECPoint returnExpectedResultPlusOperation() {
        String point1Str = "2915109630280678890720206779706963455590627465886103135194";
        BigInteger x = new BigInteger(point1Str);
        String point2Str = "2946626711558792003980654088990112021985937607003425539581";
        BigInteger y = new BigInteger(point2Str);
        return new GeneralECPoint(generalEC, new PrimeFieldElement(primeField, x), new PrimeFieldElement(primeField, y));
    }

    @Override
    protected GeneralECPoint returnsExpectedElementMultByNonZeroScalar() {
        String point1Str = "2915109630280678890720206779706963455590627465886103135194";
        BigInteger x = new BigInteger(point1Str);
        String point2Str = "2946626711558792003980654088990112021985937607003425539581";
        BigInteger y = new BigInteger(point2Str);
        return new GeneralECPoint(generalEC, new PrimeFieldElement(primeField, x), new PrimeFieldElement(primeField, y));
    }

    @Override
    protected BigInteger returnExpectedOrderOfPoint1() {
        return n;
    }

    @Override
    protected BigInteger returnPower() {
        return BigInteger.valueOf(3);
    }

}