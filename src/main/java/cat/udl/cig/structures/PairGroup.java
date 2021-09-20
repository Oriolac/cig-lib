package cat.udl.cig.structures;

import cat.udl.cig.exceptions.NotImplementedException;
import cat.udl.cig.structures.builder.PairGroupElementBuilder;
import cat.udl.cig.structures.builder.RingElementBuilder;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.Objects;
import java.util.Optional;

public class PairGroup implements Ring {

    private final Ring a;
    private final Ring b;

    public PairGroup(Ring a, Ring b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public BigInteger getSize() {
        return a.getSize().multiply(b.getSize());
    }

    @Override
    public RingElementBuilder buildElement() {
        return new PairGroupElementBuilder(this);
    }

    @Override
    public RingElement getAdditiveIdentity() {
        return (RingElement) new PairGroupElement(this.a.getAdditiveIdentity(), this.b.getAdditiveIdentity());
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

    @Override
    public BigInteger getCharacteristic() {
        throw new NotImplementedException("Characteristic nof implemented of such class.");
    }

    @Override
    public boolean containsElement(GroupElement groupElement) {
        return groupElement.getGroup().equals(this);
    }

    @Override
    public Optional<? extends RingElement> fromBytes(byte[] bytes) {
        return Optional.empty();
    }

    public boolean contains(@NotNull PairGroupElement element) {
        return this.equals(element.getGroup());
    }

    public boolean isFromSetA(@NotNull GroupElement elementA) {
        return this.a.equals(elementA.getGroup());
    }

    public boolean isFromSetB(@NotNull GroupElement elementB) {
        return this.b.equals(elementB.getGroup());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PairGroup pairGroup = (PairGroup) o;
        return Objects.equals(a, pairGroup.a) && Objects.equals(b, pairGroup.b);
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, b);
    }

    @Override
    public String toString() {
        return "PairGroup{" +
                "a=" + a +
                ", b=" + b +
                '}';
    }

    @Override
    public RingElement ZERO() {
        return new PairGroupElement(this.a.ZERO(), this.b.ZERO());
    }

    @Override
    public RingElement ONE() {
        return new PairGroupElement(this.a.ONE(), this.b.ONE());
    }

    @Override
    public RingElement THREE() {
        return new PairGroupElement(this.a.THREE(), this.b.THREE());
    }
}
