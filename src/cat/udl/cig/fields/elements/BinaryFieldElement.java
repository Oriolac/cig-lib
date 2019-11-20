package cat.udl.cig.fields.elements;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Objects;

import cat.udl.cig.exceptions.IncorrectRingElementException;
import cat.udl.cig.fields.groups.BinaryField;
import cat.udl.cig.utils.bfarithmetic.BitSetManipulation;

/**
 * Models a <i>Binary Field Element</i>. This <i>Binary Field Element</i> has a
 * value \(k\), represented by a <i>BitSet</i>, and belongs to a
 * <i>BinaryField</i> /(F/). Any attempt to operate an instance of
 * <i>BinaryFieldElement</i> with an instance of a different kind of
 * <i>RingElement</i> causes an runtime exception.
 *
 * @see RingElement
 * @author Ricard Garra
 */
public class BinaryFieldElement implements RingElement {
    /**
     * The <i>Binary Field</i> \(\mathbb{F}\) in which this <i>Binary Field
     * Element</i> is defined.
     *
     * @see BinaryField
     */
    private final BinaryField F;

    /**
     * A BitSet that encapsulates the value of this <i>Binary Field Element</i>.
     */
    private final BitSet k; // as vector of bits representing a polynomial

    /**
     * Creates a <i>BinaryFieldElement</i> with value \(k\) and belonging to the
     * <i>BinaryField</i> \(F\). If the <i>BinaryField</i> {@code F} is not
     * initialized, then the created <i>BinaryFieldElement</i> also remains
     * uninitialized. That is, {@code this.F = null} and {@code this.k = null}.
     * This constructor does not make a deep compy of \(F\).
     *
     * @param F
     *            the <i>BinaryField</i> to which {@code this}
     *            <i>BinaryFieldElement</i> will belong.
     * @param k
     *            a BitSet representing the value for {@code this}
     *            <i>BinaryFieldElement</i>.
     * @see BinaryField
     */
    public BinaryFieldElement(final BinaryField F, final BitSet k) {
        this.F = F;
        this.k = k.get(0, F.getDimension());
    }

    /**
     * Creates a copy of the <i>BinaryFieldElement</i> \(q\). If \(q\) is null
     * or uninitialized, {@code this} instance remains uninitialized.
     *
     * @param q
     *            the <i>BinaryFieldElement</i> to be copied.
     */
    public BinaryFieldElement(final BinaryFieldElement q) {
        F = new BinaryField(q.F);
        k = (BitSet) q.k.clone();
    }

    /*
     * @Override public BinaryFieldElement cloneElement() { return new
     * BinaryFieldElement(this); }
     */

    /*
     * @Override public boolean isInitialized() { return (F != null && k !=
     * null); }
     */

    @Override
    public boolean belongsToSameGroup(final GroupElement q) {
        BinaryFieldElement q1 = (BinaryFieldElement) q;
        return F.equals(q1.F);
    }

    @Override
    public BinaryField getGroup() {
        return F;
    }

    @Override
    public BitSet getValue() {
        return (BitSet) k.clone();
    }

    @Override
    public BigInteger getIntValue() {
        return BitSetManipulation.bitSetToBigInteger(k);
    }

    @Override
    public BinaryFieldElement add(final RingElement q)
            throws IncorrectRingElementException {
        /*
         * if(!isInitialized()) { throw new
         * IncorrectRingElementException("BitFieldElement not " +
         * "initialized"); }
         */

        if (belongsToSameGroup(q)) {
            BitSet new_value = (BitSet) k.clone();
            new_value.xor(((BinaryFieldElement) q).getValue());
            return new BinaryFieldElement(F, new_value);

        } else {
            throw new IncorrectRingElementException(
                "RingElement q is not a "
                    + "correct instance of BitFieldElement");
        }
    }

    @Override
    public BinaryFieldElement subtract(final RingElement q)
            throws IncorrectRingElementException {
        return add(q);
    }

