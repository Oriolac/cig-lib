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
import cat.udl.cig.structures.PairGroupElement;
import cat.udl.cig.structures.GroupElement;

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

        String AString = "";
        String BString = "";

        int mlength = data.getPossibleMessages().size();
        if (mlength != ABlist.length || mlength != wlist.length
            || mlength != ulist.length) {
            System.out
                .println("Los tamaños de los vectores no estan bien!!!");
            return false;
        }

        for (int i = 0; i < mlength; ++i) {
            AString = AString.concat(((PairGroupElement)ABlist[i].getElement()).getGroupElementA().toString());
            BString = BString.concat(((PairGroupElement)ABlist[i].getElement()).getGroupElementB().toString());
        }
        String ABString = AString.concat(BString);
        String ABHash = SHA1.getHash(ABString);

        if (!ABHash.equals(data.getHash())) {
            System.out.println("El hash no esta bien calculado!!!");
            return false;
        }

        for (int i = 0; i < mlength; ++i) {
            GroupElement AElem =
                    ((PairGroupElement) data.getCiphertextToBeProven().getElement()).getGroupElementA();

            GroupElement BElem =
                    ((PairGroupElement) data.getCiphertextToBeProven().getElement()).getGroupElementB();

            AElem = AElem.pow(ulist[i]);
            AElem = cipher.getGenerator().pow(wlist[i]).multiply(AElem);

            BElem =
                BElem.divide((GroupElement) data.getPossibleMessages()
                        .get(i));
            BElem =
                cipher.getPublicKey().pow(wlist[i])
                    .multiply(BElem.pow(ulist[i]));

            Boolean cond1 = !((PairGroupElement)ABlist[i].getElement()).getGroupElementA().equals(AElem);
            Boolean cond2 = !((PairGroupElement)ABlist[i].getElement()).getGroupElementB().equals(BElem);
            if (cond1 || cond2) return false;
        }

        return true;
    }

}
