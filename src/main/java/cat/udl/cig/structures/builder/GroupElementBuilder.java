package cat.udl.cig.structures.builder;

import cat.udl.cig.structures.GroupElement;

import java.util.Optional;

public interface GroupElementBuilder {

    Optional<? extends GroupElement> build();

}
