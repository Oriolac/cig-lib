package cat.udl.cig.fields;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

import cat.udl.cig.utils.Polynomial;

/**
 * Models an <i>Extension Field</i> \(\mathbb{F}_{p^{n}}\), where \(p\) is
 * positive and a prime number.
 *
 * @see Ring
 * @author M.Àngels Cerveró
 * @author Ricard Garra
 */
public class ExtensionField implements Ring {

    /**
     * A BigInteger that encapsulates the characteristic of this <i>Field</i>.
     */
    protected final BigInteger p; /* Characteristic */

    /**
     * An int that encapsulates the exponent of this <i>ExtensionField</i>.
     */
    private final int n; /* Exponent */

    /**
     * A Polynomial that encapsulates the reducing polynomial of this
     * <i>ExtensionField</i>.
     */
    private final Polynomial reducingPolynomial; /* Irreducible polynomial */

    /**
     * Creates an <i>ExtensionField</i> with characteristic \(p = m\), exponent
     * \(n = e\) and \(\text{reducingPolynomial} = \text{reducingPoly}\). This
     * constructor does not check if the parameters are correct. That is, if
     * \(m\) is a positive and a prime number, \(e\) is greater than 0 and the
     * degree of the reducingPoly is exactly \(e\).
     *
     * @param m
     *            the characteristic of {@code this} newly created
     *            <i>ExtensionField</i>. It must be positive and a prime number.
     * @param e
     *            the exponent of {@code this} newly created
     *            <i>ExtensionField</i>. It must be greater than 0.
     * @param reducingPoly
     *            the reducing polynomail of {@code this} newly created
     *            <i>ExtensionField</i>. It must be an irreducible polynomial of
     *            degree \(e\).
     */

    public ExtensionField(final BigInteger m, final int e,
            final Polynomial reducingPoly) {
        // TODO: CHECK IF THE POLYNOMIAL IS IRREDUCIBLE!
        // Using BigInteger.valueOf(4) when
        // super(
        // ((check && e > 0 && reducingPoly.getDegree() == e) || !check) ? m
        // : BigInteger.valueOf(4), check);
        if (e > 0 && reducingPoly.getDegree() == e && m != null) {
            p = m;
            n = e;
            reducingPolynomial = reducingPoly;
        } else {
            p = null;
            n = 0;
            reducingPolynomial = null;
        }
    }

    /**
     * Creates a copy of the <i>ExtensionField</i> \(F\). This constructor
     * trusts that \(F\) is a well-defined <i>ExtensionField</i>. That is, \(F\)
     * has a correct characteristic, exponent and reducing polynomial. So, the
     * constructor does not check the correctness of the attributes fo \(F\).
     *
     * @param F
     *            the <i>ExtensionField</i> to be copied.
     */
    public ExtensionField(final ExtensionField F) {
        p = F.p;
        // if(F.isInitialized()) {
        n = F.n;
        reducingPolynomial = new Polynomial(F.reducingPolynomial);
        /*
         * } else { n = 0; reducingPolynomial = null; }
         */
    }

    /**
     * Returns the exponent \(n\) of this <i>ExtensionField</i>.
     *
     * @return (\n\), the exponent of this <i>ExtensionField</i>.
     */
    public int getExponent() {
        return n;
    }

    /**
     * Returns the reducing polynomial of this <i>ExtensionField</i>.
     *
     * @return reducingPolynomial, the reducing polynomial of this
     *         <i>ExtensionField</i>.
     */
    public Polynomial getReducingPolynomial() {
        return reducingPolynomial;
    }

    /*
     * @Override public boolean isInitialized() { return (p != null && n > 0 &&
     * reducingPolynomial != null); }
     */

    @Override
    public BigInteger getSize() {
        return p.pow(n);
    }

    @Override
    public ExtensionFieldElement toElement(final Object polynomial) {
        Polynomial result = (Polynomial) polynomial;
        return new ExtensionFieldElement(this, result);
    }

    @Override
    public ExtensionFieldElement getRandomElement() {
        /*
         * if(!isInitialized()) { return new ExtensionFieldElement(this, new
         * Polynomial()); }
         */

        ArrayList<PrimeFieldElement> coefficients =
            new ArrayList<PrimeFieldElement>();
        Random rnd = new Random();
        String val;
        PrimeFieldElement elem;
        PrimeField F = new PrimeField(p);
        for (int i = 0; i < n - 1; i++) {
            val = String.valueOf(rnd.nextInt());
            elem = new PrimeFieldElement(F, new BigInteger(val));
            coefficients.add(elem);
        }
        int degreeElem = rnd.nextInt();
        while (degreeElem == 0) {
            degreeElem = rnd.nextInt();
        }
        val = String.valueOf(degreeElem);
        elem = new PrimeFieldElement(F, new BigInteger(val));
        coefficients.add(elem);

        Polynomial poly = new Polynomial(coefficients);

        return new ExtensionFieldElement(this, poly);
    }

    public ExtensionFieldElement getElementZERO() {
        /*
         * if(!isInitialized()) { return new ExtensionFieldElement(this, new
         * Polynomial()); }
         */

        ArrayList<PrimeFieldElement> coefficients =
            new ArrayList<PrimeFieldElement>();
        PrimeField F = new PrimeField(p);
        PrimeFieldElement elem = new PrimeFieldElement(F, BigInteger.ZERO);
        coefficients.add(elem);

        Polynomial poly = new Polynomial(coefficients);

        return new ExtensionFieldElement(this, poly);
    }

    @Override
    public ExtensionFieldElement getNeuterElement() {
        /*
         * if(!isInitialized()) { return new ExtensionFieldElement(this, new
         * Polynomial()); }
         */

        ArrayList<PrimeFieldElement> coefficients =
            new ArrayList<PrimeFieldElement>();
        PrimeField F = new PrimeField(p);
        PrimeFieldElement elem = new PrimeFieldElement(F, BigInteger.ONE);
        coefficients.add(elem);

        Polynomial poly = new Polynomial(coefficients);

        return new ExtensionFieldElement(this, poly);
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof ExtensionField)) {
            return false;
        }

        ExtensionField F = (ExtensionField) obj;

        /*
         * if(!F.isInitialized() && !isInitialized()) { return true; }
         * if(!F.isInitialized() || !isInitialized()) { return false; }
         */

        return (this == obj)
            || (p.equals(F.p) && n == F.n && reducingPolynomial
                .equals(F.reducingPolynomial));
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + n;
        hash = 11 * hash + Objects.hashCode(reducingPolynomial);
        return hash;
    }

    /**
     * @see cat.udl.cig.fields.Group#multiply(cat.udl.cig.fields.GroupElement,
     *      cat.udl.cig.fields.GroupElement)
     */
    @Override
    public ExtensionFieldElement multiply(final GroupElement x,
            final GroupElement y) {
        return (ExtensionFieldElement) x.multiply(y);
    }

    /**
     * @see cat.udl.cig.fields.Group#pow(cat.udl.cig.fields.GroupElement,
     *      java.math.BigInteger)
     */
    @Override
    public ExtensionFieldElement pow(final GroupElement x,
            final BigInteger pow) {
        return (ExtensionFieldElement) x.pow(pow);
    }

    /**
     * @see cat.udl.cig.fields.Group#getRandomExponent()
     */
    @Override
    public BigInteger getRandomExponent() {
        // TODO Auto-generated method stub
        return null;
    }
}
