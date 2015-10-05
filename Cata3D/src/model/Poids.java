package model;

import java.io.Serializable;

import model.math.Decimal;
import model.math.Vecteur;

/** 
 * Poids appliqu� � la coque
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
	
	public Decimal force; // Exprim�e en KG
	
	public Poids () { 
		position = new Vecteur ();
		force = Decimal.ZERO;
	}
}
