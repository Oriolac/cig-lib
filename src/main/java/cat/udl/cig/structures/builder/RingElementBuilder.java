package cat.udl.cig.structures.builder;

import cat.udl.cig.structures.GroupElement;
import cat.udl.cig.structures.RingElement;

import java.util.Optional;

public interface RingElementBuilder extends GroupElementBuilder {

    Optional<? extends RingElement> build();

}
