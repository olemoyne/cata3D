package model;

import model.math.Decimal;
import model.math.MapDeVecteurs;
import tarpeia.com.model.TarpeiaField;
import tarpeia.com.model.TarpeiaObject;

/***************************
 *  Permet de définir le niveau de la mer par rapport à la coque
 *  
 * @author olemoyne
 *
 ***********************************************************/


@TarpeiaObject
public class Flottaison {

	@TarpeiaField
	public Decimal pilonnement;
	
	@TarpeiaField
	public Decimal tangage;
	
	@TarpeiaField
	public Decimal gite;
	
	public Flottaison () {
		pilonnement = new Decimal(0);
		tangage = new Decimal(0); // Angle de tangage (rotation selon Y )
		gite = new Decimal(0); // Angle de gite (rotation selon Z)
	}

	/**
	 * Applique la déformation à la coque
	 * 
	 * @param map
	 * @return
	 */
	public MapDeVecteurs applyToMap (MapDeVecteurs map) {
		// TODO 
		return null;
	}
}
