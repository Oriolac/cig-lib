package cat.udl.cig.utils.discretelogarithm;


import java.math.BigInteger;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class BruteForceTest extends LogarithmAlgorithmTest {

    @Override
    protected ArrayList<LogarithmAlgorithm> returnAlgorithm() {
        ArrayList<LogarithmAlgorithm> algorithms = new ArrayList<>();
        algorithms.add(new BruteForce(returnGenerator().get(0)));
        algorithms.add(new BruteForce(returnGenerator().get(1), BigInteger.valueOf(1093)));
        return algorithms;
    }

}
