package cat.udl.cig.structures.ecc;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import cat.udl.cig.exceptions.IncorrectRingElementException;
import cat.udl.cig.structures.BinaryField;
import cat.udl.cig.structures.BinaryFieldElement;
import cat.udl.cig.structures.RingElement;
import cat.udl.cig.structures.GroupElement;

/**
 * Models a <i>Point</i> \(P\) belonging to a <i>Binary Elliptic Curve</i>
 * \(E\).
 *
 * @see BinaryField
 * @see BinaryFieldElement
 * @see BinaryEC
 * @author Ricard Garra
 */
public class BinaryECPoint extends GeneralECPoint {
    /**
     * The <i>Elliptic Curve</i> \(E\) to which the point \(P\) belongs.
     *
     * @see EllipticCurveInt
     */
    // protected final BinaryEC E;

    /**
     * The coordinates \((X:Y)\) of the point. \(X\) and \(Y\) belongs to the
     * Ring {@code this.ec.getRing()}.
     *
     * @see RingElement
     */
    // protected final BinaryFieldElement x, y;

    /**
     * It indicates if {@code this} <i>Point</i> \(P\) is the infinity point or
     * not. If \(P\) is the infinity point, then \(P = (0:1:0)\). If not, \(P =
     * (X:Y:1)\).
     */
    // protected final boolean isInfinity;

    /**
     * It represents the order of {@code this} <i>Point</i> \(P\),
     * \(\text{ord}(P)\). Given a BigInteger \(a\) such that \(a =
     * \text{ord}(P)\), then \(a \cdot P = \mathcal{O}\).
     */
    // protected BigInteger order;

    /**
     * Creates the <i>Infinity Point</i> \(P\) for the <i>GeneralEC</i> \(E\).
     * The <i>Infinity Point</i> has the coordinates \((0:1:0)\). If {@code E}
     * is not initialized, then \(P\) is a null <i>Point</i>.
     *
     * @param E
     *            the <i>GeneralEC</i> to which \(P\) belongs.
     */

    /**
     * Auxiliar attribute to accelerate some computations.
     */
    BinaryFieldElement inverseC = null;

    /**
     * Creates the <i>Infinity Point</i> \(P\) for the <i>BinaryEC</i> \(E\).
     * The <i>Infinity Point</i> has the coordinates \((0:1:0)\). If {@code E}
     * is not initialized, then \(P\) is a null <i>Point</i>.
     *
     * @param E
     *            the <i>BinaryEC</i> to which \(P\) belongs.
     */
    public BinaryECPoint(final BinaryEC E) {
        super(E);
        initializeInverseC();
    }

    /**
     * Creates a copy of the <i>BinaryECPoint</i> \(P\). This constructor makes
     * a deep copy of \(P\).
     *
     * @param P
     *            the <i>BinaryECPoint</i> to be copied.
     */
    public BinaryECPoint(final BinaryECPoint P) {
        super(P);
        initializeInverseC();
    }

    /**
     * Creates the <i>BinaryECPoint</i> \(P\) for the <i>BinaryEC</i> \(E\). The
     * <i>Point</i> has the coordinates \((x:y:1)\) or \((0:1:0)\) if {@code x}
     * and {@code y} are not initialized. If {@code E} is not initialized or
     * {@code x} and {@code y} are not elements of the same <i>BinaryField</i>,
     * then \(P\) is a null <i>Point</i>.
     *
     * @param E
     *            the <i>BinaryEC</i> to which \(P\) belongs.
     * @param x
     *            the \(x\) coordinate for \(P\). It must belong to
     *            {@code this.E.getRing()}.
     * @param y
     *            the \(y\) coordinate for \(P\). It must belong to
     *            {@code this.E.getRing()}.
     * @param order
     *            the order of the newly created <i>BinaryECPoint</i>. This
     *            constructor checks if the order is correct. If not, it is
     *            initialized to ONE.
     */
    public BinaryECPoint(final BinaryEC E, final BinaryFieldElement x,
            final BinaryFieldElement y, final BigInteger order) {
        super(E, x, y, order);
        initializeInverseC();
    }

    /**
     * Creates the <i>BinaryECPoint</i> \(P\) for the <i>BinaryEC</i> \(E\). The
     * <i>Point</i> has the coordinates \((x:y:1)\) or \((0:1:0)\) if {@code x}
     * and {@code y} are not initialized. If {@code E} is not initialized or
     * {@code x} and {@code y} are not elements of the same <i>BinaryField</i>,
     * then \(P\) is a null <i>Point</i>.
     *
     * @param E
     *            the <i>BinaryEC</i> to which \(P\) belongs.
     * @param x
     *            the \(x\) coordinate for \(P\). It must belong to
     *            {@code this.E.getRing()}.
     * @param y
     *            the \(y\) coordinate for \(P\). It must belong to
     *            {@code this.E.getRing()}.
     */
    public BinaryECPoint(final BinaryEC E, final BinaryFieldElement x,
            final BinaryFieldElement y) {
        super(E, x, y);
        initializeInverseC();
    }

