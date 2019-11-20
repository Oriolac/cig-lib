/**
 * $Id$
 * @author vmateu
 * @date   Sep 29, 2015 3:33:38 PM
 *
 * Copyright (C) 2015 Scytl Secure Electronic Voting SA
 *
 * All rights reserved.
 *
 */
package cat.udl.cig.cryptography.proofs;

import java.math.BigInteger;
import java.util.ArrayList;

import cat.udl.cig.cryptography.cryptosystems.ElGamalCypher;
import cat.udl.cig.cryptography.cryptosystems.ciphertexts.Ciphertext;
import cat.udl.cig.cryptography.cryptosystems.ciphertexts.ElGamalCiphertext;
import cat.udl.cig.cryptography.hashes.SHA1;
import cat.udl.cig.fields.elements.GroupElement;

/**
 *
 */
public class ElGamalMLSProver implements MLSProver {

    private final ElGamalCypher cipher;

    /**
     * Creates an instance of <i>ECElGamalMLSProver</i> and initializes it with
     * the specified elliptic curve \( E \), security bits value, generator \( P
     * \) and public key \( Q \).
     *
     * @param icipher
     *            The cipher used to encrypt the encrypted messages.
     */
    public ElGamalMLSProver(final ElGamalCypher icipher) {
        cipher = icipher;

    }

    /**
     * @see cat.udl.cig.cryptography.proofs.MLSProver#generateProof(java.util.ArrayList,
     *      cat.udl.cig.cryptography.cryptosystems.ciphertexts.Ciphertext, int,
     *      java.math.BigInteger)
     */
    @Override
    public MLSProofData generateProof(final ArrayList<Object> vmessages,
            final Ciphertext cyphertext, final int vmidx,
            final BigInteger r) {
        int mlength = vmessages.size();
        BigInteger ExponentModulo = cipher.getGroup().getSize();
        BigInteger x = null;
        GroupElement[] parts = new GroupElement[2];
        GroupElement[] parts2 = new GroupElement[2];
        BigInteger[] ulist = new BigInteger[mlength];
        BigInteger[] wlist = new BigInteger[mlength];
        Ciphertext[] ciphertexts = new Ciphertext[mlength];

        for (int i = 0; i < mlength; ++i) {
            if (i != vmidx) {
                ulist[i] = cipher.getGroup().getRandomExponent();
                wlist[i] = cipher.getGroup().getRandomExponent();

                parts[0] = (GroupElement) cyphertext.getParts()[0];
                parts[0] =
                        cipher.getGenerator().pow(wlist[i])
                        .multiply(parts[0].pow(ulist[i]));

                parts[1] = (GroupElement) cyphertext.getParts()[1];
                parts[1] =
                    parts[1].divide((GroupElement) vmessages.get(i));
                parts[1] =
                        cipher.getPublicKey().pow(wlist[i])
                        .multiply(parts[1].pow(ulist[i]));

                ciphertexts[i] = new ElGamalCiphertext(parts);

            } else {
                x = cipher.getGroup().getRandomExponent();

                parts2[0] = cipher.getGenerator().pow(x);
                parts2[1] = cipher.getPublicKey().pow(x);
                ciphertexts[vmidx] = new ElGamalCiphertext(parts2);
            }
        }
        String AString = new String();
        String BString = new String();
        BigInteger totalU = BigInteger.ZERO;
        for (int i = 0; i < mlength; ++i) {
            AString =
                    AString.concat(ciphertexts[i].getParts()[0].toString());
            BString =
                    BString.concat(ciphertexts[i].getParts()[1].toString());

            if (i != vmidx) {
                totalU.add(ulist[i]).mod(ExponentModulo);
            }
        }
        String ABString = AString.concat(BString);
        String ABHash = SHA1.getHash(ABString);
        BigInteger ihash = SHA1.hashToInteger(ABHash);

        ulist[vmidx] = ihash.subtract(totalU).mod(ExponentModulo);

        wlist[vmidx] =
                x.subtract(ulist[vmidx].multiply(r)).mod(ExponentModulo);

        MLSProofData data =
                new MLSProofData(cyphertext, vmessages, wlist, ulist,
                    ciphertexts, ABHash);
        return data;
    }

}
