/**
 * $Id$
 * @author vmateu
 * @date   Sep 29, 2015 12:43:52 PM
 *
 */
package cat.udl.cig.ecc;

import java.math.BigInteger;
import java.security.SecureRandom;

import cat.udl.cig.exceptions.ConstructionException;
import cat.udl.cig.exceptions.ParametersException;
import cat.udl.cig.fields.GroupElement;
import cat.udl.cig.fields.MultiplicativeSubgroup;
import cat.udl.cig.fields.Group;


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
     * @see Group#toElement(java.lang.Object)
     */
    @Override
    public GeneralECPoint toElement(final Object k) {
        GeneralECPoint aux = EC.toElement(k);
        if (aux != null) {
            if (aux.pow(cardinality).isInfinity()) {
                return aux;
            }
        }
        throw new ParametersException();
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
     * @see Group#multiply(GroupElement,
     *      GroupElement)
     */
    @Override
    public GeneralECPoint multiply(final GroupElement x,
            final GroupElement y) {
        return (GeneralECPoint) x.multiply(y);
    }

    /**
     * @see Group#pow(GroupElement,
     *      java.math.BigInteger)
     */
    @Override
    public GeneralECPoint pow(final GroupElement x, final BigInteger pow) {
        return (GeneralECPoint) x.pow(pow);
    }

    /**
     * @see MultiplicativeSubgroup#getGenerator()
     */
    @Override
    public GeneralECPoint getGenerator() {
        return generator;
    }

}
