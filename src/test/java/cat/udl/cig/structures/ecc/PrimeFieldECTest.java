package cat.udl.cig.structures.ecc;

import cat.udl.cig.structures.PrimeField;
import cat.udl.cig.structures.PrimeFieldElement;
import cat.udl.cig.structures.Ring;
import cat.udl.cig.structures.RingElement;
import cat.udl.cig.structures.builder.PrimeFieldElementBuilder;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

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
        return new GeneralECPoint(curve, builder.setValue(352).build().orElseThrow(), builder.setValue(1640).build().orElseThrow());
    }

    @Override
    protected GeneralECPoint returnExpectedResultPlusOperation() {
        String point1Str = "994";
        BigInteger x = new BigInteger(point1Str);
        String point2Str = "1928";
        BigInteger y = new BigInteger(point2Str);
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

    @Override
    protected BigInteger returnPower() {
        return BigInteger.valueOf(3);
    }
}
