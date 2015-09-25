package model;

import model.math.Decimal;
import model.math.Vecteur;
import tarpeia.com.model.TarpeiaField;
import tarpeia.com.model.TarpeiaObject;

/** 
 * Poids appliqu� � la coque
 * 
 * @author olemoyne
 *
 */
@TarpeiaObject
public class Poids {

	@TarpeiaField
	public Vecteur position;
	
	@TarpeiaField
	public Decimal force; // Exprim�e en KG
	
	public Poids () { 
		position = new Vecteur ();
		force = Decimal.ZERO;
	}
}
