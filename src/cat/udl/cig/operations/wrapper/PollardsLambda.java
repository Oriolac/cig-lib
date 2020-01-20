package cat.udl.cig.operations.wrapper;

import cat.udl.cig.exceptions.ConstructionException;
import cat.udl.cig.exceptions.NotSolutionException;
import cat.udl.cig.exceptions.ParametersException;
import cat.udl.cig.fields.Group;
import cat.udl.cig.fields.GroupElement;
import javafx.util.Pair;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Optional;

import static java.lang.Math.abs;
import static org.junit.Assert.assertEquals;

public class PollardsLambda implements PollardsLambdaInt {

    private final GroupElement alpha, beta;
    private final Group group;
    private final BigInteger b;
    private final BigInteger N;
    private HashMap<BigInteger, Pair<BigInteger, GroupElement>> hashMap;


    public PollardsLambda(GroupElement alpha, GroupElement beta) {
        if (!alpha.belongsToSameGroup(beta)) {
            throw new ConstructionException();
        }
        group = alpha.getGroup();
        b = BigInteger.TWO.pow(20);
        N = b.sqrt().add(BigInteger.ONE);
        this.alpha = alpha;
        this.beta = beta;
    }


    @Override
    public Optional<BigInteger> algorithm() {
        //TODO: Preguntar a @miret o @fsebe per a tot Zn o per  a tot Zp. A vegades no funciona ni per Zp
        Optional<BigInteger> res = Optional.empty();
        int i = 0;
        while (i < 10 && res.isEmpty()) {
            getHashMap();
            Pair<GroupElement, BigInteger> pair = getD();
            res = getExponent(pair);
            i++;
        }
        return res;
    }

    private void getHashMap() {
        this.hashMap = new HashMap<>();
        for (BigInteger k = BigInteger.ZERO; k.compareTo(N.sqrt()) < 0; k = k.add(BigInteger.ONE)) {
            BigInteger r = new BigInteger(group.getSize().bitLength(), new SecureRandom()).remainder(N).add(BigInteger.ONE);
            hashMap.put(k, new Pair<>(r, alpha.pow(r)));
        }
    }

    private Optional<BigInteger> getExponent(Pair<GroupElement, BigInteger> pair) {
        Group group = alpha.getGroup();
        BigInteger d = pair.getValue();
        GroupElement xn = pair.getKey();
        GroupElement yn = beta;
        BigInteger acc = BigInteger.ZERO;
        while (acc.compareTo(b.add(d)) <= 0) {
            Pair<BigInteger, GroupElement> tmp = hashMap.get(BigInteger.valueOf(abs(yn.hashCode()) % hashMap.size()));
            acc = acc.add(tmp.getKey());
            yn = yn.multiply(tmp.getValue());
            if (xn.equals(yn)) {
                return Optional.of(b.add(d).subtract(acc).remainder(group.getSize()).add(group.getSize())
                        .remainder(group.getSize()));
            }
        }
        return Optional.empty();
    }

    private Pair<GroupElement, BigInteger> getD() {
        GroupElement xn = alpha.pow(b);
        BigInteger d = BigInteger.ZERO;
        for (long i = 0; i < N.intValue(); i++) {
            Pair<BigInteger, GroupElement> tmp = hashMap.get(BigInteger.valueOf(abs(xn.hashCode()) % hashMap.size()));
            d = d.add(tmp.getKey());
            xn = xn.multiply(tmp.getValue());
        }
        return new Pair<>(xn, d);
    }

}
