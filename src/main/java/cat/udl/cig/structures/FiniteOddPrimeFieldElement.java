package cat.udl.cig.structures;

import cat.udl.cig.exceptions.IncorrectRingElementException;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.BitSet;

public class FiniteOddPrimeFieldElement implements RingElement {

    private final FiniteOddPrimeField field;
    private final ArrayList<PrimeFieldElement> value;

    public FiniteOddPrimeFieldElement(FiniteOddPrimeField field, ArrayList<PrimeFieldElement> value) {
        this.field = field;
        this.value = value;
    }

    @Override
    public boolean belongsToSameGroup(GroupElement q) {
        return q.getGroup().equals(field);
    }

    @Override
    public Ring getGroup() {
        return this.field;
    }

    @Override
    public BitSet getValue() {
        return null;
    }

    @Override
    public BigInteger getIntValue() {
        return null;
    }

    @Override
    public RingElement add(RingElement q) throws IncorrectRingElementException {
        return null;
    }

    @Override
    public RingElement subtract(RingElement q) throws IncorrectRingElementException {
        return null;
    }

    @Override
    public RingElement opposite() {
        return null;
    }

    @Override
    public ArrayList<RingElement> squareRoot() throws IncorrectRingElementException {
        return null;
    }

    @Override
    public RingElement multiply(GroupElement q) throws IncorrectRingElementException {
        return null;
    }

    @Override
    public RingElement divide(GroupElement q) throws IncorrectRingElementException {
        return null;
    }

    @Override
    public RingElement inverse() {
        return null;
    }

    @Override
    public RingElement pow(BigInteger k) throws IncorrectRingElementException {
        return null;
    }

    @Override
    public String toString() {
        String str = "";
        int exp = field.getExponent().intValue() - 1;
        for (PrimeFieldElement element : this.value) {
            str = str.concat(element.getIntValue() + buildDimension(exp) );
            exp--;
        }
        return field.toString() +
                ": " + str;
    }

    private String buildDimension(int exp) {
        if (exp == 0)
            return "";
        return "a^" + exp;
    }
}
