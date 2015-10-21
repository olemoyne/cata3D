package model.calcul;

import java.util.ArrayList;

import model.math.Decimal;
import model.math.Vecteur;

public class CalculSurface {
	
	public static Vecteur getCentre(Vecteur[] lst) {
		Vecteur c = new Vecteur();
		for (Vecteur v : lst) {
			c= c.add(v);
		}
		return c.multiply(Decimal.UN.divide(new Decimal(lst.length)));
	}

	public static Vecteur getCentre(ArrayList<Vecteur> lst) {
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
		Vecteur ctr = getCentre(lst);
		// Autant de Triangles 
		Decimal surf = Decimal.ZERO;
		Vecteur last = null;
		for (Vecteur v : lst) {
			if (last != null) {
				surf = surf.add(Vecteur.calculeSurface(ctr, last, v));
			}
			last = v;
		}
		// Dernier triangle
		surf = surf.add(Vecteur.calculeSurface(ctr, last, lst.get(0)));
		return surf;
	}


	
	/***
	 * Calcule le coefficient de résistance de chaque pavé de la carène
	 * 
	 * --> pour chaque élément, je calule la surface totale et la surface projetée sur le plan z = 0
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

}
