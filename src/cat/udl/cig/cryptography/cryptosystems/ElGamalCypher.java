/**
 * $Id$
 * @author vmateu
 * @date   Sep 28, 2015 3:57:57 PM
 *
 * Copyright (C) 2015 Scytl Secure Electronic Voting SA
 *
 * All rights reserved.
 *
 */
package cat.udl.cig.cryptography.cryptosystems;

import java.math.BigInteger;

import cat.udl.cig.cryptography.cryptosystems.ciphertexts.ElGamalCiphertext;
import cat.udl.cig.fields.Group;
import cat.udl.cig.fields.GroupElement;
import cat.udl.cig.fields.MultiplicativeSubgroup;

/**
 *
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

    /**
     * @see cat.udl.cig.cryptography.cryptosystems.Cypher#getPublicKey()
     */
    @Override
    public GroupElement getPublicKey() {
        return publicKey;
    }

    /**
     * @see cat.udl.cig.cryptography.cryptosystems.Cypher#getGenerator()
     */
    @Override
    public GroupElement getGenerator() {
        return generator;
    }

    /**
     * @see cat.udl.cig.cryptography.cryptosystems.Cypher#getGroup()
     */
    @Override
    public Group getGroup() {
        return group;
    }

    /**
     * @see cat.udl.cig.cryptography.cryptosystems.HomomorphicCypher#encrypt(java.lang.Object)
     */
    @Override
    public ElGamalCiphertext encrypt(final Object message) {
        GroupElement mess = (GroupElement) message;
        GroupElement[] result = new GroupElement[2];
        BigInteger rand = group.getRandomExponent();
        result[0] = generator.pow(rand);
        result[1] = publicKey.pow(rand).multiply(mess);
        return new ElGamalCiphertext(result);
    }

    /**
     * @see cat.udl.cig.cryptography.cryptosystems.HomomorphicCypher#encrypt(java.lang.Object,
     *      java.math.BigInteger)
     */
    @Override
    public ElGamalCiphertext encrypt(final Object message,
            final BigInteger r) {
        GroupElement mess = (GroupElement) message;
        GroupElement[] result = new GroupElement[2];
        result[0] = generator.pow(r);
        result[1] = publicKey.pow(r).multiply(mess);
        return new ElGamalCiphertext(result);
    }

}
