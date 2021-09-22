package cat.udl.cig.structures.ecc;

import cat.udl.cig.structures.RingElement;

@FunctionalInterface
public interface EllipticCurveFunction {

    GeneralECPoint resolve(RingElement x, RingElement y);
}
