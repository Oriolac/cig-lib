/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cat.udl.cig.utils.bfarithmetic;

import java.math.BigInteger;
import java.util.BitSet;

/**
 * Auxiliar class to compute conversions between types..
 * 
 * @author Ricard Garra
 */
public class BitSetManipulation {
    /**
     * Computes a bit right shift.
     * 
     * @param a the BitSet we want to shift.
     * 
     * @return a bit right shift over \(a\).
     */
    public static BitSet rightShift(BitSet a) {
        return a.get(1, a.length());
    }

    /**
     * Translates a BitSet to a BigInteger.
     * 
     * @param bits the BitSet to be translated.
     * 
     * @return a BigInteger equivalent to {@code bits}.
     */
    public static BigInteger bitSetToBigInteger(BitSet bits) {
        BigInteger value = BigInteger.ZERO;
        for (int i = bits.nextSetBit(0); i >= 0; i = bits.nextSetBit(i + 1)) {
            value = value.setBit(i);
        }
        return value;
    }

    /**
     * Translates a BigInteger to a BitSet.
     * 
     * @param value the BigInteger to be translated.
     * 
     * @return a BitSet equivalent to {@code value}.
     */
    public static BitSet bigIntegerToBitSet(BigInteger value) {
        BitSet bits = new BitSet();
        for (int i = 0; i < value.bitLength(); i++) {
            if (value.testBit(i)) {
                bits.set(i);
            }
        }
        return bits;
    }

    /**
     * Translates a long to a BitSet.
     * 
     * @param value the long to be translated.
     * 
     * @return a BitSet equivalent to {@code value}.
     */
    public static BitSet longToBitSet(long value) {
        BitSet bits = new BitSet();
        int index = 0;
        while (value != 0L) {
            if (value % 2L != 0) {
                bits.set(index);
            }
            ++index;
            value = value >>> 1;
        }
        return bits;
    }

    /**
     * Computes a bit left shift.
     * 
     * @param bitSet the BitSet we want to shift.
     * 
     * @return a bit left shift over \(bitSet\).
     */
    public static BitSet leftShift(BitSet bitSet) {
        final long maskOfCarry = -9223372036854775808L;
        long[] aLong = bitSet.toLongArray();
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
        return BitSet.valueOf(aLong);
    }

    /**
     * Computes a \(n\) bits left shift.
     * 
     * @param bitSet the BitSet we want to shift.
     * @param n the number of bits we want to shift.
     * 
     * @return an \(n\) bit left shift over \(bitSet\).
     */
    public static BitSet leftShift(BitSet bitSet, int n) {
        for (int i = 0; i < n; i++) {
            bitSet = leftShift(bitSet);
        }
        return bitSet;
    }

    /**
     * Translates a BitSet to a long.
     * 
     * @param bits the BitSet to be translated.
     * 
     * @return a long equivalent to {@code bits}.
     */
    public static long bitSetToLong(BitSet bits) {
        long value = 0L;
        for (int i = bits.nextSetBit(0); i >= 0; i = bits.nextSetBit(i + 1)) {
            value += (1L << i);
        }
        return value;
    }
    
    /**
     * Translates a string to a BitSet.
     * 
     * @param element the string to be translated.
     * 
     * @return a BitSet equivalent to {@code element}.
     */
    public static BitSet stringToBitSet(String element) {
        BitSet bits = new BitSet();
        String temp = element.replaceAll("\n", "");
        temp = temp.replaceAll(" ", "");
        String[] temp2 = temp.split("\\+");
        String[] temp3;        
        for (String temp21 : temp2) {
            temp3 = temp21.split("\\^");
            if (temp3.length == 2) {
                bits.set(Integer.parseInt(temp3[1]));
            } else {
                if ("1".equals(temp3[0]))
                    bits.set(0);
                else
                    bits.set(1);
            }            
        }
        return bits;
    }
    
}
