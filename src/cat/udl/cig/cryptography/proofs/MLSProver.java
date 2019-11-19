/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cat.udl.cig.cryptography.proofs;

import java.math.BigInteger;
import java.util.ArrayList;

import cat.udl.cig.cryptography.cryptosystems.ciphertexts.Ciphertext;

/**
 * Models the Prover role of the Message Lies in Set (MLS) Proof. This proof
 * allows to check that a given <i>Cyphertext</i> is well-formed. For example,
 * in an e-Voting context, the MLS proof, ensures that a given <i>Cyphertext</i>
 * corresponds to an encryption of a valid candidate. In addition, MLS is a
 * <i>Zero Knowledge Proof</i> (ZKP), so no information about the plain text
 * encrypted in the <i>Cyphertext</i> can be deduced.
 *
 * @author Víctor Mateu
 * @author M.Àngels Cerveró
 */
// Base code by Víctor Mateu; Refactorization by M.Àngels Cerveró.
public interface MLSProver {

    /**
     * Generates the proof needed to check that the given {@code cyphertext} is
     * an encryption of a valid plain message.
     *
     * @param vmessages
     *            a set of valid messages that can be encrypted using the
     *            corresponding <i>Cryptosystem</i>.
     * @param cyphertext
     *            the <i>Cyphertext</i> to be checked. It must be an encryption
     *            of a message in the set {@code vmessages}.
     * @param vmidx
     *            the index of the message in {@code vmessages} that is
     *            encrypted in the {@code cyphertext}
     * @param r
     *            a witness value to compute the proof data.
     * @return an instance of <i>MLSProofData</i> which contains all the
     *         information needed to verify the validity of the
     *         {@code cyphertext}.
     * @see Ciphertext
     * @see cat.udl.cig.cryptography.cryptosystems.Cryptosystem
     * @see MLSProofData
     */
    MLSProofData generateProof(ArrayList<Object> vmessages,
            Ciphertext cyphertext, int vmidx, BigInteger r);
}
