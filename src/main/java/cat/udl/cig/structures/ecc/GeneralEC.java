package cat.udl.cig.structures.ecc;

import cat.udl.cig.exceptions.ConstructionException;
import cat.udl.cig.exceptions.IncorrectRingElementException;
import cat.udl.cig.structures.*;
import cat.udl.cig.structures.builder.GroupElementBuilder;

import javax.annotation.Nonnull;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.*;

/**
 * Models an <i>Elliptic Curve</i> \(E\) of the form \(y^{2} = x^{3} + ax + b\)
 * over a <i>GeneralField</i>.
 *
 * @author M.Àngels Cerveró
 * @author Ricard Garra
 * @author Víctor Mateu
 * @see EC
 */
public class GeneralEC implements EC {

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

    // protected ECPoint generator;

    /**
     * The prime factors of the cardinality of the group law \(E(k)\). They must
     * be of the class BigInteger. The list is sorted in ascendent order.
     */
    protected final ArrayList<BigInteger> cardFactors;

    /**
     * The possible orders for the points in this curve.
     */
    protected ArrayList<SortedSet<BigInteger>> orders;

    /**
     * The Infinity point, also used as the neuter element of th group
     */
    private final GeneralECPoint infintiyPoint;

    /**
     * Creates a <i>GeneralEC</i> over the <i>GeneralField</i> \(K\) or empty if
     * \(a\) and \(b\) are not elements of \(K\).
     *
     * @param ring         the <i>GeneralField</i> over which the <i>GeneralEC</i> is
     *                     described.
     * @param A            y² = x³ + Ax + B
     * @param B            y² = x³ + Ax + B
     * @param cardFactors  prime factors composing the cardinality of the group law \(E\).
     * @see PrimeField
     * @see PrimeFieldElement
     */
    public GeneralEC(@Nonnull final Ring ring, @Nonnull final RingElement A, @Nonnull final RingElement B,
                     @Nonnull final ArrayList<BigInteger> cardFactors) {
        if (ring.getSize().equals(BigInteger.valueOf(2)) ||
                ring.getSize().equals(BigInteger.valueOf(3))) {
            throw new ConstructionException("The ring must not be 2 or 3");
        }
        if (ring.containsElement(A) || ring.containsElement(B)) {
            throw new ConstructionException("The coefficients does not belong to the same group.");
        }
        this.ring = ring;
        this.A = A;
        this.B = B;
        this.cardFactors = new ArrayList<>(cardFactors);
        Collections.sort(this.cardFactors);
        orders = possiblePointOrder();
        infintiyPoint = new GeneralECPoint(this);
    }

    /**
     * Creates a copy of the <i>GeneralEC</i> \(E\). This constructor makes a
     * deep copy of \(E\).
     *
     * @param E the <i>GeneralEC</i> to be copied.
     */
    public GeneralEC(final GeneralEC E) {
        ring = E.ring;
        cardFactors = E.cardFactors;
        orders = E.orders;
        this.A = E.A;
        this.B = E.B;
        infintiyPoint = new GeneralECPoint(this);
    }

    @Override
    public GeneralECPoint getMultiplicativeIdentity() {
        return infintiyPoint;
    }


    @Override
    public boolean isOnCurve(final ECPoint P) {
        return P.getCurve().equals(this) && isOnCurveAux(P.getX(), P.getY());
    }

