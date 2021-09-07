/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cat.udl.cig.utils;

import java.math.BigInteger;
import java.util.*;
import java.util.AbstractMap.SimpleEntry;
import java.util.stream.Collectors;

import cat.udl.cig.exceptions.IncorrectRingElementException;
import cat.udl.cig.structures.ExtensionField;
import cat.udl.cig.structures.PrimeField;
import cat.udl.cig.structures.PrimeFieldElement;

/**
 * Models a polynomial which coefficients are <i>PrimeFieldElement</i>.
 *
 * @see PrimeFieldElement
 * @author M.Àngels Cerveró
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

    /**
     * Creates an empty <i>Polynomial</i>. That is
     * {@code this.coefficients.isEmpty() = true} and {@code this.degree = -1}.
     */
    public Polynomial() {
        coefficients = new ArrayList<PrimeFieldElement>();
        degree = -1;
    }

    /**
     * Creates a new <i>Polynomial</i> with the specified {@code coefficients}.
     * If {@code coefficients.isEmpty() == true}, then, the new
     * <i>Polynomial</i> is keeped empty (
     * {@code this.coefficients.isEmpty() = true} and {@code this.degree = -1}).
     *
     * @param coefficients
     *            an ArrayList that contains the coefficients for {@code this}
     *            new <i>Polynomial</i>.
     * @see PrimeFieldElement
     */
    public Polynomial(final ArrayList<PrimeFieldElement> coefficients) {
        if (coefficients.isEmpty()) {
            this.coefficients = new ArrayList<PrimeFieldElement>();
            degree = -1;
        } else {
            this.coefficients =
                    new ArrayList<PrimeFieldElement>(coefficients);
            degree = this.coefficients.size() - 1;
            checkDegree();
        }
    }

    /**
     * Creates a copy of the <i>Polynomial</i> {@code poly}. This constructor
     * makes a deep copy of {@code poly}.
     *
     * @param poly
     *            the <i>Polynomial</i> to be copied.
     */
    public Polynomial(final Polynomial poly) {
        if (poly.degree >= 0) {
            coefficients =
                    new ArrayList<PrimeFieldElement>(poly.coefficients);
            degree = poly.degree;
        }
    }

    /**
     * Returns the degree of {@code this} <i>Polynomial</i>.
     *
     * @return an int representing the degree of {@code this} <i>Polynomial</i>,
     *         {@code this.degree}.
     */
    public int getDegree() {
        return degree;
    }

    /**
     * Returns the i-th coefficient of {@code this} <i>Polynomial</i>.
     *
     * @param termDegree
     *            the index of the coefficient.
     * @return a <i>PrimeFieldElement</i>, the i-th coefficient of {@code this}
     *         <i>Polynomial</i>, {@code this.coefficients.get(i)}.
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
     * @param q
     *            the <i>Polynomial</i> we want to add to {@code this}.
     * @return a new <i>Polynomial</i> \(r\), where \(r = p + q\).
     */
    public Polynomial add(final Polynomial q) {
        Polynomial p1, p2;
        Polynomial r = new Polynomial();
        if (degree < q.degree) {
            p1 = this;
            p2 = q;
        } else {
            p1 = q;
            p2 = this;
        }

        try {
            for (int i = 0; i <= p1.degree; i++) {
                r.coefficients.add(p1.coefficients.get(i).add(
                    p2.coefficients.get(i)));
            }

            for (int i = p1.degree + 1; i <= p2.degree; i++) {
                r.coefficients.add(new PrimeFieldElement(p2.coefficients
                    .get(i)));
            }

            r.degree = p2.degree;
            r.checkDegree();
        } catch (IncorrectRingElementException e) {
        }

        return r;
    }

    /**
     * Computes the operation \(p - q\), where {@code p} is the {@code this}
     * <i>Polynomial</i>.
     *
     * @param q
     *            the <i>Polynomial</i> we want to subtract to {@code this}.
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
        Polynomial r = new Polynomial();

        for (int i = 0; i <= degree; i++) {
            r.coefficients.add(coefficients.get(i).opposite());
        }

        r.degree = degree;

        return r;
    }

    /**
     * Computes the operation \(p \cdot q (\mod modulus)\), where {@code p} is
     * {@code this} <i>Polynomial</i>. If \(q\) and \(modulus\) do not belong to
     * the same <i>Ring</i> as \(p\), then, this method returns an empty
     * <i>Polynomial</i>.
     *
     * @param q
     *            the <i>Polynomial</i> we want to multiply {@code this}.
     * @param modulus
     *            the reducing polynomial (modulus) of the multiplication.
     * @return a new <i>Polynomial</i> \(r\), where \(r = p \cdot q (\mod
     *         modulus)\).
     */
    public Polynomial multiply(final Polynomial q, final Polynomial modulus) {
        if (!belongToSameBaseRing(q) || !belongToSameBaseRing(modulus)) {
            return new Polynomial();
        }
        return euclideanMultiplication(q).euclideanDivision(modulus)
                .getValue();
    }

    /**
     * Computes the operation \(p / q (\mod modulus)\), where {@code p} is
     * {@code this} <i>Polynomial</i>. If \(q\) and \(modulus\) do not belong to
     * the same <i>Ring</i> as \(p\), then, this method returns an empty
     * <i>Polynomial</i>.
     *
     * @param q
     *            the <i>Polynomial</i> we want to divide {@code this}.
     * @param modulus
     *            the reducing polynomial (modulus) of the division.
     * @return a new <i>Polynomial</i> \(r\), where \(r = p / q (\mod
     *         modulus)\).
     */
    public Polynomial divide(final Polynomial q, final Polynomial modulus) {
        if (!belongToSameBaseRing(q) || !belongToSameBaseRing(modulus)) {
            return new Polynomial();
        }
        return multiply(q.inverse(modulus), modulus);
    }

    /**
     * Returns \(1 / p (\mod modulus)\), where {@code p} is {@code this}
     * <i>Polynomial</i>. If \(modulus\) do not belong to the same <i>Ring</i>
     * as \(p\), then, this method returns an empty <i>Polynomial</i>.
     *
     * @param modulus
     *            the reducing polynomial (modulus) of the inversion.
     * @return a new <i>Polynomial</i> \(r\), where \(r = 1 / p (\mod
     *         modulus)\).
     */
    /*
     * function inverse(a, p) t := 0; newt := 1; r := p; newr := a; while newr ≠
     * 0 quotient := r div newr (r, newr) := (newr, r - quotient * newr) (t,
     * newt) := (newt, t - quotient * newt) if degree(r) > 0 then return
     * "Either p is not irreducible or a is a multiple of p" return (1/r) * t
     */
    public Polynomial inverse(final Polynomial modulus) {
        if (!belongToSameBaseRing(modulus)) {
            return new Polynomial();
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
        SimpleEntry<Polynomial, Polynomial> divResult;
        while (newr.degree != -1 && !newr.isZeroPolynomial()) {
            divResult = r.euclideanDivision(newr);
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
        return new Polynomial();
    }

    /**
     * Computes the operation \(p^{k} (\mod modulus)\), where {@code p} is
     * {@code this} <i>Polynomial</i>.
     *
     * @param k
     *            a BigInteger representing the exponent we want to apply to
     *            {@code this}.
     * @param modulus
     *            the reducing polynomial (modulus) of this method.
     * @return a new <i>Polynomial</i> \(r\), where \(r = p^{k} (\mod
     *         modulus)\).
     */
    public Polynomial pow(BigInteger k, final Polynomial modulus) {
        // k es pot reduir mòdul (p ^ n - 1)
        BigInteger p = coefficients.get(0).getGroup().getSize();
        k = k.mod(p.pow(modulus.degree).subtract(BigInteger.ONE));

        ArrayList<PrimeFieldElement> rcoefficients =
                new ArrayList<PrimeFieldElement>();
        rcoefficients.add(new PrimeFieldElement(coefficients.get(0)
            .getGroup(), BigInteger.ONE));
        Polynomial r = new Polynomial(rcoefficients);
        Polynomial tmp = this;

        String binary = k.toString(2);
        int nbits = binary.length();
        for (int i = nbits - 1; i >= 0; i--) {
            if (binary.charAt(i) == '1') {
                r = r.multiply(tmp, modulus);
            }
            tmp = tmp.multiply(tmp, modulus);
        }
        r.checkDegree();
        return r;
    }

    /**
     * Computes the operation \(\sqrt{p} (\mod modulus)\), where {@code p} is
     * {@code this} <i>Polynomial</i>.
     *
     * @param modulus
     *            the reducing polynomial (modulus) of this method.
     * @return a <i>Polynomial</i> \(r\), where \(r = \sqrt{p}\) and
     *         {@code r.getGroup() = this.getGroup()}, or an empty
     *         <i>Polynomial</i> if {@code this} is not a quadratic residue.
     */
    public Polynomial squareRoot(final Polynomial modulus) {
        // TODO: in need to be checkd. The computations are too slow!
        Polynomial root = new Polynomial();
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
                    new ArrayList<PrimeFieldElement>();
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
            root = pow(tmpE, modulus).multiply(h, modulus);
            root.checkDegree();
        }
        return root;
    }

    /**
     * Auxiliar method to check if {@code this} <i>Polynomial</i> is a quadratic
     * residue, that is, if it exists \(\sqrt(p)\), where \(p\) is {@code this}
     * <i>Polynomial</i>.
     *
     * @param modulus
     *            the reducing polynomial (modulus) of this method.
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
     * @param q
     *            the <i>Polynomial</i> we want to multiply {@code this}.
     * @return a new <i>Polynomial</i> \(r\), where \(r = p \cdot q\).
     */
    public Polynomial euclideanMultiplication(final Polynomial q) {
        Polynomial r;
        try {
            r = new Polynomial();

            r.degree = degree + q.degree;
            for (int i = 0; i <= r.degree; i++) {
                r.coefficients.add(new PrimeFieldElement(coefficients.get(
                    0).getGroup(), BigInteger.ZERO));
            }

            // Multiply polynomials
            for (int i = 0; i <= degree; i++) {
                for (int j = 0; j <= q.degree; j++) {
                    PrimeFieldElement mult =
                            coefficients.get(i)
                            .multiply(q.coefficients.get(j));
                    r.coefficients.set(i + j, r.coefficients.get(i + j)
                        .add(mult));
                }
            }
            r.checkDegree();
        } catch (IncorrectRingElementException e) {
            r = new Polynomial();
        }

        return r;
    }

    /**
     * Auxiliar method to compute the Euclidean Division \(p / q\), where
     * {@code p} is {@code this} <i>Polynomial</i>.
     *
     * @param q
     *            the <i>Polynomial</i> we want to multiply {@code this}.
     * @return a new <i>Polynomial</i> \(r\), where \(r = p \cdot q\).
     */
    public SimpleEntry<Polynomial, Polynomial> euclideanDivision(
        final Polynomial q) {
        Polynomial quotient = new Polynomial();
        Polynomial remainder = new Polynomial();
        SimpleEntry<Polynomial, Polynomial> result;

        if ((degree > -1 && q.degree > -1 && !coefficients.get(0)
                .getGroup().equals(q.coefficients.get(0).getGroup()))
                || degree < 0 || q.degree < 0) {
            result =
                    new SimpleEntry<Polynomial, Polynomial>(new Polynomial(),
                            new Polynomial());
            return result;
        }

        if (q.degree > degree) {
            remainder.degree = degree;
            for (int i = 0; i <= degree; i++) {
                remainder.coefficients =
                        new ArrayList<PrimeFieldElement>(coefficients);
            }
            quotient.degree = 0;
            quotient.coefficients.add(new PrimeFieldElement(coefficients
                .get(0).getGroup(), BigInteger.ZERO));
            result =
                    new SimpleEntry<Polynomial, Polynomial>(quotient,
                            remainder);
        } else {
            remainder = this;
            quotient.degree = degree - q.degree;
            for (int i = 0; i <= quotient.degree; i++) {
                quotient.coefficients.add(new PrimeFieldElement(
                    coefficients.get(0).getGroup(), BigInteger.ZERO));
            }

            try {
                Polynomial curQuotient = new Polynomial();
                while (remainder.degree >= q.degree
                        && !remainder.isZeroPolynomial()) {
                    curQuotient.degree = remainder.degree - q.degree;
                    curQuotient.coefficients.clear();
                    for (int i = 0; i <= curQuotient.degree; i++) {
                        curQuotient.coefficients
                        .add(new PrimeFieldElement(coefficients.get(0)
                            .getGroup(), BigInteger.ZERO));
                    }
                    quotient.coefficients.set(curQuotient.degree,
                        remainder.coefficients.get(remainder.degree)
                        .divide(q.coefficients.get(q.degree)));
                    curQuotient.coefficients.set(curQuotient.degree,
                        quotient.coefficients.get(curQuotient.degree));

                    remainder =
                            remainder.subtract(q
                                .euclideanMultiplication(curQuotient));
                }

                quotient.checkDegree();
                remainder.checkDegree();
                result =
                        new SimpleEntry<Polynomial, Polynomial>(quotient,
                                remainder);
            } catch (IncorrectRingElementException e) {
                result =
                        new SimpleEntry<Polynomial, Polynomial>(
                                new Polynomial(), new Polynomial());
            }
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Polynomial that = (Polynomial) o;
        return degree == that.degree &&
                Objects.equals(coefficients, that.coefficients);
    }

    @Override
    public int hashCode() {
        return Objects.hash(coefficients, degree);
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
     * @param q
     *            the <i>Polynomial</i> we want to check.
     * @return {@code true} if \(p\) and \(q\) belong to the same <i>Ring</i>
     *         and{@code false} otherwise.
     */
    private boolean belongToSameBaseRing(final Polynomial q) {
        if (degree == -1 || q.degree == -1) {
            return false;
        } else {
            return coefficients.get(0).getGroup().getSize()
                    .equals(q.coefficients.get(0).getGroup().getSize());
        }
    }

    public static class PolynomialBuilder {

        private final HashMap<Integer, PrimeFieldElement> coefficients;

        public PolynomialBuilder() {
            coefficients = new HashMap<>();
        }

        public PolynomialBuilder addTerm(Integer degree, PrimeFieldElement coefficient) {
            coefficients.put(degree, coefficient);
            return this;
        }

        public Polynomial build() {
            Optional<Integer> polynomialDegree = this.coefficients.keySet().stream().max((a, b) -> a - b);
            if (polynomialDegree.isEmpty())
                return new Polynomial();
            PrimeField field = coefficients.get(polynomialDegree.get()).getGroup();
            PrimeFieldElement[] elements = new PrimeFieldElement[polynomialDegree.get() + 1];
            Arrays.fill(elements, field.getAdditiveIdentity());
            for (Map.Entry<Integer, PrimeFieldElement> entry : this.coefficients.entrySet()) {
                elements[entry.getKey()] = entry.getValue();
            }
            return new Polynomial(new ArrayList<>(Arrays.asList(elements)));
        }

    }
}
