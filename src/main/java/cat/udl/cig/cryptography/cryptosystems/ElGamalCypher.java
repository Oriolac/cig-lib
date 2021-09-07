
package cat.udl.cig.cryptography.cryptosystems;

import java.math.BigInteger;

import cat.udl.cig.cryptography.cryptosystems.ciphertexts.ElGamalCiphertext;
import cat.udl.cig.structures.*;

/**
 * $Id$
 *
 * @author vmateu
 * @date Sep 28, 2015 3:57:57 PM
 * <p>
 * Copyright (C) 2015 Scytl Secure Electronic Voting SA
 * <p>
 * All rights reserved.
 */
public class ElGamalCypher implements HomomorphicCypher {

    private final MultiplicativeSubgroup group;

    private final RingElement generator;

    private final GroupElement publicKey;

    public ElGamalCypher(final MultiplicativeSubgroup gr,
                         final RingElement g, final GroupElement y) {
        group = gr;
        generator = g;
        publicKey = y;
    }

    @Override
    public RingElement getPublicKey() {
        return (RingElement) publicKey;
    }

    @Override
    public RingElement getGenerator() {
        return (RingElement) generator;
    }

    @Override
    public Group getGroup() {
        return group;
    }

    @Override
    public ElGamalCiphertext encrypt(final GroupElement message) {
        BigInteger rand = getGroup().getRandomExponent();
        return encrypt(message, rand);
    }

    @Override
    public ElGamalCiphertext encrypt(final GroupElement message,
                                     final BigInteger r) {
        PairGroupElement result = new PairGroupElement(generator.pow(r), (RingElement) publicKey.pow(r).multiply(message));
        return new ElGamalCiphertext(result);
    }

}
