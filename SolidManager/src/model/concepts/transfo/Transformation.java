package model.concepts.transfo;

import model.concepts.math.Vecteur;


/**
 * Une transformation permet de modifier un point dans l'espace
 * 
 * @author olemoyne
 *
 */
public abstract class Transformation {
	
	/**
	 * Cas d'une transformation imbriqu�e
	 */
	private Transformation upper;
	
	public Transformation (Transformation u) {
		upper = u;
	}
	


	/**
	 * Transformation d'un point unitaire
	 * @param pt
	 * @return
	 */
	public Vecteur transforme(Vecteur pt) {
		Vecteur v = null;
		if (upper != null) v = upper.transforme(pt);
		else v = pt;
		
		return getPoint(v);
	}

	
	public Transformation getReverse(Transformation tr) {
		// Gestion de la transformation supérieure
		if (this.upper != null) {
			Transformation ret = upper.getReverse(tr);			
			return ret;
		}
		return tr;
	}

	/**
	 * Application de la transformation
	 * 
	 * @param ret
	 * @return
	 */
	protected abstract Vecteur getPoint(Vecteur ret);
	
}
