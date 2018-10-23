package model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * D�finit l'habitacle en fonction des �l�ments 
 *    * �paisseur de la coque (en mm)
 *    * Position du fond de cale (en Y)
 *    * Position des compartiments 
 *    
 *    * Liste des compartiments
 *     
 * @author olemoyne
 *
 */
public class Habitacle implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2297125890223114510L;

	public long epaisseurDeCoque;
	public long positionFondDeCale;
	public ArrayList<Compartiment> compartiments;
	
	public Habitacle () {
		compartiments = new ArrayList<Compartiment>();
	}
	

}
