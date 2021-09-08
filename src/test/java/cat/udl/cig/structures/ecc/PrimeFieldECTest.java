package cat.udl.cig.structures.ecc;

import cat.udl.cig.structures.Ring;
import cat.udl.cig.structures.RingElement;

import java.math.BigInteger;

public class PrimeFieldECTest extends GeneralECTest {


    @Override
    protected GeneralEC returnGeneralEC() {
        return null;
    }

    @Override
    protected Ring returnRingOfEC() {
        return null;
    }

    @Override
    protected RingElement returnBadCoordinateX() {
        return null;
    }

    @Override
    protected GeneralECPoint returnGeneralECPoint1() {
        return null;
    }

    @Override
    protected GeneralECPoint returnGeneralECPoint2() {
        return null;
    }

    @Override
    protected BigInteger returnExpectedOrderOfPoint1() {
        return null;
    }
}
