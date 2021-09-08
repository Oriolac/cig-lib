package cat.udl.cig.structures.ecc;

import cat.udl.cig.structures.Ring;
import cat.udl.cig.structures.RingElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

abstract class GeneralECTest {

    GeneralEC generalEC;
    GeneralECPoint point1;
    GeneralECPoint point2;
    RingElement xCoordinateFromPoint1;
    RingElement yCoordinateFromPoint1;
    RingElement xCoordinateFromPoint2;
    RingElement yCoordinateFromPoint2;
    Ring ring;

    @BeforeEach
    void setUp() {
        generalEC = returnGeneralEC();
        ring = returnRingOfEC();
        point1 = returnGeneralECPoint1();
        point2 = returnGeneralECPoint2();
        xCoordinateFromPoint1 = point1.x;
        yCoordinateFromPoint1 = point1.y;
        xCoordinateFromPoint2 = point2.x;
        yCoordinateFromPoint2 = point2.y;
    }

    protected abstract GeneralEC returnGeneralEC();

    protected abstract Ring returnRingOfEC();

    protected abstract GeneralECPoint returnGeneralECPoint1();

    protected abstract GeneralECPoint returnGeneralECPoint2();

    @Test
    void testElementPlusInfinityEqualsElement() {
        GeneralECPoint inf = generalEC.getMultiplicativeIdentity();
        GeneralECPoint elem1 = point1.multiply(inf);
        GeneralECPoint elem2 = point2.multiply(inf);
        assertEquals(point1, elem1);
        assertEquals(point2, elem2);
    }

    @Test
    void testElementMult1EqualsElement() {
        BigInteger elem = (BigInteger.ONE);
        GeneralECPoint mult1 = point1.pow(elem);
        GeneralECPoint mult2 = point2.pow(elem);
        assertEquals(point1, mult1);
        assertEquals(point2, mult2);

    }

    @Test
    void testElementPlusAnotherElement() {
        GeneralECPoint plus = generalEC.multiply(point1, point2);
        assertEquals(point1.multiply(point2), plus);

    }

    @Test
    void testElementMultByNonZeroScalar() {
        BigInteger power = BigInteger.valueOf(3L);
        GeneralECPoint mult = generalEC.pow(point1, power);
        assertEquals(point1.pow(power), mult);
    }

    @Test
    void testIsElementOnCurve() {
        boolean elem1 = generalEC.isOnCurve(point1);
        boolean elem2 = generalEC.isOnCurve(point2);
        assertEquals(point1.getCurve().equals(generalEC), elem1);
        assertEquals(point2.getCurve().equals(generalEC), elem2);
    }

    @Test
    void testIsCoordenatesOnCurve() {
        boolean elem1 = generalEC.isOnCurve(xCoordinateFromPoint1, yCoordinateFromPoint1);
        boolean elem2 = generalEC.isOnCurve(xCoordinateFromPoint2, yCoordinateFromPoint2);
        boolean cond1 = xCoordinateFromPoint1.getGroup().equals(ring) && !xCoordinateFromPoint1.getGroup().equals(yCoordinateFromPoint1.getGroup());
        boolean cond2 = xCoordinateFromPoint2.getGroup().equals(ring) && !xCoordinateFromPoint2.getGroup().equals(yCoordinateFromPoint2.getGroup());
        assertEquals(cond1, elem1);
        assertEquals(cond2, elem2);
    }

    @Test
    void testCorrectLiftX() {

    }

    @Test
    void testIncorrectLiftX() {

    }

    @Test
    void testIsOnCurveAndContainsElement() {

    }

    @Test
    void testComputesOrder() {

    }

    @Test
    void testAreCoordinatesOfAPointFromSameField() {

    }


}