package model;

import java.awt.Color;
import java.io.Serializable;

import model.math.Vecteur;

/******************************
 * Identification d'un compartiment du bateau
 * 
 * @author olemoyne
 *
 */
public class Compartiment implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5179267508659646199L;
	
	/** Elements d�finis  **/
	Vecteur position;
	Vecteur taille;
	String nom; // Mon du compartiment
	
	Color couleur;
		
	
	public Compartiment () {
		position = new Vecteur ("0;0;0"); 
		taille = new Vecteur ("1;1;1");
		nom = "Chambre";
		couleur = Color.blue;
	}


	public String getNom() {
		return nom;
	}
	
	public Vecteur getPosition () {
		return position ;
	}

	public Vecteur getTaille () {
		return taille;
	}

	public Color getCouleur() {
		return couleur;
	}

	
	public void setData(String n, Vecteur p, Vecteur t, Color col) {
		nom = n;
		position = p;
		taille = t;
		couleur = col;
	}

	public String toString () {
		return position.toString()+" "+taille.toString();
	}
	
}