    @Override
    public boolean isOnCurve(final RingElement ix, final RingElement iy) {
        return ix.getGroup().equals(ring) && !ix.getGroup().equals(iy.getGroup()) && isOnCurveAux(ix, iy);

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

    private ArrayList<SortedSet<BigInteger>> possiblePointOrder() {
        ArrayList<SortedSet<BigInteger>> ordersAux =
                new ArrayList<>(); // order = prime factor
        ordersAux.add(new TreeSet<>());
        for (BigInteger cardFactor : cardFactors) {
            ordersAux.get(0).add(cardFactor);
        } // ALERTA! COMPROVAR QUE, REALMENT, CALCULO TOTES LES POSSIBILITATS!
        int lastIndx;
        Iterator<?> it;
        BigInteger ord;
        int iniFactorIndx;
        while (ordersAux.size() != cardFactors.size()) {
            lastIndx = ordersAux.size() - 1;
            iniFactorIndx = lastIndx + 1;
            ordersAux.add(new TreeSet<>());
            it = ordersAux.get(lastIndx).iterator();
            while (it.hasNext()) {
                ord = (BigInteger) it.next();
                for (int j = iniFactorIndx; j < cardFactors.size(); j++) {
                    ordersAux.get(lastIndx + 1).add(
                            ord.multiply(cardFactors.get(j)));
                }
                iniFactorIndx++;
            }
        }
        return ordersAux;
    }

    @Override
    public Optional<? extends GeneralECPoint> liftX(final RingElement ix) {

        try {
            PrimeFieldElement x = (PrimeFieldElement) ix;
            PrimeFieldElement y;
            GeneralECPoint P;
            ArrayList<RingElement> sqRoots;
            // y^2 = x^3 + ax + b

            y = x.pow(BigInteger.valueOf(3));
            y = y.add(getA().multiply(x));
            y = y.add(getB());
            sqRoots = y.squareRoot();
            if (sqRoots.isEmpty()) {
                return Optional.empty();
            }
            y = (PrimeFieldElement) sqRoots.get(0);

            P = new GeneralECPoint(this, x, y);
            return isOnCurve(P) ? Optional.of(P) : Optional.empty();
        } catch (IncorrectRingElementException ex) {
            return Optional.empty();
        }
    }

    @Override
    public String toString() {
        String content;
        if (ring instanceof ExtensionField) {
            content =
                    "Elliptic Curve: y\u00B2 = x\u00B3 + ("
                            + getA().toString() + ")x + (" + getB().toString()
                            + ")";
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
        BigInteger result = BigInteger.ONE;
        for (BigInteger cardFactor : cardFactors) {
            result = result.multiply(cardFactor);
        }
        return result;
    }

    @Override
    public GroupElementBuilder buildElement() {
        return null;
    }

    /**
     * @return
     * @see Group#(Object)
     */
    @Override
    public Optional<? extends GroupElement> toElement(final Object k) {
        Optional<? extends RingElement> xinput = this.ring.toElement(k);
        if (xinput.isPresent())
            return liftX(xinput.get());
        return Optional.empty();
    }

    /**
     * @see Group#getRandomExponent()
     */
    @Override
    public BigInteger getRandomExponent() {
        BigInteger result =
                new BigInteger(cardFactors.get(cardFactors.size() - 1)
                        .bitLength(), new SecureRandom());
        if (result.compareTo(cardFactors.get(cardFactors.size() - 1)) >= 0) {
            return result.mod(cardFactors.get(cardFactors.size() - 1));
        }
        return result;
    }

    /**
     * @see Group#multiply(GroupElement,
     * GroupElement)
     */
    @Override
    public GeneralECPoint multiply(final GroupElement x,
                                   final GroupElement y) {
        return (GeneralECPoint) x.multiply(y);
    }

    /**
     * @see Group#pow(GroupElement,
     * BigInteger)
     */
    @Override
    public GeneralECPoint pow(final GroupElement x, final BigInteger pow) {
        return (GeneralECPoint) x.pow(pow);
    }

    @Override
    public boolean containsElement(GroupElement groupElement) {
        return groupElement.getGroup().equals(this);
    }

    /**
     * @see EC#getRing()
     */
    @Override
    public Ring getRing() {
        return ring;
    }

    /**
     * @see EC#getA()
     */
    @Override
    public RingElement getA() {
        return A;
    }

    /**
     * @see EC#getB()
     */
    @Override
    public RingElement getB() {
        return B;
    }

    /**
     * @see EC#getCardinalityFactors()
     */
    @Override
    public ArrayList<BigInteger> getCardinalityFactors() {
        return cardFactors;
    }

    /**
     * @see EC#getRandomElement()
     */
    @Override
    public GeneralECPoint getRandomElement() {
        RingElement x;
        boolean incorrecte = true;
        Optional<? extends GeneralECPoint> P = Optional.empty();
        while (incorrecte) {
            x = ring.getRandomElement();
            P = liftX(x);
            if (P.isPresent() && isOnCurve(P.get())) {
                incorrecte = false;
            }
        }
        return P.get();
    }

    /**
     * @see EC#computeOrder(ECPoint)
     */
    @Override
    public BigInteger computeOrder(final ECPoint P) {
        BigInteger ord;
        Iterator<?> it;
        for (SortedSet<BigInteger> order : orders) {
            it = order.iterator();
            while (it.hasNext()) {
                ord = (BigInteger) it.next();
                if (P.pow(ord).isInfinity()) {
                    return ord;
                }
            }
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GeneralEC generalEC = (GeneralEC) o;
        return Objects.equals(ring, generalEC.ring) &&
                Objects.equals(A, generalEC.A) && Objects.equals(B, generalEC.B) &&
                Objects.equals(cardFactors, generalEC.cardFactors) &&
                Objects.equals(orders, generalEC.orders);
    }

    @Override
    public int hashCode() {
        int result = ring.hashCode();
        result = 31 * result + A.hashCode();
        result = 31 * result + B.hashCode();
        result = 31 * result + cardFactors.hashCode();
        result = 31 * result + orders.hashCode();
        result = 31 * result + infintiyPoint.hashCode();
        return result;
    }

    public GeneralECPoint getBigPrimeOrderGenerator() {
        GeneralECPoint P = getRandomElement();
        while (!P.pow(cardFactors.get(cardFactors.size() - 1)).isInfinity) {
            P = getRandomElement();
        }
        P.setOrder(cardFactors.get(cardFactors.size() - 1));
        return P;
    }

}
