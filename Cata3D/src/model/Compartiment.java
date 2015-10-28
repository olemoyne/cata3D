package model;

import java.io.Serializable;
import java.util.ArrayList;

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
	
	/** Elements définis  **/
	long positionDebut;
	long positionFin;
	String nom; // Mon du compartiment
	
	/** Elements calculés **/
	ArrayList<Vecteur> faceArriere;
	ArrayList<Vecteur> faceAvant;
	
	long hauteurMin;
	long largeurMin;
	long longueur;
	
	
	public Compartiment () {
		
	}

}
