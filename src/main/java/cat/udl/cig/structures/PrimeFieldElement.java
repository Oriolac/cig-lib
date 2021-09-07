package cat.udl.cig.structures;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

import cat.udl.cig.exceptions.IncorrectModuleException;
import cat.udl.cig.exceptions.IncorrectRingElementException;

/**
 * Models a <i>Prime Field Element</i>. This <i>Prime Field Element</i> has a
 * value \(k\), represented by a <i>BigInteger</i>, and belongs to a
 * <i>PrimeField</i> /(F/). Any attempt to operate an instance of
 * <i>PrimeFieldElement</i> with an instance of a different kind of
 * <i>RingElement</i> causes an runtime exception.
 *
 * @author M.Àngels Cerveró
 * @author Ricard Garra
 * @see RingElement
 */
public class PrimeFieldElement implements RingElement {
    /**
     * The <i>Prime Field</i> \(\mathbb{F}_{p}\) in which this <i>Prime Field
     * Element</i> is defined.
     *
     * @see PrimeField
     */
    private final PrimeField field;

    /**
     * A BigInteger that encapsulates the value of this <i>Prime Field
     * Element</i>.
     */
    private final BigInteger k;

    /**
     * Creates a <i>PrimeFieldElement</i> with value \(k\) and belonging to the
     * <i>PrimeField</i> \(F\). If the <i>PrimeField</i> {@code F} is not
     * initialized, then the created <i>PrimeFieldElement</i> also remains
     * uninitialized. That is {@code this.F = null} and {@code this.k = null}.
     * This constructor does not make a deep compy of \(F\).
     *
     * @param field the <i>PrimeField</i> to which {@code this}
     *          <i>PrimeFieldElement</i> will belong.
     * @param k a BigInteger representing the value for {@code this}
     *          <i>PrimeFieldElement</i>.
     * @see PrimeField
     */
    public PrimeFieldElement(final PrimeField field, final BigInteger k) {
        this.field = field;
        if (k.compareTo(field.getSize()) >= 0
                || k.compareTo(BigInteger.ZERO) < 0) {
            this.k = k.mod(field.getSize());
        } else {
            this.k = k;
        }
    }

    /**
     * Creates a copy of the <i>PrimeFieldElement</i> \(q\). If \(q\) is null or
     * uninitialized, {@code this} instance remains uninitialized.
     *
     * @param q the <i>PrimeFieldElement</i> to be copied.
     */
    public PrimeFieldElement(final PrimeFieldElement q) {
        field = q.getGroup();
        k = q.getValue();
    }

    protected PrimeFieldElement(final PrimeField field, final BigInteger k,
                                final boolean safe) {
        this.field = field;
        this.k = k;
    }

    @Override
    public BigInteger getValue() {
        return k;
    }

    @Override
    public PrimeFieldElement add(final RingElement q)
            throws IncorrectRingElementException {
        if (belongsToSameGroup(q)) {
            BigInteger val = k.add(q.getIntValue());
            return new PrimeFieldElement(field, val);
        } else {
            throw new IncorrectRingElementException(
                    "RingElement q is not a "
                            + "correct instance of PrimeFieldElement");
        }
    }

    @Override
    public PrimeFieldElement subtract(final RingElement q)
            throws IncorrectRingElementException {
        if (belongsToSameGroup(q)) {
            BigInteger val = k.subtract(q.getIntValue());
            return new PrimeFieldElement(field, val);
        } else {
            throw new IncorrectRingElementException(
                    "RingElement q is not a "
                            + "correct instance of PrimeFieldElement");
        }
    }

    @Override
    public PrimeFieldElement multiply(final GroupElement q)
            throws IncorrectRingElementException {
        if (belongsToSameGroup(q)) {
            BigInteger val = k.multiply(q.getIntValue());
            val = val.mod(field.getSize());
            return new PrimeFieldElement(field, val, true);
        } else {
            throw new IncorrectRingElementException(
                    "RingElement q is not a "
                            + "correct instance of PrimeFieldElement");
        }
    }

    public PrimeFieldElement multiply(final BigInteger q) {
        BigInteger val = k.multiply(q);
        return new PrimeFieldElement(field, val);
    }

    @Override
    public PrimeFieldElement divide(final GroupElement q)
            throws IncorrectRingElementException {
        if (belongsToSameGroup(q)) {
            BigInteger val =
                    k.multiply((q.getIntValue()).modInverse(field.getSize()));
            val = val.mod(field.getSize());
            return new PrimeFieldElement(field, val, true);
        } else {
            throw new IncorrectRingElementException(
                    "RingElement q is not a "
                            + "correct instance of PrimeFieldElement");
        }
    }

    @Override
    public PrimeFieldElement opposite() {
        return new PrimeFieldElement(field, field.getSize().subtract(k), true);
    }

    @Override
    public PrimeFieldElement inverse() {
        BigInteger val = k.modInverse(field.getSize());
        return new PrimeFieldElement(field, val, true);
    }

    @Override
    public PrimeFieldElement pow(final BigInteger k) {
        // System.out.println("exponent = " + k.toString());
        BigInteger val = this.k.modPow(k, field.getSize());
        return new PrimeFieldElement(field, val, true);
    }