    @Override
    public ECPoint clone() {
        return new BinaryECPoint(this);
    }

    /**
     * Auxiliar method to initialize the auxiliar attribute inverseC. The
     * attribute inverseC is only initialized if E is an initialized super
     * singular curve. Then, \(inverseC = \frac{1}{E.c}\).
     */
    private void initializeInverseC() {
        if (((BinaryEC) curve).isSuperSingularEC()) { // && E.isInitialized()) {
            inverseC = ((BinaryEC) curve).getC().inverse();
        }
    }

    @Override
    public BinaryECPoint square() {
        /*
         * if(!isInitialized()) { return null; }
         */
        if (isInfinity) {
            return this;
        }

        if (((BinaryEC) curve).isSuperSingularEC()) {
            return SSDoublePoint();
        }
        return NSSDoublePoint();
    }

    @Override
    public BinaryECPoint multiply(final GroupElement iQ)
            throws ArithmeticException {
        /*
         * if(!isInitialized() || !Q.isInitialized()) { return null; }
         */
        BinaryECPoint Q = (BinaryECPoint) iQ;

        if (!curve.equals(Q.curve)) {
            throw new ArithmeticException(
                "Trying to add points from different" + "Elliptic Curves");
        }

        if (((BinaryEC) curve).isSuperSingularEC()) {
            return SSAdd(Q);
        }
        return NSSAdd(Q);
    }

    @Override
    public BinaryECPoint inverse() {
        /*
         * if(!isInitialized()) { return null; }
         */

        try {
            BinaryECPoint invP;
            if (((BinaryEC) curve).isSuperSingularEC()) {
                invP =
                        new BinaryECPoint(
                            (BinaryEC) curve,
                            (BinaryFieldElement) x,
                            ((BinaryFieldElement) y).add(((BinaryEC) curve).getC()));
            } else {
                invP =
                    new BinaryECPoint((BinaryEC) curve,
                        (BinaryFieldElement) x,
                        ((BinaryFieldElement) x).add(y));
            }

            return invP;
        } catch (IncorrectRingElementException ex) {
            return null;
        }
    }

    @Override
    public boolean equals(final Object obj) {
        BinaryECPoint P = (BinaryECPoint) obj;
        return (super.equals(obj) && ((BinaryEC) curve).isSuperSingularEC() == ((BinaryEC) P.curve)
            .isSuperSingularEC());
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + (((BinaryEC) curve).isSuperSingularEC() ? 1 : 0);
        return hash;
    }

