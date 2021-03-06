/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cat.udl.cig.cryptography.cryptosystems;

import cat.udl.cig.cryptography.cryptosystems.ciphertexts.Ciphertext;
import cat.udl.cig.cryptography.cryptosystems.ciphertexts.ElGamalCiphertext;
import cat.udl.cig.fields.Group;
import cat.udl.cig.fields.GroupElement;
import cat.udl.cig.fields.Ring;

import java.math.BigInteger;

/**
 * Models the general skeleton of a <i>Cypher</i>, the component of any
 * <i>Cryptosystem</i> that deals with the public parameters. It allows to
 * encrypt a given message.
 *
 * @author Víctor Mateu
 * @author M.Àngels Cerveró
 */
// Base code by Víctor Mateu; Refactorization by M.Àngels Cerveró.
public interface Cypher {
    /**
     * Encrypts the given {@code message} (a plain text) and returns its
     * corresponding <i>Cyphertext</i>.
     *
     * @param message
     *            the plain text to be encrypted.
     * @return the cyphertext corresponding to the given {@code message}.
     * @see Ciphertext
     */
    default Ciphertext encrypt(final GroupElement message) {
        BigInteger rand = getGroup().getRandomExponent();
        return encrypt(message, rand);
    }
    /**
     * Encrypts the given {@code message} (a plain text) and returns its
     * corresponding <i>Cyphertext</i>.
     *
     * @param message message to be encrypted.
     * @param rand Random BigInteger to be used for encrypt.
     * @return the cyphertext corresponding to the given {@code message}.
     * @see Ciphertext
     */
    Ciphertext encrypt(final GroupElement message, BigInteger rand);

    GroupElement getPublicKey();

    /**
     * Returns the generator element of the cyclic group in the <i>Ring</i> or
     * <i>Field</i> {@code F} of the <i>Cryptosystem</i>.
     *
     * @return the generator element of the cyclic groun of {@code this}
     *         <i>Cryptosystem</i> <i>Ring</i> or <i>Field</i> {@code F}.
     */
    GroupElement getGenerator();

    /**
     * Returns the <i>Ring</i> or <i>Field</i> {@code F} of the
     * <i>Cryptosystem</i>.
     *
     * @return the <i>Ring</i> or <i>Field</i> {@code F} of {@code this}
     *         <i>Cryptosystem</i>.
     * @see Ring
     */
    Group getGroup();
}
