/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cat.udl.cig.utils.bfarithmetic;

import java.math.BigInteger;
import java.util.BitSet;

import cat.udl.cig.exceptions.IncorrectRingElementException;
import cat.udl.cig.structures.BinaryField;
import cat.udl.cig.structures.BinaryFieldElement;

/**
 * Auxiliar class to compute quadratic equations over GF(2).
 *
 * @author Ricard Garra
 */
public class QuadraticEquations {
    /**
     * Auxiliar constant value.
     */
    final static BigInteger TWO = BigInteger.valueOf(2);

    /**
     * Solves a quadratic equation of the form y^2 + y + d = 0 where d and y are
     * elements of a BinaryField GF(2^m)
     *
     * @param d
     *            The independent term of the equation y^2 + y + d = 0
     * @return A solution y to the equation y^2 + y + d = 0
     */
    public static BinaryFieldElement solveQuadratic(
            final BinaryFieldElement d) {
        BinaryField k = d.getGroup();
        int m = k.getDimension();
        if (m % 2 == 0b1) {
            return solveOddM(d, k);
        }
        if (m % 4 == 0b0) {
            return solve0mod4(d, k);
        }
        return solve2mod4(d, k);

    }

    /*
     * solves a quadratic equation of the form y^2 + y + d = 0 when the
     * dimension of the field elements is odd INPUT: a BinaryFieldElement d
     * OUTPUT: a BinaryFieldElement y which is a solution of the equation
     * y^2+y+d=0 or null if there's no solution Uses
     * "Formulas for the Solutions of Quadratic Equations over GF (2^m)",
     * CHIN-LONG CHEN, IEEE Transactions of Information Theory, Vol. IT-28, No.
     * 5, September 1982
     */
    private static BinaryFieldElement solveOddM(
            final BinaryFieldElement d, final BinaryField k) {

        // if the trace is not 0, there's no solution to the equation
        if (oddTrace(d, k) != 0) {
            return null;
        }
        int aux = (k.getDimension() - 1) >> 1;
        BinaryFieldElement h = new BinaryFieldElement(d);
        try {
            for (int i = 1; i <= aux; i++) {
                h = h.square().square();
                h = h.add(d);
            }
            return h;
        } catch (IncorrectRingElementException ex) {
            return null;
        }
    }

    /*
     * solves a quadratic equation of the form y^2 + y + d = 0 when the
     * dimension of the field elements is even, and congruent 2 modulo 4 INPUT:
     * a BinaryFieldElement d OUTPUT: a BinaryFieldElement y which is a solution
     * of the equation y^2+y+d=0 or null if there's no solution Uses
     * "Formulas for the Solutions of Quadratic Equations over GF (2^m)",
     * CHIN-LONG CHEN, IEEE Transactions of Information Theory, Vol. IT-28, No.
     * 5, September 1982
     */
    private static BinaryFieldElement solve2mod4(
            final BinaryFieldElement d, final BinaryField k) {
        BinaryFieldElement y = new BinaryFieldElement(k, new BitSet());
        int aux = ((k.getDimension() - 6) / 4) + 1;
        final BigInteger THREE = BigInteger.valueOf(3);
        BigInteger exponent;
        try {
            final BinaryFieldElement dPlusdSquared = d.add(d.square());
            for (int i = 0; i < aux; i++) {
                exponent = TWO.pow(2 + 4 * i);
                // sum += (k+k^2)^(2^(2+4*i))
                y = y.add((dPlusdSquared.pow(exponent)));
            }

            if (evenTrace(d, k) == 1) {
                BitSet primitive = BitSetManipulation.longToBitSet(2);
                BinaryFieldElement alpha =
                        new BinaryFieldElement(k, primitive);
                exponent =
                        ((TWO.pow(k.getDimension())).subtract(BigInteger.ONE))
                        .divide(THREE);
                alpha = alpha.pow(exponent);
                // alpha = generator^( (2^m -1)/3)
                y = y.add(alpha);
            }
            return y;

        } catch (IncorrectRingElementException ex) {
            return null;
        }
    }

