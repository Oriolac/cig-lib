package cat.udl.cig.structures.ecc;

import cat.udl.cig.exceptions.ConstructionException;
import cat.udl.cig.structures.Group;
import cat.udl.cig.structures.GroupElement;
import cat.udl.cig.structures.Ring;
import cat.udl.cig.structures.RingElement;
import cat.udl.cig.utils.discretelogarithm.BruteForce;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

/**
 * Models a <i>Point</i> \(P\) belonging to a <i>General Elliptic Curve</i>
 * \(E\).
 *
 * @author Víctor Mateu
 * @see ECPoint
 * @see GeneralEC
 */
public class GeneralECPoint implements ECPoint {

    /**
     * The <i>Elliptic Curve</i> \(E\) to which the point \(P\) belongs.
     *
     * @see EC
     */
    final protected GeneralEC curve;

    /**
     * The coordinates \((X:Y)\) of the point. \(X\) and \(Y\) belongs to the
     * Ring {@code this.ec.getRing()}.
     *
     * @see RingElement
     */
    final protected RingElement x, y;

    /**
     * It indicates if {@code this} <i>Point</i> \(P\) is the infinity point or
     * not. If \(P\) is the infinity point, then \(P = (0:1:0)\). If not, \(P =
     * (X:Y:1)\).
     */
    final protected boolean isInfinity;

    /**
     * It represents the order of {@code this} <i>Point</i> \(P\),
     * \(\text{ord}(P)\). Given a BigInteger \(a\) such that \(a =
     * \text{ord}(P)\), then \(a \cdot P = \mathcal{O}\).
     */
    protected BigInteger order;

    private ECPrimeOrderSubgroup ecPrimeOrderSubgroup;

    /**
     * Creates the <i>Infinity Point</i> \(P\) for the <i>GeneralEC</i> \(E\).
     * The <i>Infinity Point</i> has the coordinates \((0:1:0)\). If {@code E}
     * is not initialized, then \(P\) is a null <i>Point</i>.
     *
     * @param E1 the <i>GeneralEC</i> to which \(P\) belongs.
     */
    public GeneralECPoint(final GeneralEC E1) {
        curve = E1;
        // Infinity point (0:1:0)
        x = curve.getRing().getAdditiveIdentity();
        y = curve.getRing().getMultiplicativeIdentity();
        isInfinity = true;
        order = BigInteger.ONE;
    }

    static public GeneralECPoint infinityPoint(@NotNull final GeneralEC E1) {
        return new GeneralECPoint(E1);
    }

    /**
     * Creates a copy of the <i>GeneralECPoint</i> \(P\). This constructor makes
     * a deep copy of \(P\).
     *
     * @param P the <i>GeneralECPoint</i> to be copied.
     */
    public GeneralECPoint(@NotNull final GeneralECPoint P) {
        curve = P.curve;
        x = P.x;
        y = P.y;
        isInfinity = P.isInfinity;
        ecPrimeOrderSubgroup = P.ecPrimeOrderSubgroup;
        order = P.order;
    }

    /**
     * Creates the <i>GeneralECPoint</i> \(P\) for the <i>GeneralEC</i> \(E\).
     * The <i>Point</i> has the coordinates \((x:y:1)\) or \((0:1:0)\) if
     * {@code x} and {@code y} are not initialized. If {@code E} is not
     * initialized or {@code x} and {@code y} are not elements of the same
     * <i>GneralField</i>, then \(P\) is a null <i>Point</i>.
     *
     * @param curve  the <i>GeneralEC</i> to which \(P\) belongs.
     * @param ix     the \(x\) coordinate for \(P\). It must belong to
     *               {@code this.E.getRing()}.
     * @param iy     the \(y\) coordinate for \(P\). It must belong to
     *               {@code this.E.getRing()}.
     * @param iOrder the order of the newly created <i>GeneralECPoint</i>. This
     *               constructor checks if the order is correct. If not, it is
     *               initialized to ONE.
     */
    protected GeneralECPoint(@NotNull final GeneralEC curve, @NotNull final RingElement ix,
                             @NotNull final RingElement iy, @NotNull final BigInteger iOrder) {
        this.curve = curve;
        if (!curve.getRing().containsElement(ix) || !curve.getRing().containsElement(iy)) {
            throw new ConstructionException("The point is not constructed on the correct ring ecc.");
        }
        x = ix;
        y = iy;
        isInfinity = false;
        order = iOrder;
        if (curve.validOrder(ix, iy).isEmpty()) {
            throw new ConstructionException("The point has not valid order.");
        }
    }

