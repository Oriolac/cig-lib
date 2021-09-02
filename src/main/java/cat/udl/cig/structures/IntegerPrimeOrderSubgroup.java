

package cat.udl.cig.structures;

import cat.udl.cig.exceptions.ConstructionException;
import cat.udl.cig.exceptions.ParametersException;
import cat.udl.cig.structures.builder.IntegerPrimeOrderElementBuilder;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Objects;
import java.util.Optional;

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

    @Override
    public IntegerPrimeOrderElementBuilder buildElement() {
        return new IntegerPrimeOrderElementBuilder(this.field);
    }

    /**
     * @see Ring#toElement(Object)
     * @return
     */
    @Override
    public Optional<? extends GroupElement> toElement(final Object k) {
        if(k instanceof BigInteger) {
            BigInteger input = (BigInteger) k;
            if (input.modPow(getSize(), field.getSize())
                    .equals(BigInteger.ONE)) {
                return Optional.of(new PrimeFieldElement(field, input));
            }
        } else {
            throw new ParametersException();
        }
        return Optional.empty();
    }

    /**
     * @see Group#getMultiplicativeIdentity()
     */
    @Override
    public PrimeFieldElement getMultiplicativeIdentity() {
        return field.getMultiplicativeIdentity();
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
