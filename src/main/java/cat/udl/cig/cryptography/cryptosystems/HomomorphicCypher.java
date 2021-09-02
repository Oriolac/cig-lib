package cat.udl.cig.cryptography.cryptosystems;

import java.math.BigInteger;

import cat.udl.cig.cryptography.cryptosystems.ciphertexts.HomomorphicCiphertext;
import cat.udl.cig.structures.GroupElement;

/**
 * $Id$
 * @author vmateu
 * @date   Jul 6, 2015 12:21:10 PM
 *
 * Copyright (C) 2015 Scytl Secure Electronic Voting SA
 *
 * All rights reserved.
 *
 */
public interface HomomorphicCypher extends Cypher {

    /**
     * Encrypts the given {@code message} (a plain text) and returns its
     * corresponding <i>Cyphertext</i>.
     *
     * @param message
     *            the plain text to be encrypted.
     * @return the cyphertext corresponding to the given {@code message}.
     * @see HomomorphicCiphertext
     */
    default HomomorphicCiphertext encrypt(final GroupElement message) {
        BigInteger rand = getGroup().getRandomExponent();
        return encrypt(message, rand);
    }
    HomomorphicCiphertext encrypt(GroupElement message, BigInteger r);
}
