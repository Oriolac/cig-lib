package cat.udl.cig.structures.builder;

import cat.udl.cig.structures.BinaryField;
import cat.udl.cig.structures.BinaryFieldElement;
import cat.udl.cig.structures.GroupElement;

import java.util.BitSet;
import java.util.Optional;

public class BinaryFieldElementBuilder implements RingElementBuilder {

    private final BinaryField field;
    private BitSet bitset;

    public BinaryFieldElementBuilder(BinaryField field) {
        this.field = field;
    }

    public BinaryFieldElementBuilder setBitSet(BitSet k) {
        this.bitset = k;
        return this;
    }

    @Override
    public Optional<? extends BinaryFieldElement> build() {
        if (bitset == null)
            return Optional.empty();
        return Optional.of(new BinaryFieldElement(field, bitset));
    }
}
