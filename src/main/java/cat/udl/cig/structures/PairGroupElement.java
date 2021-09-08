package cat.udl.cig.structures;

import cat.udl.cig.exceptions.IncorrectModuleException;
import cat.udl.cig.exceptions.IncorrectRingElementException;
import cat.udl.cig.exceptions.NotImplementedException;
import cat.udl.cig.structures.PairGroup;
import cat.udl.cig.structures.GroupElement;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class PairGroupElement implements RingElement {

    private final PairGroup pairGroup;
    private final RingElement groupElementA;
    private final RingElement groupElementB;

    public PairGroupElement(final PairGroup pairGroup, RingElement groupElementA, RingElement groupElementB) {
        this.pairGroup = pairGroup;
        this.groupElementA = groupElementA;
        this.groupElementB = groupElementB;
    }

    public PairGroupElement(RingElement groupElementA, RingElement groupElementB) {
        this.pairGroup = new PairGroup(groupElementA.getGroup(), groupElementB.getGroup());
        this.groupElementA = groupElementA;
        this.groupElementB = groupElementB;
    }


    public GroupElement getGroupElementA() {
        return groupElementA;
    }

    public GroupElement getGroupElementB() {
        return groupElementB;
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
    public Ring getGroup() {
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
    public PairGroupElement add(RingElement q) throws IncorrectRingElementException {
        if (!( q instanceof PairGroupElement && belongsToSameGroup(q))) return null;
        PairGroupElement element = (PairGroupElement) q;
        RingElement resultA = this.groupElementA.add(element.groupElementA);
        RingElement resultB = this.groupElementB.add(element.groupElementB);
        return new PairGroupElement(this.pairGroup, resultA, resultB);
    }

    @Override
    public PairGroupElement subtract(RingElement q) throws IncorrectRingElementException {
        return this.add(q.opposite());
    }

    @Override
    public RingElement opposite() {
        return new PairGroupElement(this.groupElementA.opposite(), this.groupElementB.opposite());
    }

    @Override
    public ArrayList<RingElement> squareRoot() throws IncorrectRingElementException {
        ArrayList<RingElement> listA = groupElementA.squareRoot();
        ArrayList<RingElement> listB = groupElementB.squareRoot();
        ArrayList<RingElement> result = new ArrayList<>();

        if (listA.size() != listB.size()) return null;

        for (int i = 0; i < listA.size(); i++) {
            result.add(new PairGroupElement(listA.get(i), listB.get(i)));
        }
        return result;
    }

    @Override
    public PairGroupElement multiply(GroupElement q) throws IncorrectRingElementException {
        if (!( q instanceof PairGroupElement && belongsToSameGroup(q))) return null;
        PairGroupElement element = (PairGroupElement) q;
        RingElement resultA = this.groupElementA.multiply(element.groupElementA);
        RingElement resultB = this.groupElementB.multiply(element.groupElementB);
        return new PairGroupElement(this.pairGroup, resultA, resultB);
    }

    @Override
    public PairGroupElement divide(GroupElement q) throws IncorrectRingElementException {
        if (!( q instanceof PairGroupElement && belongsToSameGroup(q))) return null;
        PairGroupElement element = (PairGroupElement) q;
        RingElement resultA = this.groupElementA.divide(element.groupElementA);
        RingElement resultB = this.groupElementB.divide(element.groupElementB);
        return new PairGroupElement(this.pairGroup, resultA, resultB);
    }

    @Override
    public PairGroupElement inverse() {
        RingElement inverseA = this.groupElementA.inverse();
        RingElement inverseB = this.groupElementB.inverse();
        return new PairGroupElement(this.pairGroup, inverseA, inverseB);
    }

    @Override
    public PairGroupElement pow(BigInteger k) throws IncorrectRingElementException {
        RingElement powA = this.groupElementA.pow(k);
        RingElement powB = this.groupElementB.pow(k);
        return new PairGroupElement(this.pairGroup, powA, powB);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PairGroupElement that = (PairGroupElement) o;
        return Objects.equals(pairGroup, that.pairGroup) && Objects.equals(groupElementA, that.groupElementA) && Objects.equals(groupElementB, that.groupElementB);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pairGroup, groupElementA, groupElementB);
    }

    @Override
    public String toString() {
        return "PairGroupElement{" +
                "pairGroup=" + pairGroup +
                ", groupElementA=" + groupElementA +
                ", groupElementB=" + groupElementB +
                '}';
    }
}
