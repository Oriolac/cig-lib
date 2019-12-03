/**
 * $Id$
 * @author vmateu
 * @date   Sep 28, 2015 3:52:59 PM
 *
 * Copyright (C) 2015 Scytl Secure Electronic Voting SA
 *
 * All rights reserved.
 *
 */
package cat.udl.cig.cryptography.cryptosystems;

import java.math.BigInteger;

import cat.udl.cig.cryptography.cryptosystems.ciphertexts.Ciphertext;
import cat.udl.cig.cryptography.cryptosystems.ciphertexts.ElGamalCiphertext;
import cat.udl.cig.fields.Group;
import cat.udl.cig.fields.GroupElement;
import cat.udl.cig.fields.MultiplicativeSubgroup;

/**
 *
 */
public class ElGamal implements HomomorphicCryptosystem {

    private final ElGamalCypher cypher;

    private final BigInteger secretKey;

    public ElGamal(final MultiplicativeSubgroup gr) {
        secretKey = gr.getRandomExponent();
        GroupElement g = gr.getRandomElement();

        cypher = new ElGamalCypher(gr, g, g.pow(secretKey));
    }



    /**
     * @see cat.udl.cig.cryptography.cryptosystems.Cryptosystem#decrypt(cat.udl.cig.cryptography.cryptosystems.ciphertexts.Ciphertext)
     */
    @Override
    public GroupElement decrypt(final Ciphertext cyphertext) {
        ElGamalCiphertext input = (ElGamalCiphertext) cyphertext;
        return input.getParts()[1].divide(input.getParts()[0]
                .pow(secretKey));
    }

    /**
     * @see cat.udl.cig.cryptography.cryptosystems.Cryptosystem#getPublicKey()
     */
    @Override
    public GroupElement getPublicKey() {
        return cypher.getPublicKey();
    }

    /**
     * @see cat.udl.cig.cryptography.cryptosystems.Cryptosystem#getGenerator()
     */
    @Override
    public GroupElement getGenerator() {
        return cypher.getGenerator();
    }

    /**
     * @see cat.udl.cig.cryptography.cryptosystems.Cryptosystem#getGroup()
     */
    @Override
    public Group getGroup() {
        return cypher.getGroup();
    }

    /**
     * @see cat.udl.cig.cryptography.cryptosystems.HomomorphicCryptosystem#getCypher()
     */
    @Override
    public ElGamalCypher getCypher() {
        return cypher;
    }

}
