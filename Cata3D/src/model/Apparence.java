package model;

import java.awt.Color;
import java.io.Serializable;

/**
 * Gestion de l'affichage d'un composant
 * 
 * @author olemoyne
 *
 */
public class Apparence implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6025426685863408904L;

	/** 
	 * Couleur d'affichage du composant 
	 * **/
	public Color couleur;
	
	/**
	 * Mode d'affichage du composant (facettes ou grillage ) 
	 */
	public Boolean transparence;

	
	public Apparence () {
		couleur = Color.GRAY;
		transparence = true;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		
		return sb.toString();
	}
}
