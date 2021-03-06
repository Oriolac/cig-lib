package cat.udl.cig.fields;

import cat.udl.cig.exceptions.ConstructionException;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.security.spec.ECField;
import java.util.Objects;

/**
 * Models a <i>Prime Field</i> \(\mathbb{F}_{p}\), where \(p\) is positive and a
 * prime number.
 *
 * @see Ring
 * @author M.Àngels Cerveró
 * @author Ricard Garra
 */
public class PrimeField implements Ring, ECField {

    /**
     * A BigInteger that encapsulates the characteristic of this <i>Field</i>.
     */
    protected final BigInteger p; /* Characteristic */

    /**
     * Creates a <i>PrimeField</i> which characteristic is \(p = m\). This
     * constructor does not check if parameter \(m\) is correct. That is, if
     * \(m\) is a positive and a prime number.
     *
     * @param m
     *            the characteristic of {@code this} newly created
     *            <i>PrimeField</i>. It must be positive and a prime number.
     */
    public PrimeField(final BigInteger m) {
        if ((!m.isProbablePrime(80) || m.signum() == -1)) {
            throw new ConstructionException("m must be prime");
        } else {
            p = m;
        }
    }

    /**
     * Creates a copy of the <i>PrimeField</i> \(F\). This constructor trusts
     * that \(F\) is a well-defined <i>PrimeField</i>. That is, \(F\) has a
     * correct characteristic. So, the constructor does not check if {@code F.p}
     * is positive and a prime number.
     *
     * @param F
     *            the <i>PrimeField</i> to be copied.
     */
    public PrimeField(final PrimeField F) {
        p = F.p;
    }

    @Override
    public BigInteger getSize() {
        return p;
    }

    @Override
    public PrimeFieldElement getRandomElement() {
        BigInteger result =
            new BigInteger(p.bitLength(), new SecureRandom());
        return new PrimeFieldElement(this, result);
    }

    public PrimeFieldElement getElementZERO() {
        return new PrimeFieldElement(this, BigInteger.ZERO);
    }

    @Override
    public String toString() {
        return "PrimeField of size " + p.toString() + "("
            + String.valueOf(p.bitLength()) + " bits).";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PrimeField that = (PrimeField) o;
        return Objects.equals(p, that.p);
    }

    @Override
    public int hashCode() {
        return Objects.hash(p);
    }

    public BigInteger getRandomExponent() {
        BigInteger result =
            new BigInteger(getSize().bitLength(), new SecureRandom());
        if (result.compareTo(getSize()) >= 0) {
            return result.mod(getSize());
        }
        return result;
    }

    /**
     * @see Group#toElement(Object)
     */
    @Override
    public PrimeFieldElement toElement(final Object k) {
        BigInteger result = (BigInteger) k;
        return new PrimeFieldElement(this, result);
    }

    /**
     * @see Group#getNeuterElement()
     */
    @Override
    public PrimeFieldElement getNeuterElement() {
        return new PrimeFieldElement(this, BigInteger.ONE);
    }

    /**
     * @see Group#multiply(GroupElement,
     *      GroupElement)
     */
    @Override
    public PrimeFieldElement multiply(final GroupElement x,
            final GroupElement y) {
        return (PrimeFieldElement) x.multiply(y);
    }

    /**
     * @see Group#pow(GroupElement,
     *      BigInteger)
     */
    @Override
    public PrimeFieldElement pow(final GroupElement x, final BigInteger pow) {
        return (PrimeFieldElement) x.pow(pow);
    }

    @Override
    public RingElement fromBytes(byte[] bytes) {
        BigInteger bigInteger = new BigInteger(bytes);
        return this.toElement(bigInteger);
    }


    @Override
    public int getFieldSize() {
        return getSize().intValue();
    }
}