    private GeneralECPoint(@NotNull final GeneralEC iE, @NotNull final RingElement ix,
                           @NotNull final RingElement iy, @NotNull final BigInteger iOrder,
                           final boolean iIsInfinity) {
        curve = iE;
        if (!iIsInfinity) {
            x = ix;
            y = iy;
            order = iOrder;
            if (curve.validOrder(ix, iy).isEmpty()) {
                throw new ConstructionException("The point has not valid order.");
            }
            isInfinity = false;
        } else if (!curve.getRing().containsElement(ix) || !curve.getRing().containsElement(iy)) {
            throw new ConstructionException("The point is not constructed on the correct ring ecc.");
        } else {
            x = curve.getRing().getAdditiveIdentity();
            y = curve.getRing().getMultiplicativeIdentity();
            isInfinity = true;
            order = BigInteger.ONE;
        }

    }

    /**
     * Creates the <i>GeneralECPoint</i> \(P\) for the <i>GeneralEC</i> \(E\).
     * The <i>Point</i> has the coordinates \((x:y:1)\) or \((0:1:0)\) if
     * {@code x} and {@code y} are not initialized. If {@code E} is not
     * initialized or {@code x} and {@code y} are not elements of the same
     * <i>GneralField</i>, then \(P\) is a null <i>Point</i>.
     *
     * @param iE the <i>GeneralEC</i> to which \(P\) belongs.
     * @param ix the \(x\) coordinate for \(P\). It must belong to
     *           {@code this.E.getRing()}.
     * @param iy the \(y\) coordinate for \(P\). It must belong to
     *           {@code this.E.getRing()}.
     */
    public GeneralECPoint(final GeneralEC iE, final RingElement ix,
                          final RingElement iy) {
        curve = iE;
        if (!(curve.getRing().containsElement(ix) && curve.getRing().containsElement(iy) && curve.isOnCurve(ix, iy))) {
            throw new ConstructionException("The point does not belong to the curve.");
        }
        x = ix;
        y = iy;
        isInfinity = false;
        order = null;
    }

    /**
     * Returns the <i>EllipticCurve</i> to which {@code this} <i>ECPoint</i>
     * \(P\) belongs.
     *
     * @return an EC, the <i>EllipticCurve</i> to which {@code this}
     * <i>ECPoint</i> \(P\) belongs. That is, \(P \in E(K)\).
     */
    @Override
    public GeneralEC getCurve() {
        return curve;
    }

    /**
     * Returns the coordinate \(x\) of {@code this} <i>ECPoint</i> \(P\).
     *
     * @return a copy of the RingElement representing the \(x\) of {@code this}
     * <i>ECPoint</i> \(P\).
     */
    @Override
    public RingElement getX() {
        return x;
    }

    /**
     * Returns the coordinate \(y\) of {@code this} <i>ECPoint</i> \(P\).
     *
     * @return a copy of the RingElement representing the \(x\) of {@code this}
     * <i>ECPoint</i> \(P\).
     */
    @Override
    public RingElement getY() {
        return y;
    }

    /**
     * @see GroupElement#divide(GroupElement)
     */
    @Override
    public GeneralECPoint divide(final GroupElement iQ)
            throws ArithmeticException {
        GeneralECPoint Q = (GeneralECPoint) iQ;
        if (isInfinity) {
            return Q.inverse();
        }
        if (Q.isInfinity()) {
            return this;
        }
        return multiply(Q.inverse());
    }