    /**
     * Computes {@code this*q (mod p)}, where \(p\) is the irreducible
     * polynomial.
     *
     * @param q
     *            the <i>RingElement</i> we want to multiply {@code this}.
     * @return a new <i>BinaryElement</i> \(r\), where \(r = p \cdot q (mod p)\)
     *         and {@code r.getRing() = this.getRing()}.
     * @throws IncorrectRingElementException
     *             if {@code this} or {@code q} are not initialized. Also throws
     *             IncorrectRingElementException {@code q} does not belong to
     *             the same <i>BinaryField</i> than {@code this} ({@code this}
     *             and {@code q} are not compatible <i>BinaryFieldElements</i>).
     */
    @Override
    public BinaryFieldElement multiply(final GroupElement q)
            throws IncorrectRingElementException {
        /*
         * if(!isInitialized()) { throw new
         * IncorrectRingElementException("BitFieldElement not " +
         * "initialized"); }
         */
        if (!belongsToSameGroup(q)) {
            throw new IncorrectRingElementException(
                "RingElement q is not a "
                    + "correct instance of BitFieldElement");
        }
        // a*b = p
        BitSet a = getValue();
        BitSet b = (BitSet) q.getValue();
        BitSet p = new BitSet();
        Boolean carry;
        BitSet modifiedReducingPolynomial;
        modifiedReducingPolynomial =
            (BitSet) F.getReducingPolynomial().clone();
        modifiedReducingPolynomial.clear(F.getDimension());

        for (int i = 0; i < F.getDimension() && !a.isEmpty()
            && !b.isEmpty(); i++) {
            if (b.get(0)) {
                p.xor(a);
            }
            b = BitSetManipulation.rightShift(b);
            carry = a.get(F.getDimension() - 1);
            a = BitSetManipulation.leftShift(a);
            a.clear(F.getDimension());
            if (carry) {
                a.xor(modifiedReducingPolynomial);
            }

        }

        return new BinaryFieldElement(F, p);
    }

    @Override
    public BinaryFieldElement divide(final GroupElement q)
            throws IncorrectRingElementException {
        /*
         * if (!isInitialized()) { throw new
         * IncorrectRingElementException("BinaryFieldElement not " +
         * "initialized"); }
         */
        if (!belongsToSameGroup(q)) {
            throw new IncorrectRingElementException(
                "RingElement q is not a "
                    + "correct instance of BitFieldElement");
        }
        if (((BinaryFieldElement) q).k.isEmpty()) {
            throw new IncorrectRingElementException(
                "RingElement q cannot be 0");
        }

        BinaryFieldElement q_inverse = (BinaryFieldElement) q.inverse();
        return multiply(q_inverse);
    }

    @Override
    public BinaryFieldElement opposite() {// throws
        // IncorrectRingElementException {
        /*
         * if(!isInitialized()) { throw new
         * IncorrectRingElementException("BitFieldElement not " +
         * "initialized"); }
         */

        return this;
    }

    /**
     * Returns the inverse of this
     * http://en.wikipedia.org/wiki/Extended_Euclidean_algorithm
     *
     * @return The inverse
     * @throws IncorrectRingElementException
     */
    @Override
    public BinaryFieldElement inverse() {// throws IncorrectRingElementException
        // {
        long[] u = getValue().toLongArray();
        long[] v = F.getReducingPolynomial().toLongArray();
        long[] g1 = new long[1]; // 1
        g1[0] = 1L;
        long[] g2 = new long[1]; // 0
        long[] aux;
        int j;

        while (degree(u) != 1) {
            j = degree(u) - degree(v);
            if (j < 0) {
                aux = u;
                u = v;
                v = aux;

                aux = g1;
                g1 = g2;
                g2 = aux;

                j = -j;
            }

            u = longXor(u, longLeftShift(v, j));
            g1 = longXor(g1, longLeftShift(g2, j));

        }
        return new BinaryFieldElement(F, BitSet.valueOf(g1));
    }

    /**
     * Computes the degree of a given polynomial stored in the parameter called
     * bitArray.
     *
     * @param bitArray
     *            the representation of the polynomial (in base 2)
     * @return an int representing the degree of the polynomial represented by
     *         the parameter {@code bitArray}.
     */
    private int degree(final long[] bitArray) {
        int firstNonZeroWord = bitArray.length - 1;
        while (firstNonZeroWord >= 0 && bitArray[firstNonZeroWord] == 0L) {
            firstNonZeroWord--;
        }
        if (firstNonZeroWord < 0) {
            return 0;
        }
        return 64 * (firstNonZeroWord)
            + (64 - Long.numberOfLeadingZeros(bitArray[firstNonZeroWord]));
    }

    /**
     * Computes \(a \oplus b\).
     *
     * @param a
     *            a long[] representing the first operand.
     * @param b
     *            a long[] representing the second operand.
     * @return a long[] \(r = a \oplus b\).
     */
    private long[] longXor(long[] a, final long[] b) {

        int wordsInCommon = Math.min(a.length, b.length);
        for (int i = 0; i < wordsInCommon; i++) {
            a[i] ^= b[i];
        }

        if (wordsInCommon < b.length) {
            long[] newA = new long[a.length + b.length - wordsInCommon];
            System.arraycopy(a, 0, newA, 0, a.length);
            a = newA;
            System.arraycopy(b, wordsInCommon, a, wordsInCommon, b.length
                - wordsInCommon);
        }
        return a;
    }

