package cat.udl.cig.utils.discretelogarithm;

import cat.udl.cig.structures.ecc.EllipticCurvePoint;

import java.math.BigInteger;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BabyStepGiantStepTest extends LogarithmAlgorithmTest {

    @Override
    protected ArrayList<LogarithmAlgorithm> returnAlgorithm() {
        ArrayList<LogarithmAlgorithm> algorithms = new ArrayList<>();
        algorithms.add(new BabyStepGiantStep(returnGenerator().get(0)));
        algorithms.add(new BabyStepGiantStep((EllipticCurvePoint) returnGenerator().get(1), BigInteger.valueOf(1093)));
        return algorithms;
    }


}
