package cat.udl.cig.structures;

import cat.udl.cig.exceptions.ConstructionException;
import cat.udl.cig.structures.builder.RingElementBuilder;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Optional;
import java.util.Random;

public class FiniteOddPrimeField implements Ring {

    private final BigInteger p;
    private final BigInteger m;
    private final BigInteger pm;
    private final PrimeField primeField;

    public FiniteOddPrimeField(BigInteger p, final BitSet reducingPolynomial) {
        this(p, BigInteger.TWO, reducingPolynomial);
    }

    public FiniteOddPrimeField(BigInteger p, BigInteger m, final BitSet reducingPolynomial) {
        if (p.equals(BigInteger.TWO))
            throw new ConstructionException("p must not be 2");
        if ((!p.isProbablePrime(80) || p.signum() == -1))
            throw new ConstructionException("p must be prime");
        this.p = p;
        this.primeField = new PrimeField(p);
        this.m = m;
        this.pm = p.pow(m.intValue());
    }

    @Override
    public BigInteger getSize() {
        return pm;
    }

    public BigInteger getPrime() {
        return p;
    }

    public BigInteger getExponent() {
        return m;
    }

    @Override
    public BigInteger getRandomExponent() {
        return null;
    }

    @Override
    public Optional<? extends RingElement> toElement(Object k) {
        return Optional.empty();
    }

    @Override
    public FiniteOddPrimeFieldElement getRandomElement() {
        ArrayList<PrimeFieldElement> element = new ArrayList<>(m.intValue());
        Random rand = new Random();
        for (int i = 0; i < m.intValue(); i++) {
            element.add(i, new PrimeFieldElement(primeField, BigInteger.valueOf(rand.nextInt())));
        }
        return new FiniteOddPrimeFieldElement(this, element);
    }

    @Override
    public RingElementBuilder buildElement() {
        return null;
    }

    @Override
    public FiniteOddPrimeFieldElement getAdditiveIdentity() {
        return null;
    }

    @Override
    public FiniteOddPrimeFieldElement getMultiplicativeIdentity() {
        return null;
    }

    @Override
    public FiniteOddPrimeFieldElement multiply(GroupElement x, GroupElement y) {
        return null;
    }

    @Override
    public FiniteOddPrimeFieldElement pow(GroupElement x, BigInteger pow) {
        return null;
    }

    @Override
    public boolean containsElement(GroupElement groupElement) {
        return groupElement.getGroup().equals(this);
    }

    @Override
    public Optional<? extends RingElement> fromBytes(byte[] bytes) {
        return Optional.empty();
    }
}
