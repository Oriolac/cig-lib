package cat.udl.cig.ecc;

import cat.udl.cig.cryptography.cryptosystems.ElGamal;
import cat.udl.cig.cryptography.cryptosystems.ElGamalCypher;
import cat.udl.cig.cryptography.cryptosystems.ciphertexts.ElGamalCiphertext;
import cat.udl.cig.fields.*;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.ArrayList;

class ECPrimeOrderSubgroupTest {

    //private ArrayList<BigInteger> getCardElements(Ring ring) {

    //}
    static BigInteger MODULE = new BigInteger("6277101735386680763835789423207666416083908700390324961279");
    static BigInteger n = new BigInteger("6277101735386680763835789423176059013767194773182842284081");
    static RingElement[] COEF = new RingElement[2];
    static BigInteger b = new BigInteger("64210519e59c80e70fa7e9ab72243049feb8deecc146b9b1", 16);
    static ArrayList<BigInteger> card = new ArrayList<>();
    static GeneralECPoint gen;
    static BigInteger gx = new BigInteger("188da80eb03090f67cbf20eb43a18800f4ff0afd82ff1012", 16);
    static BigInteger gy = new BigInteger("07192b95 ffc8da78 631011ed 6b24cdd5 73f977a1 1e794811"
            .replaceAll("\\s", ""), 16);

    @Test
    void getSize() {
        PrimeField ring = new PrimeField(MODULE);
        COEF[0] = new PrimeFieldElement(ring, BigInteger.valueOf(-3));
        COEF[1] = new PrimeFieldElement(ring, b);
        card.add(n);
        GeneralEC curve = new GeneralEC(ring, COEF, card);
        gen = new GeneralECPoint(curve, new PrimeFieldElement(ring, gx), new PrimeFieldElement(ring, gy));
        ECPrimeOrderSubgroup grup = new ECPrimeOrderSubgroup(curve, n, gen);
        System.out.println(curve.toString());

        ElGamal elgamal = new ElGamal(grup);
        ElGamalCypher cipher = elgamal.getCypher();
        System.out.println("Clau pública: " + elgamal.getPublicKey().toString());
        GeneralECPoint message = grup.getRandomElement();
        System.out.println("Message: " + message.toString());
        ElGamalCiphertext ciphertext = cipher.encrypt(message);
        GeneralECPoint decryptedMessage = (GeneralECPoint) elgamal.decrypt(ciphertext);
        System.out.println("Decrypted message: " + decryptedMessage.toString());
        Assert.assertEquals(message, decryptedMessage);
    }


}