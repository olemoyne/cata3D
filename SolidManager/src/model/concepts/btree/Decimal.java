package model.concepts.btree;

import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import tarpeia.com.model.TarpeiaField;
import tarpeia.com.model.TarpeiaObject;

/**
 * Gestion des nombre d�cimaux 
 * 
 * @author olemoyne
 *
 */
@TarpeiaObject
public class Decimal implements Comparable<Decimal>{
	
	public static final long MAX_VALUE = 1000000000;
	public static final long MIN_VALUE = -1000000000;
	
	public static final DecimalFormat FORMAT = new DecimalFormat("#######0.000000");
	
	public static final int[] ENTIERS = {2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61};
	
	
	public static final Decimal ZERO = new Decimal("0");

	public static final Decimal UN = new Decimal("1");

	public static final Decimal DEUX = new Decimal("2");

	public static final Decimal TROIS = new Decimal("3");

	public static final Decimal QUATRE = new Decimal("4");

	public static final Decimal DIX = new Decimal("10");
	
	public static final Decimal CENT = new Decimal("100");

	public static final Decimal MILLE = new Decimal("1000");

	public static final MathContext ROUNDING_CTX = new MathContext(8, RoundingMode.HALF_UP);

	/**
	 * valeur - on tente en long
	 */
	private long numerateur;
	private long diviseur;
	
	@TarpeiaField
	private String val;

	
	public Decimal() {
	}

	/**
	 * 
	 * @param d
	 */
	public Decimal(String d) {
		val = d;
		try {
			// Transforme le double en numerateur et diviseur
			int pos = d.indexOf('/');
			if (pos != -1) {
				numerateur = Long.parseLong(d.substring(1, pos));
				diviseur = Long.parseLong(d.substring(pos+1));	
				return;
			}
			pos = d.indexOf('.');
			if (pos == -1) { // Pas de diviseur
				numerateur = Long.parseLong(d);
				if (numerateur > Decimal.MAX_VALUE) System.err.println("Decimal au del� la limite : "+d);
				if (numerateur < Decimal.MIN_VALUE) System.err.println("Decimal au del� la limite : "+d);


				diviseur = 1l;
			} else {
				int len = d.length()-1;
				int nb = len - pos; // taille des caract�res d�cimaux
				if (nb > 18) throw new NumberFormatException("Precision exceeded Decimal format");
				diviseur = (long)Math.pow(10d, nb);
				StringBuilder str =  new StringBuilder();
				str.append(d.substring(1, pos));
				str.append(d.substring(pos +1));
				numerateur = Long.parseLong(str.toString());
			}
		} catch (NumberFormatException e) {
			System.err.println("Decimal illisible : "+d);
			throw e;
		}
	}

	/**
	 *  creation d'un D�cimal � partir d'un double
	 *  
	 * @param d
	 */
	public Decimal(double d) {
		if (d > Decimal.MAX_VALUE) System.err.println("Decimal au del� la limite : "+d);
		if (d < Decimal.MIN_VALUE) System.err.println("Decimal au del� la limite : "+d);
		
		// Determine le nombre de d�cimales
		int nbDeco = -1;
		for (int p = 0; p < 9; p++) {
			double num = Math.pow(10d, p);
			if (d%num == 0) nbDeco = p;
		}
		
		if (nbDeco == -1) nbDeco = 9;
		
		this.diviseur = (long)Math.pow(10d, nbDeco);
		this.numerateur = Math.round(d*diviseur); 
		
		StringBuilder sb = new StringBuilder ();
		sb.append(numerateur);
		sb.append('/');
		sb.append(diviseur);
		val = sb.toString();
	}

	
	/**
	 * Cr�ation d'un d�cimal de type long
	 * 
	 * @param x
	 */
	public Decimal(long d) {
		numerateur = d;
		diviseur = 1;
		val = Long.toString(d);
	}

