package cat.udl.cig.structures.builder;

import cat.udl.cig.exceptions.ConstructionException;
import cat.udl.cig.structures.GroupElement;
import cat.udl.cig.structures.PairGroup;
import cat.udl.cig.structures.PairGroupElement;
import cat.udl.cig.structures.RingElement;

import java.util.Optional;

public class PairGroupElementBuilder implements RingElementBuilder {

    private final PairGroup pairGroup;
    private RingElement elementA;
    private RingElement elementB;

    public PairGroupElementBuilder(PairGroup pairGroup) {
        this.pairGroup = pairGroup;
    }

    public void setElementA(RingElement elementA) {
        if (!pairGroup.isFromSetA(elementA)){
            throw new ConstructionException("Element is not from set A.");
        }
        this.elementA = elementA;
    }

    public PairGroupElementBuilder setElementB(RingElement elementB) {
        if (!pairGroup.isFromSetB(elementB)){
            throw new ConstructionException("Element is not from set A.");
        }
        this.elementB = elementB;
        return this;
    }

    @Override
    public Optional<? extends RingElement> build() {
        if (this.elementA == null || this.elementB == null)
            return Optional.empty();
        return Optional.of(new PairGroupElement(elementA, elementB));
    }
}
