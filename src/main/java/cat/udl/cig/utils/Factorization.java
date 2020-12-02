
  /* Programming With Prime Numbers */
/* http://programmingpraxis.com/programming-with-prime-numbers-source-code-in-java/*/
package cat.udl.cig.utils;


import java.util.Random;
import java.math.BigInteger;
import java.util.ArrayList;

/**
 * Auxiliar class to compute factorizations.
 * 
 * @author Ricard Garra
 */
public class Factorization {
    /*Auxiliar method*/
    private static Boolean isSpsp(BigInteger n, BigInteger a)
    {
        BigInteger two = BigInteger.valueOf(2);
        BigInteger n1 = n.subtract(BigInteger.ONE);
        BigInteger d = n1;
        int s = 0;

        while (d.mod(two).equals(BigInteger.ZERO))
        {
            d = d.divide(two);
            s += 1;
        }

        BigInteger t = a.modPow(d, n);

        if (t.equals(BigInteger.ONE) || t.equals(n1))
        {
            return true;
        }

        while (--s > 0)
        {
            t = t.multiply(t).mod(n);
            if (t.equals(n1))
            {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks if \(n\) is a prime number.
     * 
     * @param n the BigInteger we want to check if it is a prime number.
     * 
     * @return {@code true} if \(n\) is prime and {@code false} otherwise.
     */
    public static Boolean isPrime(BigInteger n)
    {
        Random r = new Random();
        BigInteger two = BigInteger.valueOf(2);
        //BigInteger n3 = n.subtract(BigInteger.valueOf(3));
        BigInteger a;
        int k = 25;

        if (n.compareTo(two) < 0)
        {
            return false;
        }

        if (n.mod(two).equals(BigInteger.ZERO))
        {
            return n.equals(two);
        }

        while (k > 0)
        {
            a = new BigInteger(n.bitLength(), r).add(two);
            while (a.compareTo(n) >= 0)
            {
                a = new BigInteger(n.bitLength(), r).add(two);
            }

            if (! isSpsp(n, a))
            {
                return false;
            }

            k -= 1;
        }

        return true;
    }

    /*Auxiliar method*/
    private static BigInteger rhoFactor(BigInteger n, BigInteger c)
    {
        BigInteger t = BigInteger.valueOf(2);
        BigInteger h = BigInteger.valueOf(2);
        BigInteger d = BigInteger.ONE;

        while (d.equals(BigInteger.ONE))
        {
            t = t.multiply(t).add(c).mod(n);
            h = h.multiply(h).add(c).mod(n);
            h = h.multiply(h).add(c).mod(n);
            d = t.subtract(h).gcd(n);
        }

        if (d.equals(n)) /* cycle */
        {
            return rhoFactor(n, c.add(BigInteger.ONE));
        }
        else if (isPrime(d)) /* success */
        {
            return d;
        }
        else /* found composite factor */
        {
            return rhoFactor(d, c.add(BigInteger.ONE));
        }
    }

    /**
     * Computes the factors of \(n\).
     * 
     * @param n the BigInteger we want to factorise.
     * 
     * @return and ArrayList with the factors of \(n\).
     */
    public static ArrayList<BigInteger> rhoFactors(BigInteger n)
    {
        BigInteger f;
        BigInteger two = BigInteger.valueOf(2);
        ArrayList<BigInteger> fs = new ArrayList<BigInteger>();

        if (n.compareTo(two) < 0)
        {
            return fs;
        }

        while (n.mod(two).equals(BigInteger.ZERO))
        {
            n = n.divide(two);
            fs.add(two);
        }

        if (n.equals(BigInteger.ONE))
        {
            return fs;
        }

        while (! n.isProbablePrime(25))
        {
            f = rhoFactor(n, BigInteger.ONE);
            n = n.divide(f);
            fs.add(f);
        }

        fs.add(n);        
        return fs;
    } 
  
 
}
    
