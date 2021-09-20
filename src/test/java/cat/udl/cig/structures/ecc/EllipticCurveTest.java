package cat.udl.cig.structures.ecc;

import cat.udl.cig.structures.Ring;
import cat.udl.cig.structures.RingElement;
import cat.udl.cig.utils.discretelogarithm.BruteForce;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

abstract class EllipticCurveTest {

    private EllipticCurve ellipticCurve;
    private GeneralECPoint point1;
    private GeneralECPoint point2;
    private RingElement xCoordinateFromPoint1;
    private RingElement yCoordinateFromPoint1;
    private RingElement xCoordinateFromPoint2;
    private RingElement yCoordinateFromPoint2;
    private BigInteger expectedOrderPoint1;
    private Ring ring;
    private BigInteger powerThree;
    private RingElement xBadCoordinate;
    private GeneralECPoint expectedResultPlusOperation;
    private GeneralECPoint expectedResultMultByZeroNonScalarOperation;
    private BigInteger powerTwo;
    private ArrayList<GeneralECPoint> lessPointsOfPoint1;


    @BeforeEach
    void setUp() {
        ellipticCurve = returnGeneralEC();
        ring = returnRingOfEC();
        point1 = returnGeneralECPoint1();
        point2 = returnGeneralECPoint2();
        xBadCoordinate = returnBadCoordinateX();
        xCoordinateFromPoint1 = point1.x;
        yCoordinateFromPoint1 = point1.y;
        xCoordinateFromPoint2 = point2.x;
        yCoordinateFromPoint2 = point2.y;
        expectedResultPlusOperation = returnExpectedResultPlusOperation();
        expectedResultMultByZeroNonScalarOperation = returnsExpectedElementMultByTHREE();
        powerThree = BigInteger.valueOf(3);
        powerTwo = BigInteger.valueOf(2);
        expectedOrderPoint1 = returnExpectedOrderOfPoint1();
        lessPointsOfPoint1 = returnLessPointsOfPoint1();
    }

    protected abstract EllipticCurve returnGeneralEC();

    protected abstract Ring returnRingOfEC();

    protected abstract RingElement returnBadCoordinateX();

    protected abstract GeneralECPoint returnGeneralECPoint1();

    protected abstract GeneralECPoint returnGeneralECPoint2();

    protected abstract GeneralECPoint returnExpectedResultPlusOperation();

    protected abstract GeneralECPoint returnsExpectedElementMultByTHREE();

    protected abstract BigInteger returnExpectedOrderOfPoint1();

    protected abstract ArrayList<GeneralECPoint> returnLessPointsOfPoint1();

    @Test
    void testElementPlusInfinityEqualsElement() {
        GeneralECPoint inf = ellipticCurve.getMultiplicativeIdentity();
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
        GeneralECPoint plus = ellipticCurve.multiply(point1, point2);
        assertEquals(this.expectedResultPlusOperation, plus, "P" + point1 + " + P" + point2);

    }

    @Test
    void testElementMultByNonZeroScalar() {
        GeneralECPoint mult = ellipticCurve.pow(point1, powerThree);
        assertEquals(this.expectedResultMultByZeroNonScalarOperation, mult, "P" + point1 + " * 3");
    }

    @Test
    void multToSizePoint1() {
        System.out.println(point1);
        GeneralECPoint result = ellipticCurve.pow(point1, ellipticCurve.getSize());
        assertEquals(ellipticCurve.getMultiplicativeIdentity(), result);
    }

    @Test
    void multToSizePoint2() {
        GeneralECPoint result = ellipticCurve.pow(point2, ellipticCurve.getSize());
        assertEquals(ellipticCurve.getMultiplicativeIdentity(), result);
    }

    @Test
    void arePointsFromSameGroup() {
        Optional<BigInteger> power = new BruteForce(point1).algorithm(point2);
        assertTrue(power.isPresent());
    }

    @Test
    void testIsElementOnCurve() {
        boolean elem1 = ellipticCurve.isOnCurve(point1);
        assertEquals(point1.getCurve().equals(ellipticCurve), elem1);
        boolean elem2 = ellipticCurve.isOnCurve(point2);
        assertEquals(point2.getCurve().equals(ellipticCurve), elem2);
    }

