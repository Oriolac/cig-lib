package cat.udl.cig.structures.ecc;

import cat.udl.cig.structures.GroupElement;

import java.math.BigInteger;

public interface ECSubgroup {

    public BigInteger getOrder();

    public GroupElement getGenerator();

    public boolean containsElement(GroupElement groupElement);
}
