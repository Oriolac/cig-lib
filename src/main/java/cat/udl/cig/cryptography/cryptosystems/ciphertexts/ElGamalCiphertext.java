/**
 * $Id$
 * @author vmateu
 * @date   Sep 28, 2015 4:06:41 PM
 *
 * Copyright (C) 2015 Scytl Secure Electronic Voting SA
 *
 * All rights reserved.
 *
 */
package cat.udl.cig.cryptography.cryptosystems.ciphertexts;

import cat.udl.cig.structures.GroupElement;
import cat.udl.cig.structures.PairGroupElement;

import java.util.Objects;

/**
 *
 */
public class ElGamalCiphertext implements HomomorphicCiphertext {

    private final PairGroupElement pairGroupElement;

    public ElGamalCiphertext(final PairGroupElement element) {
        pairGroupElement = element;
    }

    /**
     * @see HomomorphicCiphertext#HomomorphicOperation(HomomorphicCiphertext)
     */
    @Override
    public ElGamalCiphertext HomomorphicOperation(
            final HomomorphicCiphertext op2) {
        ElGamalCiphertext input = (ElGamalCiphertext) op2;
        GroupElement resultA = pairGroupElement.getGroupElementA().multiply(input.getElement().getGroupElementA());
        GroupElement resultB = pairGroupElement.getGroupElementB().multiply(input.getElement().getGroupElementB());
        return new ElGamalCiphertext(new PairGroupElement(resultA, resultB));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ElGamalCiphertext that = (ElGamalCiphertext) o;
        return Objects.equals(pairGroupElement, that.pairGroupElement);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pairGroupElement);
    }

    @Override
    public String toString() {
        return "ElGamalCiphertext{" +
                "pairGroupElement=" + pairGroupElement +
                '}';
    }

    @Override
    public PairGroupElement getElement() {
        return null;
    }
}
