
package cat.udl.cig.ecc;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Objects;
import java.util.Optional;

import cat.udl.cig.exceptions.ConstructionException;
import cat.udl.cig.exceptions.ParametersException;
import cat.udl.cig.fields.GroupElement;
import cat.udl.cig.fields.MultiplicativeSubgroup;
import cat.udl.cig.fields.Group;

/**
 * $Id$
 * @author vmateu
 * @date   Sep 29, 2015 12:43:52 PM
 *
 */
public class ECPrimeOrderSubgroup implements MultiplicativeSubgroup {

    private final GeneralEC EC;

    private final BigInteger cardinality;

    private final GeneralECPoint generator;

    public ECPrimeOrderSubgroup(final GeneralEC curve,
            final BigInteger orderOfSubgroup, final GeneralECPoint g) {
        if (g.pow(orderOfSubgroup).isInfinity()) {
            EC = curve;
            cardinality = orderOfSubgroup;

            generator = g;
        } else {
            throw new ConstructionException("G^orderOfSubgroup must be Infinity");
        }
    }

    /**
     * @see Group#getSize()
     */
    @Override
    public BigInteger getSize() {
        return cardinality;
    }

    /**
     * @see Group#toElement(Object)
     * @return
     */
    @Override
    public Optional<? extends GroupElement> toElement(final Object k) {
        Optional<? extends GroupElement> aux = EC.toElement(k);
        if (aux.isPresent() && aux.get() instanceof GeneralECPoint) {
            GeneralECPoint point = (GeneralECPoint) aux.get();
            if (point.pow(cardinality).isInfinity()) {
                return Optional.of(point);
            }
        }
        return Optional.empty();
    }

    /**
     * @see Group#getRandomElement()
     */
    @Override
    public GeneralECPoint getRandomElement() {
        return generator.pow(getRandomExponent());
    }

    /**
     * @see Group#getRandomExponent()
     */
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
     * @see Group#getNeuterElement()
     */
    @Override
    public GeneralECPoint getNeuterElement() {
        return EC.getNeuterElement();
    }

    /**
     * @see Group#multiply(cat.udl.cig.fields.GroupElement,
     *      cat.udl.cig.fields.GroupElement)
     */
    @Override
    public GeneralECPoint multiply(final cat.udl.cig.fields.GroupElement x,
                                   final cat.udl.cig.fields.GroupElement y) {
        return (GeneralECPoint) x.multiply(y);
    }

    /**
     * @see Group#pow(cat.udl.cig.fields.GroupElement,
     *      BigInteger)
     */
    @Override
    public GeneralECPoint pow(final cat.udl.cig.fields.GroupElement x, final BigInteger pow) {
        return (GeneralECPoint) x.pow(pow);
    }

    /**
     * @see MultiplicativeSubgroup#getGenerator()
     */
    @Override
    public GeneralECPoint getGenerator() {
        return generator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ECPrimeOrderSubgroup that = (ECPrimeOrderSubgroup) o;
        return Objects.equals(EC, that.EC) &&
                Objects.equals(cardinality, that.cardinality) &&
                Objects.equals(generator, that.generator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(EC, cardinality, generator);
    }
}