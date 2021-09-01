package cat.udl.cig.fields;

import cat.udl.cig.exceptions.IncorrectRingElementException;
import cat.udl.cig.exceptions.NotImplementedException;
import cat.udl.cig.fields.GroupElement;

import java.math.BigInteger;

public class PairGroupElement implements GroupElement {

    private final PairGroup pairGroup;
    private final GroupElement groupElementA;
    private final GroupElement groupElementB;

    public PairGroupElement(final PairGroup pairGroup, GroupElement groupElementA, GroupElement groupElementB) {
        this.pairGroup = pairGroup;
        this.groupElementA = groupElementA;
        this.groupElementB = groupElementB;
    }

    @Override
    public boolean belongsToSameGroup(GroupElement q) {
        if (!( q instanceof PairGroupElement)) return false;
        Group A = groupElementA.getGroup();
        Group B = groupElementB.getGroup();
        PairGroupElement element = (PairGroupElement) q;
        return (element).groupElementA.getGroup().equals(A) && (element).groupElementB.getGroup().equals(B);
    }

    @Override
    public Group getGroup() {
        return pairGroup;
    }

    @Override
    public Object getValue() {
        return this;
    }

    @Override
    public BigInteger getIntValue() {
        throw new NotImplementedException();
    }

    @Override
    public PairGroupElement multiply(GroupElement q) throws IncorrectRingElementException {
        if (!( q instanceof PairGroupElement && belongsToSameGroup(q))) return null;
        PairGroupElement element = (PairGroupElement) q;
        GroupElement resultA = this.groupElementA.multiply(element.groupElementA);
        GroupElement resultB = this.groupElementB.multiply(element.groupElementB);
        return new PairGroupElement(this.pairGroup, resultA, resultB);
    }

    @Override
    public PairGroupElement divide(GroupElement q) throws IncorrectRingElementException {
        if (!( q instanceof PairGroupElement && belongsToSameGroup(q))) return null;
        PairGroupElement element = (PairGroupElement) q;
        GroupElement resultA = this.groupElementA.divide(element.groupElementA);
        GroupElement resultB = this.groupElementB.divide(element.groupElementB);
        return new PairGroupElement(this.pairGroup, resultA, resultB);
    }

    @Override
    public PairGroupElement inverse() {
        GroupElement inverseA = this.groupElementA.inverse();
        GroupElement inverseB = this.groupElementB.inverse();
        return new PairGroupElement(this.pairGroup, inverseA, inverseB);
    }

    @Override
    public PairGroupElement pow(BigInteger k) throws IncorrectRingElementException {
        GroupElement powA = this.groupElementA.pow(k);
        GroupElement powB = this.groupElementB.pow(k);
        return new PairGroupElement(this.pairGroup, powA, powB);
    }
}