    /*
     * solves a quadratic equation of the form y^2 + y + d = 0 when the
     * dimension of the field elements is even, and congruent 0 modulo 4 INPUT:
     * a BinaryFieldElement d OUTPUT: a BinaryFieldElement y which is a solution
     * of the equation y^2+y+d=0 or null if there's no solution Uses
     * "Formulas for the Solutions of Quadratic Equations over GF (2^m)",
     * CHIN-LONG CHEN, IEEE Transactions of Information Theory, Vol. IT-28, No.
     * 5, September 1982
     */
    private static BinaryFieldElement solve0mod4(
            final BinaryFieldElement d, final BinaryField k) {
        int trace = evenTrace(d, k);
        if (trace == 1) {
            return solve0mod4Trace1(d, k);
        }
        if (trace == 0) {
            /*
             * We want to solve x^2+ x + d = 0 with trace = 0 we select an
             * element y of GF (2^m) such that oddTrace(y) = 1. Next, we compute
             * d_1 = y + y^2 and solve z^2 + z + d_1 + d = 0 using the result of
             * liftX0mod4Trace1 (evenTrace(d+d_1) is always 1). Then, x = y + z
             * is the solution.
             */
            BinaryFieldElement y;
            do {
                y = k.getRandomElement();
            } while (oddTrace(y, k) != 1);

            try {
                BinaryFieldElement d_1 = y.add(y.square());
                BinaryFieldElement z = solve0mod4Trace1(d.add(d_1), k);
                return y.add(z);
            } catch (IncorrectRingElementException ex) {
                return null;
            }
        }
        return null;
    }

    /*
     * Auxiliar method when the dimension is even and congruent 0 modulo 4, and
     * evenTrace(d) == 1
     */
    private static BinaryFieldElement solve0mod4Trace1(
            final BinaryFieldElement d, final BinaryField k) {
        BinaryFieldElement s = new BinaryFieldElement(k, new BitSet());
        BigInteger first_term;
        BigInteger second_term;
        int m = k.getDimension();
        int quarterM = m / 4;
        try {
            for (int j = 1; j < quarterM; j++) {
                for (int i = j; i < quarterM; i++) {
                    first_term = TWO.pow(2 * i - 1 + m / 2);
                    second_term = TWO.pow(2 * j - 2);
                    s = s.add(d.pow(first_term.add(second_term)));
                }
            }
            BinaryFieldElement sum = k.getMultiplicativeIdentity();
            for (int i = 0; i < quarterM; i++) {
                // sum += d^(2^(2*i+m//2))
                sum = sum.add(d.pow(TWO.pow(2 * i + m / 2)));
            }
            BinaryFieldElement result =
                    sum.multiply(d.pow(TWO.pow(m - 1)));
            return result.add(s).add(s.square());

        } catch (IncorrectRingElementException ex) {
            return null;
        }
    }

    // The trace of the element when the dimension is even. Is either 0 or 1,
    // -1 for error
    private static int evenTrace(final BinaryFieldElement el,
            final BinaryField k) {
        int aux = (k.getDimension() - 2) / 2 + 1;
        BinaryFieldElement t = new BinaryFieldElement(k, new BitSet());
        try {
            for (int i = 0; i < aux; i++) {
                t = t.add(el.pow(TWO.pow(2 * i)));
            }
        } catch (IncorrectRingElementException ex) {
            return -1;
        }
        if (t.getIntValue().equals(BigInteger.ONE)) {
            return 1;
        }
        if (t.getIntValue().equals(BigInteger.ZERO)) {
            return 0;
        }
        return -1;
    }

    // The trace of the element when the dimension is odd. Is either 0 or 1,
    // -1 for error
    private static int oddTrace(final BinaryFieldElement el,
            final BinaryField k) {
        BinaryFieldElement t = new BinaryFieldElement(el);
        try {
            for (int i = 1; i < k.getDimension(); i++) {
                t = t.square().add(el);
            }
        } catch (IncorrectRingElementException ex) {
            return -1;
        }
        if (t.getIntValue().equals(BigInteger.ONE)) {
            return 1;
        }
        if (t.getIntValue().equals(BigInteger.ZERO)) {
            return 0;
        }
        return -1;
    }

}
