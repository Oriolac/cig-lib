package cat.udl.cig.structures;

import cat.udl.cig.structures.builder.GroupElementBuilder;
import cat.udl.cig.structures.builder.PairGroupElementBuilder;

import javax.annotation.Nonnull;
import java.math.BigInteger;
import java.util.Optional;

public class PairGroup implements Group {

    private final Group a;
    private final Group b;

    public PairGroup(Group a, Group b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public BigInteger getSize() {
        return a.getSize().multiply(b.getSize());
    }

    @Override
    public GroupElementBuilder buildElement() {
        return new PairGroupElementBuilder(this);
    }

    @Override
    public Optional<? extends GroupElement> toElement(Object k) {
        return Optional.empty();
    }

    @Override
    public PairGroupElement getRandomElement() {
        return new PairGroupElement(this, a.getRandomElement(), b.getRandomElement());
    }

    @Override
    public BigInteger getRandomExponent() {
        return null;
    }

    @Override
    public PairGroupElement getMultiplicativeIdentity() {
        return new PairGroupElement(this, a.getMultiplicativeIdentity(),b.getMultiplicativeIdentity());
    }

    @Override
    public PairGroupElement multiply(GroupElement x, GroupElement y) {
        return (PairGroupElement) x.multiply(y);
    }

    @Override
    public PairGroupElement pow(GroupElement x, BigInteger pow) {
        return (PairGroupElement) x.pow(pow);
    }

    public boolean contains(@Nonnull PairGroupElement element) {
        return this.equals(element.getGroup());
    }

    public boolean isFromSetA(@Nonnull GroupElement elementA) {
        return this.a.equals(elementA.getGroup());
    }

    public boolean isFromSetB(@Nonnull GroupElement elementB) {
        return this.b.equals(elementB.getGroup());
    }
}
