package cat.udl.cig.fields;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Models a <i>Prime Field</i> \(\mathbb{F}_{p}\), where \(p\) is positive and a
 * prime number.
 *
 * @see Ring
 * @author M.Àngels Cerveró
 * @author Ricard Garra
 */
public class PrimeField implements Ring {

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
            p = null;
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

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((p == null) ? 0 : p.hashCode());
        return result;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        PrimeField other = (PrimeField) obj;
        if (p == null) {
            if (other.p != null) {
                return false;
            }
        } else if (!p.equals(other.p)) {
            return false;
        }
        return true;
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
     * @see cat.udl.cig.fields.Group#toElement(java.lang.Object)
     */
    @Override
    public PrimeFieldElement toElement(final Object k) {
        BigInteger result = (BigInteger) k;
        return new PrimeFieldElement(this, result);
    }

    /**
     * @see cat.udl.cig.fields.Group#getNeuterElement()
     */
    @Override
    public PrimeFieldElement getNeuterElement() {
        return new PrimeFieldElement(this, BigInteger.ONE);
    }

    /**
     * @see cat.udl.cig.fields.Group#multiply(cat.udl.cig.fields.GroupElement,
     *      cat.udl.cig.fields.GroupElement)
     */
    @Override
    public PrimeFieldElement multiply(final GroupElement x,
            final GroupElement y) {
        return (PrimeFieldElement) x.multiply(y);
    }

    /**
     * @see cat.udl.cig.fields.Group#pow(cat.udl.cig.fields.GroupElement,
     *      java.math.BigInteger)
     */
    @Override
    public PrimeFieldElement pow(final GroupElement x, final BigInteger pow) {
        return (PrimeFieldElement) x.pow(pow);
    }

}