    @Override
    public GeneralECPoint square() {
        if (isInfinity) {
            return curve.getMultiplicativeIdentity();
        }
        RingElement[] point = computeDoublePoint();
        Optional<? extends GeneralECPoint> generalECPoint = this.curve.buildElement().setXYCoordinates(point[0], point[1]).build();
        if (generalECPoint.isEmpty()) {
            throw new IllegalStateException();
        }
        return generalECPoint.get();
    }

    private Optional<GeneralECPoint> fromCoordinates(RingElement xCoord, RingElement yCoord) {
        GeneralECPoint point = new GeneralECPoint(curve, xCoord, yCoord);
        if (this.curve.isOnCurve(point))
            return Optional.of(point);
        return Optional.empty();
    }

    private RingElement[] computeDoublePoint() {
        RingElement[] result = new RingElement[2];
        final RingElement lambda = computeLambdaAdditionSamePoint();
        result[0] = lambda.multiply(lambda);
        result[0] = result[0].subtract(this.x).subtract(this.x);
        result[1] = lambda.multiply(this.x.subtract(result[0]));
        result[1] = result[1].subtract(this.y);
        return result;

    }

    private RingElement computeLambdaAdditionSamePoint() {
        final RingElement THREE = this.x.getGroup().THREE();
        RingElement numerador = THREE.multiply(x.pow(BigInteger.TWO)).add(curve.getA());
        RingElement denominador = this.y.add(this.y);
        return numerador.multiply(denominador.inverse());
    }

    @Override
    public GeneralECPoint multiply(final GroupElement iQ)
            throws ArithmeticException {
        if (!(iQ instanceof GeneralECPoint))
            throw new IllegalArgumentException("The point is not a GeneralECPoint.");
        GeneralECPoint Q = (GeneralECPoint) iQ;
        if (!curve.equals(Q.curve))
            throw new ArithmeticException(
                    "Trying to add points from different Elliptic Curves");
        if (Q.isInfinity()) {
            return this;
        } else if (this.isInfinity()) {
            return Q;
        } else if (x.equals(Q.x)) {
            if (y.equals(Q.y)) {
                return square();
            } else {
                return curve.getMultiplicativeIdentity();
            }
        } else {
            return computeDifferentPointAddition(Q);
        }
    }

    private GeneralECPoint computeDifferentPointAddition(GeneralECPoint q) {
        return computeDifferentPointAddition(q.x, q.y);
    }

    private GeneralECPoint computeDifferentPointAddition(final RingElement Bx,
                                                         final RingElement By) {
        RingElement[] result = new RingElement[2];
        final RingElement lambda;
        lambda = computeLambdaAdditionDifferentPoint(Bx, By);
        result[0] = lambda.multiply(lambda);
        result[0] = result[0].subtract(this.x).subtract(Bx);
        result[1] = lambda.multiply(this.x.subtract(result[0]));
        result[1] = result[1].subtract(this.y);
        return this.curve.buildElement().setXYCoordinates(result[0], result[1]).build()
                .orElseThrow(() -> new ConstructionException("Cannot create the new point. It does not belong in the current EC."));
    }

    private RingElement computeLambdaAdditionDifferentPoint(RingElement Bx, RingElement By) {
        final RingElement num;
        final RingElement denom;
        num = By.subtract(this.y);
        denom = Bx.subtract(this.x).inverse();
        return num.multiply(denom);
    }


    @Override
    public boolean belongsToSameGroup(final GroupElement q) {
        return curve.equals(q.getGroup());
    }

    @Override
    public String toString() {
        int z = 1;
        if (isInfinity) {
            z = 0;
        }
        return "(" + x.toString() + ":" + y.toString() + ":" + z + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GeneralECPoint that = (GeneralECPoint) o;
        return isInfinity == that.isInfinity &&
                Objects.equals(curve, that.curve) &&
                Objects.equals(x, that.x) &&
                Objects.equals(y, that.y) &&
                Objects.equals(order, that.order);
    }

    @Override
    public int hashCode() {
        return Objects.hash(curve, x, y, isInfinity, order);
    }

    @Override
    public GeneralECPoint inverse() {
        if (isInfinity) {
            return this;
        }
        return new GeneralECPoint(getCurve(), getX(), getY().opposite());
    }

