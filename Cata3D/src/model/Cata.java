package model;

import java.io.Serializable;
import java.util.ArrayList;

import model.math.Decimal;
import model.math.MapDeVecteurs;
import model.math.Vecteur;


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

	// D�finition des xx points 
	public int x, y;
	public ArrayList<Vecteur> points;
	
	/** Pr�cision de calcul et d'affichage **/
	public Decimal precisionDeCalcul;
	public Decimal precisionDAffichage;
	
	/** Mod�les matematiques d'affichage et de calcul **/
	private MapDeVecteurs mapAffichage;
	private MapDeVecteurs mapCalcul;

	/** Flottaison **/
	public Flottaison mer;
	
	/** Poids **/
	public ArrayList<Poids> poids;
	
	/** Gabarits **/
	public ArrayList<Gabarit> gabarit;
	
	/** Poutres **/
	public ArrayList<Poutre> poutres;
	
	public Cata () {
		
	}
	
	
	
}
