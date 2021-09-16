package cat.udl.cig.structures.ecc;

import cat.udl.cig.structures.*;
import cat.udl.cig.structures.builder.PrimeFieldElementBuilder;
import cat.udl.cig.utils.Polynomial;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.Reader;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExtensionFieldECTest extends EllipticCurveTest {
    EllipticCurve curve;
    ExtensionField extensionField;
    BigInteger p = BigInteger.valueOf(2213);
    PrimeField primeField;
    static private final boolean TEST_MULT_RUN_FLAG = false;

    @Override
    protected EllipticCurve returnGeneralEC() {
        extensionField = ExtensionField.ExtensionFieldP2(p);
        Polynomial.PolynomialBuilder pBuilder = new Polynomial.PolynomialBuilder();
        primeField = new PrimeField(p);
        ExtensionFieldElement A = extensionField.buildElement().setPolynomial(
                pBuilder.addTerm(0, new PrimeFieldElement(primeField, BigInteger.valueOf(3)))
                        .build())
                .build().orElseThrow();
        ExtensionFieldElement B = extensionField.buildElement().setPolynomial(
                pBuilder.addTerm(0, new PrimeFieldElement(primeField, BigInteger.valueOf(49)))
                        .build())
                .build().orElseThrow();
        curve = new EllipticCurve(extensionField, A, B, BigInteger.valueOf(4901312));
        return curve;
    }

    @Override
    protected Ring returnRingOfEC() {
        return curve.getRing();
    }

    @Override
    protected RingElement returnBadCoordinateX() {
        Polynomial.PolynomialBuilder pBuilder = new Polynomial.PolynomialBuilder();
        return extensionField.buildElement().setPolynomial(
                pBuilder.addTerm(1, primeField.getMultiplicativeIdentity())
                        .addTerm(0, primeField.buildElement().setValue(5).build().orElseThrow())
                        .build())
                .build().orElseThrow();
    }

    @Override
    protected GeneralECPoint returnGeneralECPoint1() {
        Polynomial.PolynomialBuilder pBuilder = new Polynomial.PolynomialBuilder();
        ExtensionFieldElement x = extensionField.buildElement().setPolynomial(
                pBuilder.addTerm(1, primeField.getMultiplicativeIdentity())
                        .build())
                .build().orElseThrow();
        ExtensionFieldElement y = extensionField.buildElement().setPolynomial(
                pBuilder.addTerm(1, primeField.buildElement().setValue(606).build().orElseThrow())
                        .addTerm(0, primeField.buildElement().setValue(451).build().orElseThrow())
                        .build())
                .build().orElseThrow();
        return new GeneralECPoint(curve, x, y);
    }

    @Override
    protected GeneralECPoint returnGeneralECPoint2() {
        GeneralECPoint point2 = returnGeneralECPoint1().pow(BigInteger.TWO);
        Polynomial.PolynomialBuilder pBuilder = new Polynomial.PolynomialBuilder();
        ExtensionFieldElement x = extensionField.buildElement().setPolynomial(
                pBuilder.addTerm(1, primeField.buildElement().setValue(1550).build().orElseThrow())
                        .addTerm(0, primeField.buildElement().setValue(1407).build().orElseThrow())
                        .build())
                .build().orElseThrow();
        ExtensionFieldElement y = extensionField.buildElement().setPolynomial(
                pBuilder.addTerm(1, primeField.buildElement().setValue(1415).build().orElseThrow())
                        .addTerm(0, primeField.buildElement().setValue(2210).build().orElseThrow())
                        .build())
                .build().orElseThrow();
        GeneralECPoint expected = new GeneralECPoint(curve, x, y);
        assertEquals(expected, point2);
        return point2;
    }

    @Override
    protected GeneralECPoint returnExpectedResultPlusOperation() {
        Polynomial.PolynomialBuilder pBuilder = new Polynomial.PolynomialBuilder();
        ExtensionFieldElement x = extensionField.buildElement().setPolynomial(
                pBuilder.addTerm(1, primeField.buildElement().setValue(1667).build().orElseThrow())
                        .addTerm(0, primeField.buildElement().setValue(1802).build().orElseThrow())
                        .build())
                .build().orElseThrow();
        ExtensionFieldElement y = extensionField.buildElement().setPolynomial(
                pBuilder.addTerm(1, primeField.buildElement().setValue(305).build().orElseThrow())
                        .addTerm(0, primeField.buildElement().setValue(1166).build().orElseThrow())
                        .build())
                .build().orElseThrow();
        return new GeneralECPoint(curve, x, y);
    }

    @Override
    protected GeneralECPoint returnsExpectedElementMultByTHREE() {
        Polynomial.PolynomialBuilder pBuilder = new Polynomial.PolynomialBuilder();
        ExtensionFieldElement x = extensionField.buildElement().setPolynomial(
                pBuilder.addTerm(1, primeField.buildElement().setValue(1667).build().orElseThrow())
                        .addTerm(0, primeField.buildElement().setValue(1802).build().orElseThrow())
                        .build())
                .build().orElseThrow();
        ExtensionFieldElement y = extensionField.buildElement().setPolynomial(
                pBuilder.addTerm(1, primeField.buildElement().setValue(305).build().orElseThrow())
                        .addTerm(0, primeField.buildElement().setValue(1166).build().orElseThrow())
                        .build())
                .build().orElseThrow();
        return new GeneralECPoint(curve, x, y);
    }

    @Override
    protected BigInteger returnExpectedOrderOfPoint1() {
        return BigInteger.valueOf(306332);
    }

    @Override
    protected ArrayList<GeneralECPoint> returnLessPointsOfPoint1() {
        ArrayList<GeneralECPoint> res = new ArrayList<>();
        res.add(returnGeneralPoint(0, 1, 0, 617));
        res.add(returnGeneralPoint(0, 1432, 1573, 0));
        res.add(returnGeneralPoint(0, 1432, 1573, 0));
        return res;
    }

    public GeneralECPoint returnGeneralPoint(int x1, int x0, int y1, int y0) {
        Polynomial.PolynomialBuilder pBuilder = new Polynomial.PolynomialBuilder();
        ExtensionFieldElement x = extensionField.buildElement().setPolynomial(
                pBuilder.addTerm(1, primeField.buildElement().setValue(x1).build().orElseThrow())
                        .addTerm(0, primeField.buildElement().setValue(x0).build().orElseThrow())
                        .build())
                .build().orElseThrow();
        ExtensionFieldElement y = extensionField.buildElement().setPolynomial(
                pBuilder.addTerm(1, primeField.buildElement().setValue(y1).build().orElseThrow())
                        .addTerm(0, primeField.buildElement().setValue(y0).build().orElseThrow())
                        .build())
                .build().orElseThrow();
        return new GeneralECPoint(curve, x, y);
    }


    @Test
    void testPolynomial() {
        Polynomial polynomial = new Polynomial.PolynomialBuilder()
                .addTerm(2, primeField.buildElement().setValue(1).build().orElseThrow())
                .addTerm(0, primeField.buildElement().setValue(2).build().orElseThrow())
                .build();
        assertEquals(polynomial, this.extensionField.getReducingPolynomial());
    }

    @Test
    void testMultiplication() {
        if (!TEST_MULT_RUN_FLAG) return;
        GeneralECPoint point1 = returnGeneralECPoint1();
        try (Reader reader = Files.newBufferedReader(Paths.get("filetest/orderp1ext.csv"));
             CSVReader csvReader = new CSVReader(reader)) {
            int i = 0;
            String[] record;
            while ((record = csvReader.readNext()) != null) {
                BigInteger power = new BigInteger(record[0]);
                String[] pointStr = record[1].substring(1, record[1].length() - 1).split(": ");
                String x = pointStr[0].replaceAll("\\u0020", "");
                String y = pointStr[1].replaceAll("\\u0020", "");
                BigInteger z = new BigInteger(pointStr[2].replaceAll("\\u0020", ""));
                GeneralECPoint point = null;
                if (z.equals(BigInteger.ZERO)) {
                    point = curve.getMultiplicativeIdentity();
                } else {
                    ExtensionFieldElement xCoord = strToExtensionFieldElement(x);
                    ExtensionFieldElement yCoord = strToExtensionFieldElement(y);
                    point = curve.buildElement().setXYCoordinates(xCoord, yCoord).build().orElseThrow();
                }
                try {

                    assertEquals(point, point1.pow(power), point1 + " * " + power);
                } catch (Exception ex) {
                    System.out.println("Failed at " + point + " * " + power);
                }
                i++;
            }
        } catch (IOException | CsvValidationException ex) {
            ex.printStackTrace();
        }
    }

    private ExtensionFieldElement strToExtensionFieldElement(String s) {
        Pattern firstDegreePattern = Pattern.compile("([0-9]+\\*)?a");
        Pattern coefPattern = Pattern.compile("[0-9]*\\*");
        Pattern zeroDegreePattern = Pattern.compile("[0-9]+$");
        Matcher firstDegreeMatcher = firstDegreePattern.matcher(s);
        PrimeFieldElementBuilder coefFirstDegree = this.primeField.buildElement().setValue(0);
        if (firstDegreeMatcher.find()) {
            Matcher coefMatcher = coefPattern.matcher(firstDegreeMatcher.group());
            if (coefMatcher.find()) {
                coefFirstDegree = coefFirstDegree.setValue(new BigInteger(coefMatcher.group().split("\\*")[0]));
            } else {
                coefFirstDegree = coefFirstDegree.setValue(1);
            }
        }
        PrimeFieldElement firstDegree = coefFirstDegree.build().orElseThrow();
        Matcher zeroDegreeMatcher = zeroDegreePattern.matcher(s);
        PrimeFieldElementBuilder coefZeroDegree = this.primeField.buildElement().setValue(0);
        if (zeroDegreeMatcher.find()) {
            coefZeroDegree = coefZeroDegree.setValue(new BigInteger(zeroDegreeMatcher.group()));
        }
        PrimeFieldElement zeroDegree = coefZeroDegree.build().orElseThrow();
        Polynomial polynomial = new Polynomial.PolynomialBuilder().addTerm(0, zeroDegree).addTerm(1, firstDegree).build();
        return extensionField.buildElement().setPolynomial(polynomial).build().orElseThrow();
    }

    @Test
    void testSizePow() {
        GeneralECPoint point1 = returnGeneralECPoint1();
        BigInteger order = point1.getOrder();
        assertEquals(new BigInteger("306332"), order);
        GeneralECPoint subPoint = point1.pow(order.subtract(BigInteger.ONE));
        assertEquals(curve.getMultiplicativeIdentity(), subPoint.multiply(point1));
        assertEquals(curve.getMultiplicativeIdentity(), point1.pow(order));
    }

    @Test
    void testSizePowInternal() {
        GeneralECPoint point1 = returnGeneralECPoint1();
        ExtensionFieldElement x = extensionField.buildElement()
                .setPolynomial(new Polynomial.PolynomialBuilder()
                        .addTerm(1, primeField.getAdditiveIdentity())
                        .addTerm(0, primeField.buildElement().setValue(2016).build().orElseThrow())
                        .build())
                .build().orElseThrow();
        ExtensionFieldElement y = extensionField.buildElement()
                .setPolynomial(new Polynomial.PolynomialBuilder()
                        .addTerm(0, primeField.getAdditiveIdentity())
                        .build())
                .build().orElseThrow();
        GeneralECPoint rootInf = new GeneralECPoint(curve, x, y);
        rootInf.multiply(rootInf);
    }

}
