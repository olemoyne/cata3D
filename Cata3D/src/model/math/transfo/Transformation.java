package model.math.transfo;

import model.math.Vecteur;


/**
 * Une transformation permet de modifier un point dans l'espace
 * 
 * @author olemoyne
 *
 */
public abstract class Transformation {
	
	/**
	 * Cas d'une transformation imbriquée
	 */
	private Transformation upper;
	


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

	
	/**
	 * Application de la transformation
	 * 
	 * @param ret
	 * @return
	 */
	public abstract Vecteur getPoint(Vecteur ret);
	
}
