package cat.udl.cig.structures.ecc;

import cat.udl.cig.structures.PrimeField;
import cat.udl.cig.structures.PrimeFieldElement;
import cat.udl.cig.structures.Ring;
import cat.udl.cig.structures.RingElement;
import cat.udl.cig.structures.builder.PrimeFieldElementBuilder;
import cat.udl.cig.utils.discretelogarithm.BruteForce;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PrimeFieldECTest extends GeneralECTest {

    GeneralEC curve;
    PrimeField primeField;
    PrimeFieldElementBuilder builder;

    @Override
    protected GeneralEC returnGeneralEC() {
        primeField = new PrimeField(BigInteger.valueOf(2213));
        builder = primeField.buildElement();
        ArrayList<BigInteger> cardFactor = new ArrayList<>(List.of(BigInteger.valueOf(1093)));
        curve = new GeneralEC(
                primeField,
                builder.setValue(1).build().orElseThrow(),
                builder.setValue(49).build().orElseThrow(),
                cardFactor);
        return curve;
    }

    @Override
    protected Ring returnRingOfEC() {
        return curve.getRing();
    }

    @Override
    protected RingElement returnBadCoordinateX() {
        return builder.setValue(2).build().orElseThrow();
    }

    @Override
    protected GeneralECPoint returnGeneralECPoint1() {
        return new GeneralECPoint(curve, builder.setValue(1583).build().orElseThrow(), builder.setValue(1734).build().orElseThrow());
    }

    @Override
    protected GeneralECPoint returnGeneralECPoint2() {
        GeneralECPoint point2 = returnGeneralECPoint1().pow(BigInteger.TWO);
        assertEquals(new GeneralECPoint(curve, builder.setValue(821).build().orElseThrow(), builder.setValue(1341).build().orElseThrow()), point2);
        return point2;
    }

    @Override
    protected GeneralECPoint returnExpectedResultPlusOperation() {
        String xStr = "297";
        BigInteger x = new BigInteger(xStr);
        String yStr = "968";
        BigInteger y = new BigInteger(yStr);
        return new GeneralECPoint(curve, new PrimeFieldElement(primeField, x), new PrimeFieldElement(primeField, y));
    }

    @Override
    protected GeneralECPoint returnsExpectedElementMultByNonZeroScalar() {
        String point1Str = "297";
        BigInteger x = new BigInteger(point1Str);
        String point2Str = "968";
        BigInteger y = new BigInteger(point2Str);
        return new GeneralECPoint(curve, new PrimeFieldElement(primeField, x), new PrimeFieldElement(primeField, y));
    }

    @Override
    protected BigInteger returnExpectedOrderOfPoint1() {
        return BigInteger.valueOf(1093);
    }

    @Test
    void testTwoPointsOrderAddition() {
        GeneralEC generalEC = returnGeneralEC();
        ECPoint plusOp = returnExpectedResultPlusOperation();
        BigInteger expectedOrderPoint1 = returnExpectedOrderOfPoint1();
        Optional<BigInteger> optOrderPoint = generalEC.validOrder(plusOp);
        assertTrue(optOrderPoint.isPresent());
        BigInteger orderPoint = optOrderPoint.get();
        assertEquals(expectedOrderPoint1, orderPoint);
        assertEquals(generalEC.getMultiplicativeIdentity(), plusOp.pow(orderPoint));
        Optional<BigInteger> power = new BruteForce(plusOp).algorithm(generalEC.getMultiplicativeIdentity());
        assertTrue(power.isPresent());
        assertEquals(generalEC.getMultiplicativeIdentity(), plusOp.pow(power.get()));
        assertEquals(generalEC.getMultiplicativeIdentity(), plusOp.pow(BigInteger.valueOf(1093)));
    }

}
