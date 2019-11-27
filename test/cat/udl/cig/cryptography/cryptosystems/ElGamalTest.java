package cat.udl.cig.cryptography.cryptosystems;

import cat.udl.cig.cryptography.cryptosystems.ciphertexts.ElGamalCiphertext;
import cat.udl.cig.fields.GroupElement;
import cat.udl.cig.fields.IntegerPrimeOrderSubgroup;
import cat.udl.cig.fields.PrimeFieldElement;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

class ElGamalTest {

    @Test
    void test() {
        BigInteger MODULE = BigInteger.valueOf(11);
        BigInteger EXPONENT = BigInteger.valueOf(21);
        BigInteger GENERATOR = BigInteger.valueOf(7);

        IntegerPrimeOrderSubgroup group = new IntegerPrimeOrderSubgroup(MODULE, EXPONENT, GENERATOR);
        ElGamal gam = new ElGamal(group);
        ElGamalCypher cypher = gam.getCypher();

        PrimeFieldElement element = group.getRandomElement();
        //PrimeFieldElement element = group.getGenerator().pow(BigInteger.valueOf(2)); //Exemple de cifrar el 3.

        System.out.println("Donada el grup Z" + MODULE + ", amb el generador " + GENERATOR + ", cifrem " + element);
        ElGamalCiphertext cipher = cypher.encrypt(element);
        GroupElement[] mCifrat = cipher.getParts();
        System.out.println("Missatge cifrat : (" + mCifrat[0].toString() + ", " + mCifrat[1].toString() + ")");
        GroupElement m = gam.decrypt(cipher);
        System.out.println("Missatge : " + m);

    }

}