    /**
     * Returns the result of \(k \cdot P\), where \(P\) is {@code this}
     * <i>Point</i>.
     *
     * @param k a BigInteger number to multiply the <i>Point</i> \(P\).
     * @return a <i>Point</i> \(R\), where \(R = k \cdot P\) or {@code null} if
     * {@code this} is not initialized.
     */
    @Override
    public GeneralECPoint pow(BigInteger k) {
        return peasantRussianPow(k);
    }

    public GeneralECPoint peasantRussianPow(BigInteger k) {
        if (isInfinity()) {
            return this;
        }
        if (order != null && k.compareTo(order) > 0) {
            k = k.mod(order);
        }
        final BigInteger power = k;
        String binaries = power.toString(2);
        binaries = binaries.substring(1);
        GeneralECPoint acc = new GeneralECPoint(this);
        for (char bin : binaries.toCharArray()) {
            acc = acc.multiply(acc);
            if (bin == '1') {
                acc = acc.multiply(this);
            }
        }
        return acc;
    }

    @NotNull
    private GeneralECPoint powNAFMethod(BigInteger k) {
        if (isInfinity()) {
            return this;
        }
        if (order != null && k.compareTo(order) > 0) {
            k = k.mod(order);
        }
        final BigInteger times = k;
        RingElement[] me = toProjective(this);
        Ring ring = me[0].getGroup();
        RingElement[] minusP = toProjective(inverse());
        RingElement[] Q =
                {ring.ZERO(), ring.ONE(), ring.ZERO()};
        ArrayList<Integer> kbits = nonAdjacentForm(times);
        for (int i = kbits.size() - 1; i >= 0; i--) {
            Q = ProjectiveDouble(Q);
            if (kbits.get(i) > 0) {
                Q = projectiveAdd(Q, me);
            }
            if (kbits.get(i) < 0) {
                Q = projectiveAdd(Q, minusP);
            }
        }
        return toECPoint(Q).orElse(null);
    }

    private RingElement[] toProjective(final GeneralECPoint P) {
        RingElement[] result = new RingElement[3];
        result[0] = P.getX();
        result[1] = P.getY();
        result[2] = P.getX().getGroup().ONE();
        return result;
    }

    private Optional<GeneralECPoint> toECPoint(final RingElement[] point) {
        if (point[2].equals(point[2].getGroup().ZERO())) {
            return Optional.of(curve.getMultiplicativeIdentity());
        }
        final RingElement aux = point[2].inverse();
        //final PrimeFieldElement X =
        //    (PrimeFieldElement) E.getRing().toElement(
        //            aux.multiply(point[0]));
        //final PrimeFieldElement Y =
        //    (PrimeFieldElement) E.getRing().toElement(
        //            aux.multiply(point[1]));
        return fromCoordinates(aux.multiply(point[0]), aux.multiply(point[1]));
    }

    private RingElement[] projectiveAdd(final RingElement[] point1,
                                        final RingElement[] point2) {
        Ring ring = point1[2].getGroup();
        final RingElement[] infPoint =
                {ring.ZERO(), ring.ONE(), ring.ZERO()};
        if (point2[2].equals(ring.ZERO())) {
            return point1;
        }
        if (point1[2].equals(ring.ZERO())) {
            return point2;
        }
        if (point1[0].equals(point2[0])) {
            if (point1[1].equals(point2[1])) {
                return ProjectiveDouble(point1);
            } else {
                return infPoint;
            }
        }

        if (point2[2].equals(ring.ONE())) {
            if (point1[2].equals(ring.ONE())) {
                return projectiveAddZs1(point1, point2);
            } else {
                return projectiveAddZ21(point1, point2);
            }
        }
        return projectiveAddDifferentPoints(point1, point2);
    }

