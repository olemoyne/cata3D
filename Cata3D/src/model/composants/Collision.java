package model.composants;

import java.io.Serializable;

import model.math.MapDeVecteurs;

/**
 * Repr�sente une collision entre deux mod�lisations
 * Chaque collisison a : 
 *    * un composant en collision
 *    * une zone (MAP) de collision
 * 
 * @author olemoyne
 *
 */
public class Collision implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6103383203619934341L;
	public Composant autre;
	public MapDeVecteurs collision;

}
