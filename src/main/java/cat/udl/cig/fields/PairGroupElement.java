package cat.udl.cig.fields;

import cat.udl.cig.exceptions.IncorrectRingElementException;
import cat.udl.cig.fields.GroupElement;

import java.math.BigInteger;

public class PairGroupElements implements GroupElement {


    @Override
    public boolean belongsToSameGroup(GroupElement q) {
        return false;
    }

    @Override
    public Group getGroup() {
        return null;
    }

    @Override
    public Object getValue() {
        return null;
    }

    @Override
    public BigInteger getIntValue() {
        return null;
    }

    @Override
    public GroupElement multiply(GroupElement q) throws IncorrectRingElementException {
        return null;
    }

    @Override
    public GroupElement divide(GroupElement q) throws IncorrectRingElementException {
        return null;
    }

    @Override
    public GroupElement inverse() {
        return null;
    }

    @Override
    public GroupElement pow(BigInteger k) throws IncorrectRingElementException {
        return null;
    }
}
