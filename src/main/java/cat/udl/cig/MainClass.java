package cat.udl.cig;

import cat.udl.cig.exceptions.ConstructionException;
import cat.udl.cig.structures.ExtensionField;
import cat.udl.cig.structures.ExtensionFieldElement;
import cat.udl.cig.structures.PrimeField;
import cat.udl.cig.structures.ecc.EllipticCurve;
import cat.udl.cig.utils.Polynomial;

import java.math.BigInteger;
import java.util.List;

public class MainClass {

    public static void main(String[] args) {
        BigInteger p = BigInteger.valueOf(3);
        PrimeField primeField = new PrimeField(p);
        ExtensionField field = ExtensionField.ExtensionFieldP2(p);
        ExtensionFieldElement A = field.buildElement().setPolynomial(new Polynomial.PolynomialBuilder()
                .addTerm(1, primeField.buildElement().setValue(2).build().orElseThrow())
                .addTerm(0, primeField.getMultiplicativeIdentity())
                .build()).build().orElseThrow();
        ExtensionFieldElement B = field.buildElement().setPolynomial(new Polynomial.PolynomialBuilder()
                .addTerm(1, primeField.buildElement().setValue(2).build().orElseThrow())
                .addTerm(0, primeField.buildElement().setValue(2).build().orElseThrow())
                .build()).build().orElseThrow();
        EllipticCurve ec = new EllipticCurve(field, A, B);
        System.out.println(ec);
        BigInteger size = ec.getSize();
        System.out.println("Size: " + size);
        System.out.println(ec.getSubgroups());
        searchEllipticCurve(List.of(3, 5, 7, 11, 13, 17, 19));
    }

    public static void searchEllipticCurve(List<Integer> primes) {
        for (Integer prime : primes) {
            ExtensionField field = ExtensionField.ExtensionFieldP2(BigInteger.valueOf(prime));
            System.out.println("Field: " + field.getCharacteristic() + "^2");
            for (int i = 0; i < field.getSize().intValue(); i++) {
                ExtensionFieldElement elA = field.getRandomElement();
                for (int j = 0; j < field.getSize().intValue(); j++) {
                    ExtensionFieldElement elB = field.getRandomElement();
                    try {
                        EllipticCurve ec = new EllipticCurve(field, elA, elB);
                        System.out.println(ec);
                        System.out.println("W: " + ec.getDiscriminant());
                        System.out.println("Size: " + ec.getSize());
                        if (ec.getSize().equals(field.getCharacteristic().add(BigInteger.ONE).pow(2))) {
                            System.out.println("SIZE == (p+1)^2");
                        }
                        System.out.println("Finish");
                    } catch (ConstructionException ignored) {
                    }
                }
            }
        }
    }
}