    /**
     * Computes a \(n\) bits left shift, where the binary number is represented
     * as a long[]
     *
     * @param aLong
     *            a long[] representing a binary number
     * @param n
     *            the number of bits that will be shifted to the left.
     * @return a long[] that contains a binary number which is the result of
     *         applying \(n\) bits left shift to the parameter {@code aLong}.
     */
    private long[] longLeftShift(final long[] aLong, final int n) {
        long[] tmp = new long[aLong.length];
        System.arraycopy(aLong, 0, tmp, 0, aLong.length);
        for (int i = 0; i < n; i++) {
            tmp = longLeftShift(tmp);
        }
        return tmp;
    }

    /**
     * Computes a bit left shift, where the binary number is represented as a
     * long[]
     *
     * @param aLong
     *            a long[] representing a binary number
     * @return a long[] that contains a binary number which is the result of
     *         applying a bit left shift to the parameter {@code aLong}.
     */
    private long[] longLeftShift(long[] aLong) {
        final long maskOfCarry = -9223372036854775808L;
        boolean carry = false;
        for (int i = 0; i < aLong.length; ++i) {
            if (carry) {
                carry = ((aLong[i] & maskOfCarry) != 0);
                aLong[i] <<= 1;
                ++aLong[i];
            } else {
                carry = ((aLong[i] & maskOfCarry) != 0);
                aLong[i] <<= 1;
            }
        }
        if (carry) {
            long[] tmp = new long[aLong.length + 1];
            System.arraycopy(aLong, 0, tmp, 0, aLong.length);
            ++tmp[aLong.length];
            aLong = tmp;
        }
        return aLong;
    }

    /**
     * Computes {@code this^k (mod p)}, where \(p\) is the irreducible
     * polynomial.
     *
     * @param k
     *            a <i>BigInteger</i> representing the exponent of the
     *            operation.
     * @return a BinaryFieldElement {@code r = this ^ k (mod p)}.
     * @throws IncorrectRingElementException
     *             if {@code this} is not initialized.
     */
    @Override
    public BinaryFieldElement pow(BigInteger k)
            throws IncorrectRingElementException {
        /*
         * if(!isInitialized()) { throw new
         * IncorrectRingElementException("BitFieldElement not " +
         * "initialized"); }
         */

        // BitSet temp = new BitSet();
        // temp.set(0);
        BinaryFieldElement result = F.getNeuterElement();
        BinaryFieldElement b = new BinaryFieldElement(this);

        // k mod ((2^m) -1)
        k = k.mod(F.getSize().subtract(BigInteger.ONE));

        while (k.bitLength() != 0) {
            if (k.testBit(0)) {
                result = result.multiply(b);
            }
            k = k.shiftRight(1);
            b = b.square();
        }
        return result;
    }

    /**
     * Computes {@code this^q1 (mod p)}, where \(p\) is the irreducible
     * polynomial.
     *
     * @param q1
     *            a <i>BitSet</i> representing the exponent of the operation.
     * @return a BinaryFieldElement {@code r = this ^ q1 (mod p)}.
     * @throws IncorrectRingElementException
     *             if {@code this} is not initialized.
     */
    public BinaryFieldElement pow(final BitSet q1)
            throws IncorrectRingElementException {
        /*
         * if(!isInitialized()) { throw new
         * IncorrectRingElementException("BitFieldElement not " +
         * "initialized"); }
         */

        BitSet temp = new BitSet();
        temp.set(0);
        BinaryFieldElement result = new BinaryFieldElement(F, temp);
        BinaryFieldElement b = new BinaryFieldElement(F, getValue());

        BitSet q = (BitSet) q1.clone();
        BigInteger value = BitSetManipulation.bitSetToBigInteger(q);
        if (value.compareTo(F.getSize().subtract(BigInteger.ONE)) > 0) {
            q =
                BitSetManipulation.bigIntegerToBitSet(value.mod(F
                    .getSize().subtract(BigInteger.ONE)));
        }

        while (!q.isEmpty()) {
            if (q.get(0)) {
                result = result.multiply(b);
            }
            q = BitSetManipulation.rightShift(q);
            b = b.square();
        }
        return result;
    }

