/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cat.udl.cig.cryptography.signers;

import java.math.BigInteger;

import cat.udl.cig.cryptography.hashes.SHA1;
import cat.udl.cig.fields.elements.PrimeFieldElement;

/**
 * Models a Digital Signature Verifier (DSV) over the DSA signer. This DSV needs
 * to ensure that a <i>Signature</i> created with the DSA algorithm: 1.-
 * corresponds to the given message; 2.- has been done for the specified user.
 *
 * @author M.Àngels Cerveró
 */
public class DSAVerifier implements SignerVerifier {
    /**
     * A <i>PrimeField</i> \( \mathbb{F}_{p} \) where \( p \) is randomly chosen
     * during the <i>DSA</i> construction. This <i>PrimeField</i> is where the
     * message and its corresponding <i>Signature</i> lies.
     *
     * @see DSA
     * @see Signature
     */
    // private PrimeField F;

    /**
     * A BigInteger value to initialize the DSA algorithm and its verifier. The
     * \( q \) value depends on the chosen \( p \) value (see the <i>DSA</i>
     * constructor function).
     *
     * @see DSA
     */
    private final BigInteger q; /* Public parameter */

    /**
     * A BigInteger storing an element of the <i>PrimeField</i> \(
     * \mathbb{F}_{p} \) (see the <i>DSA</i> constructor function).
     *
     * @see DSA
     */
    private final PrimeFieldElement g; /* Public parameter */

    /**
     * A BigInteger storing the public key used for the <i>DSA</i>.
     *
     * @see DSA
     */
    private final PrimeFieldElement y; /* Public parameter */

    /**
     * Creates an instance of <i>DSAVerifier</i> containing the specified
     * <i>PrimeField</i> \( F \), and the values q, g and y.
     *
     * @param F
     *            the <i>PrimeField</i> where the message and its corresponding
     *            <i>Signature</i> lies.
     * @param q
     *            a BigInteger value to initialize the DSA algorithm and
     *            {@code this} verifier.
     * @param g
     *            a BigInteger storing an element of the <i>PrimeField</i> \(
     *            \mathbb{F}_{p} \)
     * @param y
     *            a BigInteger storing the public key used for the <i>DSA</i>.
     * @see DSA
     */
    public DSAVerifier(final BigInteger q, final PrimeFieldElement g,
            final PrimeFieldElement y) {
        // this.F = F;
        this.q = q;
        this.g = g;
        this.y = y;
    }

    @Override
    public boolean verifySignature(final Object message,
            final Signature signature) {
        PrimeFieldElement r = (PrimeFieldElement) signature.getA();
        PrimeFieldElement s = (PrimeFieldElement) signature.getB();
        BigInteger w = s.getValue().modInverse(q);

        String mhash = SHA1.getHash(message.toString());
        BigInteger imhash = SHA1.hashToInteger(mhash);

        // try {
        BigInteger u1 = imhash.multiply(w).mod(q);
        BigInteger u2 = r.getValue().multiply(w).mod(q);
        BigInteger v1 = g.pow(u1).multiply(y.pow(u2)).getValue();
        v1 = v1.mod(q);

        if (v1.compareTo(r.getValue()) == 0) {
            return true;
        }
        // } catch (IncorrectRingElementException e) {
        // e.printStackTrace();
        // }

        return false;
    }
}
