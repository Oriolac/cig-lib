/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cat.udl.cig.cryptography.signers;

import java.math.BigInteger;
import java.util.Random;

import cat.udl.cig.cryptography.hashes.SHA1;
import cat.udl.cig.fields.groups.PrimeField;
import cat.udl.cig.fields.elements.PrimeFieldElement;

/**
 * Models the Digital Signature Algorithm (DSA) to digitally sign messages.
 *
 * @author M.Àngels Cerveró
 */
public class DSA extends Signer {
    /**
     * A BigInteger value to initialize the DSA algorithm. The \( q \) value
     * depends on the chosen \( p \) value (see the constructor function).
     */
    private BigInteger q; /* Public parameter */

    /**
     * A BigInteger storing the private key used for the <i>DSA</i>.
     */
    private BigInteger x; /* Private key */

    /**
     * A BigInteger storing an element of the <i>PrimeField</i> \(
     * \mathbb{F}_{p} \) (see the constructor function).
     */
    private PrimeFieldElement g; /* Public parameter */

    /**
     * A BigInteger storing the public key used for the <i>DSA</i>.
     */
    private PrimeFieldElement y; /* Public parameter */

    /**
     * Creates an instance of <i>DSA</i> signer. During the construction
     * process, a random value \( p \) of {@code pbits} is chosen. Notice that
     * pbits must be a multiple of 64. This constructor also initializes the
     * corresponding <i>DSAVerifier</i> with the public parameters.
     *
     * @param pbits
     *            an integer containing the bit-length required for the \( p \)
     *            value.
     * @param qbits
     *            an integer containing the bit-length required for the \( q \)
     *            value.
     */
    public DSA(int pbits, final int qbits) {
        securityBits = pbits;
        if (pbits % 64 != 0) {
            pbits = 0;
        } else {
            q = new BigInteger(qbits, new Random());
            BigInteger p = new BigInteger(pbits, new Random());
            p = p.nextProbablePrime();
            F = new PrimeField(p);

            BigInteger z = p.subtract(BigInteger.ONE).divide(q);

            BigInteger h = new BigInteger(securityBits, new Random());
            g = new PrimeFieldElement((PrimeField) F, h.multiply(z));
            while (h.compareTo(BigInteger.ONE) < 1
                    && h.compareTo(p.subtract(BigInteger.ONE)) > -1
                    && g.getValue().compareTo(BigInteger.ONE) < 1) {
                h = new BigInteger(securityBits, new Random());
                g = new PrimeFieldElement((PrimeField) F, h.multiply(z));
            }

            x = new BigInteger(securityBits, new Random());
            while (x.compareTo(BigInteger.ONE) < 1
                    && x.compareTo(q.subtract(BigInteger.ONE)) > -1) {
                x = new BigInteger(securityBits, new Random());
            }

            y = new PrimeFieldElement((PrimeField) F, x.multiply(x));

            verifier = new DSAVerifier(q, g, y);
        }
    }

    @Override
    public Signature sign(final Object message) {
        BigInteger k = new BigInteger(securityBits, new Random());
        while (k.compareTo(BigInteger.ONE) < 1 && k.compareTo(q) > -1) {
            k = new BigInteger(securityBits, new Random());
        }

        BigInteger r = g.getValue().modPow(k, F.getSize());
        r = r.mod(q);

        String mhash = SHA1.getHash(message.toString());
        BigInteger imhash = SHA1.hashToInteger(mhash);

        BigInteger s = imhash.add(r.multiply(x)).mod(q);
        s = s.divide(k).mod(q);

        if (s.equals(BigInteger.ZERO) || r.equals(BigInteger.ZERO)) {
            return sign(message);
        } else {
            return new Signature(new PrimeFieldElement((PrimeField) F, r),
                new PrimeFieldElement((PrimeField) F, s));
        }
    }
}
