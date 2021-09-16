package cat.udl.cig.utils.bfarithmetic;

import java.math.BigInteger;

public class GreaterCommonDivisor {

    public static BigInteger gcd(BigInteger a, BigInteger b){
        if (b.equals(BigInteger.ZERO)) return a;
        return gcd(b, a.mod(b));
    }
}
