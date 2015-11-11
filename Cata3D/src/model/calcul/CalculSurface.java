package model.calcul;

import java.util.ArrayList;

import model.math.Decimal;
import model.math.Vecteur;

public class CalculSurface {
	
	public static Vecteur getCentreGeometrique(Vecteur[] lst) {
		Vecteur c = new Vecteur();
		for (Vecteur v : lst) {
			c= c.add(v);
		}
		return c.multiply(Decimal.UN.divide(new Decimal(lst.length)));
	}

	public static Vecteur getCentreGeometrique(ArrayList<Vecteur> lst) {
		Vecteur c = new Vecteur();
		for (Vecteur v : lst) {
			c= c.add(v);
		}
		return c.multiply(Decimal.UN.divide(new Decimal(lst.size())));
	}

	/** 
	 * Calcul la surface 
	 * */
	public static Decimal getSurface(ArrayList<Vecteur> lst) {
		// FORMULE : Aire = 1/2 * somme[I=0 --> n-1] (XiYi+1 - YiXi+1)
		Decimal total = Decimal.ZERO;
		
		for (int i = 0; i < lst.size()-1; i++) {
			Vecteur v = lst.get(i);
			Vecteur vplusun = lst.get(i+1);
			Decimal sub = v.getDecY().multiply(vplusun.getDecZ()).minus(v.getDecZ().multiply(vplusun.getDecY()));
			total = total.add(sub);
		}
		
		return total.divide(Decimal.DEUX);		
	}


	/** 
	 * Calcul le centre de surface d'une aire
	 * */
	public static Vecteur getCentreSurface(ArrayList<Vecteur> lst) {
		// FORMULE : centre =(xg, yg) A = aire
		// xg = 1/(6*A) * somme[I=0 --> n-1] (Xi + Xi+1)*(XiYi+1 - YiXi+1)
		// yg = 1/(6*A) * somme[I=0 --> n-1] (Yi + Yi+1)*(XiYi+1 - YiXi+1)
		
		Decimal A = getSurface(lst);
		if (A.isZero()) return new Vecteur();
		
		Decimal totY = Decimal.ZERO;
		Decimal totZ = Decimal.ZERO;

		for (int i = 0; i < lst.size()-1; i++) {
			Vecteur v = lst.get(i);
			Vecteur vplusun = lst.get(i+1);
			Decimal y = v.getDecY().add(vplusun.getDecY());
			Decimal z = v.getDecZ().add(vplusun.getDecZ());
			Decimal sub = v.getDecY().multiply(vplusun.getDecZ()).minus(v.getDecZ().multiply(vplusun.getDecY()));
			totY = totY.add(sub.multiply(y));
			totZ = totZ.add(sub.multiply(z));
		}
		
		Decimal delta = new Decimal(6).multiply(A);
		return new Vecteur (Decimal.ZERO, totY.divide(delta), totZ.divide(delta));
	}

	/***
	 * Calcule le coefficient de r�sistance de chaque pav� de la car�ne
	 * 
	 * --> pour chaque �l�ment, je calule la surface totale et la surface projet�e sur le plan z = 0
	 * --> retourne une valeu entre 0 et 256 
	 */

	public static Decimal getCoeficient (ArrayList<Vecteur> lst) {
		if (lst == null) return Decimal.ZERO;
		if (lst.size() != 4 ) return Decimal.ZERO;
		
		
		Decimal srf = CalculSurface.getSurface(lst);
		if (srf.isZero()) return Decimal.ZERO;

		ArrayList<Vecteur> pts = new ArrayList<Vecteur> (4);
		for (Vecteur v : lst) pts.add(new Vecteur(v.getDecX(), v.getDecY(), Decimal.ZERO));

		Decimal frein = CalculSurface.getSurface(pts);

		
		Decimal res = frein.divide(srf).multiply(new Decimal(10f));
		return res;
	}

	
	/**
	 * Test des fonctions de calcule
	 * 
	 * @param args
	 */
	public static void main (String[] args) {
		ArrayList<Vecteur> pts = new ArrayList<Vecteur>();
		pts.add(new Vecteur("0;0;0.0019"));
		pts.add(new Vecteur("0;0;0.5575"));
		pts.add(new Vecteur("0;0;1.1132"));
		pts.add(new Vecteur("0;0;1.6687"));
		pts.add(new Vecteur("0;0;2.2244"));
		pts.add(new Vecteur("0;0;2.7799"));
		pts.add(new Vecteur("0;0;3.3356"));
		pts.add(new Vecteur("0;0;3.8911"));
		pts.add(new Vecteur("0;0;4.4468"));
		pts.add(new Vecteur("0;0;5.0024"));
		pts.add(new Vecteur("0;-0.1341;5.0043"));
		pts.add(new Vecteur("0;-0.195;4.4495"));
		pts.add(new Vecteur("0;-0.2303;3.8944"));
		pts.add(new Vecteur("0;-0.2525;3.3391"));
		pts.add(new Vecteur("0;-0.2645;2.7837"));
		pts.add(new Vecteur("0;-0.2696;2.2282"));
		pts.add(new Vecteur("0;-0.2681;1.6725"));
		pts.add(new Vecteur("0;-0.2582;1.1168"));
		pts.add(new Vecteur("0;-0.1356;0.5594"));
		pts.add(new Vecteur("0;-0.1039;0.0034"));
		
		System.out.println("Centre GET : "+getCentreSurface(pts));
		System.out.println("Surface GET : "+getSurface(pts));

	}
}