    /**
     * Computes \(q^2\), where \(q\) is {@code this} BinaryFieldElement. It is
     * optimized for trinomials of the form \(x^m + x^n +1\), where \(n &#60;=
     * \text{floor}(m/2)\)
     *
     * @return a BinaryFieldElement representing \(q^2\).
     * @throws IncorrectRingElementException
     *             if {@code this} is not initialized.
     */
    public BinaryFieldElement square()
            throws IncorrectRingElementException {
        /*
         * if(!isInitialized()) { throw new
         * IncorrectRingElementException("BitFieldElement not " +
         * "initialized"); }
         */

        BitSet P = F.getReducingPolynomial();
        int m = P.length() - 1;
        int n = P.nextSetBit(1);
        // P has to be trinomial of the form x^m + x^n +1, where n<=floor(m/2)
        // is trinomial? has the 1 term? is the size correct?
        if (P.cardinality() != 3 || !P.get(0) || m != F.getDimension()
                // n has to be at most m/2
                || n > (m / 2)) {
            if (P.cardinality() == 3) {
                return squareTrinomialGeneral();
            }
            return multiply(this);
        }

        if (m % 2 == 0b0) { // m even
            if (n == m / 2) { // n = m/2
                return squareType2();
            }
            return squareType1(); // n < m/2
        }
        // m odd
        // n even
        if (n % 2 == 0b0) {
            return squareType4();
        } // n odd
        return squareType3();
    }

    /**
     * Computes \(q^2\), where \(q\) is {@code this} BinaryFieldElement. It is
     * optimized for trinomials of the form \(x^m + x^n +1\), where \(m\) is
     * even and \(n\) is odd, \(n &#60; m/2\) Method from "Low Complexity
     * Bit-Parallel Square Root Computation over GF(2^m) for all Trinomials", by
     * F. Rodríguez, G. Morales, J. López. NOTE: There's an error in the
     * original paper, formula 18, for the squaring of type I polynomials, in
     * the case of i odd, i < n. It should be (m-((n-i)/2)), instad of
     * (m+1-((n+i)/2))
     *
     * @return a BinaryFieldElement representing \(q^2\).
     */
    private BinaryFieldElement squareType1() {
        BitSet P = F.getReducingPolynomial();
        final BitSet a = k;
        int m = P.length() - 1;
        int n = P.nextSetBit(1);
        BitSet c = new BitSet(m);
        int j;
        for (int i = 0; i < m; i += 2) {
            if (((i < n || i >= n * 2) && (a.get(i / 2) ^ a
                .get((m + i) / 2)))
                || (i > n && (i < (n * 2) && (a.get(i / 2)
                    ^ a.get((m + i) / 2) ^ a.get(m - n + (i / 2)))))) {
                c.set(i);
            }
            j = i + 1;
            if (j < m && (j < n && (a.get(m - ((n - j) / 2))))
                || (j >= n && (a.get((m - n + j) / 2)))) {
                c.set(j);
            }
        }
        /*
         * for (int i = 0; i < m; i++) { if (i%2 == 0b0) { //i even if (n < i &&
         * i < (n*2) ) { //n < i < 2n if (a.get(i/2) ^ a.get((m+i)/2) ^
         * a.get(m-n+(i/2))) c.set(i); continue; } //i<n or i>=2n if (a.get(i/2)
         * ^ a.get((m+i)/2)) c.set(i); continue; } //i odd if (i<n) { //i<n if
         * (a.get(m-((n-i)/2))) c.set(i); continue; } //i>=n if
         * (a.get((m-n+i)/2)) c.set(i); }
         */
        return new BinaryFieldElement(F, c);
    }

    /**
     * Computes \(q^2\), where \(q\) is {@code this} BinaryFieldElement. It is
     * optimized for trinomials of the form \(x^m + x^n +1\), where \(m\) is
     * even and \(n\) is odd, \(n = m/2\) Method from "Low Complexity
     * Bit-Parallel Square Root Computation over GF(2^m) for all Trinomials", by
     * F. Rodríguez, G. Morales, J. López. NOTE: There's an error in the
     * original paper, formula 19, for the squaring of type II polynomials, in
     * the case of i odd, i < n. It should be (m-((n-i)/2)), instad of
     * (m+1-((n+i)/2))
     *
     * @return a BinaryFieldElement representing \(q^2\).
     */
    private BinaryFieldElement squareType2() {
        BitSet P = F.getReducingPolynomial();
        final BitSet a = k;
        int m = P.length() - 1;
        int n = P.nextSetBit(1);
        BitSet c = new BitSet(m);
        int j;
        for (int i = 0; i < m; i += 2) {
            if ((i < n && (a.get(i / 2) ^ a.get((m + i) / 2)))
                || (i > n && a.get(i / 2))) {
                c.set(i);
            }
            j = i + 1;
            if (j < m
                && ((j < n && a.get(m - ((n - j) / 2))) || (j >= n && (a
                    .get((n + j) / 2))))) {
                c.set(j);
            }
        }
        /*
         * for (int i = 0; i < m; i++) { if (i%2 == 0b0) { //i even if (i < n) {
         * //i<n if (a.get(i/2) ^ a.get((m+i)/2) ) c.set(i); continue; } //i>n
         * if (a.get(i/2)) c.set(i); continue; } //i odd if (i < n) { //i<n if
         * (a.get(m-((n-i)/2))) c.set(i); continue; } //i>=n if (a.get((n+i)/2))
         * c.set(i); }
         */
        return new BinaryFieldElement(F, c);
    }

