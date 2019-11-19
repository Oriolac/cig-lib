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

import cat.udl.cig.fields.GroupElement;

/**
 *
 */
public class ElGamalCiphertext implements HomomorphicCiphertext {

    private final GroupElement[] parts;

    public ElGamalCiphertext(final GroupElement[] parts1) {
        parts = parts1.clone();
    }

    /**
     * @see cat.udl.cig.cryptography.cryptosystems.ciphertexts.Ciphertext#getParts()
     */
    @Override
    public GroupElement[] getParts() {
        return parts;
    }

    /**
     * @see cat.udl.cig.cryptography.cryptosystems.ciphertexts.HomomorphicCiphertext#HomomorphicOperation(cat.udl.cig.cryptography.cryptosystems.ciphertexts.HomomorphicCiphertext)
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
}
