/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cat.udl.cig.utils.bfarithmetic;

import static cat.udl.cig.utils.Factorization.rhoFactors;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Iterator;
import java.util.TreeSet;

/**
 * Auxiliar class to check if a polynomial is irreducible over GF(2).
 * 
 * @author Ricard Garra
 */
public class Irreducibility {
    /**
     * Returns wether f is an irreducible polynomial over GF(2)
     * http://en.wikipedia.org/wiki/Factorization_of_polynomials_over_finite_fields#Rabin.27s_test_of_irreducibility
     * @param f a BitSet representing the polynomial to be checked.
     * @return {@code true} if \(f\) is irreducible and {@code false} otherwise.
     */
    public static boolean isIrreducible(BitSet f) {
        if (f == null || f.isEmpty() || !f.get(0)) {
            return false;
        }
        int n = f.length() - 1;
        ArrayList<BigInteger> factorList = rhoFactors(BigInteger.valueOf(n));
        TreeSet<BigInteger> p = new TreeSet<BigInteger>();
        p.addAll(factorList);
        Iterator<BigInteger> iterator = p.descendingIterator();
        BitSet g;
        BitSet gn = null;
        BitSet gm;
        BitSet ONE = new BitSet();
        ONE.set(0);
        BigInteger next;
        int nj;
        int previousNj = 0;
        while (iterator.hasNext()) {
            next = iterator.next();
            nj = n / next.intValue();
            gm = getGm(gn, previousNj, nj, f);
            previousNj = nj;
            gn = (BitSet) gm.clone();
            gm.flip(1);
            g = BasicOperations.gcd(f, gm);
            if (!g.equals(ONE)) {
                return false;
            }
        }
        gm = getGm(gn, previousNj, n, f);
        gm.flip(1);
        return gm.isEmpty();
    }

    /*
    Computes the value of x^(2^m) mod f, where f is a polynomial over GF(2^a)[x]
    We define G_m as x^(2^m) mod f, where f represents a polynomial.
    G_(m+1) = G_m ^2, so to speed up computations, we can pass the parameter
    gn as g_n, from where only m-n steps will be needed.
    In each step, we use the fact that if f is of the form f = x^a + x^b +1,
    when we have x^a we can substitute it for x^b +1 (mod f). If we have an
    exponent greater than a, we susbtract a from it, and multiply it by x^b+1.
    If gn is not given (null), it will compute from the beginning
     */
    private static BitSet getGm(BitSet gn, int n, int m, BitSet f) {
        if (n > m || n < 0 || m < 1 || f == null) {
            return null;
        }
        int fDegree = f.length() - 1;
        //BitSet fReduced = f.get(0, fDegree);
        BitSet gm = new BitSet(f.length());
        if (gn == null || n == 0) {
            gm.set(1);
            n = 0;
        } else {
            gm = (BitSet) gn.clone();
        }
        BitSet nextgm = new BitSet(f.length());
        for (int i = n; i < m; i++) {
            nextgm.clear();
            for (int j = gm.nextSetBit(0); j >= 0; j = gm.nextSetBit(j + 1)) {
                if (j * 2 < fDegree) {
                    nextgm.flip(j * 2);
                } else {
                    nextgm.xor(reduce(j * 2, f));
                }
            }
            gm = (BitSet) nextgm.clone();
        }
        return gm;
    }
    
    /*
    Recursive method to reduce an exponent modulo a polynomial: if we have
    f = x^169 + x^10 +1, and we want to reduce the exponent 180 (i.e. x^180),
    we can substract the highest term (180-169 = 11) and multiply that by f
    without the highest term: x^180 = (x^10+1)*x^11 = x^21 + x^11
    If after reducing the exponent is still higher than the degree of f, repeat
     */
    private static BitSet reduce(int exponent, BitSet f) {
        int fDegree = f.length() - 1;
        BitSet result = new BitSet(fDegree);
        BitSet fReduced = f.get(0, fDegree);
        if (exponent < fDegree) {
            result.set(exponent);
            return result;
        }
        int difference = exponent - fDegree;
        for (int j = fReduced.nextSetBit(0); j >= 0; j = fReduced.nextSetBit(j + 1)) {
            if (j + difference < fDegree) {
                result.flip(j + difference);
            } else {
                if (j + difference > exponent) {
                    throw new ArithmeticException("The exponent can't be more" + "than double the degree");
                }
                BitSet reduced = reduce(j + difference, f);
                result.xor(reduced);
            }
        }
        return result;
    }
    
}
