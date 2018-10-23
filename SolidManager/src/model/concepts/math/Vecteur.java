package model.concepts.math;

import tarpeia.com.model.TarpeiaField;
import tarpeia.com.model.TarpeiaObject;


/**
 * Classe de gestion des coordonn�es dans l'espace
 * Les coordonn�es sont exprim�es en microns
 * 
 * @author olemoyne
 *
 */
@TarpeiaObject
public class Vecteur {
	
	/** Coordonnées en 10000 eme de metre (100 micron)  MAX = -922 337 203 685 477,5808 metres */
	public static final Decimal METER = new Decimal("10000");
	public static final double LMETER = 10000d;
	
	
	
	/** Coordonn�es en microns - int - 4km **/
	@TarpeiaField
	private long x;
	@TarpeiaField
	private long y;
	@TarpeiaField
	private long z;

	public long getX() { return x;}
	public long getY() { return y;}
	public long getZ() { return z;}

	public Decimal getDecX() { return new Decimal(x).divide(METER);}
	public Decimal getDecY() { return new Decimal(y).divide(METER);}
	public Decimal getDecZ() { return new Decimal(z).divide(METER);}

	public Vecteur() {
		x = 0;
		y  = 0;
		z = 0;
	}

	/**
	 *  Creation d'un vecteur en donnant des donn�es 
	 * @param x2
	 * @param y2
	 * @param z2
	 */
	public Vecteur(Decimal x2, Decimal y2, Decimal z2) {
		x = x2.multiply(METER).longVectorValue();
		y  = y2.multiply(METER).longVectorValue();
		z = z2.multiply(METER).longVectorValue();
	}

	/**
	 *  Creation d'un vecteur en donnant des donn�es 
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
		String[] strs = str.split(";");
		if (strs.length < 3) throw new NullPointerException("Vecteur illisible "+str);
		x = new Decimal(strs[0]).multiply(METER).longVectorValue();
		y  = new Decimal(strs[1]).multiply(METER).longVectorValue();
		z = new Decimal(strs[2]).multiply(METER).longVectorValue();
	}

	public Vecteur(Vecteur o) {
		x = o.x;
		y = o.y;
		z = o.z;
	}
	/**
	 * Calcule de la distance entre deux points
	 * Distance rtetourn�e en MICRONS
	 */
	public Decimal distance (Vecteur v) {
		if (v == null) return Decimal.ZERO;
		
		// V�rification des valeurs des vecteurs 
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
	 * Application d'un ratio de type d�cimal
	 * 
	 * @param ratio
	 * @return
	 */
	public Vecteur multiply(Decimal ratio) {
		return new Vecteur (ratio.multiply(x).longVectorValue(), ratio.multiply(y).longVectorValue(), ratio.multiply(z).longVectorValue());
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
		
		// Division du produit vectoriel pour retomber sur les bonnes m�triques 

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
	 * Retourne une chaine de caract�res correspondant � la description du point
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
	 * Teste si le vecteur pass� en parametre est colin�aire
	 * v�rifier X1/X2 =  Y1/Y2 = Z1/Z2
	 * 
	 * @param vecteur � tester 
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
	 * Retourne la coordonn�e sur un axe donn�
	 * @param axe
	 * @return
	 */
	public long get(int axe) {
		if (axe == Axis.XAxis) return x;
		if (axe == Axis.YAxis) return y;
		if (axe == Axis.ZAxis) return z;
		return -1;
	}
	
	
	public Vecteur set(int axe, long val) {
		if (axe == Axis.XAxis) return new Vecteur(val, y, z);
		if (axe == Axis.YAxis) return new Vecteur(x, val, z);
		if (axe == Axis.ZAxis) return new Vecteur(x, y, val);
		return null;
	}

	/** 
	 * Retourne un vecteur oppos� � celui-ci 
	 * 
	 * @return
	 */
	public Vecteur negat() {
		return new Vecteur(-x, -y, -z);
	}

	
	public static void main (String[] args) {
//		Vecteur a = new Vecteur ("0;0;0");
//		Vecteur b = new Vecteur ("1;0;0");
//		Vecteur c = new Vecteur ("1;1;0");
		
		Decimal dec = new Decimal("0.99999999");
		Vecteur e = new Vecteur (dec, dec, dec);
		System.out.println(e.toString());
		
		
//		Decimal surf = Vecteur.calculeSurface(a, b, c);
//		System.out.println("Surface = "+surf);
	}
	
}
