package cat.udl.cig.utils.bfarithmetic;

import java.math.BigInteger;

public class LeastCommonMultiple {

    public static BigInteger lcm(BigInteger number1, BigInteger number2) {
        if (number1.equals(BigInteger.ZERO) || number2.equals(BigInteger.ZERO))
            return BigInteger.ZERO;
        else {
            BigInteger gcdRes = GreaterCommonDivisor.gcd(number1, number2);
            return number1.multiply(number2).abs().divide(gcdRes);
        }
    }
}
