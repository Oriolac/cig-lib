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

import java.util.Arrays;

/**
 *
 */
public class ElGamalCiphertext implements HomomorphicCiphertext {

    private final GroupElement[] parts;

    public ElGamalCiphertext(final GroupElement[] parts1) {
        parts = parts1.clone();
    }

    /**
     * @see Ciphertext#getParts()
     */
    @Override
    public GroupElement[] getParts() {
        return parts;
    }

    /**
     * @see HomomorphicCiphertext#HomomorphicOperation(HomomorphicCiphertext)
     */
    @Override
    public ElGamalCiphertext HomomorphicOperation(
            final HomomorphicCiphertext op2) {
        ElGamalCiphertext input = (ElGamalCiphertext) op2;
        GroupElement[] result = new GroupElement[parts.length];
        for (int i = 0; i < parts.length; i++) {
            result[i] = parts[i].multiply(input.getParts()[i]);
        }
        return new ElGamalCiphertext(result);
    }

    @Override
    public String toString() {
        return "ElGamalCiphertext{" +
                "parts=" + Arrays.toString(parts) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ElGamalCiphertext that = (ElGamalCiphertext) o;
        return Arrays.equals(parts, that.parts);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(parts);
    }
}
