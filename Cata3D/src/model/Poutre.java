package model;

import java.io.Serializable;

import model.math.Vecteur;

/**
 * Poutre de soustient entre les gabarits
 * 
 * @author olemoyne
 *
 */
public class Poutre implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1538624996389463009L;

	public Vecteur depart; 

	public Vecteur arrivee; 
	
	public Vecteur taille;

}
