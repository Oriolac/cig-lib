package cat.udl.cig.structures.ecc;

import cat.udl.cig.structures.Ring;
import cat.udl.cig.structures.RingElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.ArrayList;

abstract class GeneralECTest {

    GeneralEC generalEC;
    GeneralECPoint point1;
    GeneralECPoint point2;
    RingElement xCoordinateFromPoint1;
    RingElement yCoordinateFromPoint1;
    Ring ring;

    @BeforeEach
    void setUp() {
        generalEC = returnGeneralEC();
        ring = returnRingOfEC();
        point1 = returnGeneralECPoint1();
        point2 = returnGeneralECPoint2();
        xCoordinateFromPoint1 = point1.x;
        yCoordinateFromPoint1 = point1.y;
    }

    protected abstract GeneralEC returnGeneralEC();

    protected abstract Ring returnRingOfEC();

    protected abstract GeneralECPoint returnGeneralECPoint1();

    protected abstract GeneralECPoint returnGeneralECPoint2();

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
    void testIsCoordenatesOnCurve() {

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