    /**
     * Computes \(q^2\), where \(q\) is {@code this} BinaryFieldElement. It is
     * optimized for trinomials of the form \(x^m + x^n +1\), where \(m\) is odd
     * and \(n\) is odd, \(n &#60; (m + 1)/2\) Method from "Low Complexity
     * Bit-Parallel Square Root Computation over GF(2^m) for all Trinomials", by
     * F. Rodríguez, G. Morales, J. López.
     *
     * @return a BinaryFieldElement representing \(q^2\).
     */
    private BinaryFieldElement squareType3() {
        BitSet P = F.getReducingPolynomial();
        final BitSet a = k;
        int m = P.length() - 1;
        int n = P.nextSetBit(1);
        int j;
        BitSet c = new BitSet(m);
        for (int i = 0; i < m; i += 2) {
            if ((i < n && a.get(i / 2))
                || (i > n
                    && (i < (n * 2) && (a.get(i / 2)
                        ^ a.get((i / 2) + ((m - n) / 2)) ^ a.get((i / 2)
                        + m - n))) || i >= 2 * n
                    && (a.get(i / 2) ^ a.get((i / 2) + ((m - n) / 2))))) {
                c.set(i);
            }
            j = i + 1;
            if (j < m
                && ((j < n && (a.get((m + j) / 2) ^ a
                    .get(((m * 2) + j - n) / 2))) || (j >= n && a
                    .get((m + j) / 2)))) {
                c.set(j);
            }
        }

        /*
         * for (int i = 0; i < m; i++) { if (i%2 == 0b0) { //i even if (i < n) {
         * //i<n if (a.get(i/2)) c.set(i); continue; } //i>n if (i<(n*2)) {
         * //n<i<2n if (a.get(i/2) ^ a.get((i/2)+((m-n)/2)) ^ a.get((i/2)+m-n))
         * c.set(i); continue; } //i>=2n if (a.get(i/2) ^
         * a.get((i/2)+((m-n)/2))) c.set(i); continue; } //i odd if (i < n) {
         * //i<n if (a.get((m+i)/2) ^ a.get(((m*2)+i-n)/2 )) c.set(i); continue;
         * } //i>=n if (a.get((m+i)/2)) c.set(i); }
         */

        return new BinaryFieldElement(F, c);
    }

    /**
     * Computes \(q^2\), where \(q\) is {@code this} BinaryFieldElement. It is
     * optimized for trinomials of the form \(x^m + x^n +1\), where \(m\) is odd
     * and \(n\) is even, \(n &#60; (m + 1)/2\) Method from "Low Complexity
     * Bit-Parallel Square Root Computation over GF(2^m) for all Trinomials", by
     * F. Rodríguez, G. Morales, J. López.
     *
     * @return a BinaryFieldElement representing \(q^2\).
     */
    private BinaryFieldElement squareType4() {
        BitSet P = F.getReducingPolynomial();
        final BitSet a = k;
        int m = P.length() - 1;
        int n = P.nextSetBit(1);
        BitSet c = new BitSet(m);
        int j;

        for (int i = 0; i < m; i += 2) {
            if (i < n
                && (a.get(i / 2) ^ a.get((i / 2) + m - (n / 2)))
                || (i >= n && i < 2 * n && (a.get(i / 2) ^ a.get((i / 2)
                    + m - n))) || (i >= 2 * n && a.get(i / 2))) {
                c.set(i);
            }
            j = i + 1;
            if (j < m
                && ((j < n && a.get((m + j) / 2)) || (j > n && (a
                    .get((m + j) / 2) ^ a.get((m + j - n) / 2))))) {
                c.set(j);
            }
        }
        /*
         * for (int i = 0; i < m; i++) { if (i%2 == 0b0) { //i even if (i < n) {
         * //i<n if (a.get(i/2) ^ a.get((i/2)+m-(n/2))) c.set(i); continue; }
         * //i>=n if (i < (n*2)) { //n<=i<2n if (a.get(i/2) ^ a.get((i/2)+m-n))
         * c.set(i); continue; } //i>=2n if (a.get(i/2)) c.set(i); continue; }
         * //i odd if (i < n) { //i<n if (a.get((m+i)/2)) c.set(i); continue; }
         * //i>n if (a.get((m+i)/2) ^ a.get((m+i-n)/2)) c.set(i); }
         */
        return new BinaryFieldElement(F, c);
    }

