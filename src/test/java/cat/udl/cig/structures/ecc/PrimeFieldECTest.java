package cat.udl.cig.structures.ecc;

import cat.udl.cig.structures.PrimeField;
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
    protected BigInteger returnExpectedOrderOfPoint1() {
        return BigInteger.valueOf(1093);
    }
}
