package cat.udl.cig.structures.builder;

import cat.udl.cig.exceptions.ConstructionException;
import cat.udl.cig.structures.GroupElement;
import cat.udl.cig.structures.PairGroup;
import cat.udl.cig.structures.PairGroupElement;

import java.util.Optional;

public class PairGroupElementBuilder implements GroupElementBuilder {

    private final PairGroup pairGroup;
    private GroupElement elementA;
    private GroupElement elementB;

    public PairGroupElementBuilder(PairGroup pairGroup) {
        this.pairGroup = pairGroup;
    }

    public void setElementA(GroupElement elementA) {
        if (!pairGroup.isFromSetA(elementA)){
            throw new ConstructionException("Element is not from set A.");
        }
        this.elementA = elementA;
    }

    public void setElementB(GroupElement elementB) {
        if (!pairGroup.isFromSetB(elementB)){
            throw new ConstructionException("Element is not from set A.");
        }
        this.elementB = elementB;
    }

    @Override
    public Optional<? extends GroupElement> buildElement() {
        if (this.elementA == null || this.elementB == null)
            return Optional.empty();
        return Optional.of(new PairGroupElement(elementA, elementB));
    }
}
