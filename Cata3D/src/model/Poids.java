package model;

import java.io.Serializable;

import model.math.Decimal;
import model.math.Vecteur;

/** 
 * Poids appliqué à la coque
 * 
 * @author olemoyne
 *
 */
public class Poids implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2766347239318331795L;

	public Vecteur position;
	
	public Decimal force; // Exprimée en KG
	
	public Poids () { 
		position = new Vecteur ();
		force = Decimal.ZERO;
	}
}
