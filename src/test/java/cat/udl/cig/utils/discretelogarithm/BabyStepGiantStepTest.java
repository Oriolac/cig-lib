package cat.udl.cig.utils.discretelogarithm;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class BabyStepGiantStepTest extends LogarithmAlgorithmTest {

    @Override
    protected ArrayList<LogarithmAlgorithm> returnAlgorithm() {
        return returnGenerator().stream().map(BabyStepGiantStep::new).collect(Collectors.toCollection(ArrayList::new));
    }


}