    @Test
    void testAreCoordenatesOnCurve() {
        boolean elem1 = ellipticCurve.isOnCurve(xCoordinateFromPoint1, yCoordinateFromPoint1);
        boolean elem2 = ellipticCurve.isOnCurve(xCoordinateFromPoint2, yCoordinateFromPoint2);
        boolean cond1 = xCoordinateFromPoint1.getGroup().equals(ring) && yCoordinateFromPoint1.getGroup().equals(ring);
        boolean cond2 = xCoordinateFromPoint2.getGroup().equals(ring) && yCoordinateFromPoint2.getGroup().equals(ring);
        assertEquals(cond1, elem1);
        assertEquals(cond2, elem2);
    }

    @Test
    void testCorrectLiftX() {
        testCorrectLiftX(point1);
        testCorrectLiftX(point2);
    }

    private void testCorrectLiftX(GeneralECPoint point) {
        ArrayList<? extends GeneralECPoint> points = ellipticCurve.liftX(point.x);
        assertTrue(points.size() > 0);
        assertTrue(points.contains(point), points + "does not contain point " + point);
        GeneralECPoint actualPoint = points.stream().filter(p -> p.equals(point)).collect(Collectors.toList()).get(0);
        assertEquals(actualPoint, point);
        assertTrue(ring.containsElement(actualPoint.x));
        assertTrue(ring.containsElement(actualPoint.y));
    }

    @Test
    void testIncorrectLiftX() {
        ArrayList<? extends GeneralECPoint> point = ellipticCurve.liftX(xBadCoordinate);
        assertTrue(point.isEmpty());
    }

    @Test
    void testIsOnCurveAndContainsElement() {
        testIsOnCurveAndContainsElement(point1);
        testIsOnCurveAndContainsElement(point2);
    }

    private void testIsOnCurveAndContainsElement(GeneralECPoint point1) {
        ellipticCurve.isOnCurve(point1);
        ellipticCurve.containsElement(point1);
        ArrayList<? extends GeneralECPoint> points = new EllipticCurve(ellipticCurve).liftX(point1.x);
        assertFalse(points.isEmpty());
        assertTrue(points.contains(point1));
        GeneralECPoint actualPoint = points.stream().filter(p -> p.equals(point1)).collect(Collectors.toList()).get(0);
        assertEquals(point1, actualPoint);
        assertTrue(ellipticCurve.containsElement(point1));
        points.forEach(p -> assertTrue(ellipticCurve.containsElement(p)));
    }

    @Test
    void testComputesOrder() {
        point1.getOrder();
    }

    @Test
    void testPoint2OrderAddition() {
        point2.getOrder();
    }

    @Test
    void testAreCoordinatesOfAPointFromSameField() {
        ArrayList<? extends GeneralECPoint> points = ellipticCurve.liftX(point1.x);
        assertTrue(points.size() > 0);
        GeneralECPoint actualPoint = points.stream().filter(p -> p.equals(point1)).collect(Collectors.toList()).get(0);
        assertTrue(ring.containsElement(actualPoint.x));
        assertTrue(ring.containsElement(actualPoint.y));
        assertTrue(actualPoint.x.belongsToSameGroup(actualPoint.y));
    }

    @Test
    void testDoubleIsSameThatTwoAdd() {
        GeneralECPoint doublePoint = point1.multiply(point1);
        assertEquals(doublePoint, point1.pow(BigInteger.TWO));
    }

    @Test
    void testInfinityElement() {
        GeneralECPoint infinity = ellipticCurve.getMultiplicativeIdentity();
        assertEquals(ellipticCurve.getRing().ZERO(), infinity.x);
        assertEquals(ellipticCurve.getRing().ONE(), infinity.y);
        assertTrue(infinity.isInfinity);
    }

    @Test
    void testLessComparison() {
        for (GeneralECPoint lessPoint : lessPointsOfPoint1) {
            assertTrue(lessPoint.compareTo(point1) < 0);
        }
    }

    @Test
    void testEqualComparison() {
        assertEquals(0, point1.compareTo(point1));
        assertEquals(0, point2.compareTo(point2));
    }

    @Test
    void testSizeCanBeDividedBySubgroupOrders() {
        BigInteger size = this.ellipticCurve.getSize();
        for (ECSubgroup subgroup : this.ellipticCurve.getSubgroups()) {
            assertEquals(BigInteger.ZERO, size.mod(subgroup.getOrder()));
        }

    }
}