package cat.udl.cig.structures.ecc;

import cat.udl.cig.structures.Ring;
import cat.udl.cig.structures.RingElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

abstract class GeneralECTest {

    private GeneralEC generalEC;
    private GeneralECPoint point1;
    private GeneralECPoint point2;
    private RingElement xCoordinateFromPoint1;
    private RingElement yCoordinateFromPoint1;
    private RingElement xCoordinateFromPoint2;
    private RingElement yCoordinateFromPoint2;
    private BigInteger expectedOrderPoint1;
    private Ring ring;
    private RingElement xBadCoordinate;
    private GeneralECPoint expectedResultPlusOperation;
    private GeneralECPoint expectedResultMultByZeroNonScalarOperation;


    @BeforeEach
    void setUp() {
        generalEC = returnGeneralEC();
        ring = returnRingOfEC();
        point1 = returnGeneralECPoint1();
        point2 = returnGeneralECPoint2();
        xBadCoordinate = returnBadCoordinateX();
        xCoordinateFromPoint1 = point1.x;
        yCoordinateFromPoint1 = point1.y;
        xCoordinateFromPoint2 = point2.x;
        yCoordinateFromPoint2 = point2.y;
        expectedResultPlusOperation = returnExpectedResultPlusOperation();
        expectedResultMultByZeroNonScalarOperation = returnsExpectedElementMultByNonZeroScalar();
        expectedOrderPoint1 = returnExpectedOrderOfPoint1();
    }

    protected abstract GeneralEC returnGeneralEC();

    protected abstract Ring returnRingOfEC();

    protected abstract RingElement returnBadCoordinateX();

    protected abstract GeneralECPoint returnGeneralECPoint1();

    protected abstract GeneralECPoint returnGeneralECPoint2();

    protected abstract GeneralECPoint returnExpectedResultPlusOperation();

    protected abstract GeneralECPoint returnsExpectedElementMultByNonZeroScalar();

    protected abstract BigInteger returnExpectedOrderOfPoint1();

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
        assertEquals(this.expectedResultPlusOperation, plus);

    }

    @Test
    void testElementMultByNonZeroScalar() {
        BigInteger power = BigInteger.valueOf(3L);
        GeneralECPoint mult = generalEC.pow(point1, power);
        assertEquals(this.expectedResultMultByZeroNonScalarOperation, mult);
    }

    @Test
    void testIsElementOnCurve() {
        boolean elem1 = generalEC.isOnCurve(point1);
        boolean elem2 = generalEC.isOnCurve(point2);
        assertEquals(point1.getCurve().equals(generalEC), elem1);
        assertEquals(point2.getCurve().equals(generalEC), elem2);
    }

    @Test
    void testAreCoordenatesOnCurve() {
        boolean elem1 = generalEC.isOnCurve(xCoordinateFromPoint1, yCoordinateFromPoint1);
        boolean elem2 = generalEC.isOnCurve(xCoordinateFromPoint2, yCoordinateFromPoint2);
        boolean cond1 = xCoordinateFromPoint1.getGroup().equals(ring) && !xCoordinateFromPoint1.getGroup().equals(yCoordinateFromPoint1.getGroup());
        boolean cond2 = xCoordinateFromPoint2.getGroup().equals(ring) && !xCoordinateFromPoint2.getGroup().equals(yCoordinateFromPoint2.getGroup());
        assertEquals(cond1, elem1);
        assertEquals(cond2, elem2);
    }

    @Test
    void testCorrectLiftX() {
        testCorrectLiftX(point1);
        testCorrectLiftX(point2);
    }

    private void testCorrectLiftX(GeneralECPoint point1) {
        ArrayList<? extends GeneralECPoint> points = generalEC.liftX(point1.x);
        assertTrue(points.size() > 0);
        assertTrue(points.contains(point1));
        GeneralECPoint actualPoint = points.stream().filter(p -> p.equals(point1)).collect(Collectors.toList()).get(0);
        assertEquals(actualPoint, point1);
        assertTrue(ring.containsElement(actualPoint.x));
        assertTrue(ring.containsElement(actualPoint.y));
    }

    @Test
    void testIncorrectLiftX() {
        ArrayList<? extends GeneralECPoint> point = generalEC.liftX(xBadCoordinate);
        assertTrue(point.isEmpty());
    }

    @Test
    void testIsOnCurveAndContainsElement() {
        testIsOnCurveAndContainsElement(point1);
        testIsOnCurveAndContainsElement(point2);
    }

    private void testIsOnCurveAndContainsElement(GeneralECPoint point1) {
        generalEC.isOnCurve(point1);
        generalEC.containsElement(point1);
        ArrayList<? extends GeneralECPoint> points = new GeneralEC(generalEC).liftX(point1.x);
        assertFalse(points.isEmpty());
        assertTrue(points.contains(point1));
        GeneralECPoint actualPoint = points.stream().filter(p -> p.equals(point1)).collect(Collectors.toList()).get(0);
        assertEquals(point1, actualPoint);
        assertTrue(generalEC.containsElement(point1));
        points.forEach(p -> assertTrue(generalEC.containsElement(p)));
    }

    @Test
    void testComputesOrder() {
        BigInteger orderPoint1 = generalEC.computeOrder(point1);
        assertNotNull(orderPoint1);
        assertEquals(this.expectedOrderPoint1, orderPoint1);
    }

    @Test
    void testAreCoordinatesOfAPointFromSameField() {
        ArrayList<? extends GeneralECPoint> points = generalEC.liftX(point1.x);
        assertTrue(points.size() > 0);
        GeneralECPoint actualPoint = points.stream().filter(p -> p.equals(point1)).collect(Collectors.toList()).get(0);
        assertTrue(ring.containsElement(actualPoint.x));
        assertTrue(ring.containsElement(actualPoint.y));
        assertTrue(actualPoint.x.belongsToSameGroup(actualPoint.y));
    }
}