    /**
     * Returns the result of \(k \cdot P\), where \(P\) is {@code this}
     * <i>Point</i>. For non-supersingular curves, it uses the method described
     * in "Fast Multiplication on Elliptic Curves over GF (2^m) without
     * Precomputation", by López and Dahab. For supersingular curves, it uses
     * the general method in ECPoint.
     *
     * @param k
     *            a BigInteger number to multiply the <i>Point</i> \(P\).
     * @return a <i>BinaryECPoint</i> \(R\), where \(R = k \cdot P\) or
     *         {@code null} if {@code this} is not initialized.
     */
    @Override
    public BinaryECPoint pow(final BigInteger k) {
        if (k.equals(BigInteger.ZERO) || isInfinity) {
            return (BinaryECPoint) curve.getMultiplicativeIdentity();
        }
        if (((BinaryEC) curve).isSuperSingularEC()) {
            return normalPow(k);
        }

        try {
            BinaryFieldElement x1 =
                    new BinaryFieldElement((BinaryFieldElement) x);
            BinaryFieldElement xSquared =
                    ((BinaryFieldElement) x).square();
            BinaryFieldElement x2 =
                xSquared.add(curve.getB().divide(xSquared));
            BinaryFieldElement t;
            for (int i = k.bitLength() - 2; i >= 0; i--) {
                t = x1.divide(x1.add(x2));
                if (k.testBit(i)) {
                    x1 = (BinaryFieldElement) (x.add(t)).add(t.square());
                    xSquared = x2.square();
                    x2 = xSquared.add(curve.getB().divide(xSquared));
                } else {
                    xSquared = x1.square();
                    x1 = xSquared.add(curve.getB().divide(xSquared));
                    x2 = (BinaryFieldElement) x.add(t).add(t.square());
                }
            }
            BinaryFieldElement r1 = x1.add(x);
            BinaryFieldElement r2 = x2.add(x);
            BinaryFieldElement aux =
                (r1.multiply(r2)).add(((BinaryFieldElement) x).square())
                    .add(y);
            BinaryFieldElement y1 = r1.multiply(aux);
            y1 = y1.divide(x);
            y1 = y1.add(y);

            return new BinaryECPoint((BinaryEC) curve, x1, y1);
        } catch (IncorrectRingElementException ex) {
            Logger.getLogger(BinaryECPoint.class.getName()).log(
                Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Returns the result of \(k \cdot P\), where \(P\) is {@code this}
     * <i>Point</i>.
     *
     * @param k
     *            a BigInteger number to multiply the <i>Point</i> \(P\).
     * @return a <i>Point</i> \(R\), where \(R = k \cdot P\) or {@code null} if
     *         {@code this} is not initialized.
     */
    private BinaryECPoint normalPow(BigInteger k) {

        if (isInfinity()) {
            return this;
        }
        if (order != null && k.compareTo(order) >= 0) {
            k = k.mod(order);
        }
        ArrayList<Integer> kbits = nonAdjacentForm(k);
        BinaryECPoint Q = (BinaryECPoint) curve.getMultiplicativeIdentity();
        final ECPoint minusP = inverse();
        for (int i = kbits.size() - 1; i >= 0; i--) {
            Q = Q.square();
            if (kbits.get(i) > 0) {
                Q = Q.multiply(this);
            }
            if (kbits.get(i) < 0) {
                Q = Q.multiply(minusP);
            }
        }
        return Q;

    }

    /**
     * Returns the result of \(2 \cdot P\), where \(P\) is {@code this}
     * <i>Point</i>, and the <i>BinaryEC</i> \(E\) is super singular.
     *
     * @return <ul>
     *         <li>a <i>BinaryECPoint</i> \(R\), where \(R = 2 \cdot P\) or</li>
     *         <li>{@code null} if {@code this} <i>BinaryECPoint</i> is not
     *         initialized or</li>
     *         <li>the infinity <i>Point</i> if any exception occurred.</li>
     *         </ul>
     */
    private BinaryECPoint SSDoublePoint() {
        BinaryFieldElement Rx, Ry;
        if (inverseC == null) {
            initializeInverseC();
        }
        try {
            Rx = ((BinaryFieldElement) x).square();
            Rx = Rx.add(curve.getA());
            Rx = Rx.multiply(inverseC);
            Rx = Rx.square();

            Ry = ((BinaryFieldElement) x).square();
            Ry = Ry.add(curve.getA());
            Ry = Ry.multiply(inverseC);
            Ry = Ry.multiply(x.add(Rx));
            Ry = Ry.add(y);
            Ry = Ry.add(((BinaryEC) curve).getC());

            BinaryECPoint R = new BinaryECPoint((BinaryEC) curve, Rx, Ry);

            return R;
        } catch (IncorrectRingElementException ex) {
            return null;
        }
    }

    /**
     * Returns the result of \(2 \cdot P\), where \(P\) is {@code this}
     * <i>Point</i>, and the <i>BinaryEC</i> \(E\) is non super singular.
     *
     * @return <ul>
     *         <li>a <i>BinaryECPoint</i> \(R\), where \(R = 2 \cdot P\) or</li>
     *         <li>{@code null} if {@code this} <i>BinaryECPoint</i> is not
     *         initialized or</li>
     *         <li>the infinity <i>Point</i> if any exception occurred.</li>
     *         </ul>
     */
    private BinaryECPoint NSSDoublePoint() {
        BinaryFieldElement Rx, Ry;
        BinaryFieldElement l;
        if (x.equals(x.getGroup().getAdditiveIdentity())) {
            // if x == 0, it's double is the infinity point
            return (BinaryECPoint) curve.getMultiplicativeIdentity();
        }
        try {
            l = (BinaryFieldElement) y.divide(x);
            l = l.add(x);

            BinaryFieldElement xSquared =
                    ((BinaryFieldElement) x).square();
            Rx = xSquared;
            Rx = Rx.add(curve.getB().divide(Rx));

            Ry = xSquared;
            Ry = Ry.add(l.multiply(Rx));
            Ry = Ry.add(Rx);

            BinaryECPoint R = new BinaryECPoint((BinaryEC) curve, Rx, Ry);

            return R;
        } catch (IncorrectRingElementException ex) {
            return null;
        }

    }

    /**
     * Auxiliar method to compute \(P + Q\), where \(P\) is {@code this}
     * <i>BinaryECPoint</i>, and the <i>BinaryEC</i> \(E\) is non super
     * singular.
     *
     * @return <ul>
     *         <li>a <i>BinaryECPoint</i> \(R\), where \(R = P + Q\) or</li>
     *         <li>{@code null} if {@code this} or {@code Q}
     *         <i>BinaryECPoint</i> are not initialized or</li>
     *         <li>the infinity <i>Point</i> if any exception occurred.</li>
     *         </ul>
     */
    private BinaryECPoint NSSAdd(final BinaryECPoint Q) {
        BinaryFieldElement Rx, Ry;
        BinaryFieldElement l;

        if (!curve.equals(Q.curve)) {
            throw new ArithmeticException(
                "Trying to add points from different" + "Elliptic Curves");
        }

        if (Q.isInfinity()) {
            return this;
        }
        if (isInfinity()) {
            return Q;
        }
        if (equals(Q.inverse())) {
            return (BinaryECPoint) curve.getMultiplicativeIdentity(); /* Point at infinity */
        }
        if (equals(Q)) {
            return square();
        }
        try {
            l = (BinaryFieldElement) y.add(Q.y);
            l = l.divide(x.add(Q.x));

            Rx = l.square();
            Rx = Rx.add(l);
            Rx = Rx.add(x);
            Rx = Rx.add(Q.x);
            Rx = Rx.add(curve.getA());

            Ry = l.multiply(x.add(Rx));
            Ry = Ry.add(Rx);
            Ry = Ry.add(y);

            BinaryECPoint R = new BinaryECPoint((BinaryEC) curve, Rx, Ry);

            return R;
        } catch (IncorrectRingElementException ex) {
            return null;
        }

    }

    /**
     * Auxiliar method to compute \(P + Q\), where \(P\) is {@code this}
     * <i>BinaryECPoint</i>, and the <i>BinaryEC</i> \(E\) is super singular.
     *
     * @return <ul>
     *         <li>a <i>BinaryECPoint</i> \(R\), where \(R = P + Q\) or</li>
     *         <li>{@code null} if {@code this} or {@code Q}
     *         <i>BinaryECPoint</i> are not initialized or</li>
     *         <li>the infinity <i>Point</i> if any exception occurred.</li>
     *         </ul>
     */
    private BinaryECPoint SSAdd(final BinaryECPoint Q) {
        BinaryFieldElement Rx, Ry;
        BinaryFieldElement num, denom;

        if (!curve.equals(Q.curve)) {
            throw new ArithmeticException(
                "Trying to add points from different" + "Elliptic Curves");
        }

        if (Q.isInfinity()) {
            return this;
        }
        if (isInfinity()) {
            return Q;
        }
        if (equals(Q.inverse())) {
            return (BinaryECPoint) curve.getMultiplicativeIdentity();
        }
        if (equals(Q)) {
            return square();
        }
        try {
            num = (BinaryFieldElement) y.add(Q.y);
            denom = (BinaryFieldElement) x.add(Q.x);

            BinaryFieldElement inverseDenom = denom.inverse();
            BinaryFieldElement numOverDenom = num.multiply(inverseDenom);
            Rx = numOverDenom;
            Rx = Rx.square();
            Rx = Rx.add(x);
            Rx = Rx.add(Q.x);

            Ry = numOverDenom;
            Ry = Ry.multiply(x.add(Rx));
            Ry = Ry.add(y);
            Ry = Ry.add(((BinaryEC) curve).getC());

            BinaryECPoint R = new BinaryECPoint((BinaryEC) curve, Rx, Ry);

            return R;

        } catch (IncorrectRingElementException ex) {
            return null;
        }
    }

    /**
     * @see GroupElement#belongsToSameGroup(GroupElement)
     */
    @Override
    public boolean belongsToSameGroup(final GroupElement iq) {
        BinaryECPoint q = (BinaryECPoint) iq;
        return curve.equals(q.getCurve());
    }

    /**
     * @see GroupElement#getGroup()
     */
    @Override
    public BinaryField getGroup() {
        return (BinaryField) curve.getRing();
    }

    /**
     * @see GroupElement#getValue()
     */
    @Override
    public Object getValue() {
        return getX();
    }

    /**
     * @see GroupElement#getIntValue()
     */
    @Override
    public BigInteger getIntValue() {
        return x.getIntValue();
    }

    /**
     * @see ECPoint#getX()
     */
    @Override
    public BinaryFieldElement getX() {
        return (BinaryFieldElement) x;
    }

    /**
     * @see ECPoint#getY()
     */
    @Override
    public BinaryFieldElement getY() {
        return (BinaryFieldElement) y;
    }

    /**
     * @see ECPoint#getOrder()
     */
    @Override
    public BigInteger getOrder() {
        return order;
    }

    /**
     * @see ECPoint#getCurve()
     */
    @Override
    public BinaryEC getCurve() {
        return (BinaryEC) curve;
    }

    /**
     * @see ECPoint#divide(GroupElement)
     */
    @Override
    public BinaryECPoint divide(final GroupElement Q)
            throws ArithmeticException {
        return multiply(Q.inverse());
    }

    /**
     * @see ECPoint#isInfinity()
     */
    @Override
    public boolean isInfinity() {
        return isInfinity;
    }
}
