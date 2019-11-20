/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cat.udl.cig.cryptography.signers;

import cat.udl.cig.fields.elements.RingElement;

/**
 * Models a digital <i>Signtarue</i> and stores its information data.
 * 
 * @author M.Àngels Cerveró
 */
public class Signature {
    /**
     * An <i>Object</i> representing the first item that forms the tuple of
     * {@code this} <i>Signature</i>. Usually, a <i>Signature</i> is composed
     * by a tuple \( (A, B) \). This <i>Object</i> can be instantiated as a
     * <i>RingElement</i> or as an <i>ECPoint</i> depending on the used
     * <i>Signer</i>.
     * 
     * @see RingElement
     * @see cat.udl.cig.ecc.ECPoint
     */
    private final Object A;
    
    /**
     * An <i>Object</i> representing the second item that forms the tuple of
     * {@code this} <i>Signature</i>. Usually, a <i>Signature</i> is composed
     * by a tuple \( (A, B) \). This <i>Object</i> can be instantiated as a
     * <i>RingElement</i> or as an <i>ECPoint</i> depending on the used
     * <i>Signer</i>.
     * 
     * @see RingElement
     * @see cat.udl.cig.ecc.ECPoint
     */
    private final Object B;
    
    /**
     * Creates a <i>Signature</i> which content is the tuple \( (A, B) \).
     * 
     * @param A the <i>Object</i> that represents the first item of the tuple of
     *          {@code this} <i>Signature</i>. It can be a <i>RingElement</i>
     *          or an <i>ECPoint</i>.
     * @param B the <i>Object</i> that represents the second item of the tuple of
     *          {@code this} <i>Signature</i>. It can be a <i>RingElement</i>
     *          or an <i>ECPoint</i>.
     * 
     * @see RingElement
     * @see cat.udl.cig.ecc.ECPoint
     */
    public Signature(Object A, Object B) {
        this.A = A;
        this.B = B;
    }
    
    /**
     * Returns the first item of the tuple \( (A, B) \) of {@code this}
     * <i>Signature</i>.
     * 
     * @return the <i>Object</i> A, the first element of the tuple of {@code this}
     *          <i>Signature</i>. Depending on the <i>Signer</i> used,
     *          A can be an instance of <i>RingElement</i> or <i>ECPoint</i>.
     * 
     * @see Signer
     * @see RingElement
     * @see cat.udl.cig.ecc.ECPoint
     */
    public Object getA() {
        return A;
    }
    
    /**
     * Returns the second item of the tuple \( (A, B) \) of {@code this}
     * <i>Signature</i>.
     * 
     * @return the <i>Object</i> B, the second element of the tuple of {@code this}
     *          <i>Signature</i>. Depending on the <i>Signer</i> used,
     *          B can be an instance of <i>RingElement</i> or <i>ECPoint</i>.
     * 
     * @see Signer
     * @see RingElement
     * @see cat.udl.cig.ecc.ECPoint
     */
    public Object getB() {
        return B;
    }
}
