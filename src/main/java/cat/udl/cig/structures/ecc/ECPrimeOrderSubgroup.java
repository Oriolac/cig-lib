
package cat.udl.cig.structures.ecc;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Objects;
import java.util.Optional;

import cat.udl.cig.exceptions.ConstructionException;
import cat.udl.cig.structures.GroupElement;
import cat.udl.cig.structures.MultiplicativeSubgroup;
import cat.udl.cig.structures.Group;
import cat.udl.cig.structures.builder.ecc.ECPointBuilder;
import cat.udl.cig.utils.discretelogarithm.BabyStepGiantStep;
import cat.udl.cig.utils.discretelogarithm.BruteForce;
import org.jetbrains.annotations.NotNull;


/**
 * $Id$
 * @author vmateu
 * @date   Sep 29, 2015 12:43:52 PM
 *
 */
public class ECPrimeOrderSubgroup implements MultiplicativeSubgroup, ECSubgroup {

    private final EllipticCurve EC;

    private final BigInteger orderOfSubgroup;

    private final GeneralECPoint generator;

    public ECPrimeOrderSubgroup(@NotNull final EllipticCurve curve, @NotNull final BigInteger orderOfSubgroup, @NotNull final GeneralECPoint generatorPoint) {
        if (generatorPoint.pow(orderOfSubgroup).isInfinity()) {
            EC = curve;
            this.orderOfSubgroup = orderOfSubgroup;
            generator = generatorPoint;
        } else {
            throw new ConstructionException("G^orderOfSubgroup must be Infinity");
        }
    }

    /**
     * @see Group#getSize()
     */
    @Override
    public BigInteger getSize() {
        return orderOfSubgroup;
    }

    @Override
    public ECPointBuilder buildElement() {
        return new ECPointBuilder(this.EC);
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
     * @see Group#getMultiplicativeIdentity()
     */
    @Override
    public GeneralECPoint getMultiplicativeIdentity() {
        return EC.getMultiplicativeIdentity();
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
     *      BigInteger)
     */
    @Override
    public GeneralECPoint pow(final GroupElement x, final BigInteger pow) {
        return (GeneralECPoint) x.pow(pow);
    }

    @Override
    public boolean containsElement(GroupElement groupElement) {
        return new BabyStepGiantStep(this.generator, this.orderOfSubgroup).algorithm(groupElement).isPresent();
    }

    public Optional<BigInteger> discreteLog(GroupElement groupElement) {
        return new BabyStepGiantStep(this.generator, this.orderOfSubgroup).algorithm(groupElement);
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
                Objects.equals(orderOfSubgroup, that.orderOfSubgroup) &&
                Objects.equals(generator, that.generator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(EC, orderOfSubgroup, generator);
    }

    @Override
    public GroupElement ZERO() {
        return getMultiplicativeIdentity();
    }

    @Override
    public GroupElement ONE() {
        return getGenerator();
    }

    @Override
    public GroupElement THREE() {
        return getGenerator().pow(BigInteger.valueOf(3));
    }

    @Override
    public BigInteger getOrder() {
        return this.orderOfSubgroup;
    }
}
