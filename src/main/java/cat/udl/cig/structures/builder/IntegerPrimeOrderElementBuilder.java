package cat.udl.cig.structures.builder;

import cat.udl.cig.exceptions.ParametersException;
import cat.udl.cig.structures.PrimeField;
import cat.udl.cig.structures.PrimeFieldElement;

import java.math.BigInteger;
import java.util.Optional;

public class IntegerPrimeOrderElementBuilder implements RingElementBuilder{

    private final PrimeField field;
    private BigInteger value;

    public IntegerPrimeOrderElementBuilder(PrimeField field) {
        this.field = field;
    }

    public IntegerPrimeOrderElementBuilder setValue(BigInteger value) {
        this.value = value;
        return this;
    }

    @Override
    public Optional<? extends PrimeFieldElement> buildElement() {
        if(value != null) {
            if (value.modPow(field.getSize(), field.getSize())
                    .equals(BigInteger.ONE)) {
                return Optional.of(new PrimeFieldElement(field, value));
            }
        }
        return Optional.empty();
    }
}
