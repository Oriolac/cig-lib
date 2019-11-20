package cat.udl.cig.fields.groups;

import cat.udl.cig.fields.elements.GroupElement;

import java.math.BigInteger;
import java.security.spec.ECFieldFp;
import java.security.spec.EllipticCurve;

public class EllipticCurveGroup extends EllipticCurve implements MultiplicativeSubgroup {

    public EllipticCurveGroup(PrimeField field, BigInteger a, BigInteger b) {
        super(field, a, b);
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
