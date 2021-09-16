package cat.udl.cig.utils.discretelogarithm;

import cat.udl.cig.structures.ecc.GeneralECPoint;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BabyStepGiantStepTest extends LogarithmAlgorithmTest {

    @Override
    protected ArrayList<LogarithmAlgorithm> returnAlgorithm() {
        ArrayList<LogarithmAlgorithm> algorithms = new ArrayList<>();
        algorithms.add(new BabyStepGiantStep(returnGenerator().get(0)));
        algorithms.add(new BabyStepGiantStep((GeneralECPoint) returnGenerator().get(1), BigInteger.valueOf(1093)));
        return algorithms;
    }


}
