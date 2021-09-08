package cat.udl.cig.structures.ecc;

import cat.udl.cig.structures.Ring;
import cat.udl.cig.structures.RingElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

abstract class GeneralECTest {

    private GeneralEC generalEC;
    private GeneralECPoint point1;
    private GeneralECPoint point2;
    private RingElement xCoordinateFromPoint1;
    private RingElement yCoordinateFromPoint1;
    private BigInteger expectedOrderPoint1;
    private Ring ring;
    private RingElement xBadCoordinate;

    @BeforeEach
    void setUp() {
        generalEC = returnGeneralEC();
        ring = returnRingOfEC();
        point1 = returnGeneralECPoint1();
        point2 = returnGeneralECPoint2();
        xBadCoordinate = returnBadCoordinateX();
        xCoordinateFromPoint1 = point1.x;
        yCoordinateFromPoint1 = point1.y;
        expectedOrderPoint1 = returnExpectedOrderOfPoint1();
    }

    protected abstract GeneralEC returnGeneralEC();

    protected abstract Ring returnRingOfEC();

    protected abstract RingElement returnBadCoordinateX();

    protected abstract GeneralECPoint returnGeneralECPoint1();

    protected abstract GeneralECPoint returnGeneralECPoint2();

    protected abstract BigInteger returnExpectedOrderOfPoint1();

    @Test
    void testElementPlusInfinityEqualsElement() {

    }

    @Test
    void testElementMult1EqualsElement() {

    }

    @Test
    void testElementPlusAnotherElement() {

    }

    @Test
    void testElementMultByNonZeroScalar() {

    }

    @Test
    void testIsElementOnCurve() {

    }

    @Test
    void testAreCoordinatesOnCurve() {

    }

    @Test
    void testCorrectLiftX() {
        testCorrectLiftX(point1);
        testCorrectLiftX(point2);
    }

    private void testCorrectLiftX(GeneralECPoint point1) {
        Optional<? extends GeneralECPoint> point = generalEC.liftX(point1.x);
        assertTrue(point.isPresent());
        assertEquals(point.get(), point1);
        assertTrue(ring.containsElement(point.get().x));
        assertTrue(ring.containsElement(point.get().y));
    }

    @Test
    void testIncorrectLiftX() {
        Optional<? extends GeneralECPoint> point = generalEC.liftX(xBadCoordinate);
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
        GeneralECPoint point = new GeneralEC(generalEC).liftX(point1.x).orElseThrow();
        assertEquals(point1, point);
        assertTrue(generalEC.containsElement(point1));
        assertTrue(generalEC.containsElement(point));
    }

    @Test
    void testComputesOrder() {
        BigInteger orderPoint1 = generalEC.computeOrder(point1);
        assertNotNull(orderPoint1);
        assertEquals(this.expectedOrderPoint1, orderPoint1);
    }

    @Test
    void testAreCoordinatesOfAPointFromSameField() {
        Optional<? extends GeneralECPoint> point = generalEC.liftX(point1.x);
        assertTrue(point.isPresent());
        assertTrue(ring.containsElement(point.get().x));
        assertTrue(ring.containsElement(point.get().y));
        assertTrue(point.get().x.belongsToSameGroup(point.get().y));
    }
}