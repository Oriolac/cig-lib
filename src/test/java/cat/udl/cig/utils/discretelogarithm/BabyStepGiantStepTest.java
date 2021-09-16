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
        return returnGenerator().stream().map(BabyStepGiantStep::new).collect(Collectors.toCollection(ArrayList::new));
    }

    @Test
    public void testOrder() {
        if (generators.get(1) instanceof GeneralECPoint) {
            GeneralECPoint point = (GeneralECPoint) generators.get(1);
            GeneralECPoint ZERO = point.getCurve().getMultiplicativeIdentity();
            BigInteger order = new BabyStepGiantStep(point).algorithm(ZERO).orElseThrow();
            assertEquals(BigInteger.valueOf(1093), order);
        }
    }


}
