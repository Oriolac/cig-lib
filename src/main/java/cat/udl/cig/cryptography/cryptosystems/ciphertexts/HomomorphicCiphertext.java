package cat.udl.cig.cryptography.cryptosystems.ciphertexts;

public interface HomomorphicCiphertext extends Ciphertext {

    HomomorphicCiphertext HomomorphicOperation(HomomorphicCiphertext op2);

}
