package cat.udl.cig.operations.wrapper;

import cat.udl.cig.exceptions.NotSolutionException;
import cat.udl.cig.exceptions.ParametersException;
import cat.udl.cig.fields.Group;
import cat.udl.cig.fields.GroupElement;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class PollardsLambda {


    private final static int N = 11;

    public static BigInteger algorithm(GroupElement alpha, GroupElement beta) throws NotSolutionException {
        //TODO: Preguntar a @miret o @fsebe per a tot Zn o per  a tot Zp 
        if (!alpha.belongsToSameGroup(beta)) {
            throw new ParametersException();
        }
        Group group = alpha.getGroup();
        BigInteger b = group.getSize().subtract(BigInteger.ONE);
        GroupElement xn = alpha.pow(b);
        BigInteger d = BigInteger.ZERO;
        for (int i = 0; i < N; i++) {
            d = d.add(randomMap(xn));
            xn = xn.multiply(alpha.pow(randomMap(xn)));
        }
        assertEquals(xn, alpha.pow(b.add(d)));
        GroupElement yn = beta;
        BigInteger acc = BigInteger.ZERO;
        while (!stopComputing(yn, xn, acc, b.add(d))) {
            acc = acc.add(randomMap(yn));
            yn = yn.multiply(alpha.pow(randomMap(yn)));
            assertEquals(beta.multiply(alpha.pow(acc)), yn);
        }
        return b.add(d).subtract(acc).remainder(group.getSize().subtract(BigInteger.ONE));
    }

    private static boolean stopComputing(GroupElement yn, GroupElement xn, BigInteger di, BigInteger bad) throws NotSolutionException {
        if (di.compareTo(bad) > 0)
            throw new NotSolutionException();
        return xn.equals(yn);
    }

    private static BigInteger randomMap(GroupElement elem) {
        //BigInteger module = elem.getGroup().getSize();
        return elem.getIntValue().add(BigInteger.TWO);
        //TODO: @vmateu change so it is random.
    }
}
