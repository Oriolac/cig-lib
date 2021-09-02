
package cat.udl.cig.cryptography.cryptosystems;

import java.math.BigInteger;

import cat.udl.cig.cryptography.cryptosystems.ciphertexts.ElGamalCiphertext;
import cat.udl.cig.fields.Group;
import cat.udl.cig.fields.GroupElement;
import cat.udl.cig.fields.MultiplicativeSubgroup;
import cat.udl.cig.fields.PairGroupElement;

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

    private final GroupElement generator;

    private final GroupElement publicKey;

    public ElGamalCypher(final MultiplicativeSubgroup gr,
                         final GroupElement g, final GroupElement y) {
        group = gr;
        generator = g;
        publicKey = y;
    }

    @Override
    public GroupElement getPublicKey() {
        return publicKey;
    }

    @Override
    public GroupElement getGenerator() {
        return generator;
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
        PairGroupElement result = new PairGroupElement(generator.pow(r), publicKey.pow(r).multiply(message));
        return new ElGamalCiphertext(result);
    }

}
