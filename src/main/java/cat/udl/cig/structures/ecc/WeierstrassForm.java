package cat.udl.cig.structures.ecc;

import cat.udl.cig.structures.RingElement;

@FunctionalInterface
public interface WeierstrassForm {

    EllipticCurveFunction apply(RingElement a1, RingElement a2, RingElement a3, RingElement a4, RingElement a6);
}
