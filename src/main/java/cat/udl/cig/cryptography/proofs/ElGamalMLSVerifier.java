/**
 * $Id$
 * @author vmateu
 * @date   Sep 29, 2015 3:33:54 PM
 *
 * Copyright (C) 2015 Scytl Secure Electronic Voting SA
 *
 * All rights reserved.
 *
 */
package cat.udl.cig.cryptography.proofs;

import java.math.BigInteger;

import cat.udl.cig.cryptography.cryptosystems.ElGamalCypher;
import cat.udl.cig.cryptography.cryptosystems.ciphertexts.Ciphertext;
import cat.udl.cig.cryptography.hashes.SHA1;
import cat.udl.cig.fields.GroupElement;

/**
 *
 */
public class ElGamalMLSVerifier implements MLSVerifier {

    private final ElGamalCypher cipher;

    /**
     * Creates an instance of <i>ECElGamalMLSProver</i> and initializes it with
     * the cypher used to encrypt the ballots.
     *
     * @param icipher
     *            The cipher used to encrypt the encrypted messages.
     */
    public ElGamalMLSVerifier(final ElGamalCypher icipher) {
        cipher = icipher;

    }

    /**
     * @see MLSVerifier#verify(MLSProofData)
     */
    @Override
    public boolean verify(final MLSProofData data) {
        Ciphertext[] ABlist = data.getABValues();
        BigInteger[] wlist = data.getWValues();
        BigInteger[] ulist = data.getUValues();

        String AString = new String();
        String BString = new String();

        int mlength = data.getPossibleMessages().size();
        if (mlength != ABlist.length || mlength != wlist.length
            || mlength != ulist.length) {
            System.out
                .println("Los tamaños de los vectores no estan bien!!!");
            return false;
        }

        for (int i = 0; i < mlength; ++i) {
            AString = AString.concat(ABlist[i].getParts()[0].toString());
            BString = BString.concat(ABlist[i].getParts()[1].toString());
        }
        String ABString = AString.concat(BString);
        String ABHash = SHA1.getHash(ABString);

        if (!ABHash.equals(data.getHash())) {
            System.out.println("El hash no esta bien calculado!!!");
            return false;
        }

        for (int i = 0; i < mlength; ++i) {
            GroupElement AElem =
                (GroupElement) data.getCiphertextToBeProven().getParts()[0];

            GroupElement BElem =
                (GroupElement) data.getCiphertextToBeProven().getParts()[1];

            AElem = AElem.pow(ulist[i]);
            AElem = cipher.getGenerator().pow(wlist[i]).multiply(AElem);

            BElem =
                BElem.divide((GroupElement) data.getPossibleMessages()
                        .get(i));
            BElem =
                cipher.getPublicKey().pow(wlist[i])
                    .multiply(BElem.pow(ulist[i]));

            if (!ABlist[i].getParts()[0].equals(AElem)
                || !ABlist[i].getParts()[1].equals(BElem)) {
                return false;
            }
        }

        return true;
    }

}
