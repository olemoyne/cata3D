package model;

import java.io.Serializable;

import model.math.Decimal;
import model.math.Vecteur;

/**
 * Poutre de soustient entre les gabarits
 * 
 * @author olemoyne
 *
 */
public class Poutre implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1538624996389463009L;

	public Vecteur depart; 
	
	public Decimal longueur;
	public Decimal epaisseur;
	public Decimal hauteur;

	public Poutre () {
		// Position 0
		depart = new Vecteur ();
		// Un metre
		longueur= Decimal.UN;
		// Epaisseur
		epaisseur= new Decimal(0.04d);// 4 cm
		hauteur= new Decimal(0.02d);// 2 cm
	}

	public Vecteur  getEnd() {
		return new Vecteur (depart.getDecX().add(epaisseur.divide(Decimal.DEUX)), 
				depart.getDecY().minus(hauteur.divide(Decimal.DEUX)), depart.getDecZ().add(longueur));
	}	
	
	// Génère les données à afficher
	public String toString () {
		Vecteur end = this.getEnd();
		return depart.toString().concat("-").concat(end.toString());
	}

	public Vecteur getStart() {
		return new Vecteur (depart.getDecX().add(epaisseur.divide(Decimal.DEUX).negate()), 
				depart.getDecY().minus(hauteur.divide(Decimal.DEUX).negate()), depart.getDecZ());
	}
}
