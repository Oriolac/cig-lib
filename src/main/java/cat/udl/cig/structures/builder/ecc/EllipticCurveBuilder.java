package cat.udl.cig.structures.builder.ecc;

import cat.udl.cig.operations.wrapper.data.Pair;
import cat.udl.cig.structures.Ring;
import cat.udl.cig.structures.RingElement;
import cat.udl.cig.structures.ecc.EllipticCurve;
import cat.udl.cig.structures.ecc.GeneralECPoint;

import java.math.BigInteger;

public class EllipticCurveBuilder {

    private Ring ring;
    private RingElement a1;
    private RingElement a2;
    private RingElement a3;
    private RingElement a4;
    private RingElement a6;
    private BigInteger size;
    private boolean onlyOneGroup;

    public EllipticCurveBuilder() {
        ring = null;
        a1 = null;
        a2 = null;
        a3 = null;
        a4 = null;
        a6 = null;
        size = null;
        onlyOneGroup = false;
    }


    public EllipticCurveBuilder(EllipticCurveBuilder builder) {
        this.ring = builder.ring;
        this.a1 = builder.a1;
        this.a2 = builder.a2;
        this.a3 = builder.a3;
        this.a4 = builder.a4;
        this.a6 = builder.a6;
        this.size = builder.size;
        this.onlyOneGroup = builder.onlyOneGroup;
    }

    public EllipticCurveBuilder setRing(Ring ring) {
        if (this.ring == null) {
            EllipticCurveBuilder res = new EllipticCurveBuilder(this);
            res.ring = ring;
            return res;
        }
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
        if (this.a2 == null) {
            EllipticCurveBuilder res = new EllipticCurveBuilder(this);
            res.a2 = a2;
            return res;
        }
        if (this.a2.equals(a2))
            return this;
        throw new IllegalStateException("RingElement a2 already set in that builder");
    }

    public EllipticCurveBuilder setA3(RingElement a3) {
        if (this.a3 == null) {
            EllipticCurveBuilder res = new EllipticCurveBuilder(this);
            res.a3 = a3;
            return res;
        }
        if (this.a3.equals(a3))
            return this;
        throw new IllegalStateException("RingElement a3 already set in that builder");
    }

    public EllipticCurveBuilder setA4(RingElement a4) {
        if (this.a4 == null) {
            EllipticCurveBuilder res = new EllipticCurveBuilder(this);
            res.a4 = a4;
            return res;
        }
        if (this.a4.equals(a4))
            return this;
        throw new IllegalStateException("RingElement a4 already set in that builder");
    }

    public EllipticCurveBuilder setA6(RingElement a6) {
        if (this.a6 == null) {
            EllipticCurveBuilder res = new EllipticCurveBuilder(this);
            res.a6 = a6;
            return res;
        }
        if (this.a6.equals(a6))
            return this;
        throw new IllegalStateException("RingElement a1 already set in that builder");
    }

    public EllipticCurveBuilder setOrderOnlySubgroup(BigInteger order) {
        if (this.size == null) {
            EllipticCurveBuilder res = new EllipticCurveBuilder(this);
            res.size = order;
            res.onlyOneGroup = true;
            return res;
        }
        if (this.size.equals(order))
            return this;
        throw new IllegalStateException("RingElement a1 already set in that builder");
    }

    public EllipticCurveBuilder setSize(BigInteger size) {
        if (this.size == null) {
            EllipticCurveBuilder res = new EllipticCurveBuilder(this);
            res.size = size;
            return res;
        }
        if (this.size.equals(size))
            return this;
        throw new IllegalStateException("RingElement a1 already set in that builder");
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