    @Override
    public ArrayList<RingElement> squareRoot() {

        ArrayList<RingElement> elems = new ArrayList<RingElement>();

        try {
            if (jacobiSymbol(k, field.getSize()) == -1) {
                return elems;
            }
        } catch (IncorrectModuleException ime) {
            return elems;
        }

        BigInteger b =
                new BigInteger(field.getSize().bitLength(), new Random());
        while (b.compareTo(BigInteger.ZERO) == 0) {
            b = new BigInteger(field.getSize().bitLength(), new Random());
        }
        b = b.mod(field.getSize());

        try {
            while (jacobiSymbol(b, field.getSize()) != -1) {
                b = b.add(BigInteger.ONE);
            }
        } catch (IncorrectModuleException ime) {
            return elems;
        }

        BigInteger e = BigInteger.ZERO;
        BigInteger TWO = new BigInteger("2");
        BigInteger mod1 = field.getSize().subtract(BigInteger.ONE);
        while (mod1.mod(TWO).compareTo(BigInteger.ZERO) == 0) {
            mod1 = mod1.divide(TWO);
            e = e.add(BigInteger.ONE);
        }

        BigInteger inverse = k.modInverse(field.getSize());
        BigInteger c = b.modPow(mod1, field.getSize());
        BigInteger r =
                k.modPow(mod1.add(BigInteger.ONE).divide(new BigInteger("2")),
                        field.getSize());

        for (int i = 1; i < e.intValue(); i++) {
            BigInteger exp =
                    BigInteger.valueOf(2).pow(e.intValue() - i - 1);
            BigInteger d =
                    (r.pow(2)).multiply(inverse).modPow(exp, field.getSize());

            if (d.mod(field.getSize()).compareTo(
                    new BigInteger("-1").mod(field.getSize())) == 0) {
                r = r.multiply(c);
                r.mod(field.getSize());
            }
            c = c.modPow(TWO, field.getSize());
        }

        elems.add(new PrimeFieldElement(field, r));
        elems.add(new PrimeFieldElement(field, r.negate()));
        return elems;
    }

    @Override
    public String toString() {
        return k.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PrimeFieldElement that = (PrimeFieldElement) o;
        return Objects.equals(field, that.field) &&
                Objects.equals(k, that.k);
    }

    @Override
    public int hashCode() {
        return Objects.hash(field, k);
    }

    /**
     * Computes the Jacobi Symbol of \(k\) with respecto to the modulus \(m\).
     *
     * @param k a BigInteger.
     * @param m a BigInteger representing the modulus.
     * @return 0 if \( k \equiv 0 (\pmod m) \), 1 if \( k \not\equiv 0 (\pmod m)
     * \) and \( \exist x : k \equiv x^2 (\pmod m) \) or -1 if \(
     * \not\exist x : k \equiv x^2 (\pmod m) \) \).
     * @throws IncorrectModuleException if {@code m == 0}.
     */
    private int jacobiSymbol(final BigInteger k, final BigInteger m)
            throws IncorrectModuleException {
        BigInteger ZERO = BigInteger.ZERO;
        BigInteger ONE = BigInteger.ONE;
        BigInteger TWO = new BigInteger("2");
        BigInteger THREE = new BigInteger("3");
        BigInteger FOUR = new BigInteger("4");
        BigInteger SEVEN = new BigInteger("7");
        BigInteger EIGHT = new BigInteger("8");

        if (m.equals(ZERO)) {
            throw new IncorrectModuleException(
                    "Module m can not be equal to 0");
            /*
             * Caldria mirar si m és un primer? Si pot no ser un primer, com
             * comprovo que la primera crida es fa sobre un PrimeField?
             */
        }

        if (k.equals(ZERO)) {
            return 0;
        }
        if (k.equals(ONE)) {
            return 1;
        }

        BigInteger e = BigInteger.ZERO;
        BigInteger k1 = BigInteger.ONE;
        k1 = k1.multiply(k);

        while (!k1.testBit(0)) {
            k1 = k1.divide(TWO);
            e = e.add(ONE);
        }

        BigInteger s;

        if (!e.testBit(0)) {
            s = BigInteger.ONE;
        } else if (m.mod(EIGHT).equals(ONE) || m.mod(EIGHT).equals(SEVEN)) {
            s = BigInteger.ONE;
        } else {
            s = new BigInteger("-1");
        }

        if (m.mod(FOUR).equals(THREE) && k1.mod(FOUR).equals(THREE)) {
            s = s.negate();
        }

        if (!(k1.equals(ONE))) {
            BigInteger m1 = m.mod(k1);
            s =
                    s.multiply(new BigInteger(String.valueOf(jacobiSymbol(m1,
                            k1))));
        }
        return s.intValue();
    }

    /**
     * @see GroupElement#belongsToSameGroup(GroupElement)
     */
    @Override
    public boolean belongsToSameGroup(final GroupElement q) {
        return field.equals(q.getGroup());
    }

    /**
     * @see GroupElement#getGroup()
     */
    @Override
    public PrimeField getGroup() {
        return field;
    }

    /**
     * @see RingElement#getIntValue()
     */
    @Override
    public BigInteger getIntValue() {
        return k;
    }

    @Override
    public byte[] toBytes() throws UnsupportedOperationException {
        return getIntValue().toByteArray();
    }

    public boolean isQuadraticNonResidue() {
        PrimeFieldElement i = field.getAdditiveIdentity();
        for (i = i.add(field.getMultiplicativeIdentity()); !i.equals(field.getAdditiveIdentity()); i = i.add(field.getMultiplicativeIdentity())) {
            if (i.multiply(i).equals(this)) {
                return false;
            }
        }
        return true;
    }
}