    // Auxiliar method for squareXOR.
    private BitSet getAugmentedVector(final BitSet a) {
        BitSet augmented = new BitSet(a.length() * 2);
        for (int i = 0; i < a.length() * 2; i += 2) {
            if (a.get(i / 2)) {
                augmented.set(i);
            }
        }
        return augmented;
    }

    // Auxiliar method for squareTrinomialGeneral.
    private BitSet squareXOR(final BitSet a, final int m, final int n) {
        BitSet augmented = getAugmentedVector(a);
        BitSet W = augmented.get(0, m);
        BitSet X = augmented.get(m, 2 * m);
        BitSet Y =
            BitSetManipulation.leftShift(augmented.get(m, 2 * m - n), n);
        BitSet Z = augmented.get(2 * m - n, 2 * m);
        BitSet Z2 =
            BitSetManipulation.leftShift(augmented.get(2 * m - n, 2 * m),
                n);
        Z.xor(Z2);

        W.xor(X);
        W.xor(Y);
        W.xor(Z);

        return W;
    }

    /**
     * Computes \(q^2\), where \(q\) is {@code this} BinaryFieldElement, when
     * the reducing polynomial is a trinomial. Only used if the trinomial is not
     * any of the 4 special polynomials for squaring (see squareType1 to 4).
     * Method from "Low Complexity Bit-Parallel Square Root Computation over
     * GF(2^m) for all Trinomials", by F. Rodríguez, G. Morales, J. López.
     *
     * @return a BinaryFieldElement representing \(q^2\).
     */
    private BinaryFieldElement squareTrinomialGeneral() {
        BitSet P = F.getReducingPolynomial();
        return new BinaryFieldElement(F, squareXOR(k, P.length() - 1,
            P.nextSetBit(1)));
    }

    /**
     * Computes \(\sqrt(q)\), where \(q\) is {@code this} BinaryFieldElement. It
     * is optimized for trinomials of the form \(x^m + x^n +1\), where \(n
     * &#60;= \text{floor}(m/2)\) and \(m\) is ood, \(n\) is even and
     * \(\text{ceil}((m - 1) / 4) &#60;= n &#60; \text{floor}((m - 1) / 3)\).
     *
     * @return a BinaryFieldElement representing \(\sqrt(q)\).
     * @throws IncorrectRingElementException
     *             if {@code this} <i>BinaryFieldElement</i> is not initialized.
     */
    @Override
    public ArrayList<RingElement> squareRoot()
            throws IncorrectRingElementException {
        /*
         * if(!isInitialized()) { throw new
         * IncorrectRingElementException("BitFieldElement not " +
         * "initialized"); }
         */
        ArrayList<RingElement> root = new ArrayList<RingElement>();
        BitSet P = F.getReducingPolynomial();
        int m = P.length() - 1;
        int n = P.nextSetBit(1);
        // P has to be trinomial of the form x^m + x^n +1, where n<=floor(m/2),
        // or if m is odd and n even, ceil((m-1)/4) <= n < floor((m-1)/3)
        // is trinomial? has the 1 term? is the size correct?
        if (P.cardinality() != 3 || !P.get(0) || m != F.getDimension()
                // n has to be at most m/2, and if m is odd and n even,
                || n > (m / 2) || (m % 2 == 0b1 && n % 2 == 0b0
                // n cant be smaller than ceil((m-1)/4) or greater than
                // floor((m-1)/3)
                && (n < Math.ceil((m - 1) / 4.0) || n >= ((m - 1) / 3)))) {

            BinaryFieldElement a = this;
            for (int i = 0; i < F.getDimension() - 1; i++) {
                a = a.square();
            }
            root.add(a);
            return root;
        }

        BinaryFieldElement result;

        if (m % 2 == 0b0) { // m even
            if (n == m / 2) { // n = m/2
                result = squareRootType2();
            } else {
                result = squareRootType1(); // n < m/2
            }
        } else {
            // m odd
            if (n % 2 == 0b0) { // n even
                result = squareRootType4();
            } else { // n odd
                result = squareRootType3();
            }
        }

        root.add(result);
        return root;

    }

