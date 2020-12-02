/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cat.udl.cig.cryptography.proofs;

import cat.udl.cig.cryptography.cryptosystems.ciphertexts.Ciphertext;
import cat.udl.cig.cryptography.hashes.SHA1;
import java.math.BigInteger;
import java.util.ArrayList;

/**
 * Encapsulates all the information required to perform a <i>Messages Lies
 * in Set</i> (MLS) Proof.
 * 
 * @author Víctor Mateu
 * @author M.Àngels Cerveró
 */
// Base code by Víctor Mateu; Refactorization by M.Àngels Cerveró.
public class MLSProofData {
    /**
     * An array of BigIntegers with the w values needed to perform the MLS proof.
     */
    private final BigInteger[] wlist; 
    
    /**
     * An array of BigIntegers with the u values needed to perform the MLS proof.
     */
    private final BigInteger[] ulist;
    
    /**
     * An array with the pseudo-cyphertexts computed during the MLS proof.
     * 
     * @see Ciphertext
     */
    private final Ciphertext[] ctlist; //ciphertext list
    
    /**
     * A SHA1 hash of the concatenated pseudo-cyphertexts.
     */
    private final String hashtext;    
    
    private final Ciphertext ciphertextToBeProven;
    
    private final ArrayList<Object> possibleMessages;
    
    
    /**
     * Creates an instance of <i>MLSProofData</i> containing the specified w
     * and u values, the set of pseudo-cyphertexts and the hash of their
     * concatenation.
     * 
     * @param wlist an ArrayList of BigIntegers that stores the w values
     *                  generated during a MLS Proof.
     * @param ulist an ArrayList of BigIntegers that stores the u values
     *                  generated during a MLS Proof.
     * @param ctlist an ArrayList of Cyphertext that stores the
     *                  pseudo-cyphertexts generated during a MLS Proof.
     * @param hashtext a String representing the SHA1 hash of the concatenated
     *                  pseudo-cyphertexts in {@code ctlist}.
     * 
     * @see Ciphertext
     * @see SHA1
     */
    public MLSProofData(Ciphertext c, ArrayList<Object> vmessages, BigInteger[] wlist,
            BigInteger[] ulist, Ciphertext[] ctlist, String hashtext) {
        this.wlist = wlist;
        this.ulist = ulist;
        this.ctlist = ctlist;
        this.hashtext = hashtext;
        this.ciphertextToBeProven = c;
        this.possibleMessages = vmessages;
    }
    
    /**
     * Returns the w values generated during a MLS Proof.
     * 
     * @return the ArrayList {@code wlist} stored in {@code this}
     * <i>MSLProofData</i>.
     */
    public BigInteger[] getWValues() {
        return wlist;
    }
    
    /**
     * Returns the u values generated during a MLS Proof.
     * 
     * @return the ArrayList {@code ulist} stored in {@code this}
     * <i>MLSProofData</i>. 
     */
    public BigInteger[] getUValues() {
        return ulist;
    }
    
    /**
     * Returns the pseudo-cyphertexts generated during a MLS Proof.
     * 
     * @return the ArrayList {@code ctlist} stored in {@code this}
     * <i>MLSProofData</i>
     */
    public Ciphertext[] getABValues() {
        return ctlist;
    }
    
    /**
     * Returns the hash generated during a MLS Proof.
     * 
     * @return the String {@code hashtext} stored in {@code this}
     * <i>MLSProofData</i>.
     */
    public String getHash() {
        return hashtext;
    }
    
    
    /**
     * Returns the ciphertext associated with the MLS Proof.
     * 
     * @return the ciphertext {@code ciphertextToBeProven} stored in {@code this}
     * <i>MLSProofData</i>.
     */
	public Ciphertext getCiphertextToBeProven() {
		return ciphertextToBeProven;
	}

    /**
     * Returns the set of possible messages associated with the MLS Proof.
     * 
     * @return the ArrayList of possibleMessages {@code possibleMessages} stored in {@code this}
     * <i>MLSProofData</i>.
     */
	public ArrayList<Object> getPossibleMessages() {
		return possibleMessages;
	}
}
