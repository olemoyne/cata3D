package model.math;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.io.Serializable;


/**
 * Classe de gestion des coordonnées dans l'espace
 * Les coordonnées sont exprimées en microns
 * 
 * @author olemoyne
 *
 */
public class Vecteur implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 237903479162147361L;
	
	public static final Decimal METER = new Decimal("10000");
	public static final double LMETER = 10000d;

	
	
	/** Coordonnées en microns - int - 4km **/
	private long x;
	private long y;
	private long z;

	public long getX() { return x;}
	public long getY() { return y;}
	public long getZ() { return z;}

	public Decimal getDecX() { return new Decimal(x).divide(METER);}
	public Decimal getDecY() { return new Decimal(y).divide(METER);}
	public Decimal getDecZ() { return new Decimal(z).divide(METER);}
	
	public float[] getFloats() {
		float[] flts = new float[3];
		flts[0] = new Decimal(x).divide(METER).floatValue();
		flts[1] = new Decimal(y).divide(METER).floatValue();
		flts[2] = new Decimal(z).divide(METER).floatValue();
		return flts;
	}

	public Vecteur() {
		x = 0;
		y  = 0;
		z = 0;
	}

	public boolean isZero () {
		return (x + y + z == 0);
	}
	
	/**
	 *  Creation d'un vecteur en donnant des données 
	 * @param x2
	 * @param y2
	 * @param z2
	 */
	public Vecteur(Decimal x2, Decimal y2, Decimal z2) {
		x = x2.multiply(METER).longValue();
		y  = y2.multiply(METER).longValue();
		z = z2.multiply(METER).longValue();
	}

	/**
	 *  Creation d'un vecteur en donnant des données 
	 * @param x2
	 * @param y2
	 * @param z2
	 */
	public Vecteur(long x2, long y2, long z2) {
		x = x2;
		y  = y2;
		z = z2;
	}

	public Vecteur(String str) {
		if (str.startsWith("(")) {
			str = str.substring(1);
		}
		if (str.endsWith(")")) {
			str = str.substring(0, str.length()-1);
		}
		String[] strs = str.split(";");
		if (strs.length < 3) throw new NumberFormatException("Vecteur illisible "+str);
		x = new Decimal(strs[0]).multiply(METER).longValue();
		y  = new Decimal(strs[1]).multiply(METER).longValue();
		z = new Decimal(strs[2]).multiply(METER).longValue();
	}

	public Vecteur(Vecteur o) {
		x = o.x;
		y = o.y;
		z = o.z;
	}
	/**
	 * Calcule de la distance entre deux points
	 * Distance rtetournée en MICRONS
	 */
	public Decimal distance (Vecteur v) {
		if (v == null) return Decimal.ZERO;
		
		// Vérification des valeurs des vecteurs 
//		if ((Long.isNaN(v1.x))||(Float.isNaN(v1.y))||(Float.isNaN(v1.z))) return 0;
//		if ((Float.isNaN(v2.x))||(Float.isNaN(v2.y))||(Float.isNaN(v2.z))) return 0;
		
		long m =  v.x-x;
		long res = m*m;

		m =  v.y -y;
		res += m*m;

		m =  v.z-z;
		res += m*m;

		Decimal dec = new Decimal(res);
		return dec.racine();
	}

	/**
	 * Calcule de la distance entre deux points
	 * Distance rtetournée en MICRONS
	 */
	public Decimal decDistance (Vecteur v) {
		if (v == null) return Decimal.ZERO;
		
		// Vérification des valeurs des vecteurs 
//		if ((Long.isNaN(v1.x))||(Float.isNaN(v1.y))||(Float.isNaN(v1.z))) return 0;
//		if ((Float.isNaN(v2.x))||(Float.isNaN(v2.y))||(Float.isNaN(v2.z))) return 0;
		
		long m =  v.x-x;
		long res = m*m;

		m =  v.y -y;
		res += m*m;

		m =  v.z-z;
		res += m*m;

		Decimal dec = new Decimal(res);
		return dec.racine().divide(Vecteur.METER);
	}

	/**
	 *  Distance avec une droite 
	 */
	public Decimal distance (Vecteur a, Vecteur b) {
		if ((a.getX() == b.getX()) && (a.getY() == b.getY())) {
			return this.distance(a);
		}
		
	    long Dx  = b.getX()-a.getX();
	    long Dy  = b.getY()-a.getY();
	    
	    // ((pt.x - a.x)*Dx + (pt.y - a.y)*Dy)/ (Dx * Dx + Dy * Dy) 
	    long num1 = (x - a.getX())*Dx;
	    long num2 = (y - a.getY())*Dy;
	    Decimal div = new Decimal(Dx*Dx+Dy*Dy);
	    
	    Decimal num = new Decimal(num1 + num2);
	    Decimal ratio = num.divide(div);
	    
	    if ( ratio.doubleValue() < 0)  return distance(a);
	    if ( ratio.doubleValue() > 1) return distance(b);
	    
	    Decimal rat = Decimal.UN.minus(ratio);
	    Vecteur v = a.add(b).multiply(rat); 
	    
	    return distance(v);
	}

	
	/**
	 * Validation de la similitude de position
	 * 
	 * @param other
	 * @return
	 */
	public boolean equals(Object other) {
		if (other.getClass() == Vecteur.class) {
			Vecteur v = (Vecteur)other;
			if (x!=v.x) return false;
			if (y!=v.y) return false;
			if (z!=v.z) return false;
			return true;
		}
		return false;
	}
	
	/**
	 * Application d'un ratio
	 * 
	 * @param ratio
	 * @return
	 */
	public Vecteur multiply(Decimal ratio) {
		return new Vecteur (ratio.multiply(x).longValue(), ratio.multiply(y).longValue(), ratio.multiply(z).longValue());
	}
	/**
	 * Ajoute deux Vecteurs
	 *  
	 * @param b
	 * @return
	 */
	public Vecteur add(Vecteur b) {
		return new Vecteur (x+b.x, y+b.y, z+b.z);
	}
	
	/**
	 * Calcul d'un surface d'un triangle
	 * @param centre
	 * @param a
	 * @param a2
	 * @return
	 */
	public static Decimal calculeSurface(Vecteur A, Vecteur B, Vecteur C) {
		Decimal surf = Decimal.ZERO;
		// Check des points non-nulls
		if (A == null) return surf;
		if (B == null) return surf;
		if (C == null) return surf;
		
		// Check des points identiques
		if (A.equals(B)) return surf;
		if (B.equals(C)) return surf;
		if (C.equals(A)) return surf;

		Decimal a = A.distance(B); // distance en microns
		Decimal b = B.distance(C);
		Decimal c = C.distance(A);
		
		Decimal p = a.add(b).add(c);
		Decimal deux = new Decimal("2");
		
		//s = p*(p-a)*(p-b)*(p-c);
		Decimal s = p.multiply(p.minus(a.multiply(deux))).multiply(p.minus(b.multiply(deux))).multiply(p.minus(c.multiply(deux))).divide(new Decimal("16"));

		if (s.doubleValue() < 0d)  {
			if (s.doubleValue() > 0.000001d) System.err.println("Erreur d'approximation : "+s);
			else return new Decimal("0");
		}

		return s.racine().divide(Vecteur.METER).divide(Vecteur.METER);
	}
	
	
	/**
	 * Vecteur moins un autre
	 * 
	 * @param o
	 * @return
	 */
	public Vecteur minus(Vecteur o) {
		return new Vecteur (x-o.x, y-o.y, z-o.z);
	}

	public Vecteur produitVectoriel(Vecteur autre) {
		Vecteur u = this;
		Vecteur v = autre;
//		A = u2 * v3 - u3 * v2;
		long X = (u.getY()*v.getZ())-(u.getZ()*v.getY());
//		B = u3 * v1 - u1 * v3;
		long Y = (u.getZ()*v.getX())-(u.getX()*v.getZ());
//		C = u1 * v2 - u2 * v1;
		long Z = (u.getX()*v.getY())-(u.getY()*v.getX());
		
		// Division du produit vectoriel pour retomber sur les bonnes métriques 

		return new Vecteur (X, Y, Z);//.multiply(METER.inverse());
	}
	
	public Decimal getProduitScalaire(Vecteur autre) {
		Vecteur u = this;
		Vecteur v = autre;
		
		double prod = (u.x/LMETER)*(v.x/LMETER);
		prod += (u.y/LMETER)*(v.y/LMETER); 
		prod += (u.z/LMETER)*(v.z/LMETER); 

		return new Decimal(prod);
	}

	
	/**
	 * Retourne une chaine de caractères correspondant à la description du point
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder("("); 
		sb.append(new Decimal(x).divide(METER)); 
		sb.append(";"); 
		sb.append(new Decimal(y).divide(METER)); 
		sb.append(";"); 
		sb.append(new Decimal(z).divide(METER)); 
		sb.append(")");
		return sb.toString();
	}
	
	/**
	 * Teste si le vecteur passé en parametre est colinéaire
	 * vérifier X1/X2 =  Y1/Y2 = Z1/Z2
	 * 
	 * @param vecteur à tester 
	 * @return
	 */
	public boolean estColineaire(Vecteur o) {
		if (o == null) return false;
		long k = this.getX()*o.getY();
		long l = this.getY()*o.getX();
		if (k - l != 0) return false;
		k = getX()*o.getZ();
		l = getZ()*o.getX();
		if (k - l != 0) return false;
		k = getY()*o.getZ();
		l = getZ()*o.getY();
		if (k - l != 0) return false;
		return true;
	}
	
	

	/**
	 * Calcule la norme du vecteur 
	 *  
	 * @return
	 */
	public Decimal getNorme() {	
		Decimal dec = new Decimal(x).square().add(new Decimal(y).square()).add(new Decimal(z).square());
		return dec.racine().divide(METER);
	}
	
	
	/** 
	 * Retourne un vecteur opposé à celui-ci 
	 * 
	 * @return
	 */
	public Vecteur negat() {
		return new Vecteur(-x, -y, -z);
	}

	public Vecteur normalize () {
		Decimal n = this.getNorme().inverse();
		return this.multiply(n);
	}

	
	public static Vecteur getNormale (Vecteur v1, Vecteur v2) {
		Vecteur n = v1.produitVectoriel(v2);
		return n.normalize();
	}
	
	public static void main (String[] args) {
		Vecteur a = new Vecteur ("0;0;0");
		Vecteur b = new Vecteur ("2;0;0");
		Vecteur c = new Vecteur ("2;2;0");

		// Calcul de la normale de deux vecteurs
		Vecteur n = getNormale(a.minus(b), c.minus(b));
		System.out.println("Norme = "+n.toString());
	}
	
	
	public Vecteur set(int axis, long pos) {		
		if (axis == Axis.XAxis) return new Vecteur (pos, y, z);
		if (axis == Axis.YAxis) return new Vecteur (x, pos, z);
		if (axis == Axis.ZAxis) return new Vecteur (x, y, pos);
		return null;
	}
	
	public Vecteur setDec(int axis, Decimal v) {
		if (axis == Axis.XAxis) return new Vecteur (Math.round(v.doubleValue()*Vecteur.LMETER), y, z);
		if (axis == Axis.YAxis) return new Vecteur (x, Math.round(v.doubleValue()*Vecteur.LMETER), z);
		if (axis == Axis.ZAxis) return new Vecteur (x, y, Math.round(v.doubleValue()*Vecteur.LMETER));
		return null;
	}

	
	
	public long get(int axis) {
		if (axis == Axis.XAxis) return x;
		if (axis == Axis.YAxis) return y;
		if (axis == Axis.ZAxis) return z;
		return -1;
	}

	public Decimal getDec(int axis) {
		if (axis == Axis.XAxis) return new Decimal(x).divide(METER);
		if (axis == Axis.YAxis) return new Decimal(y).divide(METER);
		if (axis == Axis.ZAxis) return new Decimal(z).divide(METER);
		return null;
	}

	/**
	 * Calcule le centre de trois points
	 *  
	 * @param a
	 * @param b
	 * @param c
	 * @return
	 */
	public static Vecteur getCentre(Vecteur a, Vecteur b, Vecteur c) {
		Vecteur somme = a.add(b).add(c);
		Decimal tiers = new Decimal(1d/3d);
		return somme.multiply(tiers);
	}
	
	// Retourne les un point 2D selon l'axe choisi
	public Double get2D(int ax) {
		if (ax == Axis.XAxis) return new Point2D.Double(this.getDecY().floatValue(), this.getDecZ().floatValue());
		if (ax == Axis.YAxis) return new Point2D.Double(this.getDecX().floatValue(), this.getDecZ().floatValue());
		if (ax == Axis.ZAxis) return new Point2D.Double(this.getDecX().floatValue(), this.getDecY().floatValue());
		return null;
	}
	
}
