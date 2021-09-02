/**
 * $Id$
 * @author vmateu
 * @date   Sep 28, 2015 10:25:14 AM
 *
 * Copyright (C) 2015 Scytl Secure Electronic Voting SA
 *
 * All rights reserved.
 *
 */
package cat.udl.cig.structures;

import java.math.BigInteger;

import cat.udl.cig.exceptions.IncorrectRingElementException;

/**
 *
 */
public interface GroupElement {

    /**
     * Checks if <i>GroupElement</i> {@code q} belongs to the same <i>Group</i>
     * than {@code this} <i>GroupElement</i>
     *
     * @param q
     *            the GroupElement to be checked.
     * @return {@code true} if {@code q} and {@code this} are elements of the
     *         same <i>Group</i>. Otherwise, returns {@code false}.
     * @see Group
     */
    public boolean belongsToSameGroup(GroupElement q);

    /**
     * Returns the <i>Group</i> \(K\) of {@code this} <i>GroupElement</i>.
     *
     * @return \(K\), the <i>Group</i> of {@code this} <i>GroupElement</i>.
     */
    public Group getGroup();

    /**
     * Returns the value of the {@code this} <i>GroupElement</i>. This value
     * belongs to the cyclic group of the <i>Group</i> \(K\) that defines
     * {@code this} <i>GroupElement</i>.
     *
     * @return a BigInteger representing the value of {@code this}
     *         <i>GroupElement</i>.
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
     * Computes the operation \(p \cdot q\), where {@code p} is the {@code this}
     * <i>GroupElement</i>. The result belongs to the <i>Group</i>
     * {@code this.getGroup()}.
     *
     * @param q
     *            the <i>GroupElement</i> we want to multiply {@code this}.
     * @return a new <i>GroupElement</i> \(r\), where \(r = p \cdot q\) and
     *         {@code r.getGroup() = this.getGroup()}.
     * @throws IncorrectGroupElementException
     *             if {@code this} or {@code q} are not initialized. Also throws
     *             IncorrectGroupElementException {@code q} does not belong to
     *             the same <i>Group</i> than {@code this} ({@code this} and
     *             {@code q} are not compatible <i>GroupElements</i>).
     */
    public GroupElement multiply(GroupElement q)
            throws IncorrectRingElementException;

    /**
     * Computes the operation \(p / q\), where {@code p} is the {@code this}
     * <i>GroupElement</i>. The result belongs to the <i>Group</i>
     * {@code this.getGroup()}.
     *
     * @param q
     *            the <i>GroupElement</i> we want to divide {@code this}.
     * @return a new <i>GroupElement</i> \(r\), where \(r = p / q\) and
     *         {@code r.getGroup() = this.getGroup()}.
     * @throws IncorrectGroupElementException
     *             if {@code this} or {@code q} are not initialized. Also throws
     *             IncorrectGroupElementException {@code q} does not belong to
     *             the same <i>Group</i> than {@code this} ({@code this} and
     *             {@code q} are not compatible <i>GroupElements</i>).
     */
    public GroupElement divide(GroupElement q)
            throws IncorrectRingElementException;

    /**
     * Returns \(1 / p\), where {@code p} is the {@code this}
     * <i>GroupElement</i>.
     *
     * @return a new <i>GroupElement</i> \(r\), where \(r = 1 / p\) and;
     *         {@code r.getGroup() = this.getGroup()}.
     * @throws IncorrectGroupElementException
     *             if {@code this} is not initialized.
     */
    public GroupElement inverse();

    /**
     * Computes the operation \(p^{k}\), where {@code p} is the {@code this}
     * <i>GroupElement</i>.
     *
     * @param k
     *            a BigInteger representing the exponent we want to apply to
     *            {@code this}.
     * @return a new <i>GroupElement</i> \(r\), where \(r = p^{k}\) and
     *         {@code r.getGroup() = this.getGroup()}.
     * @throws IncorrectGroupElementException
     *             if {@code this} is not initialized.
     */
    public GroupElement pow(BigInteger k)
            throws IncorrectRingElementException;


    default byte[] toBytes() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }
}
