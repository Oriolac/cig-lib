/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cat.udl.cig.cryptography.cryptosystems.ciphertexts;

import cat.udl.cig.fields.GroupElement;

/**
 * Models an encrypted text. This encrypted text is done with an specific
 * <i>Cryptosystem</i>.
 *
 * @author M.Àngels Cerveró
 */
public interface Ciphertext {

    GroupElement[] getParts();
}
