package cat.udl.cig.fields;

import cat.udl.cig.exceptions.NotImplementedException;

import java.math.BigInteger;
import java.security.spec.EllipticCurve;

public class EllipticCurveGroup extends EllipticCurve implements MultiplicativeSubgroup {

    private final PrimeFieldElement generator;
    private final PrimeField field;

    public EllipticCurveGroup(PrimeField field, BigInteger a, BigInteger b, BigInteger g) {
        super(field, a, b);
        generator = new PrimeFieldElement(field, g);
        this.field = field;
    }

    @Override
    public GroupElement getGenerator() {
        return generator;
    }

    @Override
    public BigInteger getSize() {
        return this.field.getSize();
    }

    @Override
    public GroupElement toElement(Object k) {
        throw new NotImplementedException();
    }

    @Override
    public GroupElement getRandomElement() {
        throw new NotImplementedException();
    }

    @Override
    public BigInteger getRandomExponent() {
        throw new NotImplementedException();
    }

    @Override
    public GroupElement getNeuterElement() {
        throw new NotImplementedException();
    }

    @Override
    public GroupElement multiply(GroupElement x, GroupElement y) {
        throw new NotImplementedException();
    }

    @Override
    public GroupElement pow(GroupElement x, BigInteger pow) {
        throw new NotImplementedException();
    }
}
