/**
 * $Id$
 * @author vmateu
 * @date   Sep 28, 2015 10:15:55 AM
 *
 * Copyright (C) 2015 Scytl Secure Electronic Voting SA
 *
 * All rights reserved.
 *
 */
package cat.udl.cig.fields;

import java.math.BigInteger;

/**
 *
 */
public interface Group {

    /**
     * Returns the number of elements of the <i>Group</i>.
     *
     * @return a BigInteger representing the number of elements of the
     *         <i>Group</i>.
     */
    public BigInteger getSize();

    /**
     * Converts a k to a <i>GroupElement</i> belonging to {@code this}
     * <i>Group</i> \(K\).
     *
     * @param k
     *            an object that could be converted into a <i>GroupElement</i>
     *            of {@code this} <i>Group</i>.
     * @return a new <i>GroupElement</i> \(r\), such that
     *         {@code r value = k.mod(this.getSize())} and
     *         {@code r.getGroup() = this}.
     */
    public GroupElement toElement(Object k);

    /**
     * Returns a random element \(k\) of {@code this} <i>Ring</i>.
     *
     * @return a random element \(k\) that belongs to {@code this} <i>Group</i>.
     *         The element \(k\) is an instance of <i>GroupElement</i>.
     * @see GroupElement
     */
    public GroupElement getRandomElement();

    /**
     * Returns a random exponent for an element of {@code this} <i>Group</i>.
     *
     * @return a random exponent to {@code this} <i>Group</i>.
     */
    public BigInteger getRandomExponent();

    /**
     * Returns the neuter element of {@code this} <i>Group</i>.
     *
     * @return an element \(k = 1\) that belongs to {@code this} <i>Group</i>.
     *         The element \(k\) is an instance of <i>GroupElement</i>.
     * @see GroupElement
     */
    public GroupElement getNeuterElement();

    /**
     * Returns the element resulting of the multiplication of two elements of
     * the <i>Group</i>.
     *
     * @param x
     *            an element to be multiplied.
     * @param y
     *            another element to be multiplied.
     * @return a group element being x multiplied by y
     * @see GroupElement
     */
    public GroupElement multiply(GroupElement x, GroupElement y);

    /**
     * Returns the element resulting of the multiplication of two elements of
     * the <i>Group</i>.
     *
     * @param x
     *            an element to be exponentiated.
     * @param pow
     *            the power in which the group element is raised to.
     * @return a group element being x power y.
     * @see GroupElement
     */
    public GroupElement pow(GroupElement x, BigInteger pow);

}
