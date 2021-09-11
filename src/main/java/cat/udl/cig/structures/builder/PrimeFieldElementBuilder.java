package cat.udl.cig.structures.builder;

import cat.udl.cig.structures.Group;
import cat.udl.cig.structures.PrimeField;
import cat.udl.cig.structures.PrimeFieldElement;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.Optional;

public class PrimeFieldElementBuilder implements RingElementBuilder {

    private final PrimeField field;
    private BigInteger value;

    public PrimeFieldElementBuilder(@NotNull PrimeField field) {
        this.field = field;
    }

    public PrimeFieldElementBuilder setValue(@NotNull BigInteger value) {
        this.value = value;
        return this;
    }

    public PrimeFieldElementBuilder setValue(long l) {
        value = BigInteger.valueOf(l);
        return this;
    }

    @Override
    public Optional<PrimeFieldElement> build() {
        if (value == null)
            return Optional.empty();
        return Optional.of(new PrimeFieldElement(field, value));
    }
}
