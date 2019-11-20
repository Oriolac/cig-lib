/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cat.udl.cig.cryptography.signers;

import cat.udl.cig.fields.groups.Group;
import cat.udl.cig.fields.groups.Ring;

/**
 * Abstract class to model the basic skeleton of any signer algorithm.
 *
 * @author M.Àngels Cerveró
 */
public abstract class Signer {
    /**
     * The <i>Ring</i> or <i>Field</i> in which the message to be digitally
     * signed is defined. Hence, the resulting <i>Signature</i> will also belong
     * to the same <i>Ring</i> or <i>Field</i>.
     *
     * @see Ring
     */
    protected Group F;

    /**
     * An integer value to represent the number of bits required to ensure the
     * security of the <i>Signer</i> used to create the <i>Signature</i>.
     *
     * @see Signature
     */
    protected int securityBits;

    /**
     * The Verifier needed to ensure that a <i>Signature</i> is valid: 1.-
     * Corresponds to the given message 2.- Has been done for the specified
     * user.
     */
    protected SignerVerifier verifier;

    /**
     * Given a {@code message} this functions signs it using the identifiyng
     * parameters of the signer user (his/her private key).
     *
     * @param message
     *            an Object containing the message to be signed. This Object can
     *            be instantiated as an <i>ECPoint</i> or as a
     *            <i>RingElement</i>, depending on the signer algorithm used.
     * @return a <i>Signature</i> containing the digital signature of the user
     *         over the given message.
     */
    public abstract Signature sign(Object message);

    /**
     * Returns the verifier corresponding to {@code this} <i>Signer</i>
     * algorithm.
     *
     * @return a <i>SignerVerifier</i> corresponding to {@code this}
     *         <i>Signer</i>
     */
    public SignerVerifier getVerifier() {
        return verifier;
    }

}
