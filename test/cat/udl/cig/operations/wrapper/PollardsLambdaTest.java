package cat.udl.cig.operations.wrapper;

import cat.udl.cig.fields.GroupElement;
import cat.udl.cig.fields.IntegerPrimeOrderSubgroup;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.Optional;

class PollardsLambdaTest {

    static BigInteger MODULE = new BigInteger("11");
    static BigInteger n = new BigInteger("7");


    @Test
    void algorithm() {
        IntegerPrimeOrderSubgroup g = new IntegerPrimeOrderSubgroup(MODULE, MODULE.subtract(BigInteger.ONE), n);
        GroupElement alpha = g.getGenerator();
        LinkedList<Integer> vertader = new LinkedList<>();
        LinkedList<Integer> falser = new LinkedList<>();
        for(int xi = 1; xi < g.getSize().intValue(); xi++) {
            BigInteger x = BigInteger.valueOf(xi);
            GroupElement beta = alpha.pow(x);
            PollardsLambda lambda = new PollardsLambda(alpha);
            Optional<BigInteger> res = lambda.algorithm(beta);
            if(res.isPresent() && res.get().equals(x)) {
                vertader.add(xi);
            } else {
                falser.add(xi);
            }
        }
        System.out.println("False: " + falser.size());
    }

}