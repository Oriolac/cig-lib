package cat.udl.cig.structures.ecc;

import cat.udl.cig.structures.PrimeField;
import cat.udl.cig.structures.PrimeFieldElement;
import cat.udl.cig.structures.Ring;
import cat.udl.cig.structures.RingElement;
import cat.udl.cig.structures.builder.PrimeFieldElementBuilder;
import cat.udl.cig.utils.discretelogarithm.BruteForce;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class PrimeFieldECTest extends EllipticCurveTest {

    EllipticCurve curve;
    PrimeField primeField;
    PrimeFieldElementBuilder builder;

    @Override
    protected EllipticCurve returnGeneralEC() {
        primeField = new PrimeField(BigInteger.valueOf(2213));
        builder = primeField.buildElement();
        curve = new EllipticCurve(
                primeField,
                builder.setValue(1).build().orElseThrow(),
                builder.setValue(49).build().orElseThrow());
        return curve;
    }

    @Override
    protected Ring returnRingOfEC() {
        return curve.getRing();
    }

    @Override
    protected RingElement returnBadCoordinateX() {
        return builder.setValue(2).build().orElseThrow();
    }

    @Override
    protected EllipticCurvePoint returnGeneralECPoint1() {
        return new EllipticCurvePoint(curve, builder.setValue(1583).build().orElseThrow(), builder.setValue(1734).build().orElseThrow());
    }

    @Override
    protected EllipticCurvePoint returnGeneralECPoint2() {
        EllipticCurvePoint point2 = returnGeneralECPoint1().pow(BigInteger.TWO);
        assertEquals(new EllipticCurvePoint(curve, builder.setValue(821).build().orElseThrow(), builder.setValue(1341).build().orElseThrow()), point2);
        return point2;
    }

    @Override
    protected EllipticCurvePoint returnExpectedResultPlusOperation() {
        String xStr = "297";
        BigInteger x = new BigInteger(xStr);
        String yStr = "968";
        BigInteger y = new BigInteger(yStr);
        return new EllipticCurvePoint(curve, new PrimeFieldElement(primeField, x), new PrimeFieldElement(primeField, y));
    }

    @Override
    protected EllipticCurvePoint returnsExpectedElementMultByTHREE() {
        String point1Str = "297";
        BigInteger x = new BigInteger(point1Str);
        String point2Str = "968";
        BigInteger y = new BigInteger(point2Str);
        return new EllipticCurvePoint(curve, new PrimeFieldElement(primeField, x), new PrimeFieldElement(primeField, y));
    }

    @Override
    protected BigInteger returnExpectedOrderOfPoint1() {
        return BigInteger.valueOf(1093);
    }

    @Override
    protected ArrayList<EllipticCurvePoint> returnLessPointsOfPoint1() {
        return new ArrayList<>(List.of(returnsExpectedElementMultByTHREE(), returnExpectedResultPlusOperation(), returnGeneralECPoint2()));
    }

    @Test
    void testTwoPointsOrderAddition() {
        EllipticCurve ellipticCurve = returnGeneralEC();
        ECPoint plusOp = returnExpectedResultPlusOperation();
        BigInteger expectedOrderPoint1 = returnExpectedOrderOfPoint1();
        Optional<BigInteger> optOrderPoint = ellipticCurve.validOrder(plusOp);
        assertTrue(optOrderPoint.isPresent());
        BigInteger orderPoint = optOrderPoint.get();
        assertEquals(expectedOrderPoint1, orderPoint);
        assertEquals(ellipticCurve.getMultiplicativeIdentity(), plusOp.pow(orderPoint));
        Optional<BigInteger> power = new BruteForce(plusOp).algorithm(ellipticCurve.getMultiplicativeIdentity());
        assertTrue(power.isPresent());
        assertEquals(ellipticCurve.getMultiplicativeIdentity(), plusOp.pow(power.get()));
        assertEquals(ellipticCurve.getMultiplicativeIdentity(), plusOp.pow(BigInteger.valueOf(1093)));
        assertEquals(BigInteger.valueOf(1093), power.get());
    }

    @Test
    void testP1MultNIsInfinity() {
        EllipticCurvePoint result = returnGeneralECPoint1().pow(BigInteger.valueOf(1093));
        assertTrue(result.isInfinity());
    }

    @Test
    void testP2MultNIsInfinity() {
        EllipticCurvePoint result = returnGeneralECPoint2().pow(BigInteger.valueOf(1093));
        assertTrue(result.isInfinity());
    }

    @Test
    void testP2MultNMinus2IsNotIfinity() {
        EllipticCurvePoint result = returnGeneralECPoint2().pow(BigInteger.valueOf(1093-2));
        assertFalse(result.isInfinity());
    }

    @Test
    void testSeveralPow() {
        HashMap<BigInteger, EllipticCurvePoint> powers = PowersOfPrimeFieldAF.getPowers(curve, builder);
        for (Map.Entry<BigInteger, EllipticCurvePoint> entry : powers.entrySet()) {
            assertEquals(entry.getValue(), returnGeneralECPoint1().pow(entry.getKey()), "P1 * " + entry.getKey() + " != " + entry.getValue());
        }
    }

    @Test
    void testPow1091() {
        assertNotEquals(curve.getMultiplicativeIdentity(), returnGeneralECPoint1().pow(BigInteger.valueOf(1091)));
        assertNotEquals(curve.getMultiplicativeIdentity(), returnGeneralECPoint2().pow(BigInteger.valueOf(1091)));
    }

    @Test
    void testOrderOfAPoint() {
        EllipticCurvePoint point = returnGeneralECPoint1();
        BigInteger order = point.getOrder();
        EllipticCurvePoint result = point.pow(order);
        System.out.println("Point = " + point);
        System.out.println("Order of the point = " + order);
        System.out.println("Power = " + result);
        System.out.println("Is the power result infinity? " + result.isInfinity());
        System.out.println("Op: " + point + " * " + order + " = " + result);
        assertTrue(result.isInfinity());
    }

    @Test
    void testGetDiscriminant() {
        RingElement discriminant = this.curve.getDiscriminant();
        assertEquals(builder.setValue(601).build().orElseThrow(), discriminant);
    }

}
