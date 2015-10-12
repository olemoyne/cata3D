package model.math;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import tarpeia.com.model.TarpeiaField;
import tarpeia.com.model.TarpeiaObject;

/**
 * Gestion des nombre décimaux 
 * 
 * @author olemoyne
 *
 */
@TarpeiaObject
public class Decimal implements Comparable<Decimal>, Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7883021320037952384L;

	public static final Decimal ZERO = new Decimal("0");

	public static final Decimal UN = new Decimal("1");

	public static final Decimal DEUX = new Decimal("2");

	public static final Decimal TROIS = new Decimal("3");

	public static final Decimal QUATRE = new Decimal("4");

	public static final Decimal DIX = new Decimal("10");
	
	public static final Decimal CENT = new Decimal("100");

	public static final Decimal MILLE = new Decimal("1000");

	public static final MathContext ROUNDING_CTX = new MathContext(8, RoundingMode.HALF_UP);

	public static final Decimal DEMI = new Decimal("0.5");;

	/**
	 * montant à payer
	 */
	private BigDecimal dec;
	
	@TarpeiaField
	private String val;

	
	public Decimal() {
	}

	/**
	 * 
	 * @param d
	 */
	public Decimal(String d) {
		try {
			dec = new BigDecimal(d);
		} catch (NumberFormatException e) {
			System.err.println("Decimal illisible : "+d);
			throw e;
		}
		val = d;
	}

	/**
	 *  creation d'un Décimal
	 *  
	 * @param d
	 */
	public Decimal(BigDecimal d) {
		dec = d;
		val = d.toString();
	}

	
	/**
	 * Création d'un décimal de type long
	 * 
	 * @param x
	 */
	public Decimal(long x) {
		dec = new BigDecimal(x);
		val = Long.toString(x);
	}

	public Decimal(double x) {
		dec = new BigDecimal(x);
		val = Double.toString(x);
	}

	/**
	 * Comparaison de nombre décimaux
	 * 
	 * @param other
	 * @return
	 */
	public boolean equals(Decimal d) {
		if (dec == null) dec = new BigDecimal(val);
		if (d.dec == null) d.dec = new BigDecimal(d.val);
		return dec.round(MathContext.DECIMAL32).equals(d.dec.round(MathContext.DECIMAL32));
	}
	
	/**
	 * Division d'un décimal
	 * 
	 * @param d
	 * @return
	 */
	public Decimal divide(Decimal d) {
		if (dec == null) dec = new BigDecimal(val);
		if (d.dec == null) d.dec = new BigDecimal(d.val);
		try {
			return new Decimal (dec.divide(d.dec));
		} catch (java.lang.ArithmeticException e) {
//			System.out.println("Error dividing : "+this.toString()+" with "+d.toString());
			return new Decimal (dec.divide(d.dec, MathContext.DECIMAL32));
		}
	}

	
	/**
	 * Multiplication d'un décimal
	 * 
	 * @param d
	 * @return
	 */
	public Decimal multiply(Decimal d) {
		if (dec == null) dec = new BigDecimal(val);
		return new Decimal (dec.multiply(d.dec));
	}

	/**
	 * retourne la valeur sous forme de float
	 * 
	 * @return
	 */
	public float floatValue() {
		if (dec == null) dec = new BigDecimal(val);
		return dec.floatValue();
	}

	/**
	 * retourne la valeur sous forme de double
	 * 
	 * @return
	 */
	public double doubleValue() {
		if (dec == null) dec = new BigDecimal(val);
		return dec.doubleValue();
	}

	/**
	 * retourne la valeur négative
	 * 
	 * @return
	 */
	public Decimal negate() {
		if (dec == null) dec = new BigDecimal(val);
		return new Decimal(dec.negate());
	}

	/**
	 * Addition d'un décimal
	 * 
	 * @param d
	 * @return
	 */
	public Decimal add(Decimal d) {
		if (dec == null) dec = new BigDecimal(val);
		if (d.dec == null) d.dec = new BigDecimal(d.val);
		return new Decimal (dec.add(d.dec));
	}

	/**
	 * Soustraction d'un décimal
	 * 
	 * @param d
	 * @return
	 */
	public Decimal minus(Decimal d) {
		if (dec == null) dec = new BigDecimal(val);
		if (d.dec == null) d.dec = new BigDecimal(d.val);
		return new Decimal (dec.subtract(d.dec));
	}

	
	/**
	 * Calcul du carré
	 * @return
	 */
	public Decimal square() {
		if (dec == null) dec = new BigDecimal(val);
		return new Decimal (dec.pow(2));
	}

	/**
	 * Calcul de la racine carrée
	 * @return
	 */
	public Decimal racine() {
		if (dec == null) dec = new BigDecimal(val);
		return new Decimal ((long)Math.sqrt(dec.doubleValue()));
	}

	public boolean isZero() {
		if (dec == null) dec = new BigDecimal(val);
		return dec.abs().doubleValue() < 1d*Math.pow(10, -8); //dec.round(MathContext.DECIMAL32).equals(BigDecimal.ZERO);
	}

	public boolean isPositive() {
		if (dec == null) dec = new BigDecimal(val);
		return (dec.compareTo(BigDecimal.ZERO) > 0);
	}

	public boolean isNegative() {
		if (dec == null) dec = new BigDecimal(val);
		return (dec.compareTo(BigDecimal.ZERO) < 0);
	}

	public Decimal abs() {
		if (dec == null) dec = new BigDecimal(val);
		return new Decimal(dec.abs());
	}

	/**
	 * Retourne une chaine de caractères correspondant à la description du point
	 */
	public String toString() {
		if (dec == null) dec = new BigDecimal(val);
		return dec.round(MathContext.DECIMAL32).toString(); 
	}

	@Override
	public int compareTo(Decimal o) {
		if (dec == null) dec = new BigDecimal(val);
		if (o == null) return -1;
		if (o.dec == null) o.dec = new BigDecimal(o.val);
		return dec.round(MathContext.DECIMAL32).compareTo(o.dec.round(MathContext.DECIMAL32));
	}

	public Decimal inverse() {
		return Decimal.UN.divide(this);
	}

	/**
	 * Parse un décimal
	 * 
	 * @param d
	 * @return
	 */
	public static Decimal parse(String d) throws NumberFormatException{
		if (d.contains(",")) d = d.replace(',', '.');
		return new Decimal(d);
	}


	public BigDecimal toBigDecimal() {
		if (dec == null) dec = new BigDecimal(val);
		return dec;
	}

	public boolean isNan() {
		return dec.doubleValue() == Double.NaN;
	}

	public long longValue() {
		return dec.round(MathContext.DECIMAL32).longValue();
	}

	public Decimal multiply(long x) {
		if (dec == null) dec = new BigDecimal(val);
		BigDecimal d = new BigDecimal(x);
		return new Decimal(d.multiply(dec));
	}
	
	public static Decimal min(Decimal d1, Decimal d2) {
		if (d1 == null) return d2;
		if (d2 == null) return d1;
		
		if (d1.compareTo(d2) == -1) return d1;
		return d2;
	}

	public static Decimal max(Decimal d1, Decimal d2) {
		if (d1 == null) return d2;
		if (d2 == null) return d1;
		
		if (d1.compareTo(d2) == 1) return d1;
		return d2;
	}

}