	/**
	 * Cr�ation d'un d�cimal de type long
	 * 
	 * @param x
	 */
	public Decimal(long n, long d) {
		
		// Recherche � diviser les �l�ments pour optimiser 
		numerateur = n;
		diviseur = d;

		if (numerateur == 0) diviseur = 1;
		
//		if (diviseur > 100000)
			while (optimize());

		StringBuilder sb = new StringBuilder ();
		sb.append(numerateur);
		sb.append('/');
		sb.append(diviseur);
		val = sb.toString();
	}

	
	private boolean optimize() {
		boolean ok = false;
		for (int ent : ENTIERS) {
			if ((numerateur%ent == 0) && (diviseur%ent == 0)) {
				ok = true;
				numerateur = numerateur / ent;
				diviseur = diviseur / ent;
			}
		}
		return ok;
	}

	/**
	 * Comparaison de nombre d�cimaux
	 * 
	 * @param other
	 * @return
	 */
	public boolean equals(Decimal d) {
		return numerateur*d.diviseur == d.numerateur*diviseur;
	}
	
	/**
	 * Division d'un d�cimal
	 * 
	 * @param d
	 * @return
	 */
	public Decimal divide(Decimal d) {
		if (numerateur == 0) return new Decimal(0, 1);

		try {
			String strMoi = Long.toString(numerateur);
			String strLui = Long.toString(d.diviseur);
			if (strMoi.length()+strLui.length() > 18) throw new NumberFormatException("Precision exceeded Decimal format "+this.toDecString()+" "+d.toDecString());

			strMoi = Long.toString(diviseur);
			strLui = Long.toString(d.numerateur);
			if (strMoi.length()+strLui.length() > 18) throw new NumberFormatException("Precision exceeded Decimal format "+this.toDecString()+" "+d.toDecString());

			return new Decimal (numerateur*d.diviseur, diviseur*d.numerateur);
		} catch (java.lang.ArithmeticException e) {
			System.out.println("Error dividing : "+this.toString()+" with "+d.toString());
			return Decimal.ZERO;
		}
	}

	
	/**
	 * Multiplication d'un d�cimal
	 * 
	 * @param d
	 * @return
	 */
	public Decimal multiply(Decimal d) {
		if (d.numerateur == 0) return new Decimal(0, 1);
		if (this.numerateur == 0) return new Decimal(0, 1);
		
		String strMoi = Long.toString(numerateur);
		String strLui = Long.toString(d.diviseur);
		if (strMoi.length()+strLui.length() > 18) throw new NumberFormatException("Precision exceeded Decimal format "+this.toDecString()+" "+d.toDecString());

		strMoi = Long.toString(diviseur);
		strLui = Long.toString(d.numerateur);
		if (strMoi.length()+strLui.length() > 18) throw new NumberFormatException("Precision exceeded Decimal format "+this.toDecString()+" "+d.toDecString());

		return new Decimal (numerateur*d.numerateur, diviseur*d.diviseur);
	}

	/**
	 * retourne la valeur sous forme de float
	 * 
	 * @return
	 */
	public float floatValue() {
		return (float) numerateur/diviseur;
	}

	/**
	 * retourne la valeur sous forme de double
	 * 
	 * @return
	 */
	public double doubleValue() {
		return (double) numerateur/diviseur;
	}

	/**
	 * retourne la valeur n�gative
	 * 
	 * @return
	 */
	public Decimal negate() {
		return new Decimal(-1*numerateur, diviseur);
	}

	/**
	 * Addition d'un d�cimal
	 * 
	 * @param d
	 * @return
	 */
	public Decimal add(Decimal d) {
		if (d.diviseur == this.diviseur) {
			return new Decimal (numerateur + d.numerateur, diviseur);
		} else {
			if (d.numerateur == 0) return this;
			if (this.numerateur == 0) return d;

			String strMoi = Long.toString(numerateur);
			String strLui = Long.toString(d.diviseur);
			if (strMoi.length()+strLui.length() > 18) throw new NumberFormatException("Precision exceeded Decimal format "+this.toDecString()+" "+d.toDecString());

			strMoi = Long.toString(diviseur);
			strLui = Long.toString(d.numerateur);
			if (strMoi.length()+strLui.length() > 18) throw new NumberFormatException("Precision exceeded Decimal format "+this.toDecString()+" "+d.toDecString());

			strMoi = Long.toString(diviseur);
			strLui = Long.toString(d.diviseur);
			if (strMoi.length()+strLui.length() > 18) throw new NumberFormatException("Precision exceeded Decimal format "+this.toDecString()+" "+d.toDecString());

			return new Decimal (numerateur*d.diviseur + d.numerateur*diviseur, diviseur*d.diviseur);
		}
	}

