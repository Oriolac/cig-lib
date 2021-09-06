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
import cat.udl.cig.structures.GroupElement;
import cat.udl.cig.fields.PairGroupElement;

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
     * @see MLSProver#generateProof(ArrayList,
     *      Ciphertext, int,
     *      BigInteger)
     */
    @Override
    public MLSProofData generateProof(final ArrayList<Object> vmessages,
            final Ciphertext cyphertext, final int vmidx,
            final BigInteger r) {
        int mlength = vmessages.size();
        BigInteger ExponentModulo = cipher.getGroup().getSize();
        BigInteger x = null;
        BigInteger[] ulist = new BigInteger[mlength];
        BigInteger[] wlist = new BigInteger[mlength];
        Ciphertext[] ciphertexts = new Ciphertext[mlength];

        PairGroupElement elementCypherText = (PairGroupElement) cyphertext.getElement();
        GroupElement elementA = elementCypherText.getGroupElementA();
        GroupElement elementB = elementCypherText.getGroupElementB();

        for (int i = 0; i < mlength; ++i) {
            if (i != vmidx) {
                ulist[i] = cipher.getGroup().getRandomExponent();
                wlist[i] = cipher.getGroup().getRandomExponent();

                elementA =
                        cipher.getGenerator().pow(wlist[i])
                        .multiply(elementA.pow(ulist[i]));

                elementB =
                    elementB.divide((GroupElement) vmessages.get(i));
                elementB =
                        cipher.getPublicKey().pow(wlist[i])
                        .multiply(elementB.pow(ulist[i]));

                ciphertexts[i] = new ElGamalCiphertext(new PairGroupElement(elementA, elementB));

            } else {
                x = cipher.getGroup().getRandomExponent();

                PairGroupElement pairGroupElement = new PairGroupElement(cipher.getGenerator().pow(x), cipher.getPublicKey().pow(x));
                ciphertexts[vmidx] = new ElGamalCiphertext(pairGroupElement);
            }
        }
        String AString = new String();
        String BString = new String();
        BigInteger totalU = BigInteger.ZERO;
        for (int i = 0; i < mlength; ++i) {
            AString =
                    AString.concat(((PairGroupElement)ciphertexts[i].getElement()).getGroupElementA().toString());
            BString =
                    BString.concat(((PairGroupElement)ciphertexts[i].getElement()).getGroupElementB().toString());

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
