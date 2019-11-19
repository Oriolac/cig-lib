/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cat.udl.cig.cryptography.signers;

/**
 * Models a Digital Signature Verifier (DSV). Any DSV needs to ensure that a
 * <i>Signature</i>: 1.- corresponds to the given message; 2.- has been done for
 * the specified user.
 *
 * @author M.Àngels Cerveró
 */
public interface SignerVerifier {
    /**
     * Verifies if the given {@code signature} is valid and corresponds to the
     * given {@code message}.
     *
     * @param message
     *            an Object containing the signed message. This Object can be
     *            instantiated as an <i>ECPoint</i> or as a <i>RingElement</i>,
     *            depending on the signer algorithm used.
     * @param signature
     *            a <i>Signature</i> that contains the signature of the given
     *            {@code message}.
     * @return true if the {@code signature} is correct and false otherwise.
     */
    boolean verifySignature(Object message, Signature signature);
}
