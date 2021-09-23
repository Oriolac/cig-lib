/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cat.udl.cig.utils;

import cat.udl.cig.exceptions.ConstructionException;
import cat.udl.cig.operations.wrapper.data.Pair;
import cat.udl.cig.structures.ExtensionField;
import cat.udl.cig.structures.PrimeField;
import cat.udl.cig.structures.PrimeFieldElement;
import cat.udl.cig.structures.RingElement;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.*;

/**
 * Models a polynomial which coefficients are <i>PrimeFieldElement</i>.
 *
 * @author M.Àngels Cerveró
 * @see PrimeFieldElement
 */
public class Polynomial {
    /**
     * The coefficients of {@code this} <i>Polynomial</i>.
     *
     * @see PrimeFieldElement
     */
    private ArrayList<PrimeFieldElement> coefficients;

    /**
     * The exponent of the highest coefficient of {@code this}
     * <i>Polynomial</i>.
     */
    private int degree;

    private PrimeField field;

    /**
     * Creates a new <i>Polynomial</i> with the specified {@code coefficients}.
     * If {@code coefficients.isEmpty() == true}, then, the new
     * <i>Polynomial</i> is keeped empty (
     * {@code this.coefficients.isEmpty() = true} and {@code this.degree = -1}).
     *
     * @param coefficients an ArrayList that contains the coefficients for {@code this}
     *                     new <i>Polynomial</i>.
     * @see PrimeFieldElement
     */
    public Polynomial(final ArrayList<PrimeFieldElement> coefficients) {
        this(coefficients, coefficients.get(0).getGroup());
    }

    public Polynomial(final ArrayList<PrimeFieldElement> coefficients, PrimeField field) {
        if (coefficients.isEmpty()) {
            this.coefficients = new ArrayList<PrimeFieldElement>();
            degree = -1;
            this.field = null;
        } else {
            this.coefficients =
                    new ArrayList<>(coefficients);
            degree = this.coefficients.size() - 1;
            this.field = field;
            checkDegree();
        }
    }

    /**
     * Creates a copy of the <i>Polynomial</i> {@code poly}. This constructor
     * makes a deep copy of {@code poly}.
     *
     * @param poly the <i>Polynomial</i> to be copied.
     */
    public Polynomial(final Polynomial poly) {
        this(poly.coefficients, poly.field, poly.degree);
    }

    public Polynomial(PrimeField field) {
        this(new ArrayList<>(), field, -1);
    }

    public Polynomial(ArrayList<PrimeFieldElement> coefficients, PrimeField field, int degree) {
        this.coefficients = coefficients;
        this.field = field;
        this.degree = degree;
        this.checkDegree();
    }

    /**
     * Returns the degree of {@code this} <i>Polynomial</i>.
     *
     * @return an int representing the degree of {@code this} <i>Polynomial</i>,
     * {@code this.degree}.
     */
    public int getDegree() {
        return degree;
    }

    /**
     * Returns the i-th coefficient of {@code this} <i>Polynomial</i>.
     *
     * @param termDegree the index of the coefficient.
     * @return a <i>PrimeFieldElement</i>, the i-th coefficient of {@code this}
     * <i>Polynomial</i>, {@code this.coefficients.get(i)}.
     * @see PrimeFieldElement
     */
    public PrimeFieldElement getCoefficient(final int termDegree) {
        if (termDegree < 0 || termDegree > degree) {
            return null;
        }
        return coefficients.get(termDegree);
    }

    /**
     * Computes the operation \(p + q\), where {@code p} is the {@code this}
     * <i>Polynomial</i>.
     *
     * @param q the <i>Polynomial</i> we want to add to {@code this}.
     * @return a new <i>Polynomial</i> \(r\), where \(r = p + q\).
     */
    public Polynomial add(final Polynomial q) {
        Polynomial p1, p2;
        ArrayList<PrimeFieldElement> coefficients = new ArrayList<>();
        if (degree < q.degree) {
            p1 = this;
            p2 = q;
        } else {
            p1 = q;
            p2 = this;
        }

        for (int i = 0; i <= p1.degree; i++) {
            coefficients.add(p1.coefficients.get(i).add(
                    p2.coefficients.get(i)));
        }

        for (int i = p1.degree + 1; i <= p2.degree; i++) {
            coefficients.add(new PrimeFieldElement(p2.coefficients
                    .get(i)));
        }

        return new Polynomial(coefficients, this.field, p2.degree);
    }

