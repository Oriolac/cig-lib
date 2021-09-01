package cat.udl.cig.fields;

import java.math.BigInteger;
import java.util.Optional;

/**
 * Models any kind of <i>Ring</i>.
 *
 * @see cat.udl.cig.utils.Polynomial
 * @author Víctor Mateu
 * @author M.Àngels Cerveró
 * @author Ricard Garra
 */

// Base code by Víctor Mateu; Refactorization by M.Àngels Cerveró and Ricard
// Garra.
public interface Ring extends Group {

    /**
     * Returns the cardinality of the <i>Ring</i>.
     *
     * @return a BigInteger representing the cardinality of the <i>Ring</i>.
     */
    public BigInteger getSize();

    /**
     * Converts a T \(k\) to a <i>RingElement</i> belonging to {@code this}
     * <i>Ring</i> \(K\).
     *
     * @param k
     *            a T to be converted into a <i>RingElement</i> of {@code this}
     *            <i>Ring</i> \(K\).
     * @return a new <i>RingElement</i> \(r\), such that
     *         {@code r value = k.mod(this.getSize())} and
     *         {@code r.getRing() = this}.
     */
    @Override
    public Optional<? extends RingElement> toElement(Object k);

    /**
     * Returns a random element \(k\) of {@code this} <i>Ring</i>.
     *
     * @return a random element \(k\) that belongs to {@code this} <i>Ring</i>.
     *         The element \(k\) is an instance of <i>RingElement</i>.
     * @see RingElement
     */
    public RingElement getRandomElement();

    /**
     * Returns the element ZERO of {@code this} <i>Ring</i>.
     *
     * @return an element \(k = 0\) that belongs to {@code this} <i>Ring</i>.
     *         The element \(k\) is an instance of <i>RingElement</i>.
     * @see RingElement
     */
    public RingElement getElementZERO();

    /**
     * Returns the neuter element of {@code this} <i>Group</i>.
     *
     * @return an element \(k = 1\) that belongs to {@code this} <i>Group</i>.
     *         The element \(k\) is an instance of <i>GroupElement</i>.
     * @see GroupElement
     */
    public RingElement getNeuterElement();

    /**
     * Returns the element resulting of the multiplication of two elements of
     * the <i>Ring</i>.
     *
     * @param x
     *            an element to be multiplied.
     * @param y
     *            another element to be multiplied.
     * @return a ring element being x multiplied by y
     * @see RingElement
     */
    public RingElement multiply(GroupElement x, GroupElement y);

    /**
     * Returns the element resulting of the multiplication of two elements of
     * the <i>Ring</i>.
     *
     * @param x
     *            an element to be exponentiated.
     * @param pow
     *            the power in which the group element is raised to.
     * @return a ring element being x power y.
     * @see RingElement
     */
    public RingElement pow(GroupElement x, BigInteger pow);

    Optional<? extends RingElement> fromBytes(byte[] bytes);
}
