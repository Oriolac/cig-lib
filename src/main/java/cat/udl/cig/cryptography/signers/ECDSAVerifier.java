/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cat.udl.cig.cryptography.signers;

import java.math.BigInteger;

import cat.udl.cig.cryptography.hashes.SHA1;
import cat.udl.cig.structures.ecc.ECPoint;
import cat.udl.cig.structures.PrimeFieldElement;

/**
 * Models a Digital Signature Verifier (DSV) over the ECDSA signer. This DSV
 * needs to ensure that a <i>Signature</i> created with the ECDSA algorithm: 1.-
 * corresponds to the given message; 2.- has been done for the specified user.
 *
 * @author M.Àngels Cerveró
 */
public class ECDSAVerifier implements SignerVerifier {
    /**
     * An <i>ECPoint</i> that represents the generator of the cyclic group \(
     * E(\mathbb{F}_{p}) \). The point \( P \) and the cyclic group \(
     * E(\mathbb{F}_{p}) \) are defined by the corresponding <i>ECDSA</i>
     * signer.
     *
     * @see ECDSA
     */
    private final ECPoint P;

    /**
     * An <i>ECPoint</i> that represents the public key of the corresponding
     * <i>ECDSA</i> signer. The public key \( Q \) is computed as \( Q = d \cdot
     * P \)
     *
     * @see ECDSA
     */
    private final ECPoint Q;

    /**
     * Creates an instance of <i>ECDSAVerifier</i> containing the specified
     * <i>ECPoints</i> \( P \) and \( Q \).
     *
     * @param P
     *            the generator of the cyclic group \( E(\mathbb{F}_{p}) \)
     *            defined by the corresponding <i>ECDSA</i> signer.
     * @param Q
     *            the public key of the corresponding <i>ECDSA</i> signer.
     */
    public ECDSAVerifier(final ECPoint P, final ECPoint Q) {
        this.P = P;
        this.Q = Q;
    }

    @Override
    public boolean verifySignature(final Object message,
            final Signature signature) {
        BigInteger r = ((PrimeFieldElement) signature.getA()).getValue();
        BigInteger s = ((PrimeFieldElement) signature.getB()).getValue();
        BigInteger order = P.getOrder();
        BigInteger w, u1, u2, v, imhash;
        String mhash;
        ECPoint P1, Q1, PQ;

        if (r.compareTo(BigInteger.ONE) == -1
            || r.compareTo(order.subtract(BigInteger.ONE)) == 1
            || s.compareTo(BigInteger.ONE) == -1
            || r.compareTo(order.subtract(BigInteger.ONE)) == 1) {
            return false;
        }

        w = s.modInverse(order);
        mhash = SHA1.getHash(message.toString());
        imhash = SHA1.hashToInteger(mhash);

        u1 = imhash.multiply(w).mod(order);
        u2 = r.multiply(w).mod(order);

        P1 = P.pow(u1);
        Q1 = Q.pow(u2);
        PQ = P1.multiply(Q1);

        if (PQ.isInfinity()) {
            return false;
        }

        v = PQ.getX().getIntValue().mod(order);

        return (v.compareTo(r) == 0);
    }
}