    /**
     * Computes the operation \(p - q\), where {@code p} is the {@code this}
     * <i>Polynomial</i>.
     *
     * @param q the <i>Polynomial</i> we want to subtract to {@code this}.
     * @return a new <i>Polynomial</i> \(r\), where \(r = p - q\).
     */
    public Polynomial subtract(final Polynomial q) {
        return add(q.opposite());
    }

    /**
     * Returns \(-p\), where {@code p} is {@code this} <i>Polynomial</i>.
     *
     * @return a new <i>Polynomial</i> \(r\), where \(r = -p\).
     */
    public Polynomial opposite() {
        ArrayList<PrimeFieldElement> coefficients = new ArrayList<>();
        for (int i = 0; i <= degree; i++) {
            coefficients.add(this.coefficients.get(i).opposite());
        }
        return new Polynomial(coefficients, this.field, degree);
    }

    /**
     * Computes the operation \(p \cdot q (\mod modulus)\), where {@code p} is
     * {@code this} <i>Polynomial</i>. If \(q\) and \(modulus\) do not belong to
     * the same <i>Ring</i> as \(p\), then, this method returns an empty
     * <i>Polynomial</i>.
     *
     * @param q       the <i>Polynomial</i> we want to multiply {@code this}.
     * @param modulus the reducing polynomial (modulus) of the multiplication.
     * @return a new <i>Polynomial</i> \(r\), where \(r = p \cdot q (\mod
     * modulus)\).
     */
    public Polynomial multiply(final Polynomial q, final Polynomial modulus) {
        if (!belongToSameBaseRing(q) || !belongToSameBaseRing(modulus)) {
            throw new ArithmeticException("Polynomial does not belong to the ring");
        }
        return euclideanMultiplication(q).euclideanDivision(modulus, this.field)
                .getValue();
    }

    /**
     * Computes the operation \(p / q (\mod modulus)\), where {@code p} is
     * {@code this} <i>Polynomial</i>. If \(q\) and \(modulus\) do not belong to
     * the same <i>Ring</i> as \(p\), then, this method returns an empty
     * <i>Polynomial</i>.
     *
     * @param q       the <i>Polynomial</i> we want to divide {@code this}.
     * @param modulus the reducing polynomial (modulus) of the division.
     * @return a new <i>Polynomial</i> \(r\), where \(r = p / q (\mod
     * modulus)\).
     */
    public Polynomial divide(final Polynomial q, final Polynomial modulus) {
        if (!belongToSameBaseRing(q) || !belongToSameBaseRing(modulus)) {
            throw new ArithmeticException("Polynomial does not belong to the ring");
        }
        return multiply(q.inverse(modulus), modulus);
    }

    /**
     * Returns \(1 / p (\mod modulus)\), where {@code p} is {@code this}
     * <i>Polynomial</i>. If \(modulus\) do not belong to the same <i>Ring</i>
     * as \(p\), then, this method returns an empty <i>Polynomial</i>.
     *
     * @param modulus the reducing polynomial (modulus) of the inversion.
     * @return a new <i>Polynomial</i> \(r\), where \(r = 1 / p (\mod
     * modulus)\).
     */
    /*
     * function inverse(a, p) t := 0; newt := 1; r := p; newr := a; while newr ≠
     * 0 quotient := r div newr (r, newr) := (newr, r - quotient * newr) (t,
     * newt) := (newt, t - quotient * newt) if degree(r) > 0 then return
     * "Either p is not irreducible or a is a multiple of p" return (1/r) * t
     */
    public Polynomial inverse(final Polynomial modulus) {
        if (!belongToSameBaseRing(modulus)) {
            throw new ArithmeticException("Polynomial does not belong to the ring");
        }
        // Extended Euclidean Algorithm
        ArrayList<PrimeFieldElement> tcoefficients =
                new ArrayList<PrimeFieldElement>();
        ArrayList<PrimeFieldElement> ntcoefficients =
                new ArrayList<PrimeFieldElement>();
        Polynomial t;
        Polynomial newt;
        Polynomial r = modulus;
        Polynomial newr = this;

        tcoefficients.add(new PrimeFieldElement(coefficients.get(0)
                .getGroup(), BigInteger.ZERO));
        t = new Polynomial(tcoefficients);

        ntcoefficients.add(new PrimeFieldElement(coefficients.get(0)
                .getGroup(), BigInteger.ONE));
        newt = new Polynomial(ntcoefficients);

        Polynomial quotient, tmp;
        Pair<Polynomial, Polynomial> divResult;
        while (newr.degree != -1 && !newr.isZeroPolynomial()) {
            divResult = r.euclideanDivision(newr, this.field);
            quotient = divResult.getKey();

            r = newr;
            newr = divResult.getValue();

            tmp = newt;
            newt = t.subtract(quotient.euclideanMultiplication(newt));
            t = tmp;
        }
        if (r.degree == 0) {
            ArrayList<PrimeFieldElement> rcoefficients =
                    new ArrayList<PrimeFieldElement>();
            rcoefficients.add(r.coefficients.get(0).inverse());
            Polynomial invR = new Polynomial(rcoefficients);
            invR = invR.euclideanMultiplication(t);
            invR.checkDegree();
            return invR;
        }
        return new Polynomial(this.field);
    }

