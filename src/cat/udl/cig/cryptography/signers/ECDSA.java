/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cat.udl.cig.cryptography.signers;

import java.math.BigInteger;

import cat.udl.cig.cryptography.hashes.SHA1;
import cat.udl.cig.ecc.ECPoint;
import cat.udl.cig.ecc.ECPrimeOrderSubgroup;
import cat.udl.cig.fields.groups.PrimeField;
import cat.udl.cig.fields.elements.PrimeFieldElement;

/**
 * Models the Digital Signature Algorithm over Elliptic Curves (ECDSA) to
 * digitally sign messages.
 *
 * @author M.Àngels Cerveró
 */
public class ECDSA extends Signer {

    /**
     * An <i>ECPoint</i> that represents the generator of the cyclic group \(
     * E(\mathbb{F}_{p}) \).
     */
    private final ECPoint generator;

    /**
     * A <i>PrimeFieldElement</i> that is the private key of {@code this}
     * <i>ECDSA</i> signer. The private key \( d \) is randomly chosen in the
     * range \( [1, q] \), where \( q = ord(P) \) is the order of the generator
     * point {@code P}.
     */
    private final BigInteger privateKey;

    /**
     * An <i>ECPoint</i> that represents the public key of {@code this}
     * <i>ECDSA</i> signer. The public key \( Q \) is computed as \( Q = d \cdot
     * P \)
     */
    private final ECPoint publicKey;

    /**
     * Creates an instance of <i>ECDSA</i> signer. This constructor also
     * initializes the corresponding <i>ECDSAVerifier</i> with the public
     * parameters.
     *
     * @param E
     *            the Elliptic Curve to which the message and the corresponding
     *            <i>Signature</i> belong.
     */
    public ECDSA(final ECPrimeOrderSubgroup E) {
        F = E;
        // if(this.ellipticCurve.isInitialized()) {
        // F = ellipticCurve.getRing();
        // securityBits = F.getSize().bitLength();

        generator = E.getRandomElement();

        privateKey = E.getRandomExponent();

        publicKey = generator.pow(privateKey);

        verifier = new ECDSAVerifier(generator, publicKey);

        // }
    }

    @Override
    public Signature sign(final Object message) {
        ECPoint kP;
        BigInteger r = BigInteger.ZERO, s = BigInteger.ZERO;
        BigInteger k, invK, imhash;
        String mhash;
        while (r.equals(BigInteger.ZERO)) {
            k = F.getRandomExponent();

            kP = generator.pow(k);
            r = kP.getX().getIntValue().mod(F.getSize());

            invK = k.modInverse(F.getSize());

            mhash = SHA1.getHash(message.toString());
            imhash = SHA1.hashToInteger(mhash);

            s = imhash.add(privateKey.multiply(r));
            s = invK.multiply(s).mod(F.getSize());

            if (s.equals(BigInteger.ZERO)) {
                r = BigInteger.ZERO;
            }
        }

        return new Signature(new PrimeFieldElement((PrimeField) F, r),
            new PrimeFieldElement((PrimeField) F, s));
    }
}
