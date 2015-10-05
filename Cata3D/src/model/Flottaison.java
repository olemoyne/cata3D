package model;

import java.io.Serializable;

import model.math.Decimal;
import model.math.MapDeVecteurs;

/***************************
 *  Permet de définir le niveau de la mer par rapport à la coque
 *  
 * @author olemoyne
 *
 ***********************************************************/


public class Flottaison implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8873722559801931229L;

	public Decimal pilonnement;
	
	public Decimal tangage;
	
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
