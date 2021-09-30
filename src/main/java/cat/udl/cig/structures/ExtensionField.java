package cat.udl.cig.structures;

import cat.udl.cig.exceptions.ConstructionException;
import cat.udl.cig.exceptions.NotImplementedException;
import cat.udl.cig.structures.builder.ExtensionFieldElementBuilder;
import cat.udl.cig.utils.Polynomial;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

/**
 * Models an <i>Extension Field</i> \(\mathbb{F}_{p^{n}}\), where \(p\) is
 * positive and a prime number.
 *
 * @author M.Àngels Cerveró
 * @author Ricard Garra
 * @see Ring
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

    private final PrimeField field;
    
    /**
     * A Polynomial that encapsulates the reducing polynomial of this
     * <i>ExtensionField</i>.
     */
    private final Polynomial reducingPolynomial; /* Irreducible polynomial */

    /**
     * Creates an <i>ExtensionField</i> with characteristic \(p = p\), exponent
     * \(n = n\) and \(\text{reducingPolynomial} = \text{reducingPoly}\). This
     * constructor does not check if the parameters are correct. That is, if
     * \(p\) is a positive and a prime number, \(n\) is greater than 0 and the
     * degree of the reducingPoly is exactly \(n\).
     *
     * @param p            the characteristic of {@code this} newly created
     *                     <i>ExtensionField</i>. It must be positive and a prime number.
     * @param n            the exponent of {@code this} newly created
     *                     <i>ExtensionField</i>. It must be greater than 0.
     * @param reducingPoly the reducing polynomail of {@code this} newly created
     *                     <i>ExtensionField</i>. It must be an irreducible polynomial of
     *                     degree \(e\).
     */
    public ExtensionField(final BigInteger p, final int n,
                          final Polynomial reducingPoly) {
        // TODO: CHECK IF THE POLYNOMIAL IS IRREDUCIBLE!
        // Using BigInteger.valueOf(4) when
        // super(
        // ((check && e > 0 && reducingPoly.getDegree() == e) || !check) ? m
        // : BigInteger.valueOf(4), check);
        if (n > 0 && reducingPoly.getDegree() == n && p != null) {
            this.p = p;
            this.n = n;
            reducingPolynomial = reducingPoly;
        } else {
            throw new ConstructionException();
        }
        field = new PrimeField(p);
    }

    public static ExtensionField ExtensionFieldP2(BigInteger p) {
        return new ExtensionField(p, 2, searchIrreduciblePolynomial(p));
    }

    private static Polynomial searchIrreduciblePolynomial(BigInteger p) {
        PrimeField field = new PrimeField(p);
        Optional<PrimeFieldElement> one = field.buildElement().setValue(BigInteger.ONE).build();
        if (one.isEmpty())
            throw new ConstructionException();
        Polynomial.PolynomialBuilder polynomialBuilder = new Polynomial.PolynomialBuilder().addTerm(2, one.get());
        for (BigInteger i = BigInteger.valueOf(1); !i.equals(p.subtract(BigInteger.ONE)); i = i.add(BigInteger.ONE)) {
            Polynomial polynomial = polynomialBuilder.addTerm(0, new PrimeFieldElement(field, i)).build();
            PrimeFieldElement independentTerm = polynomial.getCoefficient(0);
            if (independentTerm.opposite().isQuadraticNonResidue()) {
                return polynomial;
            }
        }
        throw new ConstructionException("Cannot find any irreducble polynomial");
    }

    /**
     * Creates a copy of the <i>ExtensionField</i> \(F\). This constructor
     * trusts that \(F\) is a well-defined <i>ExtensionField</i>. That is, \(F\)
     * has a correct characteristic, exponent and reducing polynomial. So, the
     * constructor does not check the correctness of the attributes fo \(F\).
     *
     * @param F the <i>ExtensionField</i> to be copied.
     */
    public ExtensionField(final ExtensionField F) {
        p = F.p;
        // if(F.isInitialized()) {
        n = F.n;
        reducingPolynomial = new Polynomial(F.reducingPolynomial);
        /*
         * } else { n = 0; reducingPolynomial = null; }
         */
        field = F.field;
    }

    /**
     * Returns the exponent \(n\) of this <i>ExtensionField</i>.
     *
     * @return (\ n \), the exponent of this <i>ExtensionField</i>.
     */
    public int getExponent() {
        return n;
    }

    /**
     * Returns the reducing polynomial of this <i>ExtensionField</i>.
     *
     * @return reducingPolynomial, the reducing polynomial of this
     * <i>ExtensionField</i>.
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
    public ExtensionFieldElementBuilder buildElement() {
        return new ExtensionFieldElementBuilder(this);
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

    public ExtensionFieldElement getAdditiveIdentity() {
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
    public ExtensionFieldElement getMultiplicativeIdentity() {
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExtensionField that = (ExtensionField) o;
        return n == that.n &&
                Objects.equals(p, that.p) &&
                Objects.equals(reducingPolynomial, that.reducingPolynomial);
    }

    @Override
    public int hashCode() {
        int result = p != null ? p.hashCode() : 0;
        result = 31 * result + n;
        result = 31 * result + (field != null ? field.hashCode() : 0);
        result = 31 * result + (reducingPolynomial != null ? reducingPolynomial.hashCode() : 0);
        return result;
    }

    /**
     * @see Group#multiply(GroupElement,
     * GroupElement)
     */
    @Override
    public ExtensionFieldElement multiply(final GroupElement x,
                                          final GroupElement y) {
        return (ExtensionFieldElement) x.multiply(y);
    }

    /**
     * @see Group#pow(GroupElement,
     * BigInteger)
     */
    @Override
    public ExtensionFieldElement pow(final GroupElement x,
                                     final BigInteger pow) {
        return (ExtensionFieldElement) x.pow(pow);
    }

    @Override
    public BigInteger getCharacteristic() {
        return this.p;
    }

    @Override
    public boolean containsElement(GroupElement groupElement) {
        return groupElement.getGroup().equals(this);
    }

    @Override
    public Optional<? extends ExtensionFieldElement> fromBytes(byte[] bytes) {
        throw new NotImplementedException();
    }

    @Override
    public ExtensionFieldElement ZERO() {
        Polynomial.PolynomialBuilder pBuilder = new Polynomial.PolynomialBuilder();
        return this.buildElement().setPolynomial(pBuilder.addTerm(0, field.ZERO()).build()).build().orElseThrow();
    }

    @Override
    public ExtensionFieldElement ONE() {
        Polynomial.PolynomialBuilder pBuilder = new Polynomial.PolynomialBuilder();
        return this.buildElement().setPolynomial(pBuilder.addTerm(0, field.ONE()).build()).build().orElseThrow();
    }

    @Override
    public ExtensionFieldElement THREE() {
        Polynomial.PolynomialBuilder pBuilder = new Polynomial.PolynomialBuilder();
        return this.buildElement().setPolynomial(pBuilder.addTerm(0, field.THREE()).build()).build().orElseThrow();
    }

    /**
     * @see Group#getRandomExponent()
     */
    @Override
    public BigInteger getRandomExponent() {
        // TODO Auto-generated method stub
        return null;
    }

    public PrimeField getField() {
        return field;
    }
}
