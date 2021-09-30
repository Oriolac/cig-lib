package cat.udl.cig.structures.ecc;

import cat.udl.cig.exceptions.ConstructionException;
import cat.udl.cig.exceptions.IncorrectRingElementException;
import cat.udl.cig.operations.wrapper.data.Pair;
import cat.udl.cig.structures.*;
import cat.udl.cig.structures.builder.ecc.ECPointBuilder;
import cat.udl.cig.utils.Polynomial;
import cat.udl.cig.utils.discretelogarithm.BabyStepGiantStep;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Models an <i>Elliptic Curve</i> \(E\) of the form \(y^{2} = x^{3} + ax + b\)
 * over a <i>GeneralField</i>.
 *
 * @author M.Àngels Cerveró
 * @author Ricard Garra
 * @author Víctor Mateu
 * @see EllipticCurveInt
 */
public class EllipticCurve implements EllipticCurveInt {

    /**
     * The <i>Ring</i> or <i>Field</i> over which the <i>Elliptic Curve</i>
     * \(E\) is described.
     *
     * @see Ring
     */
    protected final Ring ring;

    /**
     * Enter belonging to the Ring {@link #ring} defining the <i>Elliptic Curve</i>
     * \(E\).
     *
     * @see RingElement
     */

    private final RingElement A;
    private final RingElement B;
    private final RingElement C;

    /**
     * The Infinity point, also used as the neuter element of th group
     */
    private final EllipticCurvePoint infintiyPoint;
    private final boolean onlyOneGroup;
    private final Set<ECSubgroup> subgroups;
    private BigInteger size;
    private final boolean isSupersingular;

    public EllipticCurve(@NotNull final Ring ring, @NotNull final RingElement A, @NotNull final RingElement B) {
        this(ring, A, B, null, false);
    }

    public EllipticCurve(@NotNull Ring ring, @NotNull RingElement A, @NotNull RingElement B, @NotNull RingElement C) {
        if (ring.getSize().equals(BigInteger.TWO) || ring.getSize().equals(BigInteger.valueOf(3))) {
            throw new ConstructionException("The ring must not be 2 or 3");
        }
        if (!ring.containsElement(A) || !ring.containsElement(B) || !ring.containsElement(C)) {
            throw new ConstructionException("The coefficients does not belong to the same group.");
        }
        this.ring = ring;
        this.A = A;
        this.B = B;
        this.C = C;
        this.infintiyPoint = new EllipticCurvePoint(this);
        this.onlyOneGroup = false;
        this.subgroups = new HashSet<>();
        BigInteger characteristic = ring.getCharacteristic();
        if (BigInteger.TWO.equals(characteristic)) {
            isSupersingular = checkChar2IsSupersingular();
        } else {
            isSupersingular = false; //TODO
        }
        checkInvariant();
    }

    /*
    public EllipticCurve(WeierstrassFormPart rightPart, WeierstrassFormPart leftPart) {
        rightPart = rightPart;
        leftPart = leftPart;
    }
     */

    private boolean checkChar2IsSupersingular() {
        BigInteger res = ring.getSize().add(BigInteger.ONE).subtract(this.getSize());
        return res.mod(BigInteger.TWO).equals(BigInteger.ZERO);
    }


    public EllipticCurve(Ring ring, RingElement A, RingElement B, BigInteger size) {
        this(ring, A, B, size, false);
    }

    private EllipticCurve(Ring ring, RingElement A, RingElement B, BigInteger order, boolean onlyOneGroup) {
        if (ring.getSize().equals(BigInteger.valueOf(2)) ||
                ring.getSize().equals(BigInteger.valueOf(3))) {
            throw new ConstructionException("The ring must not be 2 or 3");
        }
        if (!ring.containsElement(A) || !ring.containsElement(B)) {
            throw new ConstructionException("The coefficients does not belong to the same group.");
        }
        this.ring = ring;
        this.A = A;
        this.B = B;
        this.C = null;
        infintiyPoint = new EllipticCurvePoint(this);
        this.onlyOneGroup = onlyOneGroup;
        if (order != null) {
            if (!validHasseTheorem(order))
                throw new ConstructionException("Not valid size for Hasse Theorem.");
            this.size = order;
        }
        subgroups = new HashSet<>();
        if (ring.getCharacteristic().equals(BigInteger.valueOf(3L))) {
            isSupersingular = !A.equals(ring.getAdditiveIdentity());
        } else {
            isSupersingular = false;
        }
        checkInvariant();
    }

