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




}
