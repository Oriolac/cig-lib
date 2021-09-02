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
import cat.udl.cig.structures.Group;
import cat.udl.cig.structures.GroupElement;
import cat.udl.cig.structures.MultiplicativeSubgroup;

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
     * @see Cryptosystem#decrypt(Ciphertext)
     */
    @Override
    public GroupElement decrypt(final Ciphertext cyphertext) {
        ElGamalCiphertext input = (ElGamalCiphertext) cyphertext;
        return input.getParts()[1].divide(input.getParts()[0]
                .pow(secretKey));
    }

    /**
     * @see Cryptosystem#getPublicKey()
     */
    @Override
    public GroupElement getPublicKey() {
        return cypher.getPublicKey();
    }

    /**
     * @see Cryptosystem#getGenerator()
     */
    @Override
    public GroupElement getGenerator() {
        return cypher.getGenerator();
    }

    /**
     * @see Cryptosystem#getGroup()
     */
    @Override
    public Group getGroup() {
        return cypher.getGroup();
    }

    /**
     * @see HomomorphicCryptosystem#getCypher()
     */
    @Override
    public ElGamalCypher getCypher() {
        return cypher;
    }

}