    /**
     * Computes the operation \(p^{k} (\mod modulus)\), where {@code p} is
     * {@code this} <i>Polynomial</i>.
     *
     * @param k       a BigInteger representing the exponent we want to apply to
     *                {@code this}.
     * @param modulus the reducing polynomial (modulus) of this method.
     * @return a new <i>Polynomial</i> \(r\), where \(r = p^{k} (\mod
     * modulus)\).
     */
    public Polynomial pow(BigInteger k, final Polynomial modulus) {
        // k es pot reduir mòdul (p ^ n - 1)
        BigInteger p = field.getSize();
        k = k.mod(p.pow(modulus.degree).subtract(BigInteger.ONE));

        ArrayList<PrimeFieldElement> rcoefficients =
                new ArrayList<PrimeFieldElement>();
        rcoefficients.add(new PrimeFieldElement(coefficients.get(0)
                .getGroup(), BigInteger.ONE));
        Polynomial r = new Polynomial(rcoefficients);
        Polynomial polynomialTmp = this;

        String binary = k.toString(2);
        int nbits = binary.length();
        for (int i = nbits - 1; i >= 0; i--) {
            if (binary.charAt(i) == '1') {
                r = r.multiply(polynomialTmp, modulus);
            }
            polynomialTmp = polynomialTmp.multiply(polynomialTmp, modulus);
        }
        r.checkDegree();
        return r;
    }

    /**
     * Computes the operation \(\sqrt{p} (\mod modulus)\), where {@code p} is
     * {@code this} <i>Polynomial</i>.
     *
     * @param modulus the reducing polynomial (modulus) of this method.
     * @return a <i>Polynomial</i> \(r\), where \(r = \sqrt{p}\) and
     * {@code r.getGroup() = this.getGroup()}, or an empty
     * <i>Polynomial</i> if {@code this} is not a quadratic residue.
     */
    public Polynomial squareRoot(final Polynomial modulus) {
        if (modulus.degree == 2)
            return squareRootGora(modulus);
        return squareRootPrimal(modulus);
    }

