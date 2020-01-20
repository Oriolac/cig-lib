package cat.udl.cig.cryptography.cryptosystems;

import cat.udl.cig.cryptography.cryptosystems.ciphertexts.ElGamalCiphertext;
import cat.udl.cig.fields.GroupElement;
import cat.udl.cig.fields.IntegerPrimeOrderSubgroup;
import cat.udl.cig.fields.PrimeFieldElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

class ElGamalTest {

    ElGamal gam;
    ElGamalCypher cypher;
    IntegerPrimeOrderSubgroup group;

    @BeforeEach
    void setUp() {
        BigInteger MODULE = BigInteger.valueOf(11);
        BigInteger EXPONENT = BigInteger.valueOf(10);
        BigInteger GENERATOR = BigInteger.valueOf(7);
        group = new IntegerPrimeOrderSubgroup(MODULE, EXPONENT, GENERATOR);
        gam = new ElGamal(group);
        cypher = gam.getCypher();
    }

    @Test
    void test() {
        PrimeFieldElement element = group.getRandomElement();
        ElGamalCiphertext cipher = cypher.encrypt(element);
        GroupElement[] mCifrat = cipher.getParts();
        GroupElement m = gam.decrypt(cipher);
        assertEquals(element, m);
    }

}