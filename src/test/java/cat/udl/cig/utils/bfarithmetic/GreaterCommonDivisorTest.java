package cat.udl.cig.utils.bfarithmetic;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

class GreaterCommonDivisorTest {


    @ParameterizedTest
    @CsvSource({"1093,379271,378178", "1,7,11", "11,11,22"})
    void testGcd(String expectedStr, String op1Str, String op2Str) {
        BigInteger expected = new BigInteger(expectedStr);
        BigInteger op1 = new BigInteger(op1Str);
        BigInteger op2 = new BigInteger(op2Str);
        assertEquals(expected, GreaterCommonDivisor.gcd(op1, op2));

    }
}