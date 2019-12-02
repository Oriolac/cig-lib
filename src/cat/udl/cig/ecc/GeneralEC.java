package cat.udl.cig.ecc;

import cat.udl.cig.exceptions.ConstructionException;
import cat.udl.cig.exceptions.IncorrectRingElementException;
import cat.udl.cig.fields.*;

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
 * @see cat.udl.cig.ecc.EC
 */
public class GeneralEC implements EC {

    /**
     * The <i>Ring</i> or <i>Field</i> over which the <i>Elliptic Curve</i>
     * \(E\) is described.
     *
     * @see Ring
     */
    protected final Ring k;

    /**
     * Enter belonging to the Ring {@link #k} defining the <i>Elliptic Curve</i>
     * \(E\).
     *
     * @see RingElement
     */
    protected final RingElement[] coefficients;

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
     * @param iK           the <i>GeneralField</i> over which the <i>GeneralEC</i> is
     *                     described.
     * @param coefficients an ArrayList with the coefficients of the curve. They must
     *                     belong to the <i>GeneralField</i> \(K\).
     * @param cardFactors  prime factors composing the cardinality of the group law \(E\).
     * @see PrimeField
     * @see PrimeFieldElement
     */
    public GeneralEC(final Ring iK, final RingElement[] coefficients,
                     final ArrayList<BigInteger> cardFactors) {
        if (iK == null || cardFactors == null) {
            throw new ConstructionException("Ring must not be null.");
        }
        if (coefficients.length != 2
                || iK.getSize().equals(BigInteger.valueOf(2)) ||
                iK.getSize().equals(BigInteger.valueOf(3))) {
            throw new ConstructionException("Coefficients must be 2 and the ring must not be 2 or 3");
        }
        GroupElement elem;
        elem = iK.getNeuterElement();
        for (RingElement coefficient : coefficients) {
            if (!coefficient.belongsToSameGroup(elem)) {
                throw new ConstructionException("The coefficients does not belong to the same group.");
            }
        }

        k = iK;
        this.coefficients = coefficients;
        this.cardFactors = new ArrayList<>(cardFactors);
        Collections.sort(this.cardFactors);
        orders = possiblePointOrder();
        infintiyPoint = new GeneralECPoint(this);
    }

    /**
     * Creates a <i>GeneralEC</i> over the <i>GeneralField</i> \(K\) or empty if
     * \(a\) and \(b\) are not elements of \(K\).
     *
     * @param K            the <i>GeneralField</i> over which the <i>GeneralEC</i> is
     *                     described.
     * @param coefficients an ArrayList with the coefficients of the curve. They must
     *                     belong to the <i>GeneralField</i> \(K\).
     * @param cardFactors  prime factors composing the cadinality of the group law \(E\).
     * @see Ring
     * @see RingElement
     */
    protected GeneralEC(final Ring K, final RingElement[] coefficients,
                        final ArrayList<BigInteger> cardFactors,
                        final boolean conditions) {
        // TODO: @sergi -> Clean code with throws of ConstructorExceptions.
        boolean correctInput =
                !(coefficients.length != 2
                        || K.getSize().equals(BigInteger.valueOf(2)) || K
                        .getSize().equals(BigInteger.valueOf(3)));
        correctInput = correctInput && conditions;
        GroupElement elem = null;
        if (K != null) {
            elem = K.getNeuterElement();
        } else {
            correctInput = false;
        }
        for (int i = 0; i < coefficients.length && correctInput; i++) {
            if (!coefficients[i].belongsToSameGroup(elem)) {
                correctInput = false;
            }
        }
        if (correctInput) {
            k = K;
            this.coefficients = coefficients;
            if (cardFactors == null) {
                this.cardFactors = null;
            } else {
                this.cardFactors = new ArrayList<>(cardFactors);
                Collections.sort(this.cardFactors);
                orders = possiblePointOrder();
            }
        } else {
            k = null;
            this.coefficients = null;
            this.cardFactors = null;
            orders = null;
        }

        infintiyPoint = new GeneralECPoint(this);
    }

    /**
     * Creates a copy of the <i>GeneralEC</i> \(E\). This constructor makes a
     * deep copy of \(E\).
     *
     * @param E the <i>GeneralEC</i> to be copied.
     */
    public GeneralEC(final GeneralEC E) {
        k = E.k;
        coefficients = E.coefficients;
        cardFactors = E.cardFactors;
        orders = E.orders;
        infintiyPoint = new GeneralECPoint(this);
    }

    @Override
    public GeneralECPoint getNeuterElement() {
        return infintiyPoint;
    }


    @Override
    public boolean isOnCurve(final ECPoint P) {
        return P.getCurve().equals(this) && isOnCurveAux(P.getX(), P.getY());
    }

    @Override
    public boolean isOnCurve(final RingElement ix, final RingElement iy) {
        return ix.getGroup().equals(k) && !ix.getGroup().equals(iy.getGroup()) && isOnCurveAux(ix, iy);

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
    public GeneralECPoint liftX(final RingElement ix) {

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
                return null;
            }
            y = (PrimeFieldElement) sqRoots.get(0);

            P = new GeneralECPoint(this, x, y);
            return isOnCurve(P) ? P : null;
        } catch (IncorrectRingElementException ex) {
            return null;
        }
    }

    @Override
    public String toString() {
        String content;
        if (k instanceof ExtensionField) {
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

    /**
     * @see Group#toElement(java.lang.Object)
     */
    @Override
    public GeneralECPoint toElement(final Object k) {
        RingElement xinput = this.k.toElement(k);
        return liftX(xinput);
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
     * java.math.BigInteger)
     */
    @Override
    public GeneralECPoint pow(final GroupElement x, final BigInteger pow) {
        return (GeneralECPoint) x.pow(pow);
    }

    /**
     * @see cat.udl.cig.ecc.EC#getRing()
     */
    @Override
    public Ring getRing() {
        return k;
    }

    /**
     * @see cat.udl.cig.ecc.EC#getA()
     */
    @Override
    public RingElement getA() {
        return coefficients[0];
    }

    /**
     * @see cat.udl.cig.ecc.EC#getB()
     */
    @Override
    public RingElement getB() {
        return coefficients[1];
    }

    /**
     * @see cat.udl.cig.ecc.EC#getCardinalityFactors()
     */
    @Override
    public ArrayList<BigInteger> getCardinalityFactors() {
        return cardFactors;
    }

    /**
     * @see cat.udl.cig.ecc.EC#getRandomElement()
     */
    @Override
    public GeneralECPoint getRandomElement() {
        RingElement x;
        boolean incorrecte = true;
        GeneralECPoint P = null;
        while (incorrecte) {
            x = k.getRandomElement();
            P = liftX(x);
            if (!(P == null) && isOnCurve(P)) {
                incorrecte = false;
            }
        }
        return P;
    }

    /**
     * @see cat.udl.cig.ecc.EC#computeOrder(cat.udl.cig.ecc.ECPoint)
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

    /*public GeneralECPoint getBigPrimeOrderGenerator() {
        GeneralECPoint P = getRandomElement();
        while (!P.pow(cardFactors.get(cardFactors.size() - 1)).isInfinity) {
            P = getRandomElement();
        }
        P.setOrder(cardFactors.get(cardFactors.size() - 1));
        return P;
    }*/

}
