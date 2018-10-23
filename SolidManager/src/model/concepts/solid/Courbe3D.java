package model.concepts.solid;

import java.util.ArrayList;

import model.concepts.math.Decimal;
import model.concepts.math.Vecteur;

public class Courbe3D extends ArrayList<Vecteur>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public Courbe3D () {
		super();
	}

	/**  Construction de la courbe à partir d'une liste de points  **/
	public Courbe3D (Vecteur[] v) {
		super();
		
		for (Vecteur pt : v) {
			add(pt);
		}
	}

	public Vecteur[] toVectArray () {
		Vecteur[] retour = new Vecteur[size()];
		int pos = 0;
		for (Vecteur v : this) {
			retour[pos] = v;
			pos ++;
		}
		return retour;
	}
	

	public Decimal getLength () {
        Decimal sze = Decimal.ZERO;
        Vecteur last = this.get(0);
		for (Vecteur v : this) {
			sze = sze.add(last.distance(v));
			last = v;
		}
		return sze;
	}

}
