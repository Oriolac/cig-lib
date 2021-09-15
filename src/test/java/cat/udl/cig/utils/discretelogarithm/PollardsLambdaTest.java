package cat.udl.cig.utils.discretelogarithm;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class PollardsLambdaTest extends LogarithmAlgorithmTest {

    @Override
    protected ArrayList<LogarithmAlgorithm> returnAlgorithm() {
        return returnGenerator().stream().map(g -> new PollardsLambda(g, BigInteger.valueOf(50))).collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    int getNumTries() {
        return 40;
    }
}