    /**
     * Computes \(\sqrt(q)\), where \(q\) is {@code this} BinaryFieldElement. It
     * is optimized for trinomials of the form \(x^m + x^n +1\), where \(m\) is
     * even, \(n &#60; m / 2\). Method from "Low Complexity Bit-Parallel Square
     * Root Computation over GF(2^m) for all Trinomials", by F. Rodríguez, G.
     * Morales, J. López.
     *
     * @return a BinaryFieldElement representing \(\sqrt(q)\).
     */
    private BinaryFieldElement squareRootType1() {
        BitSet P = F.getReducingPolynomial();
        final BitSet a = k;
        final int m = P.length() - 1;
        final int n = P.nextSetBit(1);
        BitSet d = new BitSet(m);
        boolean doubleiBit;
        boolean doubleiPlusnModmBit;
        for (int i = 0; i < m; i++) {
            doubleiPlusnModmBit = a.get((i * 2 + n) % m);
            doubleiBit = a.get(i * 2);
            if (i <= n / 2 || (n <= i && i < m / 2)) {
                if ((doubleiBit ^ doubleiPlusnModmBit)) {
                    d.set(i);
                }
                continue;
            }
            if (n / 2 < i && i < n) {
                if (doubleiBit ^ doubleiPlusnModmBit ^ a.get(i * 2 - n)) {
                    d.set(i);
                }
                continue;
            }
            if (m / 2 <= i && i < m && doubleiPlusnModmBit) {
                d.set(i);
            }
        }
        return new BinaryFieldElement(F, d);
    }

    /**
     * Computes \(\sqrt(q)\), where \(q\) is {@code this} BinaryFieldElement. It
     * is optimized for trinomials of the form \(x^m + x^n +1\), where \(m\) is
     * even, \(n = m / 2\). Method from "Low Complexity Bit-Parallel Square Root
     * Computation over GF(2^m) for all Trinomials", by F. Rodríguez, G.
     * Morales, J. López.
     *
     * @return a BinaryFieldElement representing \(\sqrt(q)\).
     */
    private BinaryFieldElement squareRootType2() {
        BitSet P = F.getReducingPolynomial();
        final BitSet a = k;
        final int m = P.length() - 1;
        BitSet d = new BitSet(m);
        boolean doubleiBit;
        boolean doubleiPlusHalfmModm;
        for (int i = 0; i < m; i++) {
            doubleiPlusHalfmModm = a.get((2 * i + m / 2) % m);
            doubleiBit = a.get(2 * i);
            if (i < (m + 2) / 4 && (doubleiBit ^ doubleiPlusHalfmModm)) {
                d.set(i);
                continue;
            }
            if ((m + 2) / 4 <= i && i < m / 2 && doubleiBit) {
                d.set(i);
                continue;
            }
            if ((m / 2) <= i && i < m && doubleiPlusHalfmModm) {
                d.set(i);
            }
        }
        return new BinaryFieldElement(F, d);
    }

    /**
     * Computes \(\sqrt(q)\), where \(q\) is {@code this} BinaryFieldElement. It
     * is optimized for trinomials of the form \(x^m + x^n +1\), where \(m\) is
     * odd and \(n\) is odd. Method from "Low Complexity Bit-Parallel Square
     * Root Computation over GF(2^m) for all Trinomials", by F. Rodríguez, G.
     * Morales, J. López.
     *
     * @return a BinaryFieldElement representing \(\sqrt(q)\).
     */
    private BinaryFieldElement squareRootType3() {
        BitSet P = F.getReducingPolynomial();
        final BitSet a = k;
        final int m = P.length() - 1;
        final int n = P.nextSetBit(1);
        BitSet d = new BitSet(m);
        final int halfn = (n + 1) / 2;
        final int halfm = (m + 1) / 2;
        final int halfboth = (m + n) / 2;
        int doublei;
        boolean doubleiBit;
        for (int i = 0; i < m; i++) {
            doublei = i * 2;
            doubleiBit = a.get(doublei);
            if (i < halfn && doubleiBit) { // i < (n+1)/2
                d.set(i);
                continue;
            }
            if (halfn <= i && i < halfm
                && (doubleiBit ^ a.get(doublei - n))) { // (n+1)/2 <= i <
                // (m+1)/2
                d.set(i);
                continue;
            }
            if (halfm <= i && i < halfboth
                && (a.get(doublei - n) ^ a.get(doublei - m))) { // (m+1)/2 <= 0
                // < (m+n)/2
                d.set(i);
                continue;
            }
            if (halfboth <= i && i < m && a.get(doublei - m)) {
                d.set(i);
            }
        }
        return new BinaryFieldElement(F, d);
    }

    /**
     * Computes \(\sqrt(q)\), where \(q\) is {@code this} BinaryFieldElement. It
     * is optimized for trinomials of the form \(x^m + x^n +1\), where \(m\) is
     * odd, \(n\) is even and \(\text{ceil}((m - 1) / 4) &#60;= n &#60;
     * \text{floor}((m - 1) / 3)\). Method from "Low Complexity Bit-Parallel
     * Square Root Computation over GF(2^m) for all Trinomials", by F.
     * Rodríguez, G. Morales, J. López.
     *
     * @return a BinaryFieldElement representing \(\sqrt(q)\).
     */
    private BinaryFieldElement squareRootType4() {
        BitSet P = F.getReducingPolynomial();
        final BitSet a = k;
        final int m = P.length() - 1;
        final int n = P.nextSetBit(1);
        BitSet d = new BitSet(m);

        for (int i = 0; i < m; i++) {
            if (i < n) {
                firstHalfType4(i, m, n, a, d);
            } else {
                secondHalfType4(i, m, n, a, d);
            }
        }
        return new BinaryFieldElement(F, d);
    }

