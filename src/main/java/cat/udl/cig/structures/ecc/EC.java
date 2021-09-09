package cat.udl.cig.structures.ecc;

import java.math.BigInteger;
import java.util.ArrayList;

import cat.udl.cig.structures.Group;
import cat.udl.cig.structures.Ring;
import cat.udl.cig.structures.RingElement;

/**
 * Models an <i>Elliptic Curve</i> \(E\) of the form \(y^{2} = x^{3} + ax + b\).
 *
 * @see Group
 * @author Víctor Mateu
 */

public interface EC extends Group {

    /**
     * Returns the ring \(k\) for the <i>Elliptic Curve</i> \(E\).
     *
     * @return a Ring with the value {@code this.k}. Notice that {@code this.k}
     *         can be {@code null} if it has not been initialized in the
     *         constructor.
     */
    Ring getRing();

    /**
     * Returns the coefficient \(a\) of the <i>Elliptic Curve</i> \(E\).
     *
     * @return a RignElement with the value {@code this.a}. Notice that
     *         {@code this.a} can be {@code null} if it has not been initialized
     *         in the constructor.
     */
    RingElement getA();

    /**
     * Returns the coefficient \(b\) of the <i>Elliptic Curve</i> \(E\).
     *
     * @return a RignElement with the value {@code this.b}. Notice that
     *         {@code this.b} can be {@code null} if it has not been initialized
     *         in the constructor.
     */
    RingElement getB();


    /**
     * Returns the factorization of the cardinality of \(E(k)\), if initialized.
     *
     * @return an ArrayList with the value {@code this.cardFactors} or
     *         {@code null} if {@code this} is not initialized.
     */
    ArrayList<BigInteger> getCardinalityFactors();


    /**
     * Returns a random point \(P \in E(K)\).
     *
     * @return and ECPoint representing a random point \(P \in E(K)\).
     * @see ECPoint
     */
    @Override
    ECPoint getRandomElement();


    /**
     * Returns the infinity point \(P\) of this group law \(E(K)\).
     *
     * @return an ECPoint \(P = \mathcal{O}\), the infinity point of this group
     *         law \(E(K)\).
     * @see ECPoint
     */
    ECPoint getMultiplicativeIdentity();

    /**
     * Not yet implemented!
     *
     * @param l
     *            not yet implemented!
     * @return not yet implemented!
     */
    //EC getIsogenous(int l);


    /**
     * Checks if point \(P\) belongs to {@code this} <i>Elliptic Curve</i>
     * \(E\).
     *
     * @param P
     *            the <i>ECPoint</i> to be checked.
     * @return {@code true} if \(P \in E(K)\); {@code false} otherwise.
     * @see ECPoint
     */
    boolean isOnCurve(ECPoint P);

    /**
     * Computes the order \(\text{ord}(P)\) of {@code this} <i>ECPoint</i>
     * \(P\).
     *
     * @param P
     *            the <i>ECPoint</i> to be checked.
     * @return BigInteger containing the order of <i>ECPoint</i> \(P\).
     */

    BigInteger computeOrder(final ECPoint P);

    /**
     * Checks if point \(P = (x, y)\) belongs to {@code this} <i>Elliptic
     * Curve</i> \(E\).
     *
     * @param x
     *            a RingElement representing the first coordinate of the point
     *            \(P\).
     * @param y
     *            a RingElement representing the second coordinate of the point
     *            \(P\).
     * @return {@code true} if \(P = (x, y) \in E(K)\); {@code false} otherwise.
     */
    boolean isOnCurve(RingElement x, RingElement y);

    /**
     * Checks if there exists any point \(P \in E(K)\) such that its first
     * coordinate is equal to \(x\).
     *
     * @param x
     *            the RingElement we want to check if it belongs to a point \(P
     *            \in E(K)\).
     * @return a new point \(P \in E(K)\) such that its first coordinate
     *         corresponds to the value \(x\), \(P = (x, y)\), or null if there
     *         is no point \(P \in E(K)\) with its first coordinate equals to
     *         \(x\).
     * @see ECPoint
     */
    ArrayList<? extends GeneralECPoint> liftX(RingElement x);

}
