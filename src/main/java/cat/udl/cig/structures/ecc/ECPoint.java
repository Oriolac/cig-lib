package cat.udl.cig.structures.ecc;

import java.math.BigInteger;

import cat.udl.cig.structures.GroupElement;
import cat.udl.cig.structures.RingElement;
import cat.udl.cig.structures.Group;

/**
 * Models a <i>Point</i> \(P\) belonging to an <i>Elliptic Curve</i> \(E\).
 *
 * @see Group
 * @see GroupElement
 * @author Víctor Mateu
 */
public interface ECPoint extends GroupElement {


    /**
     * Returns the coordinate \(x\) of {@code this} <i>ECPoint</i> \(P\).
     *
     * @return a copy of the RingElement representing the \(x\) of {@code this}
     *         <i>ECPoint</i> \(P\).
     */
    public RingElement getX();

    /**
     * Returns the coordinate \(y\) of {@code this} <i>ECPoint</i> \(P\).
     *
     * @return a copy of the RingElement representing the \(x\) of {@code this}
     *         <i>ECPoint</i> \(P\).
     */
    public RingElement getY();

    /**
     * Returns the order of {@code this} <i>ECPoint</i> \(P\) if given when
     * created, otherwise returns null. That is \(\text{ord}(P)\).
     *
     * @return a BigInteger representing the order of {@code this}
     *         <i>ECPoint</i> \(P\).
     */
    public BigInteger getOrder();

    /**
     * Returns the <i>EllipticCurve</i> to which {@code this} <i>ECPoint</i>
     * \(P\) belongs.
     *
     * @return an EC, the <i>EllipticCurve</i> to which {@code this}
     *         <i>ECPoint</i> \(P\) belongs. That is, \(P \in E(K)\).
     */
    public EC getCurve();

    /**
     * Returns the result of \(2 \cdot P\), where \(P\) is {@code this}
     * <i>Point</i>.
     *
     * @return <ul>
     *         <li>a <i>Point</i> \(R\), where \(R = 2 \cdot P\) or</li>
     *         <li>{@code null} if {@code this} <i>Point</i> is not initialized
     *         or</li>
     *         <li>the infinity <i>Point</i> if any exception occurred.</li>
     *         </ul>
     */
    public ECPoint square();

    /**
     * Returns the result of \(P + Q\), where \(P\) is {@code this}
     * <i>Point</i>.
     *
     * @param Q
     *            the <i>Point</i> we want to add to \(P\).
     * @return <ul>
     *         <li>a <i>Point</i> \(R\), where \(R = P + Q\) or</li>
     *         <li>{@code null} if {@code this} or {@code Q} <i>Points</i> are
     *         not initialized or</li>
     *         <li>the infinity <i>Point</i> if any exception occurred.</li>
     *         </ul>
     * @throws ArithmeticException
     *             if \(P\) and \(Q\) do not belong to the same <i>Elliptic
     *             Curve</i> \(E\). That is, {@code this.E != Q.E}.
     */
    @Override
    public ECPoint multiply(GroupElement Q)
            throws ArithmeticException;

    /**
     * Returns the result of \(P - Q\), where \(P\) is {@code this}
     * <i>Point</i>.
     *
     * @param Q
     *            the <i>Point</i> we want to subtract to \(P\).
     * @return <ul>
     *         <li>a <i>Point</i> \(R\), where \(R = P - Q\) or</li>
     *         <li>{@code null} if {@code this} or {@code Q} <i>Points</i> are
     *         not initialized or</li>
     *         <li>the infinity <i>Point</i> if any exception occurred.</li>
     *         </ul>
     * @throws ArithmeticException
     *             if \(P\) and \(Q\) do not belong to the same <i>Elliptic
     *             Curve</i> \(E\). That is, {@code this.E != Q.E}.
     */
    public ECPoint divide(final GroupElement Q) throws ArithmeticException;

    /**
     * Returns the result of \(k \cdot P\), where \(P\) is {@code this}
     * <i>Point</i>.
     *
     * @param k
     *            a BigInteger number to multiply the <i>Point</i> \(P\).
     * @return a <i>Point</i> \(R\), where \(R = k \cdot P\) or {@code null} if
     *         {@code this} is not initialized.
     */

    public ECPoint pow(BigInteger k);


    /**
     * Returns the negative of \(P\), that is \(-P\), where \(P\) is
     * {@code this} <i>Point</i>.
     *
     * @return a <i>Point</i> \(R\), where \(R = -P\), {@code null} if
     *         {@code this} is not initialized or the <i>Point</i> at infinity
     *         if any exception occurred.
     */
    public ECPoint inverse();

    /**
     * Checks if \(P\) is the <i>Point</i> at Infinity, where \(P\) is
     * {@code this} <i>Point</i>
     *
     * @return true if \(P = (0:1:0)\) and false otherwise.
     */
    public boolean isInfinity();

    public ECSubgroup getSubgroup();

}
