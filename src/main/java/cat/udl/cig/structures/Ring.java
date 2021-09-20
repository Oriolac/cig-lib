package cat.udl.cig.structures;

import cat.udl.cig.structures.builder.RingElementBuilder;

import java.math.BigInteger;
import java.util.Optional;

/**
 * Models any kind of <i>Ring</i>.
 *
 * @author Víctor Mateu
 * @author M.Àngels Cerveró
 * @author Ricard Garra
 * @see cat.udl.cig.utils.Polynomial
 */

// Base code by Víctor Mateu; Refactorization by M.Àngels Cerveró and Ricard
// Garra.
public interface Ring extends Group {

    /**
     * Returns a random element \(k\) of {@code this} <i>Ring</i>.
     *
     * @return a random element \(k\) that belongs to {@code this} <i>Ring</i>.
     * The element \(k\) is an instance of <i>RingElement</i>.
     * @see RingElement
     */
    @Override
    public RingElement getRandomElement();

    @Override
    public RingElementBuilder buildElement();


    /**
     * Returns the element ZERO of {@code this} <i>Ring</i>.
     *
     * @return an element \(k = 0\) that belongs to {@code this} <i>Ring</i>.
     * The element \(k\) is an instance of <i>RingElement</i>.
     * @see RingElement
     */
    public RingElement getAdditiveIdentity();

    /**
     * Returns the neuter element of {@code this} <i>Group</i>.
     *
     * @return an element \(k = 1\) that belongs to {@code this} <i>Group</i>.
     * The element \(k\) is an instance of <i>GroupElement</i>.
     * @see GroupElement
     */
    @Override
    public RingElement getMultiplicativeIdentity();

    /**
     * Returns the element resulting of the multiplication of two elements of
     * the <i>Ring</i>.
     *
     * @param x an element to be multiplied.
     * @param y another element to be multiplied.
     * @return a ring element being x multiplied by y
     * @see RingElement
     */
    @Override
    public RingElement multiply(GroupElement x, GroupElement y);

    /**
     * Returns the element resulting of the multiplication of two elements of
     * the <i>Ring</i>.
     *
     * @param x   an element to be exponentiated.
     * @param pow the power in which the group element is raised to.
     * @return a ring element being x power y.
     * @see RingElement
     */
    @Override
    public RingElement pow(GroupElement x, BigInteger pow);

    public BigInteger getCharacteristic();

    Optional<? extends RingElement> fromBytes(byte[] bytes);

    @Override
    RingElement ZERO();

    @Override
    RingElement ONE();

    @Override
    RingElement THREE();
}
