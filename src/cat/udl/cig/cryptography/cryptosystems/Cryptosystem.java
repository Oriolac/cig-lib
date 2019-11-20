/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cat.udl.cig.cryptography.cryptosystems;

import cat.udl.cig.cryptography.cryptosystems.ciphertexts.Ciphertext;
import cat.udl.cig.fields.Group;
import cat.udl.cig.fields.Ring;

/**
 * Models the general skeleton of a <i>Cryptosystem</i>. The <i>Cryptosystem</i>
 * is responsible of decrypting any encrypted message an of holding secret its
 * private parameters. It also allows to encrypt any message through the use of
 * the corresponding <i>Cypher</i>, which contains all the public parameters
 * needed to achieve this goal.
 *
 * @author Víctor Mateu
 * @author M.Àngels Cerveró
 */
// Base code by Víctor Mateu; Refactorization by M.Àngels Cerveró.
public interface Cryptosystem {

    /**
     * Decrypts the given <i>Cyphertext</i> {@code cyphertext} (an encrypted
     * message using the same cryptosystem).
     *
     * @param cyphertext
     *            the <i>Cyphertext</i> needed to be decrypted. The plain
     *            message has been previously encrypted using the <i>Cypher</i>
     *            {@code cypher} of {@code this} <i>Cryptosystem</i>.
     * @return the plain text corresponding to the given {@code cyphertext}
     * @see Ciphertext
     */
    Object decrypt(Ciphertext cyphertext);

    /**
     * Returns the public key of the <i>Cryptosystem</i>.
     *
     * @return the public key of {@code this} <i>Cryptosystem</i>.
     */
    Object getPublicKey();

    /**
     * Returns the generator element of the cyclic group in the <i>Ring</i> or
     * <i>Field</i> {@code F} of the <i>Cryptosystem</i>.
     *
     * @return the generator element of the cyclic groun of {@code this}
     *         <i>Cryptosystem</i> <i>Ring</i> or <i>Field</i> {@code F}.
     */
    Object getGenerator();

    /**
     * Returns the <i>Ring</i> or <i>Field</i> {@code F} of the
     * <i>Cryptosystem</i>.
     *
     * @return the <i>Ring</i> or <i>Field</i> {@code F} of {@code this}
     *         <i>Cryptosystem</i>.
     * @see Ring
     */
    Group getGroup();

    /**
     * Returns the <i>Cypher</i> {@code chyper} of the <i>Cryptosystem</i>.
     *
     * @return the <i>Cypher</i> {@code chyper} of {@code this}
     *         <i>Cryptosystem</i>.
     * @see Cypher
     */
    Cypher getCypher();
}