    static public Pair<EllipticCurve, EllipticCurvePoint> EllipticCurveGeneratorOnlyOneSubgroup(Ring ring, RingElement A, RingElement B, BigInteger order, RingElement x, RingElement y) {
        EllipticCurve curve = new EllipticCurve(ring, A, B, order, true);
        EllipticCurvePoint point = new EllipticCurvePoint(curve, x, y, order);
        curve.createSubgroupOrder(point, order);
        return new Pair<>(curve, point);
    }

    // Check if existing subgroup uncontrolled.
    protected void createSubgroupOrder(EllipticCurvePoint point, BigInteger order) {
        if (this.onlyOneGroup && this.subgroups.size() > 0)
            throw new ConstructionException("Cannot create another subgroup order. OnlyOneGroup flag is activated.");
        ECSubgroup subgroup = new ECPrimeOrderSubgroup(this, order, point);
        this.subgroups.add(subgroup);
    }

    private void checkInvariant() {
        RingElement invariant = getDiscriminant();
        if (ring.getAdditiveIdentity().equals(invariant))
            throw new ConstructionException("Invariant cannot be additive identity.");
    }

    /**
     * Creates a copy of the <i>GeneralEC</i> \(E\). This constructor makes a
     * deep copy of \(E\).
     *
     * @param E the <i>GeneralEC</i> to be copied.
     */
    public EllipticCurve(final EllipticCurve E) {
        ring = E.ring;
        this.A = E.A;
        this.B = E.B;
        this.C = E.C;
        this.size = E.size;
        this.onlyOneGroup = E.onlyOneGroup;
        this.subgroups = E.subgroups;
        infintiyPoint = new EllipticCurvePoint(this);
        this.isSupersingular = E.isSupersingular;
    }


    @Override
    public EllipticCurvePoint getMultiplicativeIdentity() {
        return infintiyPoint;
    }


    @Override
    public boolean isOnCurve(final ECPoint P) {
        return P.getCurve().equals(this) && isOnCurveAux(P.getX(), P.getY());
    }

    @Override
    public boolean isOnCurve(final RingElement ix, final RingElement iy) {
        return ix.getGroup().equals(ring) && iy.getGroup().equals(ring) && isOnCurveAux(ix, iy);

    }

    /**
     * Auxiliar method to check if point \(P = (x, y)\) belongs to {@code this}
     * <i>GeneralEC</i> \(E\).
     *
     * @param x a GeneralFieldElement representing the first coordinate of the
     *          point \(P\).
     * @param y a GeneralFieldElement representing the second coordinate of
     *          the point \(P\).
     * @return {@code true} if \(P = (x, y) \in E(K)\); {@code false} otherwise.
     */
    // y^2 = x^3 + ax + b
    private boolean isOnCurveAux(final RingElement x,
                                 final RingElement y) {
        RingElement leftPart;
        RingElement rightPart;
        try {
            leftPart = y.pow(BigInteger.valueOf(2));
            rightPart = x.pow(BigInteger.valueOf(3));
            rightPart = rightPart.add(getA().multiply(x));
            rightPart = rightPart.add(getB());
        } catch (IncorrectRingElementException ex) {
            return false;
        }
        return leftPart.equals(rightPart);
    }


    @Override
    public ArrayList<? extends EllipticCurvePoint> liftX(final RingElement x) {

        try {
            RingElement y;
            EllipticCurvePoint P;
            ArrayList<? extends RingElement> ySquareRoots;
            // y^2 = x^3 + ax + b

            y = x.pow(BigInteger.valueOf(3));
            y = y.add(getA().multiply(x));
            y = y.add(getB());
            ySquareRoots = y.squareRoot();
            if (ySquareRoots.isEmpty()) {
                return new ArrayList<>();
            }
            return ySquareRoots.stream().map(newY -> new EllipticCurvePoint(this, x, newY)).filter(this::isOnCurve).collect(Collectors.toCollection(ArrayList::new));
        } catch (IncorrectRingElementException ex) {
            return new ArrayList<>();
        }
    }