    private Polynomial squareRootGora(Polynomial modulus) {
        if (coefficients.size() < 2 || this.coefficients.get(1).equals(field.getAdditiveIdentity())){
            PrimeFieldElement a0 = this.coefficients.get(0);
            if (a0.equals(field.getAdditiveIdentity()))
                return new Polynomial.PolynomialBuilder().addTerm(0, field.getAdditiveIdentity()).build();
            if (a0.isQuadraticNonResidue()) {
                PrimeFieldElement beta = modulus.getCoefficient(0).opposite();
                PrimeFieldElement b2 = a0.multiply(beta);
                assert b2.isQuadraticResidue();
                ArrayList<PrimeFieldElement> b2squareRoots = b2.squareRoot();
                if (b2squareRoots.isEmpty())
                    throw new ArithmeticException("a0*B Must have square roots");
                PrimeFieldElement b = b2squareRoots.get(0);
                Polynomial betaInverse = new Polynomial.PolynomialBuilder().addTerm(1, beta.inverse()).build();
                Polynomial firstPart = new Polynomial.PolynomialBuilder().addTerm(0, b.multiply(beta.inverse())).build();
                return betaInverse.multiply(firstPart, modulus);
            }
            PrimeFieldElement el = this.coefficients.get(0).squareRoot().get(0);
            return new PolynomialBuilder().addTerm(0, el).build();
        }
        PrimeFieldElement beta = modulus.getCoefficient(0);
        PrimeFieldElement alpha = coefficients.get(0).pow(BigInteger.TWO).add(beta.multiply(coefficients.get(1).pow(BigInteger.TWO)));
        if (alpha.isQuadraticNonResidue())
            throw new ArithmeticException("Is quadratic non-residue or it is -1");
        alpha = alpha.squareRoot().get(0);
        PrimeFieldElement delta = coefficients.get(0).add(alpha)
                .divide(field.buildElement().setValue(2).build().orElseThrow());
        if (delta.isQuadraticNonResidue())
            delta = coefficients.get(0).subtract(alpha)
                    .divide(field.buildElement().setValue(2).build().orElseThrow());
        ArrayList<PrimeFieldElement> deltaSquares = delta.squareRoot();
        if (deltaSquares.isEmpty())
            throw new ArithmeticException("No able to compute delta squareRoot");
        PrimeFieldElement x0 = deltaSquares.get(0);
        PrimeFieldElement x1 = coefficients.get(1).divide(field.buildElement().setValue(2).build().orElseThrow().multiply(x0));
        return new PolynomialBuilder().addTerm(0, x0).addTerm(1, x1).build();
    }

    @NotNull
    private Polynomial squareRootPrimal(Polynomial modulus) {
        // TODO: in need to be checkd. The computations are too slow!
        final BigInteger TWO = BigInteger.valueOf(2);
        if (isSquare(modulus)) {
            BigInteger p =
                    modulus.coefficients.get(0).getGroup().getSize();
            int n = modulus.degree;
            BigInteger q = p.pow(n);
            ExtensionField F = new ExtensionField(p, n, modulus);
            Polynomial rho = F.getRandomElement().getPolynomial();
            while ((rho.degree == 0 && rho.coefficients.get(0).getValue()
                    .compareTo(BigInteger.ZERO) == 0)
                    || rho.isSquare(modulus)) {
                rho = F.getRandomElement().getPolynomial();
            }

            int t = 0;
            BigInteger s = q.subtract(BigInteger.ONE);
            while (s.mod(TWO).compareTo(BigInteger.ZERO) == 0) {
                t++;
                s = s.divide(TWO);
            }

            Polynomial a = rho.pow(s, modulus);
            Polynomial b = pow(s, modulus);
            ArrayList<PrimeFieldElement> hcoefficients =
                    new ArrayList<>();
            hcoefficients.add(new PrimeFieldElement(coefficients.get(0)
                    .getGroup(), BigInteger.ONE));
            Polynomial h = new Polynomial(hcoefficients);

            Polynomial d;
            BigInteger tmpE;
            int k;
            for (int i = 1; i < t; i++) {
                tmpE = TWO;
                tmpE = tmpE.pow(t - 1 - i);
                d = b.pow(tmpE, modulus);
                if (d.degree == 0
                        && d.coefficients.get(0).getValue()
                        .compareTo(BigInteger.ONE) == 0) {
                    k = 0;
                } else {
                    k = 1;
                }
                tmpE = TWO;
                tmpE = tmpE.multiply(new BigInteger(String.valueOf(k)));
                b = b.multiply(a.pow(tmpE, modulus), modulus);
                tmpE = new BigInteger(String.valueOf(k));
                h = h.multiply(a.pow(tmpE, modulus), modulus);
                tmpE = TWO;
                a = a.pow(tmpE, modulus);
            }
            tmpE = s.add(BigInteger.ONE).divide(TWO);
            Polynomial root = pow(tmpE, modulus).multiply(h, modulus);
            root.checkDegree();
            return root;
        }
        throw new ArithmeticException("Is not quadratic");
    }

    /**
     * Auxiliar method to check if {@code this} <i>Polynomial</i> is a quadratic
     * residue, that is, if it exists \(\sqrt(p)\), where \(p\) is {@code this}
     * <i>Polynomial</i>.
     *
     * @param modulus the reducing polynomial (modulus) of this method.
     * @return {@code true} if \(\sqrt(p)\) exists and {@code false} otherwise.
     */
    private boolean isSquare(final Polynomial modulus) {
        BigInteger q = coefficients.get(0).getGroup().getSize();
        q = q.pow(modulus.degree);
        BigInteger exponent = q.subtract(BigInteger.ONE);
        exponent = exponent.divide(BigInteger.valueOf(2));
        Polynomial p = pow(exponent, modulus); // SLOW! (when the exponent value
        // is high
        return p.degree == 0
                && p.coefficients.get(0).getValue().compareTo(BigInteger.ONE) == 0;
    }

