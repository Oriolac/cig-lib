package cat.udl.cig.structures.builder;

import cat.udl.cig.structures.PrimeField;
import cat.udl.cig.structures.PrimeFieldElement;

import javax.annotation.Nonnull;
import java.math.BigInteger;
import java.util.Optional;

public class PrimeFieldElementBuilder implements RingElementBuilder {

    private final PrimeField field;
    private BigInteger value;

    public PrimeFieldElementBuilder(@Nonnull PrimeField field) {
        this.field = field;
    }

    public void setValue(@Nonnull BigInteger value) {
        this.value = value;
    }

    @Override
    public Optional<PrimeFieldElement> buildElement() {
        if (value == null)
            return Optional.empty();
        return Optional.of(new PrimeFieldElement(field, value));
    }

}