    public RingElement getDiscriminant() {
        RingElement invariant = ring.getAdditiveIdentity();
        if (ring instanceof ExtensionField) {
            PrimeField field = ((ExtensionField) ring).getField();
            ExtensionFieldElement el4 = new ExtensionFieldElement(((ExtensionField) ring),
                    new Polynomial.PolynomialBuilder().addTerm(0, field.buildElement().setValue(4).build().orElseThrow()).build());
            ExtensionFieldElement el27 = new ExtensionFieldElement(((ExtensionField) ring),
                    new Polynomial.PolynomialBuilder().addTerm(0, field.buildElement().setValue(27).build().orElseThrow()).build());
            ExtensionFieldElement elA = (ExtensionFieldElement) getA().pow(BigInteger.valueOf(3));
            ExtensionFieldElement elB = (ExtensionFieldElement) getB().pow(BigInteger.TWO);
            ExtensionFieldElement el_16 = new ExtensionFieldElement(((ExtensionField) ring),
                    new Polynomial.PolynomialBuilder().addTerm(0, field.buildElement().setValue(16).build().orElseThrow().opposite()).build());
            invariant = elA.multiply(el4).add(elB.multiply(el27)).multiply(el_16);
        }
        if (ring instanceof PrimeField) {
            PrimeField field = ((PrimeField) ring);
            PrimeFieldElement elA = (PrimeFieldElement) getA().pow(BigInteger.valueOf(3));
            PrimeFieldElement elB = (PrimeFieldElement) getB().pow(BigInteger.TWO);
            PrimeFieldElement el4 = field.buildElement().setValue(4).build().orElseThrow();
            PrimeFieldElement el27 = field.buildElement().setValue(27).build().orElseThrow();
            PrimeFieldElement el_16 = field.buildElement().setValue(16).build().orElseThrow().opposite();
            invariant = elA.multiply(el4).add(elB.multiply(el27)).multiply(el_16);
        }
        return invariant;
    }

    @Override
    public String toString() {
        String content;
        if (ring instanceof ExtensionField) {
            content =
                    "Elliptic Curve: y\u00B2 = x\u00B3 + ("
                            + getA().toString() + ")x + (" + getB().toString()
                            + ")" + " with modulus polynomial " + ((ExtensionField) this.ring).getReducingPolynomial();
        } else {
            content =
                    "Elliptic Curve: y\u00B2 = x\u00B3 + " + getA().toString()
                            + "x + " + getB().toString();
        }
        return content;
    }

    /**
     * Returns the cardinality of \(E(k)\), if initialized.
     *
     * @return a BigInteger with the value {@code this.cardinality} or
     * {@code null} if {@code this} is not initialized.
     */
    @Override
    public BigInteger getSize() {
        if (this.size != null)
            return this.size;
        BigInteger size = BigInteger.ONE;
        List<EllipticCurvePoint> gensOfSubgroups = new ArrayList<>();
        int i = 0;
        while (i < 6 || !this.validHasseTheorem(size)) {
            EllipticCurvePoint point = getRandomElement();
            while (point.isInfinity()) {
                point = getRandomElement();
            }
            boolean found = false;
            boolean foundGreater = false;
            for (EllipticCurvePoint gen : gensOfSubgroups) {
                Optional<BigInteger> discreteLog = new BabyStepGiantStep(gen, gen.getOrder()).algorithm(point);
                if (discreteLog.isPresent()) {
                    found = true;
                    break;
                }
                Optional<BigInteger> discreteLog2 = new BabyStepGiantStep(point, point.getOrder()).algorithm(gen);
                if (discreteLog2.isPresent() && point.getOrder().compareTo(gen.getOrder()) >= 0) {
                    foundGreater = true;
                    break;
                }
            }
            if (foundGreater) {
                EllipticCurvePoint finalPoint = point;
                BigInteger sumOrders = BigInteger.ZERO;
                List<EllipticCurvePoint> points = gensOfSubgroups.stream().filter(gen -> new BabyStepGiantStep(finalPoint, finalPoint.getOrder()).algorithm(gen).isPresent()).collect(Collectors.toList());
                System.out.println("old set: "+ gensOfSubgroups);
                System.out.println("Excluded points: " + points);
                if (!points.isEmpty())
                    sumOrders = BigInteger.ONE;
                for (EllipticCurvePoint gen : points) {
                    sumOrders = sumOrders.add(gen.getOrder().subtract(BigInteger.ONE));
                    while (!gensOfSubgroups.remove(gen)) {
                        System.out.println("ERROR: " + gen);
                        System.out.println(gen.hashCode() + " !== " + points.stream().map(EllipticCurvePoint::hashCode).collect(Collectors.toList()));
                        System.out.println(gen.hashCode() + " is the same as the other? " + points.stream().map(c -> c.equals(gen)).collect(Collectors.toList()));
                        System.out.println(gen + " in " + gensOfSubgroups + "? " + gensOfSubgroups.contains(gen));
                    }
                }
                gensOfSubgroups.add(point);
                System.out.print(size + " - " + sumOrders + " + " + point.getOrder() + " = ");
                size = size.subtract(sumOrders).add(point.getOrder());
                System.out.println(size);
                System.out.println("new set: "+gensOfSubgroups);
            } else if (!found) {
                gensOfSubgroups.add(point);
                System.out.println(gensOfSubgroups);
                BigInteger orderOfSubgroup = point.getOrder().subtract(BigInteger.ONE);
                size = accumulateSizeWithSubgroupOrders(size, orderOfSubgroup);
                System.out.println("SIZE: " + size);
            }
            i++;
        }
        for (EllipticCurvePoint gen : gensOfSubgroups) {
            createSubgroupOrder(gen, gen.getOrder());
        }
        this.size = size;
        return size;
    }

