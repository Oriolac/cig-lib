package cat.udl.cig.structures;

import cat.udl.cig.exceptions.NotImplementedException;
import cat.udl.cig.structures.builder.BinaryFieldElementBuilder;
import cat.udl.cig.utils.bfarithmetic.BitSetManipulation;
import cat.udl.cig.utils.bfarithmetic.Irreducibility;

import java.math.BigInteger;
import java.util.BitSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

/**
 * Models an <i>Binary Field</i> \(\mathbb{F}_{2^{n}}\).
 *
 * @author Ricard Garra
 * @see Ring
 */
public class BinaryField implements Ring {
    /**
     * An int that encapsulates the exponent of this <i>BinaryField</i>.
     */
    private final int n; // Dimension, GF(2^n)

    /**
     * A BitSet that encapsulates the reducing polynomial of this
     * <i>BinaryField</i>.
     */
    private final BitSet reducingPolynomial;

    /**
     * Creates a <i>BinaryField</i> with
     * {@code this.reducingPolynomial = reducingPoly}. This constructor does not
     * check if the parameter is correct. That is, if reducingPolynomial is an
     * irreducible polynomial.
     *
     * @param reducingPolynomial the reducing polynomail of {@code this} newly created
     *                           <i>BinaryField</i>. It must be an irreducible polynomial.
     */
    public BinaryField(final BitSet reducingPolynomial) {
        this(reducingPolynomial, false);
    }

    /**
     * Creates a <i>BinaryField</i> with
     * {@code this.reducingPolynomial = reducingPoly}. Iff {@code check == true}
     * it tests if the parameter is correct. That is, if reducingPolynomial is
     * an irreducible polynomial. If {@code check == false} or
     * {@code check == true} and the parameter is correct, then the new instance
     * is initialized. Otherwise, this instance of <i>BinaryField</i> remains
     * uninitialized. Being uninitialized means that {@code n = 0} and
     * {@code this.reducingPolynomial = null}.
     *
     * @param reducingPolynomial the reducing polynomail of {@code this} newly created
     *                           <i>BinaryField</i>. It must be an irreducible polynomial.
     * @param check              indicates if correctness of the parameters must be checked.
     */
    public BinaryField(final BitSet reducingPolynomial, final boolean check) {
        if (check && !Irreducibility.isIrreducible(reducingPolynomial)) {
            this.reducingPolynomial = null;
            n = 0;
        } else {
            this.reducingPolynomial = reducingPolynomial;
            n = reducingPolynomial.length() - 1;
        }
    }

    /**
     * Creates a copy of the <i>BinaryField</i> \(F\). This constructor trusts
     * that \(F\) is a well-defined <i>BinaryField</i>. That is, \(F\) has a
     * correct reducing polynomial. So, the constructor does not check the
     * correctness of the attributes fo \(F\).
     *
     * @param F the <i>BinaryField</i> to be copied.
     */
    public BinaryField(final BinaryField F) {
        n = F.n;
        reducingPolynomial = (BitSet) F.reducingPolynomial.clone();

    }

    @Override
    public BigInteger getSize() {
        return BigInteger.TWO.pow(n);
    }

    @Override
    public BinaryFieldElementBuilder buildElement() {
        return new BinaryFieldElementBuilder(this);
    }

    @Override
    public BinaryFieldElement getRandomElement() {
        BitSet out = new BitSet(n);
        Random rand = new Random();
        for (int i = 0; i < n; i++) {
            if (rand.nextBoolean()) {
                out.set(i);
            }
        }
        return new BinaryFieldElement(this, out);
    }

    public BinaryFieldElement getAdditiveIdentity() {
        return new BinaryFieldElement(this, new BitSet());
    }

    @Override
    public BinaryFieldElement getMultiplicativeIdentity() {
        return new BinaryFieldElement(this,
                BitSetManipulation.longToBitSet(1));
    }

    /**
     * Returns the exponent (dimension) \(n\) of this <i>BinaryField</i>.
     *
     * @return (\ n \), the exponent of this <i>BinaryField</i>.
     */
    public int getDimension() {
        return n;
    }

    /**
     * Returns the reducing polynomial of this <i>BinaryField</i>.
     *
     * @return reducingPolynomial, the reducing polynomial of this
     * <i>BinaryField</i>.
     */
    public BitSet getReducingPolynomial() {
        return reducingPolynomial;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BinaryField that = (BinaryField) o;
        return n == that.n &&
                Objects.equals(reducingPolynomial, that.reducingPolynomial);
    }

    @Override
    public int hashCode() {
        return Objects.hash(n, reducingPolynomial);
    }

    @Override
    public String toString() {
        if (reducingPolynomial == null) {
            return "";
        }
        if (reducingPolynomial.length() == 0) {
            return "0";
        }
        StringBuilder output = new StringBuilder();
        for (int i = reducingPolynomial.length() - 1; i >= 0; i--) {

            if (reducingPolynomial.get(i)) {
                if (i < reducingPolynomial.length() - 1) {
                    output.append("+");
                }
                if (i > 0) {
                    output.append("x^").append(i);
                } else {
                    output.append("1");
                }
            }
        }

        return output.toString();
    }

    /**
     * @see Group#multiply(GroupElement,
     * GroupElement)
     */
    @Override
    public BinaryFieldElement multiply(final GroupElement x,
                                       final GroupElement y) {
        return (BinaryFieldElement) x.multiply(y);
    }

    /**
     * @see Group#pow(GroupElement,
     * BigInteger)
     */
    @Override
    public BinaryFieldElement pow(final GroupElement x,
                                  final BigInteger pow) {
        return (BinaryFieldElement) x.pow(pow);
    }

    @Override
    public BigInteger getCharacteristic() {
        return BigInteger.TWO;
    }

    @Override
    public boolean containsElement(GroupElement groupElement) {
        return groupElement.getGroup().equals(this);
    }

    @Override
    public Optional<? extends BinaryFieldElement> fromBytes(byte[] bytes) {
        throw new NotImplementedException();
    }

    @Override
    public BinaryFieldElement ZERO() {
        return this.getAdditiveIdentity();
    }

    @Override
    public BinaryFieldElement ONE() {
        return this.getMultiplicativeIdentity();
    }

    @Override
    public BinaryFieldElement THREE() {
        return this.getMultiplicativeIdentity().add(this.getMultiplicativeIdentity()).add(this.getMultiplicativeIdentity());
    }

    /**
     * @see Group#getRandomExponent()
     */
    @Override
    public BigInteger getRandomExponent() {
        throw new NotImplementedException();
    }
}
