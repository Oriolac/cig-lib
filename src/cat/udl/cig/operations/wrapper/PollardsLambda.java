package cat.udl.cig.operations.wrapper;

import cat.udl.cig.exceptions.NotSolutionException;
import cat.udl.cig.exceptions.ParametersException;
import cat.udl.cig.fields.Group;
import cat.udl.cig.fields.GroupElement;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Optional;

public class PollardsLambda {


    private final static int N = 100;
    private final static ArrayList<GroupElement> groupElements = new ArrayList<>();

    public static BigInteger algorithm(GroupElement alpha, GroupElement beta) throws NotSolutionException {
        if (!alpha.belongsToSameGroup(beta)) {
            throw new ParametersException();
        }
        Group group = alpha.getGroup();
        GroupElement prev;
        BigInteger b = group.getSize().subtract(BigInteger.ONE);
        groupElements.add(alpha.pow(b));
        for (int i = 1; i < N; i++) {
            prev = groupElements.get(i - 1);
            groupElements.add(prev.multiply(alpha.pow(randomMap(prev))));
        }
        BigInteger d = BigInteger.ZERO;
        for (GroupElement groupElement : groupElements) {
            d = randomMap(groupElement).add(d);
        }
        GroupElement xn = alpha.pow(b.add(d));

        GroupElement yn = beta;
        BigInteger acc = randomMap(yn);
        while (!stopComputing(yn, xn, acc, b.add(d))) {
            yn = yn.multiply(alpha.pow(randomMap(yn)));
            acc = acc.add(randomMap(yn));
        }
        return b.add(d).subtract(acc);
    }

    private static boolean stopComputing(GroupElement yn, GroupElement xn, BigInteger di, BigInteger bad) throws NotSolutionException {
        if (di.compareTo(bad) > 0)
            throw new NotSolutionException();
        return xn.equals(yn);
    }

    private static BigInteger randomMap(GroupElement elem) {
        BigInteger module = elem.getGroup().getSize();
        return elem.getIntValue().remainder(module);
        //TODO: @vmateu change so it is random.
    }
}
