package cat.udl.cig.fields;

import java.math.BigInteger;
import java.security.spec.EllipticCurve;

public class EllipticCurveGroup extends EllipticCurve implements MultiplicativeSubgroup {

    private final PrimeFieldElement generator;

    public EllipticCurveGroup(PrimeField field, BigInteger a, BigInteger b, BigInteger g) {
        super(field, a, b);
        generator = new PrimeFieldElement(field, g);
    }

    @Override
    public GroupElement getGenerator() {
        return null;
    }

    @Override
    public BigInteger getSize() {
        return null;
    }

    @Override
    public GroupElement toElement(Object k) {
        return null;
    }

    @Override
    public GroupElement getRandomElement() {
        return null;
    }

    @Override
    public BigInteger getRandomExponent() {
        return null;
    }

    @Override
    public GroupElement getNeuterElement() {
        return null;
    }

    @Override
    public GroupElement multiply(GroupElement x, GroupElement y) {
        return null;
    }

    @Override
    public GroupElement pow(GroupElement x, BigInteger pow) {
        return null;
    }
}
