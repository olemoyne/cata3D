package model;

import model.math.Vecteur;
import tarpeia.com.model.TarpeiaField;
import tarpeia.com.model.TarpeiaObject;

/**
 * Poutre de soustient entre les gabarits
 * 
 * @author olemoyne
 *
 */
@TarpeiaObject
public class Poutre {

	@TarpeiaField
	public Vecteur depart; 

	@TarpeiaField
	public Vecteur arrivee; 
	
	public Vecteur taille;

}
