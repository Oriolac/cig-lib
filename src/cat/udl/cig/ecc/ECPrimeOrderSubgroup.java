/**
 * $Id$
 * @author vmateu
 * @date   Sep 29, 2015 12:43:52 PM
 *
 */
package cat.udl.cig.ecc;

import java.math.BigInteger;
import java.security.SecureRandom;

import cat.udl.cig.fields.GroupElement;
import cat.udl.cig.fields.MultiplicativeSubgroup;


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
            EC = null;
            cardinality = null;
            generator = null;
        }
    }

    /**
     * @see cat.udl.cig.fields.Group#getSize()
     */
    @Override
    public BigInteger getSize() {
        return cardinality;
    }

    /**
     * @see cat.udl.cig.fields.Group#toElement(java.lang.Object)
     */
    @Override
    public GeneralECPoint toElement(final Object k) {
        GeneralECPoint aux = EC.toElement(k);
        if (aux != null) {
            if (aux.pow(cardinality).isInfinity()) {
                return aux;
            }
        }
        return null;
    }

    /**
     * @see cat.udl.cig.fields.Group#getRandomElement()
     */
    @Override
    public GeneralECPoint getRandomElement() {
        return generator.pow(getRandomExponent());
    }

    /**
     * @see cat.udl.cig.fields.Group#getRandomExponent()
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
     * @see cat.udl.cig.fields.Group#getNeuterElement()
     */
    @Override
    public GeneralECPoint getNeuterElement() {
        return EC.getNeuterElement();
    }

    /**
     * @see cat.udl.cig.fields.Group#multiply(cat.udl.cig.fields.GroupElement,
     *      cat.udl.cig.fields.GroupElement)
     */
    @Override
    public GeneralECPoint multiply(final GroupElement x,
            final GroupElement y) {
        return (GeneralECPoint) x.multiply(y);
    }

    /**
     * @see cat.udl.cig.fields.Group#pow(cat.udl.cig.fields.GroupElement,
     *      java.math.BigInteger)
     */
    @Override
    public GeneralECPoint pow(final GroupElement x, final BigInteger pow) {
        return (GeneralECPoint) x.pow(pow);
    }

    /**
     * @see cat.udl.cig.fields.MultiplicativeSubgroup#getGenerator()
     */
    @Override
    public GeneralECPoint getGenerator() {
        return generator;
    }

}
