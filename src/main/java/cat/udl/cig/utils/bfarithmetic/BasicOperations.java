/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cat.udl.cig.utils.bfarithmetic;

import java.util.AbstractMap;
import java.util.BitSet;

/**
 * Auxiliar class to compute basic binary operations.
 * 
 * @author Ricard Garra
 */
public class BasicOperations {

    /**
     * Performs a polynomial long division, and returns the quotient and
     * remainder resulting from dividing n by d, such that n = d × q + r
     * http://en.wikipedia.org/wiki/Polynomial_long_division
     * @param n The dividend
     * @param d The divisor
     * @return A SimpleEntry containing first the quotient, then the remainder
     */
    public static AbstractMap.SimpleEntry<BitSet, BitSet> quoRem(BitSet n, BitSet d) {
        BitSet quotient = new BitSet();
        BitSet remainder = (BitSet) n.clone();
        BitSet t = new BitSet();
        while (!remainder.isEmpty() && remainder.length() >= d.length()) {
            t.clear();
            t.set(remainder.length() - d.length());
            quotient.xor(t);
            remainder.xor(multiply(t, d));
        }
        return new AbstractMap.SimpleEntry<BitSet, BitSet>(quotient, remainder);
    }

    /**
     * Computes the polynomic gcd of a and b in GF(2). a and b are polynomials
     * 
     * @param a the first polynomial.
     * @param b the second polynomial.
     * @return \(\gcd(a, b)\) in GF(2).
     */
    public static BitSet gcd(BitSet a, BitSet b) {
        BitSet temp1 = (BitSet) a.clone();
        BitSet temp2 = (BitSet) b.clone();
        while (!temp2.isEmpty()) {
            BitSet t = (BitSet) temp2.clone();
            temp2 = mod(temp1, temp2);
            temp1 = (BitSet) t.clone();
        }
        return temp1;
    }

    /**
     * Computes (a mod b) in GF(2), where a and b represent polynomials
     * @param a the fist polynomial.
     * @param b the second polynomial.
     * @return \(a \mod b\) in GF(2).
     */
    public static BitSet mod(BitSet a, BitSet b) {
        int degreeA = a.length();
        int degreeB = b.length();
        BitSet register = (BitSet) a.clone();
        for (int i = degreeA - degreeB; i >= 0; i--) {
            if (register.length() == i + degreeB) {
                register.xor(BitSetManipulation.leftShift(b, i));
            }
        }
        return register;
    }

    /**
     * Performs the polynomial multiplication of a·b, with no reduction
     * @param a the first polynomial
     * @param b the second polynomial
     * @return \(a \cdot b\).
     */
    public static BitSet multiply(final BitSet a, final BitSet b) {
        BitSet result = new BitSet();
        for (int i = a.nextSetBit(0); i >= 0; i = a.nextSetBit(i + 1)) {
            for (int j = b.nextSetBit(0); j >= 0; j = b.nextSetBit(j + 1)) {
                result.flip(i + j);
            }
        }
        return result;
    }
    
    /**
     * Performs the polynomial multiplication of a + b, with no reduction
     * @param a the first polynomial
     * @param b the second polynomial
     * @return \(a + b\).
     */
    public static BitSet add(BitSet a, BitSet b) {
        BitSet output = (BitSet) a.clone();
        output.xor(b);
        return output;
    }
    
}
