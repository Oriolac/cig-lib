package cat.udl.cig.structures;

import cat.udl.cig.exceptions.ConstructionException;
import cat.udl.cig.structures.builder.GroupElementBuilder;
import cat.udl.cig.structures.builder.PrimeFieldElementBuilder;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Objects;
import java.util.Optional;

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
    public PrimeFieldElementBuilder buildElement() {
        return new PrimeFieldElementBuilder(this);
    }

    @Override
    public PrimeFieldElement getRandomElement() {
        BigInteger result =
            new BigInteger(p.bitLength(), new SecureRandom());
        return new PrimeFieldElement(this, result);
    }

    public PrimeFieldElement getAdditiveIdentity() {
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

    @Override
    public BigInteger getRandomExponent() {
        BigInteger result =
            new BigInteger(getSize().bitLength(), new SecureRandom());
        if (result.compareTo(getSize()) >= 0) {
            return result.mod(getSize());
        }
        return result;
    }

    /**
     * @see Group#getMultiplicativeIdentity()
     */
    @Override
    public PrimeFieldElement getMultiplicativeIdentity() {
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
    public boolean containsElement(GroupElement groupElement) {
        return groupElement.getGroup().equals(this);
    }

    @Override
    public Optional<? extends PrimeFieldElement> fromBytes(byte[] bytes) {
        BigInteger bigInteger = new BigInteger(bytes);
        return this.buildElement().setValue(bigInteger).build();
    }

    @Override
    public PrimeFieldElement ZERO() {
        return new PrimeFieldElement(this, BigInteger.ZERO);
    }

    @Override
    public PrimeFieldElement ONE() {
        return new PrimeFieldElement(this, BigInteger.ONE);
    }

    @Override
    public PrimeFieldElement THREE() {
        return new PrimeFieldElement(this, BigInteger.valueOf(3L));
    }
}