    private RingElement[] projectiveAddZs1(final RingElement[] point1,
                                           final RingElement[] point2) {
        Ring ring = point1[0].getGroup();
        final RingElement TWO = ring.ONE().add(ring.ONE());
        final RingElement[] result = new RingElement[3];
        final RingElement u, uu, v, vv, vvv, R, A;

        u = point2[1].subtract(point1[1]);
        uu = u.multiply(u);
        v = point2[0].subtract(point1[0]);
        vv = v.multiply(v);
        vvv = vv.multiply(v);
        R = vv.multiply(point1[0]);
        A =
                uu.subtract(vvv).subtract(TWO.multiply(R));

        result[0] = v.multiply(A);
        result[1] = u.multiply(R.subtract(A)).subtract(vvv.multiply(point1[1]));
        result[2] = vvv;
        return result;
    }

    private RingElement[] projectiveAddZ21(final RingElement[] point1,
                                           final RingElement[] point2) {
        Ring ring = point1[0].getGroup();
        final RingElement TWO = ring.ONE().add(ring.ONE());
        final RingElement[] result = new RingElement[3];
        final RingElement u, uu, v, vv, vvv, R, A;

        u = point2[1].multiply(point1[2]).subtract(point1[1]);
        uu = u.multiply(u);
        v = point2[0].multiply(point1[2]).subtract(point1[0]);
        vv = v.multiply(v);
        vvv = vv.multiply(v);
        R = vv.multiply(point1[0]);
        A = uu.multiply(point1[2]).subtract(vvv)
                .subtract(TWO.multiply(R));

        result[0] = v.multiply(A);
        result[1] =
                u.multiply(R.subtract(A))
                        .subtract(vvv.multiply(point1[1]));
        result[2] = vvv.multiply(point1[2]);

        return result;
    }

    private RingElement[] projectiveAddDifferentPoints(final RingElement[] point1,
                                                       final RingElement[] point2) {
        Ring ring = point1[0].getGroup();
        final RingElement TWO = ring.ONE().add(ring.ONE());
        final RingElement FOUR = ring.THREE().add(ring.ONE());
        final RingElement[] result = new RingElement[3];
        final RingElement U1, U2, S1, S2, ZZ, T, TT, M, R, F, L, LL, G, W;

        U1 = point1[0].multiply(point2[2]); // U1 = X1*Z2
        U2 = point2[0].multiply(point1[2]); // U2 = X2*Z1
        S1 = point1[1].multiply(point2[2]); // S1 = Y1*Z2
        S2 = point2[1].multiply(point1[2]); // S2 = Y2*Z1
        ZZ = point1[2].multiply(point2[2]); // ZZ = Z1*Z2
        T = U1.add(U2); // T = U1+U2
        TT = T.multiply(T); // TT = T^2
        M = S1.add(S2); // M = S1+S2
        R = TT.subtract(U1.multiply(U2))
                .add(curve.getA().multiply(ZZ.multiply(ZZ))); // R = TT-U1*U2+a*ZZ2
        F = ZZ.multiply(M); // F = ZZ*M
        L = M.multiply(F); // L = M*F
        LL = L.multiply(L); // LL = L2
        final RingElement temp = T.add(L);
        G =
                temp.multiply(temp).subtract(TT).subtract(LL);// G = (T+L)^2-TT-LL
        W =
                R.multiply(R).multiply(TWO).subtract(G);// W = 2*R^2-G
        result[0] = TWO.multiply(F).multiply(W); // X3 = 2*F*W
        result[1] =
                R.multiply(G.subtract(TWO.multiply(W))).subtract(TWO.multiply(LL)); // Y3 = R*(G-2*W)-2*LL
        result[2] = FOUR.multiply(F.pow(BigInteger.valueOf(3)));// Z3 = 4*F*F2
        return result;
    }

    private RingElement[] ProjectiveDouble(final RingElement[] point) {
        Ring ring = point[2].getGroup();
        if (point[2].equals(ring.ZERO())) {
            return point;
        }

        if (point[2].equals(ring.ONE())) {
            return projectiveDoubleZ1(point);
        } else {
            return projectiveDouble(point);
        }
    }

