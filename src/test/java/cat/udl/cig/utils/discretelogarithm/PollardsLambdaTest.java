package cat.udl.cig.utils.discretelogarithm;

import java.math.BigInteger;
import java.util.ArrayList;

public class PollardsLambdaTest extends LogarithmAlgorithmTest {

    @Override
    protected ArrayList<LogarithmAlgorithm> returnAlgorithm() {
        ArrayList<LogarithmAlgorithm> logarithms = new ArrayList<>();
        logarithms.add(new PollardsLambda(returnGenerator().get(0), BigInteger.valueOf(50)));
        logarithms.add(new PollardsLambda(returnGenerator().get(1), BigInteger.valueOf(70)));
        return logarithms;
    }

    @Override
    int getNumTries() {
        return 40;
    }
}
