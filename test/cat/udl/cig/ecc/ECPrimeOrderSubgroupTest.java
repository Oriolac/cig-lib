package cat.udl.cig.ecc;

import cat.udl.cig.AbstractSetUpP192;
import cat.udl.cig.cryptography.cryptosystems.ElGamal;
import cat.udl.cig.cryptography.cryptosystems.ElGamalCypher;
import cat.udl.cig.cryptography.cryptosystems.ciphertexts.ElGamalCiphertext;
import cat.udl.cig.fields.*;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.ArrayList;

class ECPrimeOrderSubgroupTest extends AbstractSetUpP192 {

    @Test
    void testing() {
        ElGamal elgamal = new ElGamal(g);
        ElGamalCypher cipher = elgamal.getCypher();
        GeneralECPoint message = g.getRandomElement();
        ElGamalCiphertext ciphertext = cipher.encrypt(message);
        GeneralECPoint decryptedMessage = (GeneralECPoint) elgamal.decrypt(ciphertext);
        Assert.assertEquals(message, decryptedMessage);
    }

}