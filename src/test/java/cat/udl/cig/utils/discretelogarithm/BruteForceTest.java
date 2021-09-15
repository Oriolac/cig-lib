package cat.udl.cig.utils.discretelogarithm;


import java.util.ArrayList;
import java.util.stream.Collectors;

public class BruteForceTest extends LogarithmAlgorithmTest {

    @Override
    protected ArrayList<LogarithmAlgorithm> returnAlgorithm() {
        return returnGenerator().stream().map(BruteForce::new).collect(Collectors.toCollection(ArrayList::new));
    }

}
