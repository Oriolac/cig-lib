package cat.udl.cig.structures.builder.ecc;

import cat.udl.cig.operations.wrapper.data.Pair;
import cat.udl.cig.structures.Ring;
import cat.udl.cig.structures.RingElement;
import cat.udl.cig.structures.ecc.EllipticCurve;
import cat.udl.cig.structures.ecc.GeneralECPoint;

import java.math.BigInteger;

public class EllipticCurveBuilder {

    private final Ring ring;
    private RingElement a1;
    private RingElement a2;

    private final String A1 = "A1";

    public EllipticCurveBuilder() {
        ring = null;
        a1 = null;
        a2 = null;
    }


    public EllipticCurveBuilder(EllipticCurveBuilder builder) {
        this.ring = builder.ring;
        this.a1 = builder.a1;
    }

    public EllipticCurveBuilder(EllipticCurveBuilder builder, Ring ring) {
        this.ring = ring;
        this.a1 = builder.a1;
    }


    public EllipticCurveBuilder setRing(Ring ring) {
        if (this.ring == null)
            return new EllipticCurveBuilder(this, ring);
        if (this.ring.equals(ring)) {
            return this;
        }
        throw new IllegalStateException("Ring already set in that builder");
    }

    public EllipticCurveBuilder setA1(RingElement a1) {
        if (this.a1 == null) {
            EllipticCurveBuilder res = new EllipticCurveBuilder(this);
            res.a1 = a1;
            return res;
        }
        if (this.a1.equals(a1))
            return this;
        throw new IllegalStateException("RingElement a1 already set in that builder");
    }

    public EllipticCurveBuilder setA2(RingElement a2) {
        if (this.a2 == null)
            return new EllipticCurveBuilder(this, a2);
        if (this.a2.equals(a2))
            return this;
        throw new IllegalStateException("RingElement a2 already set in that builder");
    }


    static public EllipticCurve reducedForm(Ring ring, RingElement A, RingElement B) {
        return new EllipticCurve(ring, A, B);
    }

    static public EllipticCurve reducedForm(Ring ring, RingElement A, RingElement B, BigInteger size) {
        return new EllipticCurve(ring, A, B, size);
    }

    static public Pair<EllipticCurve, GeneralECPoint> reducedFormOnlyOneSubgroup(Ring ring, RingElement A, RingElement B, BigInteger order, RingElement x, RingElement y) {
        return EllipticCurve.EllipticCurveGeneratorOnlyOneSubgroup(ring, A, B, order, x, y);
    }


}
