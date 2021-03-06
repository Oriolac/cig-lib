

package cat.udl.cig.fields;

import cat.udl.cig.exceptions.ConstructionException;
import cat.udl.cig.exceptions.ParametersException;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Objects;

/**
 * Models any kind of <i>Multiplicative subgroup</i> with a prime order generator.
 *
 * @author Víctor Mateu
 */


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
            throw new ConstructionException("g^(exponentFieldSize) % m is not 1."); //TODO: It is not a good implementation though
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IntegerPrimeOrderSubgroup that = (IntegerPrimeOrderSubgroup) o;
        return Objects.equals(field, that.field) &&
                Objects.equals(cardinality, that.cardinality) &&
                Objects.equals(generator, that.generator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(field, cardinality, generator);
    }

    /**
     * @see Ring#getSize()
     */
    @Override
    public BigInteger getSize() {
        return cardinality;
    }

    /**
     * @see Ring#toElement(Object)
     */
    @Override
    public PrimeFieldElement toElement(final Object k) {
        if(k instanceof BigInteger) {
            BigInteger input = (BigInteger) k;
            if (input.modPow(getSize(), field.getSize())
                    .equals(BigInteger.ONE)) {
                return new PrimeFieldElement(field, input);
            }
        } else {
            throw new ParametersException();
        }
        return null;
    }

    /**
     * @see Group#getNeuterElement()
     */
    @Override
    public PrimeFieldElement getNeuterElement() {
        return field.getNeuterElement();
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
    public PrimeFieldElement pow(final GroupElement x, BigInteger pow) {
        if (pow.compareTo(cardinality) >= 0) {
            pow = pow.mod(cardinality);
        }
        return (PrimeFieldElement) x.pow(pow);
    }

}