    /**
     * Auxiliar method to compute the Euclidean Multiplication \(p \cdot q\),
     * where {@code p} is {@code this} <i>Polynomial</i>.
     *
     * @param q the <i>Polynomial</i> we want to multiply {@code this}.
     * @return a new <i>Polynomial</i> \(r\), where \(r = p \cdot q\).
     */
    public Polynomial euclideanMultiplication(final Polynomial q) {
        int newdegree = this.degree + q.degree;
        ArrayList<PrimeFieldElement> newCoeff = new ArrayList<>();
        PrimeField field = coefficients.get(0).getGroup();
        for (int i = 0; i <= newdegree; i++) {
            newCoeff.add(new PrimeFieldElement(field, BigInteger.ZERO));
        }

        // Multiply polynomials
        for (int i = 0; i <= degree; i++) {
            for (int j = 0; j <= q.degree; j++) {
                PrimeFieldElement mult = coefficients.get(i).multiply(q.coefficients.get(j));
                newCoeff.set(i + j, newCoeff.get(i + j).add(mult));
            }
        }
        return new Polynomial(newCoeff, this.field, newdegree);
    }

    /**
     * Auxiliar method to compute the Euclidean Division \(p / q\), where
     * {@code p} is {@code this} <i>Polynomial</i>.
     *
     * @param q the <i>Polynomial</i> we want to multiply {@code this}.
     * @return a new <i>Polynomial</i> \(r\), where \(r = p \cdot q\).
     */
    public Pair<Polynomial, Polynomial> euclideanDivision(
            final Polynomial q, PrimeField field) {
        Polynomial quotient = new Polynomial(field);
        Polynomial remainder = new Polynomial(field);
        Pair<Polynomial, Polynomial> result;

        if (degree > -1 && q.degree > -1 && !coefficients.get(0).getGroup().equals(q.coefficients.get(0).getGroup())) {
            throw new ArithmeticException();
        }
        if (degree < 0 || q.degree < 0)
            return new Pair<>(
                    new Polynomial.PolynomialBuilder()
                            .addTerm(0, this.field.buildElement().setValue(0).build().orElseThrow())
                            .build(),
                    new Polynomial.PolynomialBuilder()
                            .addTerm(0, this.field.buildElement().setValue(0).build().orElseThrow())
                            .build());

        if (q.degree > degree) {
            remainder.degree = degree;
            remainder.coefficients = new ArrayList<>(coefficients);
            quotient.degree = 0;
            quotient.coefficients.add(this.field.getAdditiveIdentity());
            result = new Pair<>(quotient, remainder);
        } else {
            remainder = this;
            quotient.degree = degree - q.degree;
            for (int i = 0; i <= quotient.degree; i++) {
                quotient.coefficients.add(i, this.field.getAdditiveIdentity());
            }

            while (remainder.degree >= q.degree
                    && !remainder.isZeroPolynomial()) {
                int newDegree = remainder.degree - q.degree;
                ArrayList<PrimeFieldElement> newCoefficients = new ArrayList<>();
                for (int i = 0; i <= newDegree; i++) {
                    newCoefficients.add(field.getAdditiveIdentity());
                }
                quotient.coefficients.set(newDegree,
                        remainder.coefficients.get(remainder.degree).divide(q.coefficients.get(q.degree)));
                newCoefficients.set(newDegree,
                        quotient.coefficients.get(newDegree));
                Polynomial curQuotient = new Polynomial(newCoefficients, this.field, newDegree);
                remainder = remainder.subtract(q
                        .euclideanMultiplication(curQuotient));
            }

            quotient.checkDegree();
            remainder.checkDegree();
            result = new Pair<>(quotient, remainder);
        }
        return result;
    }

