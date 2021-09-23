package cat.udl.cig.utils.discretelogarithm;

import cat.udl.cig.structures.ecc.EllipticCurvePoint;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BabyStepGiantStepTerrTest extends LogarithmAlgorithmTest {

    @Override
    protected ArrayList<LogarithmAlgorithm> returnAlgorithm() {
        return returnGenerator().stream().map(BSGSTerrOrder::new).collect(Collectors.toCollection(ArrayList::new));
    }

    @Test
    public void testOrder() {
        if (generators.get(1) instanceof EllipticCurvePoint) {
            EllipticCurvePoint point = (EllipticCurvePoint) generators.get(1);
            EllipticCurvePoint ZERO = point.getCurve().getMultiplicativeIdentity();
            BigInteger size = point.getCurve().getRing().getSize();
            Optional<BigInteger> order = new BSGSTerrOrder(point, size, size.add(BigInteger.TEN)).algorithm(ZERO);
            assertTrue(order.isPresent(), "Order found.");
            assertEquals(BigInteger.valueOf(1093), order.get(), point + " * " + order.get() + " = " + ZERO);
        }
    }

    @Override
    void testTrySomeManyPowers() {
    }

    @Override
    void testAlgorithm() {
    }
}
