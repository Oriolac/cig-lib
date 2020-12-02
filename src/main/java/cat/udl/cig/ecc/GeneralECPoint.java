package cat.udl.cig.ecc;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Objects;

import cat.udl.cig.fields.Group;
import cat.udl.cig.fields.RingElement;

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
    final protected GeneralEC E;

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

    /**
     * Creates the <i>Infinity Point</i> \(P\) for the <i>GeneralEC</i> \(E\).
     * The <i>Infinity Point</i> has the coordinates \((0:1:0)\). If {@code E}
     * is not initialized, then \(P\) is a null <i>Point</i>.
     *
     * @param E1 the <i>GeneralEC</i> to which \(P\) belongs.
     */
    public GeneralECPoint(final GeneralEC E1) {
        E = E1;
        // Infinity point (0:1:0)
        x = E.getRing().getElementZERO();
        y = E.getRing().getNeuterElement();
        isInfinity = true;
        order = BigInteger.ONE;
    }

    /**
     * Creates a copy of the <i>GeneralECPoint</i> \(P\). This constructor makes
     * a deep copy of \(P\).
     *
     * @param P the <i>GeneralECPoint</i> to be copied.
     */
    public GeneralECPoint(final GeneralECPoint P) {
        E = P.E;
        x = P.x;
        y = P.y;
        isInfinity = P.isInfinity;
        order = P.order;
    }

    /**
     * Creates the <i>GeneralECPoint</i> \(P\) for the <i>GeneralEC</i> \(E\).
     * The <i>Point</i> has the coordinates \((x:y:1)\) or \((0:1:0)\) if
     * {@code x} and {@code y} are not initialized. If {@code E} is not
     * initialized or {@code x} and {@code y} are not elements of the same
     * <i>GneralField</i>, then \(P\) is a null <i>Point</i>.
     *
     * @param E      the <i>GeneralEC</i> to which \(P\) belongs.
     * @param ix     the \(x\) coordinate for \(P\). It must belong to
     *               {@code this.E.getRing()}.
     * @param iy     the \(y\) coordinate for \(P\). It must belong to
     *               {@code this.E.getRing()}.
     * @param iOrder the order of the newly created <i>GeneralECPoint</i>. This
     *               constructor checks if the order is correct. If not, it is
     *               initialized to ONE.
     */
    public GeneralECPoint(final GeneralEC E, final RingElement ix,
                          final RingElement iy, final BigInteger iOrder) {
        this.E = E;
        RingElement elem = E.getRing().getNeuterElement();
        if (ix.belongsToSameGroup(elem) && iy.belongsToSameGroup(elem)) {
            x = ix;
            y = iy;
            isInfinity = false;
            order = iOrder;
        } else {
            // Infinity point (0:1:0)
            //System.out.println("Mal generador 2");
            x = E.getRing().getElementZERO();
            y = E.getRing().getNeuterElement();
            isInfinity = true;
            order = BigInteger.ONE;
        }
    }

    protected GeneralECPoint(final GeneralEC iE, final RingElement ix,
                             final RingElement iy, final BigInteger iOrder,
                             final boolean iIsInfinity, final boolean safe) {
        E = iE;
        if (!iIsInfinity) {
            x = ix;
            y = iy;
            order = iOrder;
            isInfinity = false;
        } else {
            // Infinity point (0:1:0)
            x = E.getRing().getElementZERO();
            y = E.getRing().getNeuterElement();
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
        E = iE;
        RingElement elem = E.getRing().getNeuterElement();
        if (ix.belongsToSameGroup(elem) && iy.belongsToSameGroup(elem)) {
            x = ix;
            y = iy;
            isInfinity = false;
            order = null;
        } else { // Infinity point (0:1:0)
            //System.out.println("Mal generador");
            x = E.getRing().getElementZERO();
            y = E.getRing().getNeuterElement();
            isInfinity = true;
            order = BigInteger.ONE;
        }
    }

    public GeneralECPoint(final GeneralEC iE, final RingElement ix,
                          final RingElement iy, final BigInteger iorder,
                          final boolean iIsInfinity) {
        E = iE;
        RingElement elem = E.getRing().getNeuterElement();
        if (!iIsInfinity && ix.belongsToSameGroup(elem)
                && iy.belongsToSameGroup(elem)) {
            x = ix;
            y = iy;
            isInfinity = false;
            order = iorder;
        } else {
            // Infinity point (0:1:0)
            //System.out.println("Mal generador 2");
            x = E.getRing().getElementZERO();
            y = E.getRing().getNeuterElement();
            isInfinity = true;
            order = BigInteger.ONE;
        }
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
        return E;
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
     * @see cat.udl.cig.fields.GroupElement#divide(cat.udl.cig.fields.GroupElement)
     */
    @Override
    public GeneralECPoint divide(final cat.udl.cig.fields.GroupElement iQ)
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
            return E.getNeuterElement();
        }

        BigInteger[] point =
                computeDoublePoint(x.getIntValue(), y.getIntValue());
        return new GeneralECPoint(E, E.getRing().toElement(point[0]), E
                .getRing().toElement(point[1]), order, false, true);
    }

    private BigInteger[] computeDoublePoint(final BigInteger Bx,
                                            final BigInteger By) {
        final BigInteger THREE = BigInteger.valueOf(3);
        BigInteger[] result = new BigInteger[2];
        BigInteger num;
        final BigInteger denom;
        final BigInteger modulo = x.getGroup().getSize();

        num = Bx.multiply(Bx.multiply(THREE)).mod(modulo);
        num = num.add((BigInteger) E.getA().getValue()).mod(modulo);
        denom = By.add(By).modInverse(modulo);

        result[1] = num.multiply(denom).mod(modulo);
        result[0] = result[1].multiply(result[1]).mod(modulo);
        result[0] = result[0].subtract(Bx).subtract(Bx).mod(modulo);

        result[1] = result[1].multiply(Bx.subtract(result[0])).mod(modulo);
        result[1] = result[1].subtract(By).mod(modulo);
        return result;

    }

    @Override
    public GeneralECPoint multiply(final cat.udl.cig.fields.GroupElement iQ)
            throws ArithmeticException {
        GeneralECPoint Q = (GeneralECPoint) iQ;
        if (!E.equals(Q.E)) {
            throw new ArithmeticException(
                    "Trying to add points from different Elliptic Curves");
        }

        if (Q.isInfinity()) {
            return this;
        } else if (isInfinity()) {
            return Q;
        } else if (x.equals(Q.x)) {
            if (y.equals(Q.y)) {
                return square();
            } else {
                return E.getNeuterElement();
            }
        } else {
            BigInteger[] point =
                    computePointAddition(Q.x.getIntValue(), Q.y.getIntValue());
            return new GeneralECPoint(E, E.getRing().toElement(point[0]),
                    E.getRing().toElement(point[1]), order, false, true);
            //return computePointAddition(Q);
        }
    }

    private BigInteger[] computePointAddition(final BigInteger Bx,
                                              final BigInteger By) {
        final BigInteger[] result = new BigInteger[2];
        final BigInteger Mx = x.getIntValue();
        final BigInteger My = y.getIntValue();
        final BigInteger num, denom;
        final BigInteger modulo = x.getGroup().getSize();

        num = By.subtract(My).mod(modulo);
        denom = Bx.subtract(Mx).modInverse(modulo);

        result[1] = num.multiply(denom).mod(modulo);
        result[0] = result[1].multiply(result[1]).mod(modulo);
        result[0] = result[0].subtract(Mx).subtract(Bx).mod(modulo);

        result[1] = result[1].multiply(Mx.subtract(result[0])).mod(modulo);
        result[1] = result[1].subtract(My).mod(modulo);
        return result;

    }


    @Override
    public boolean belongsToSameGroup(final cat.udl.cig.fields.GroupElement q) {
        return E.equals(q.getGroup());
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
                Objects.equals(E, that.E) &&
                Objects.equals(x, that.x) &&
                Objects.equals(y, that.y) &&
                Objects.equals(order, that.order);
    }

    @Override
    public int hashCode() {
        return Objects.hash(E, x, y, isInfinity, order);
    }

    @Override
    public GeneralECPoint inverse() {
        if (isInfinity) {
            return this;
        }
        return new GeneralECPoint(getCurve(), getX(), getY().opposite(),
                order, false, true);
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
        if (isInfinity()) {
            return this;
        }

        if (order != null && k.compareTo(order) >= 0) {
            k = k.mod(order);
        }
        BigInteger[] me = toProjective(this);
        BigInteger[] minusP = toProjective(inverse());
        BigInteger[] Q =
                {BigInteger.ZERO, BigInteger.ONE, BigInteger.ZERO};
        ArrayList<Integer> kbits = NAF(k);
        for (int i = kbits.size() - 1; i >= 0; i--) {
            Q = ProjectiveDouble(Q);
            if (kbits.get(i) > 0) {
                Q = ProjectiveAdd(Q, me);
            }
            if (kbits.get(i) < 0) {

                Q = ProjectiveAdd(Q, minusP);
            }
        }

        return toECPoint(Q);
    }

    private BigInteger[] toProjective(final GeneralECPoint P) {
        BigInteger[] result = new BigInteger[3];
        result[0] = P.getX().getIntValue();
        result[1] = P.getY().getIntValue();
        result[2] = BigInteger.ONE;
        return result;
    }

    private GeneralECPoint toECPoint(final BigInteger[] point) {
        if (point[2].equals(BigInteger.ZERO)) {
            return E.getNeuterElement();
        }

        final BigInteger aux = point[2].modInverse(E.getRing().getSize());
        //final PrimeFieldElement X =
        //    (PrimeFieldElement) E.getRing().toElement(
        //            aux.multiply(point[0]));
        //final PrimeFieldElement Y =
        //    (PrimeFieldElement) E.getRing().toElement(
        //            aux.multiply(point[1]));
        return new GeneralECPoint(E,
                E.getRing().toElement(aux.multiply(point[0])),
                E.getRing().toElement(aux.multiply(point[1])),
                order, false, true);
    }

    private BigInteger[] ProjectiveAdd(final BigInteger[] point1,
                                       final BigInteger[] point2) {
        final BigInteger[] infPoint =
                {BigInteger.ZERO, BigInteger.ONE, BigInteger.ZERO};
        if (point2[2].equals(BigInteger.ZERO)) {
            return point1;
        }
        if (point1[2].equals(BigInteger.ZERO)) {
            return point2;
        }
        if (point1[0].equals(point2[0])) {
            if (point1[1].equals(point2[1])) {
                return ProjectiveDouble(point1);
            } else {
                return infPoint;
            }
        }

        if (point2[2].equals(BigInteger.ONE)) {
            if (point1[2].equals(BigInteger.ONE)) {
                return projectiveAddZs1(point1, point2);
            } else {
                return projectiveAddZ21(point1, point2);
            }
        }
        return projectiveAdd(point1, point2);
    }

    private BigInteger[] projectiveAddZs1(final BigInteger[] point1,
                                          final BigInteger[] point2) {
        final BigInteger TWO = BigInteger.valueOf(2);
        final BigInteger[] result = new BigInteger[3];
        final BigInteger u, uu, v, vv, vvv, R, A;
        final BigInteger modulo = x.getGroup().getSize();

        u = point2[1].subtract(point1[1]).mod(modulo);
        uu = u.multiply(u).mod(modulo);
        v = point2[0].subtract(point1[0]).mod(modulo);
        vv = v.multiply(v).mod(modulo);
        vvv = vv.multiply(v).mod(modulo);
        R = vv.multiply(point1[0]).mod(modulo);
        A =
                uu.subtract(vvv).subtract(TWO.multiply(R).mod(modulo))
                        .mod(modulo);

        result[0] = v.multiply(A).mod(modulo);
        result[1] =
                u.multiply(R.subtract(A).mod(modulo)).mod(modulo)
                        .subtract(vvv.multiply(point1[1]).mod(modulo)).mod(modulo);
        result[2] = vvv;
        return result;
    }

    private BigInteger[] projectiveAddZ21(final BigInteger[] point1,
                                          final BigInteger[] point2) {
        final BigInteger TWO = BigInteger.valueOf(2);
        final BigInteger[] result = new BigInteger[3];
        final BigInteger u, uu, v, vv, vvv, R, A;
        final BigInteger modulo = x.getGroup().getSize();

        u =
                point2[1].multiply(point1[2]).mod(modulo).subtract(point1[1])
                        .mod(modulo);
        uu = u.multiply(u).mod(modulo);
        v =
                point2[0].multiply(point1[2]).mod(modulo).subtract(point1[0])
                        .mod(modulo);
        vv = v.multiply(v).mod(modulo);
        vvv = vv.multiply(v).mod(modulo);
        R = vv.multiply(point1[0]).mod(modulo);
        A =
                uu.multiply(point1[2]).mod(modulo).subtract(vvv)
                        .subtract(TWO.multiply(R).mod(modulo)).mod(modulo);

        result[0] = v.multiply(A).mod(modulo);
        result[1] =
                u.multiply(R.subtract(A).mod(modulo)).mod(modulo)
                        .subtract(vvv.multiply(point1[1]).mod(modulo)).mod(modulo);
        result[2] = vvv.multiply(point1[2]).mod(modulo);

        return result;
    }

    private BigInteger[] projectiveAdd(final BigInteger[] point1,
                                       final BigInteger[] point2) {
        final BigInteger TWO = BigInteger.valueOf(2);
        final BigInteger FOUR = BigInteger.valueOf(4);
        final BigInteger[] result = new BigInteger[3];
        final BigInteger U1, U2, S1, S2, ZZ, T, TT, M, R, F, L, LL, G, W;
        final BigInteger modulo = x.getGroup().getSize();

        U1 = point1[0].multiply(point2[2]).mod(modulo);// U1 = X1*Z2
        U2 = point2[0].multiply(point1[2]).mod(modulo);// U2 = X2*Z1
        S1 = point1[1].multiply(point2[2]).mod(modulo);// S1 = Y1*Z2
        S2 = point2[1].multiply(point1[2]).mod(modulo);// S2 = Y2*Z1
        ZZ = point1[2].multiply(point2[2]).mod(modulo);// ZZ = Z1*Z2
        T = U1.add(U2).mod(modulo);// T = U1+U2
        TT = T.multiply(T).mod(modulo);// TT = T^2
        M = S1.add(S2).mod(modulo);// M = S1+S2
        R =
                TT.subtract(U1.multiply(U2).mod(modulo))
                        .add(
                                E.getA().getIntValue()
                                        .multiply(ZZ.multiply(ZZ).mod(modulo)).mod(modulo))
                        .mod(modulo);// R = TT-U1*U2+a*ZZ2
        F = ZZ.multiply(M).mod(modulo);// F = ZZ*M
        L = M.multiply(F).mod(modulo);// L = M*F
        LL = L.multiply(L).mod(modulo);// LL = L2
        final BigInteger temp = T.add(L);
        G =
                temp.multiply(temp).mod(modulo).subtract(TT).subtract(LL)
                        .mod(modulo);// G = (T+L)^2-TT-LL
        W =
                R.multiply(R).mod(modulo).multiply(TWO).subtract(G)
                        .mod(modulo);// W = 2*R^2-G
        result[0] = TWO.multiply(F).mod(modulo).multiply(W).mod(modulo); // X3 =
        // 2*F*W
        result[1] =
                R.multiply(G.subtract(TWO.multiply(W).mod(modulo)).mod(modulo))
                        .mod(modulo).subtract(TWO.multiply(LL).mod(modulo))
                        .mod(modulo);// Y3 = R*(G-2*W)-2*LL
        result[2] = FOUR.multiply(F.pow(3).mod(modulo)).mod(modulo);// Z3 =
        // 4*F*F2

        return result;
    }

    private BigInteger[] ProjectiveDouble(final BigInteger[] point) {
        if (point[2].equals(BigInteger.ZERO)) {
            return point;
        }

        if (point[2].equals(BigInteger.ONE)) {
            return projectiveDoubleZ1(point);
        } else {
            return projectiveDouble(point);
        }
    }

    private BigInteger[] projectiveDoubleZ1(final BigInteger[] point) {
        final BigInteger THREE = BigInteger.valueOf(3);
        final BigInteger FOUR = BigInteger.valueOf(4);
        final BigInteger[] result = new BigInteger[3];
        final BigInteger XX, w, YY, R, RR, B, h;
        final BigInteger modulo = x.getGroup().getSize();

        XX = point[0].multiply(point[0]).mod(modulo);
        w = E.getA().getIntValue().add(THREE.multiply(XX)).mod(modulo);
        YY = point[1].multiply(point[1]).mod(modulo);
        R = YY.add(YY).mod(modulo);
        RR = R.multiply(R).mod(modulo);
        B =
                point[0].add(R).pow(2).mod(modulo).subtract(XX).subtract(RR)
                        .mod(modulo);
        h = w.multiply(w).mod(modulo).subtract(B).subtract(B).mod(modulo);
        result[0] = h.add(h).multiply(point[1]).mod(modulo);
        result[1] =
                w.multiply(B.subtract(h).mod(modulo)).mod(modulo).subtract(RR)
                        .subtract(RR).mod(modulo);
        result[2] =
                FOUR.multiply(point[1]).mod(modulo).multiply(R).mod(modulo);
        return result;
    }

    private BigInteger[] projectiveDouble(final BigInteger[] point) {
        final BigInteger TWO = BigInteger.valueOf(2);
        final BigInteger THREE = BigInteger.valueOf(3);
        final BigInteger XX, ZZ, w, s, R, RR, B, h;
        final BigInteger[] result = new BigInteger[3];
        final BigInteger modulo = x.getGroup().getSize();

        XX = point[0].multiply(point[0]).mod(modulo);
        ZZ = point[2].multiply(point[2]).mod(modulo);
        w =
                E.getA().getIntValue().multiply(ZZ).mod(modulo)
                        .add(THREE.multiply(XX)).mod(modulo);
        s =
                TWO.multiply(point[1]).mod(modulo).multiply(point[2])
                        .mod(modulo);
        // ss = s.pow(2).mod(modulo);
        R = point[1].multiply(s).mod(modulo);
        RR = R.multiply(R).mod(modulo);
        B =
                point[0].add(R).pow(2).mod(modulo).subtract(XX).subtract(RR)
                        .mod(modulo);
        h =
                w.multiply(w).mod(modulo).subtract(TWO.multiply(B))
                        .mod(modulo);

        result[0] = h.multiply(s).mod(modulo);
        result[1] =
                w.multiply(B.subtract(h).mod(modulo)).mod(modulo).subtract(RR)
                        .subtract(RR).mod(modulo);
        result[2] = s.pow(3).mod(modulo);
        return result;
    }

    protected ArrayList<Integer> NAF(BigInteger k) {
        ArrayList<Integer> kBits = new ArrayList<>();
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
     * @see cat.udl.cig.fields.GroupElement#getGroup()
     */
    @Override
    public Group getGroup() {
        return E;
    }

    /**
     * @see cat.udl.cig.fields.GroupElement#getValue()
     */
    @Override
    public Object getValue() {
        return x.getValue();
    }

    /**
     * @see cat.udl.cig.fields.GroupElement#getIntValue()
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
        return order;
    }

    /**
     * @see ECPoint#isInfinity()
     */
    @Override
    public boolean isInfinity() {
        return isInfinity;
    }

    protected void setOrder(final BigInteger ord1) {
        order = ord1;
    }

    @Override
    public byte[] toBytes() throws UnsupportedOperationException {
        byte[] bytes = new byte[this.E.cardFactors.get(0).bitLength() / 8 * 2 + 2];
        byte[] x = getX().toBytes();
        byte[] y = getY().toBytes();
        System.arraycopy(x, 0, bytes, bytes.length / 2 - x.length, x.length);
        System.arraycopy(y, 0, bytes, bytes.length - y.length, y.length);
        return bytes;
    }
}