    private RingElement[] projectiveDoubleZ1(final RingElement[] point) {
        Ring ring = point[0].getGroup();
        final RingElement THREE = ring.THREE();
        final RingElement FOUR = ring.THREE().add(ring.ONE());
        final RingElement[] result = new RingElement[3];
        final RingElement XX, w, YY, R, RR, B, h;

        XX = point[0].multiply(point[0]);
        w = curve.getA().add(THREE.multiply(XX));
        YY = point[1].multiply(point[1]);
        R = YY.add(YY);
        RR = R.multiply(R);
        B = point[0].add(R).pow(BigInteger.TWO).subtract(XX).subtract(RR);
        h = w.multiply(w).subtract(B).subtract(B);
        result[0] = h.add(h).multiply(point[1]);
        result[1] = w.multiply(B.subtract(h)).subtract(RR).subtract(RR);
        result[2] = FOUR.multiply(point[1]).multiply(R);
        return result;
    }

    private RingElement[] projectiveDouble(final RingElement[] point) {
        Ring ring = point[0].getGroup();
        final RingElement TWO = ring.ONE().add(ring.ONE());
        final RingElement THREE = ring.THREE();
        final RingElement XX, ZZ, w, s, R, RR, B, h;
        final RingElement[] result = new RingElement[3];

        XX = point[0].multiply(point[0]);
        ZZ = point[2].multiply(point[2]);
        w = curve.getA().multiply(ZZ)
                .add(THREE.multiply(XX));
        s = TWO.multiply(point[1]).multiply(point[2]);
        R = point[1].multiply(s);
        RR = R.multiply(R);
        B = point[0].add(R).pow(BigInteger.TWO).subtract(XX).subtract(RR);
        h = w.multiply(w).subtract(TWO.multiply(B));
        result[0] = h.multiply(s);
        result[1] = w.multiply(B.subtract(h)).subtract(RR)
                .subtract(RR);
        result[2] = s.pow(BigInteger.valueOf(3));
        return result;
    }

    protected ArrayList<Integer> nonAdjacentForm(BigInteger k) {
        ArrayList<Integer> kBits = new ArrayList<>();
        return getIntegers(k, kBits);
    }

    static ArrayList<Integer> getIntegers(BigInteger k, ArrayList<Integer> kBits) {
        final BigInteger TWO = BigInteger.valueOf(2);
        final BigInteger FOUR = BigInteger.valueOf(4);
        BigInteger aux;
        while (k.bitLength() > 0) {
            if (k.testBit(0)) {
                aux = (TWO.subtract(k.mod(FOUR)));
                kBits.add(aux.intValue());
                k = k.subtract(aux);
            } else {
                kBits.add(0);
            }
            k = k.shiftRight(1);

        }
        return kBits;
    }

    /**
     * @see GroupElement#getGroup()
     */
    @Override
    public Group getGroup() {
        return curve;
    }

    /**
     * @see GroupElement#getValue()
     */
    @Override
    public Object getValue() {
        return x.getValue();
    }

    /**
     * @see GroupElement#getIntValue()
     */
    @Override
    public BigInteger getIntValue() {
        return x.getIntValue();
    }

    /**
     * @see ECPoint#getOrder()
     */
    @Override
    public BigInteger getOrder() {
        if (order == null)
            order = new BruteForce(this).algorithm(this.curve.getMultiplicativeIdentity()).orElseThrow();
        return order;
    }

    /**
     * @see ECPoint#isInfinity()
     */
    @Override
    public boolean isInfinity() {
        return isInfinity;
    }

    @Override
    public ECSubgroup getSubgroup() {
        if (this.ecPrimeOrderSubgroup == null) {
            this.ecPrimeOrderSubgroup = new ECPrimeOrderSubgroup(this.curve, getOrder(), this);
        }
        return null;
    }

    @Override
    public byte[] toBytes() throws UnsupportedOperationException {
        byte[] bytes = new byte[this.curve.sizeOfSubgroups.get(0).bitLength() / 8 * 2 + 2];
        byte[] x = getX().toBytes();
        byte[] y = getY().toBytes();
        System.arraycopy(x, 0, bytes, bytes.length / 2 - x.length, x.length);
        System.arraycopy(y, 0, bytes, bytes.length - y.length, y.length);
        return bytes;
    }

    @Override
    public int compareTo(@NotNull GroupElement o) {
        return 0;
    }
}