    private BigInteger accumulateSizeWithSubgroupOrders(BigInteger size, BigInteger orderOfSubgroup) {

        return size.add(orderOfSubgroup);
    }


    @Override
    public ECPointBuilder buildElement() {
        return new ECPointBuilder(this);
    }

    /**
     * @see Group#getRandomExponent()
     */
    @Override
    public BigInteger getRandomExponent() {
        return new BigInteger(this.getSize().bitLength(), new SecureRandom()).mod(this.getSize());
    }

    /**
     * @see Group#multiply(GroupElement,
     * GroupElement)
     */
    @Override
    public EllipticCurvePoint multiply(final GroupElement x,
                                       final GroupElement y) {
        return (EllipticCurvePoint) x.multiply(y);
    }

    /**
     * @see Group#pow(GroupElement,
     * BigInteger)
     */
    @Override
    public EllipticCurvePoint pow(final GroupElement x, final BigInteger pow) {
        return (EllipticCurvePoint) x.pow(pow);
    }

    @Override
    public boolean containsElement(GroupElement groupElement) {
        return groupElement.getGroup().equals(this);
    }

    /**
     * @see EllipticCurveInt#getRing()
     */
    @Override
    public Ring getRing() {
        return ring;
    }

    /**
     * @see EllipticCurveInt#getA()
     */
    @Override
    public RingElement getA() {
        return A;
    }

    /**
     * @see EllipticCurveInt#getB()
     */
    @Override
    public RingElement getB() {
        return B;
    }

    /**
     * @see EllipticCurveInt#getRandomElement()
     */
    @Override
    public EllipticCurvePoint getRandomElement() {
        RingElement x;
        ArrayList<? extends EllipticCurvePoint> P;
        while (true) {
            x = ring.getRandomElement();
            P = liftX(x);
            if (!P.isEmpty()) {
                Random r = new Random();
                int iRandom = r.nextInt(P.size());
                if (isOnCurve(P.get(iRandom)))
                    return P.get(iRandom);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EllipticCurve ellipticCurve = (EllipticCurve) o;
        return Objects.equals(ring, ellipticCurve.ring) &&
                Objects.equals(A, ellipticCurve.A) && Objects.equals(B, ellipticCurve.B);
    }

    @Override
    public int hashCode() {
        int result = ring.hashCode();
        result = 31 * result + A.hashCode();
        result = 31 * result + B.hashCode();
        return result;
    }

    public Set<ECSubgroup> getSubgroups() {
        return subgroups;
    }

    @Override
    public GroupElement ZERO() {
        return getMultiplicativeIdentity();
    }

    @Override
    public GroupElement ONE() {
        throw new ConstructionException();
    }

    @Override
    public GroupElement THREE() {
        throw new ConstructionException();
    }

    public Optional<BigInteger> validOrder(ECPoint plusOp) {
        BigInteger order = plusOp.getOrder();
        BigInteger size = getSize();
        if (size.mod(order).equals(BigInteger.ZERO))
            return Optional.of(order);
        return Optional.empty();
    }

    public Optional<ECSubgroup> identifySubgroup(EllipticCurvePoint ellipticCurvePoint) {
        for (ECSubgroup subgroup : this.subgroups) {
            if (subgroup.containsElement(ellipticCurvePoint)) {
                return Optional.of(subgroup);
            }
        }
        return Optional.empty();
    }
}
