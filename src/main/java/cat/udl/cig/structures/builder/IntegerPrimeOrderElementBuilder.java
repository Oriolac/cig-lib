package cat.udl.cig.structures.builder;

import cat.udl.cig.exceptions.ParametersException;
import cat.udl.cig.structures.IntegerPrimeOrderSubgroup;
import cat.udl.cig.structures.PrimeField;
import cat.udl.cig.structures.PrimeFieldElement;

import java.math.BigInteger;
import java.util.Optional;

public class IntegerPrimeOrderElementBuilder implements RingElementBuilder{

    private IntegerPrimeOrderSubgroup integerPrimeOrderSubgroup;
    private final PrimeField field;
    private BigInteger value;

    public IntegerPrimeOrderElementBuilder(IntegerPrimeOrderSubgroup integerPrimeOrderSubgroup, PrimeField field) {
        this.integerPrimeOrderSubgroup = integerPrimeOrderSubgroup;
        this.field = field;
    }

    public IntegerPrimeOrderElementBuilder setValue(BigInteger value) {
        this.value = value;
        return this;
    }

    @Override
    public Optional<? extends PrimeFieldElement> buildElement() {
        if(value != null) {
            if (value.modPow(integerPrimeOrderSubgroup.getSize(), field.getSize())
                    .equals(BigInteger.ONE)) {
                return Optional.of(new PrimeFieldElement(field, value));
            }
        }
        return Optional.empty();
    }
}