    @Override
    public String toString() {
        if (degree < 0) {
            return "0";
        }

        String pstring = coefficients.get(degree).getValue().toString();
        if (degree == 1) {
            pstring += "x";
        } else if (degree > 1) {
            pstring += "x^" + degree;
        }

        for (int i = degree - 1; i >= 0; i--) {
            if (coefficients.get(i).getValue().compareTo(BigInteger.ZERO) != 0) {
                pstring +=
                        " + " + coefficients.get(i).getValue().toString();
                if (i == 1) {
                    pstring += "x";
                } else if (i > 1) {
                    pstring += "x^" + i;
                }
            }
        }
        return pstring;
    }

    /**
     * Auxiliar method to compute the correct degree of {@code this}
     * <i>Polynomial</i>.
     */
    private void checkDegree() {
        if (degree < 0) {
            return;
        }
        while (degree > 0
                && coefficients.get(degree).getValue()
                .compareTo(BigInteger.ZERO) == 0) {
            coefficients.remove(degree);
            degree--;
        }
    }

    /**
     * Checks if {@code this} <i>Polynomial</i> is the Zero one. That is,
     * {@code this.coefficients.get(0) = 0} and {@code this.degree = 0}.
     *
     * @return {@code true} if \(p = 0\) and {@code false} otherwise.
     */
    private boolean isZeroPolynomial() {
        return (degree == 0 && coefficients.get(0).getValue() == BigInteger.ZERO);
    }

    /**
     * Checks if \(p\) and \(q\), where \(p\) is {@code this} <i>Polynomial</i>,
     * belong to the same <i>Ring</i>.
     *
     * @param q the <i>Polynomial</i> we want to check.
     * @return {@code true} if \(p\) and \(q\) belong to the same <i>Ring</i>
     * and{@code false} otherwise.
     */
    private boolean belongToSameBaseRing(final Polynomial q) {
        if (degree == -1 || q.degree == -1) {
            return false;
        } else {
            return coefficients.get(0).getGroup().getSize()
                    .equals(q.coefficients.get(0).getGroup().getSize());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Polynomial)) return false;

        Polynomial that = (Polynomial) o;

        if (!this.field.equals(that.field)) return false;
        if (coefficients.size() < that.coefficients.size()) {
            fill(coefficients, that.coefficients.size(), that.field.getAdditiveIdentity());
        } else if (coefficients.size() > that.coefficients.size()) {
            fill(that.coefficients, coefficients.size(), that.field.getAdditiveIdentity());
        }
        for (int i = 0; i < coefficients.size(); i++) {
            if (!coefficients.get(i).equals(that.coefficients.get(i))) {
                return false;
            }
        }
        return true;
    }

    static private void fill(ArrayList<PrimeFieldElement> coefficients, int size, PrimeFieldElement additiveIdentity) {
        for (int i = coefficients.size(); i < size; i++) {
            coefficients.add(i, additiveIdentity);
        }
    }

    @Override
    public int hashCode() {
        int result = coefficients != null ? coefficients.hashCode() : 0;
        result = 31 * result + degree;
        result = 31 * result + (field != null ? field.hashCode() : 0);
        return result;
    }

    public PrimeField getField() {
        return field;
    }

    public static class PolynomialBuilder {

        private final HashMap<Integer, PrimeFieldElement> coefficients;
        private PrimeField field;

        public PolynomialBuilder() {
            coefficients = new HashMap<>();
        }

        public PolynomialBuilder addTerm(Integer degree, PrimeFieldElement coefficient) {
            coefficients.put(degree, coefficient);
            return this;
        }

        public PolynomialBuilder setField(PrimeField field) {
            this.field = field;
            return this;
        }

        public Polynomial build() {
            Optional<Integer> polynomialDegree = this.coefficients.keySet().stream().max(Comparator.comparingInt(a -> a));
            if (polynomialDegree.isEmpty()) {
                if (this.field == null)
                    throw new ConstructionException("Coefficients must be size > 0 or put the field");
                return new Polynomial(this.field);
            }
            this.field = coefficients.get(polynomialDegree.get()).getGroup();
            PrimeFieldElement[] elements = new PrimeFieldElement[polynomialDegree.get() + 1];
            Arrays.fill(elements, field.getAdditiveIdentity());
            for (Map.Entry<Integer, PrimeFieldElement> entry : this.coefficients.entrySet()) {
                elements[entry.getKey()] = entry.getValue();
            }
            return new Polynomial(new ArrayList<>(Arrays.asList(elements)), field);
        }

    }


}
