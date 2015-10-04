package model;

import java.io.Serializable;
import java.util.ArrayList;

import model.math.MapDeVecteurs;
import model.patch.Patch;


/**
 * Modelisation simple d'un catamaran 
 *   * Design
 *   --> 16 points pour d�finir la forme de la coque
 *   --> 1 point de d�part pour le roof
 *   --> une �chelle de pr�cision pour les calculs / affichage
 *   --> maillage de points pour afficher la coque
 *   --> maillage de points pour les calculs
 *   --> liste de poids associ�s
 *   --> Position (tangage, gite et pilonnement )
 * 
 *   * Construction 
 *   --> Liste de gabarits ( )
 *   --> 4 poutres et leur position
 *   
 * @author olemoyne
 *
 */
public class Cata implements Serializable{
   /**
	 * 
	 */
	private static final long serialVersionUID = -5490545959071227392L;

	public Patch patch;
	
	/** Pr�cision de calcul et d'affichage **/
	public int precisionDeCalcul;
	public int precisionDAffichage;
	
	/** Mod�les matematiques d'affichage et de calcul **/
	private MapDeVecteurs mapAffichage;
	private MapDeVecteurs mapCalcul;

	/** Flottaison **/
	public Flottaison mer;
	
	/** Poids **/
	public ArrayList<Poids> poids;
	
	/** Gabarits **/
	public ArrayList<Gabarit> gabarits;
	
	/** Poutres **/
	public ArrayList<Poutre> poutres;
	
	/** Donn�es calcul�es **/
	public Poids poussee;
	public Poids gravite;
	
	/**
	 * Constructeur --> intialisation des listes
	 */
	public Cata () {
		patch = new Patch();
		precisionDeCalcul = 20;
		precisionDAffichage = 10;
		mapAffichage = patch.getMap(precisionDAffichage);
		mapCalcul = patch.getMap(precisionDeCalcul);
		
		mer = new Flottaison();
		
		gabarits = new ArrayList<Gabarit> ();
		poutres = new ArrayList<Poutre> ();
	
	}
	

	/**
	 * Si les donne�s de patch sont modifi�es 
	 * 
	 * @param y 
	 * @param x 
	 */
	public void recalculePatch(int x, int y) {
		
		patch.recalcule(x, y);
		mapAffichage = patch.getMap(precisionDAffichage);
		mapCalcul = patch.getMap(precisionDeCalcul);
		
	}
	
	
}
