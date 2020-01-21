package cat.udl.cig.ecc;

import cat.udl.cig.AbstractSetUpP192;
import cat.udl.cig.cryptography.cryptosystems.ElGamal;
import cat.udl.cig.cryptography.cryptosystems.ElGamalCypher;
import cat.udl.cig.cryptography.cryptosystems.ciphertexts.ElGamalCiphertext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ECPrimeOrderSubgroupTest extends AbstractSetUpP192 {

    @Test
    void testing() {
        ElGamal elgamal = new ElGamal(g);
        ElGamalCypher cipher = elgamal.getCypher();
        GeneralECPoint message = g.getRandomElement();
        ElGamalCiphertext ciphertext = cipher.encrypt(message);
        GeneralECPoint decryptedMessage = (GeneralECPoint) elgamal.decrypt(ciphertext);
        assertEquals(message, decryptedMessage);
    }

}