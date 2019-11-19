/**
 * Models any kind of <i>Multiplicative subgroup</i> with a prime order generator.
 *
 * @author Víctor Mateu
 */

package cat.udl.cig.fields;

import java.math.BigInteger;
import java.security.SecureRandom;

public class IntegerPrimeOrderSubgroup implements MultiplicativeSubgroup {

    private final PrimeField field;

    private final BigInteger cardinality;

    private final PrimeFieldElement generator;

    public IntegerPrimeOrderSubgroup(final BigInteger m,
            final BigInteger exponentFieldSize, final BigInteger g) {
        if (g.modPow(exponentFieldSize, m).equals(BigInteger.ONE)) {
            field = new PrimeField(m);
            cardinality = exponentFieldSize;

            generator = new PrimeFieldElement(field, g);
        } else {
            field = null;
            cardinality = null;
            generator = null;
        }
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

    @Override
    public PrimeFieldElement getGenerator() {
        return generator;
    }

    @Override
    public PrimeFieldElement getRandomElement() {
        return generator.pow(getRandomExponent());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result =
            prime * result
                + ((cardinality == null) ? 0 : cardinality.hashCode());
        result =
            prime
                * result
                + ((generator == null) ? 0 : generator.getValue()
                    .hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        IntegerPrimeOrderSubgroup other = (IntegerPrimeOrderSubgroup) obj;
        if (cardinality == null) {
            if (other.cardinality != null) {
                return false;
            }
        } else if (!cardinality.equals(other.cardinality)) {
            return false;
        }
        if (generator == null) {
            return other.generator == null;
        } else if (!generator.equals(other.generator)) {
            return false;
        }
        return true;
    }

    /**
     * @see cat.udl.cig.fields.Ring#getSize()
     */
    @Override
    public BigInteger getSize() {
        return cardinality;
    }

    /**
     * @see cat.udl.cig.fields.Ring#toRing(java.lang.Object)
     */
    @Override
    public PrimeFieldElement toElement(final Object k) {
        if(k instanceof BigInteger) {
            BigInteger input = (BigInteger) k;
            if (input.modPow(getSize(), field.getSize())
                    .equals(BigInteger.ONE)) {
                return new PrimeFieldElement(field, input);
            }
        }
        return null;
    }

    /**
     * @see cat.udl.cig.fields.Group#getNeuterElement()
     */
    @Override
    public PrimeFieldElement getNeuterElement() {
        return field.getNeuterElement();
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
    public PrimeFieldElement pow(final GroupElement x, BigInteger pow) {
        if (pow.compareTo(cardinality) >= 0) {
            pow = pow.mod(cardinality);
        }
        return (PrimeFieldElement) x.pow(pow);
    }

}
