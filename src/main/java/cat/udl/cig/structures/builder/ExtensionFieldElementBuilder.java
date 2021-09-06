package cat.udl.cig.structures.builder;

import cat.udl.cig.structures.ExtensionField;
import cat.udl.cig.structures.ExtensionFieldElement;
import cat.udl.cig.structures.GroupElement;
import cat.udl.cig.utils.Polynomial;

import java.util.Optional;

public class ExtensionFieldElementBuilder implements RingElementBuilder {

    private final ExtensionField field;
    private Polynomial polynomial;

    public ExtensionFieldElementBuilder(ExtensionField field) {
        this.field = field;
    }

    public void setPolynomial(Polynomial polynomial) {
        this.polynomial = polynomial;
    }

    @Override
    public Optional<? extends ExtensionFieldElement> buildElement() {
        if (polynomial == null)
            return Optional.empty();
        return Optional.of(new ExtensionFieldElement(field, polynomial));
    }
}
