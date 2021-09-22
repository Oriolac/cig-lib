package cat.udl.cig.structures.ecc;

import cat.udl.cig.structures.RingElement;

@FunctionalInterface
public interface WeierstrassFormPart {

    RingElement compute(RingElement x, RingElement y);
}
