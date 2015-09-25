package model;

import model.math.Decimal;
import model.math.Vecteur;
import tarpeia.com.model.TarpeiaField;
import tarpeia.com.model.TarpeiaObject;

/** 
 * Poids appliqué à la coque
 * 
 * @author olemoyne
 *
 */
@TarpeiaObject
public class Poids {

	@TarpeiaField
	public Vecteur position;
	
	@TarpeiaField
	public Decimal force; // Exprimée en KG
	
	public Poids () { 
		position = new Vecteur ();
		force = Decimal.ZERO;
	}
}
