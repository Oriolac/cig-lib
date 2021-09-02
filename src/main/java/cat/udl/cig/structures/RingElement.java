package cat.udl.cig.structures;

import java.math.BigInteger;
import java.util.ArrayList;

import cat.udl.cig.exceptions.IncorrectRingElementException;

/**
 * Models any kind of <i>Ring Element</i>.
 *
 * @see cat.udl.cig.utils.Polynomial
 * @author Víctor Mateu
 * @author M.Àngels Cerveró
 * @author Ricard Garra
 */

// Base code by Víctor Mateu; Refactorization by M.Àngels Cerveróa and Ricard
// Garra.
public interface RingElement extends GroupElement {

    /**
     * Returns the <i>Ring</i> \(K\) of {@code this} <i>RingElement</i>.
     *
     * @return \(K\), the <i>Ring</i> of {@code this} <i>RingElement</i>.
     */
    @Override
    public Ring getGroup();

    /**
     * Returns the value of the {@code this} <i>RingElement</i>. This value
     * belongs to the cyclic group of the <i>Ring</i> \(K\) that defines
     * {@code this} <i>RingElement</i>.
     *
     * @return a BigInteger representing the value of {@code this}
     *         <i>RingElement</i>.
     */
    public Object getValue();

    /**
     * Returns the integer representation of the {@code this}. If it's a
     * PrimeField, the result is the same as getValue. If it's a ExtensionField
     * or a BinaryField, is the result of multiplying each coefficient of the
     * polynomial by the base to the corresponding power.
     *
     * @return a BigInteger representation of the value of {@code this}
     */
    public BigInteger getIntValue();

    /**
     * Computes the operation \(p + q\), where {@code p} is the {@code this}
     * <i>RingElement</i>. The result belongs to the <i>Ring</i>
     * {@code this.getRing()}.
     *
     * @param q
     *            the <i>RingElement</i> we want to add to {@code this}.
     * @return a new <i>RingElement</i> \(r\), where \(r = p + q\) and
     *         {@code r.getRing() = this.getRing()}.
     * @throws IncorrectRingElementException
     *             if {@code this} or {@code q} are not initialized. Also throws
     *             IncorrectRingElementException {@code q} does not belong to
     *             the same <i>Ring</i> than {@code this} ({@code this} and
     *             {@code q} are not compatible <i>RingElements</i>).
     */
    public RingElement add(RingElement q)
            throws IncorrectRingElementException;

    /**
     * Computes the operation \(p - q\), where {@code p} is the {@code this}
     * <i>RingElement</i>. The result belongs to the <i>Ring</i>
     * {@code this.getRing()}.
     *
     * @param q
     *            the <i>RingElement</i> we want to subtract to {@code this}.
     * @return a new <i>RingElement</i> \(r\), where \(r = p - q\) and
     *         {@code r.getRing() = this.getRing()}.
     * @throws IncorrectRingElementException
     *             if {@code this} or {@code q} are not initialized. Also throws
     *             IncorrectRingElementException {@code q} does not belong to
     *             the same <i>Ring</i> than {@code this} ({@code this} and
     *             {@code q} are not compatible <i>RingElements</i>).
     */
    public RingElement subtract(RingElement q)
            throws IncorrectRingElementException;

    /**
     * Returns \(-p\), where {@code p} is the {@code this} <i>RingElement</i>.
     *
     * @return a new <i>RingElement</i> \(r\), where \(r = -p\) and
     *         {@code r.getRing() = this.getRing()}.
     * @throws IncorrectRingElementException
     *             if {@code this} is not initialized.
     */
    public RingElement opposite();

    /**
     * Computes the operation \(\sqrt{p}\), where {@code p} is the {@code this}
     * <i>RingElement</i>.
     *
     * @return a tuple of <i>RingElements</i> \((r, -r)\), where \(r =
     *         \sqrt{p}\) and {@code r.getRing() = this.getRing()}, or <b><i>(
     *         {@code null}, {@code null})</i></b> if {@code this} is not a
     *         quadratic residue.
     * @throws IncorrectRingElementException
     *             if {@code this} is not initialized.
     */
    public ArrayList<RingElement> squareRoot()
            throws IncorrectRingElementException;

    /**
     * Computes the operation \(p \cdot q\), where {@code p} is the {@code this}
     * <i>RingElement</i>. The result belongs to the <i>Ring</i>
     * {@code this.getRing()}.
     *
     * @param q
     *            the <i>RingElement</i> we want to multiply {@code this}.
     * @return a new <i>RingElement</i> \(r\), where \(r = p \cdot q\) and
     *         {@code r.getRing() = this.getRing()}.
     * @throws IncorrectRingElementException
     *             if {@code this} or {@code q} are not initialized. Also throws
     *             IncorrectRingElementException {@code q} does not belong to
     *             the same <i>Ring</i> than {@code this} ({@code this} and
     *             {@code q} are not compatible <i>RingElements</i>).
     */
    public RingElement multiply(GroupElement q)
            throws IncorrectRingElementException;

    /**
     * Computes the operation \(p / q\), where {@code p} is the {@code this}
     * <i>RingElement</i>. The result belongs to the <i>Ring</i>
     * {@code this.getRing()}.
     *
     * @param q
     *            the <i>RingElement</i> we want to divide {@code this}.
     * @return a new <i>RingElement</i> \(r\), where \(r = p / q\) and
     *         {@code r.getRing() = this.getRing()}.
     * @throws IncorrectRingElementException
     *             if {@code this} or {@code q} are not initialized. Also throws
     *             IncorrectRingElementException {@code q} does not belong to
     *             the same <i>Ring</i> than {@code this} ({@code this} and
     *             {@code q} are not compatible <i>RingElements</i>).
     */
    public RingElement divide(GroupElement q)
            throws IncorrectRingElementException;

    /**
     * Returns \(1 / p\), where {@code p} is the {@code this}
     * <i>RingElement</i>.
     *
     * @return a new <i>RingElement</i> \(r\), where \(r = 1 / p\) and
     *         {@code r.getRing() = this.getRing()}.
     * @throws IncorrectRingElementException
     *             if {@code this} is not initialized.
     */
    public RingElement inverse();

    /**
     * Computes the operation \(p^{k}\), where {@code p} is the {@code this}
     * <i>RingElement</i>.
     *
     * @param k
     *            a BigInteger representing the exponent we want to apply to
     *            {@code this}.
     * @return a new <i>RingElement</i> \(r\), where \(r = p^{k}\) and
     *         {@code r.getRing() = this.getRing()}.
     * @throws IncorrectRingElementException
     *             if {@code this} is not initialized.
     */
    public RingElement pow(BigInteger k)
            throws IncorrectRingElementException;


}