    // Auxiliar function used for squareRootType4, for i < n
    // Depending in the value of i, some terms won't be used
    private void firstHalfType4(final int i, final int m, final int n,
            final BitSet a, final BitSet d) {
        boolean zeroTerm = a.get(2 * i);
        boolean oneTerm = a.get(2 * i + m - n);
        boolean twoTerm = a.get(2 * i + m - 2 * n);
        boolean threeTerm = a.get(2 * i + m - 3 * n);
        boolean fourTerm = a.get(Math.max(2 * i + m - 4 * n, 0));
        boolean fiveTerm = a.get(Math.max(2 * i + m - 5 * n, 0));

        boolean conditionOne = zeroTerm ^ oneTerm ^ twoTerm ^ threeTerm;
        boolean conditionTwo = conditionOne ^ fourTerm;
        boolean conditionThree = conditionTwo ^ oneTerm;
        boolean conditionFour = conditionThree ^ fiveTerm;

        if (i < ((4 * n - (m - 1)) / 2)) {
            if (conditionOne) {
                d.set(i);
            }
        } else if (i < n / 2) {
            if (conditionTwo) {
                d.set(i);
            }
        } else if (i < ((5 * n - (m - 1)) / 2)) {
            if (conditionThree) {
                d.set(i);
            }
        } else if (conditionFour) {
            d.set(i);
        }
    }

    // Auxiliar function used for squareRootType4, for i >= n
    // Depending in the value of i, some terms won't be used
    private void secondHalfType4(final int i, final int m, final int n,
            final BitSet a, final BitSet d) {
        boolean zeroTerm = a.get(2 * i);
        boolean oneTerm = a.get(Math.max(2 * i - m, 0));
        boolean twoTerm = a.get(Math.max(2 * i - m - n, 0));
        boolean threeTerm = a.get(Math.max(2 * i - m - 2 * n, 0));
        boolean fourTerm = a.get(Math.max(2 * i - m - 3 * n, 0));

        boolean conditionOne = zeroTerm;
        boolean conditionTwo = oneTerm;
        boolean conditionThree = conditionTwo ^ twoTerm;
        boolean conditionFour = conditionThree ^ threeTerm;
        boolean conditionFive = conditionFour ^ fourTerm;

        if (i <= ((m - 1) / 2)) {
            if (conditionOne) {
                d.set(i);
            }
        } else if (i < ((n + m + 1) / 2)) {
            if (conditionTwo) {
                d.set(i);
            }
        } else if (i < ((2 * n + m + 1) / 2)) {
            if (conditionThree) {
                d.set(i);
            }
        } else if (i < ((3 * n + m + 1) / 2)) {
            if (conditionFour) {
                d.set(i);
            }
        } else if (conditionFive) {
            d.set(i);
        }
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 19 * hash + Objects.hashCode(F);
        hash = 19 * hash + Objects.hashCode(k);
        return hash;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final BinaryFieldElement other = (BinaryFieldElement) obj;
        return k.equals(other.k) && F.equals(other.F);
    }

    /**
     * Returns a String with the Binary representation of {@code this}
     * <i>BinaryFieldElement</i>.
     *
     * @return a String with the Binary representation of {@code this}
     *         <i>BinaryFieldElement</i>.
     */
    public String toBinString() {
        if (k == null) {
            return "";
        }
        String output = "";
        for (int i = k.length() - 1; i >= 0; i--) {
            if (k.get(i)) {
                output += "1";
            } else {
                output += "0";
            }

        }
        return output;

    }

    @Override
    public String toString() {
        return toPolynomialString();
    }

    /**
     * Returns a String with the Polynomial representation of {@code this}
     * <i>BinaryFieldElement</i>.
     *
     * @return a String with the Polynomial representation of {@code this}
     *         <i>BinaryFieldElement</i>.
     */
    public String toPolynomialString() {
        if (k == null) {
            return "";
        }
        if (k.length() == 0) {
            return "0";
        }
        String output = "";
        for (int i = k.length() - 1; i >= 0; i--) {

            if (k.get(i)) {
                if (i < k.length() - 1) {
                    output += "+";
                }
                if (i > 0) {
                    output += "a^" + i;
                } else {
                    output += "1";
                }
            }
        }

        return output;
    }

}