	/**
	 * Soustraction d'un d�cimal
	 * 
	 * @param d
	 * @return
	 */
	public Decimal minus(Decimal d) {
		if (d.diviseur == this.diviseur) {
			return new Decimal (numerateur - d.numerateur, diviseur);
		} else {
			if (d.numerateur == 0) return this;
			if (this.numerateur == 0) return d;
			
			String strMoi = Long.toString(numerateur);
			String strLui = Long.toString(d.diviseur);
			if (strMoi.length()+strLui.length() > 18) throw new NumberFormatException("Precision exceeded Decimal format "+this.toDecString()+" "+d.toDecString());

			strMoi = Long.toString(diviseur);
			strLui = Long.toString(d.numerateur);
			if (strMoi.length()+strLui.length() > 18) throw new NumberFormatException("Precision exceeded Decimal format "+this.toDecString()+" "+d.toDecString());

			strMoi = Long.toString(diviseur);
			strLui = Long.toString(d.diviseur);
			if (strMoi.length()+strLui.length() > 18) throw new NumberFormatException("Precision exceeded Decimal format "+this.toDecString()+" "+d.toDecString());
			
			return new Decimal (numerateur*d.diviseur - d.numerateur*diviseur, diviseur*d.diviseur);
		}
	}

	
	/**
	 * Calcul du carr�
	 * @return
	 */
	public Decimal square() {
		return new Decimal(numerateur*numerateur + diviseur*diviseur);
	}

	/**
	 * Calcul de la racine carr�e
	 * @return
	 */
	public Decimal racine() {
		return new Decimal ((double)Math.sqrt(doubleValue()));
	}

	public boolean isZero() {
		if (numerateur == 0) return true;
		return Math.abs(doubleValue()) < 1d*Math.pow(10, -8); //dec.round(MathContext.DECIMAL32).equals(BigDecimal.ZERO);
	}

	public boolean isPositive() {
		return (((numerateur >= 0) && (diviseur >= 0))||((numerateur <= 0) && (diviseur <= 0)));
	}

	public boolean isNegative() {
		return (((numerateur > 0) && (diviseur < 0))||((numerateur < 0) && (diviseur > 0)));
	}

	public Decimal abs() {
		if (isPositive()) return this;
		else return this.negate();
	}

	/**
	 * Retourne une chaine de caract�res correspondant � la description du point
	 */
	public String toString() {
		return FORMAT.format(doubleValue());
	}

	/**
	 * Retourne une chaine de caract�res correspondant � la description du point
	 */
	public String toDecString() {
		StringBuilder sb = new StringBuilder ();
		sb.append(numerateur);
		sb.append('/');
		sb.append(diviseur);
		return sb.toString();
	}

	
	@Override
	public int compareTo(Decimal o) {
		if (numerateur*o.diviseur == o.numerateur*diviseur) return 0;
		if (diviseur == o.diviseur) {
			if (numerateur > o.numerateur) return 1;
			return -1;
		}
		if (doubleValue() > o.doubleValue()) return 1;
		return -1;
	}

	public Decimal inverse() {
		return Decimal.UN.divide(this);
	}

	/**
	 * Parse un d�cimal
	 * 
	 * @param d
	 * @return
	 */
	public static Decimal parse(String d) throws NumberFormatException{
		if (d.contains(",")) d = d.replace(',', '.');
		return new Decimal(d);
	}

	public long longValue() {
		return Math.round(doubleValue());
	}

	public static Decimal min(Decimal d1, Decimal d2) {
		if (d1 == null) return d2;
		if (d2 == null) return d1;
		
		if (d1.compareTo(d2) == -1) return d1;
		return d2;
	}

	/** Tente d'optimiser les chiffres **/
	public Decimal multiply(long x) {
		if (x == 0) return new Decimal (0, 1);
		if (diviseur%x == 0) return new Decimal(numerateur, diviseur/x);
		else return new Decimal(numerateur*x, diviseur);
	}

}
