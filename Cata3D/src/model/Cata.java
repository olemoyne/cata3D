package model;

import java.util.ArrayList;

import model.math.Decimal;
import model.math.MapDeVecteurs;
import model.math.Vecteur;
import tarpeia.com.model.TarpeiaField;
import tarpeia.com.model.TarpeiaObject;


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
@TarpeiaObject
public class Cata {
   // D�finition des 16 points 
	@TarpeiaField
	public ArrayList<Vecteur> points;

	@TarpeiaField
	public Decimal rooFDistance;
	
	/** Pr�cision de calcul et d'affichage **/
	@TarpeiaField
	public Decimal precisionDeCalcul;
	@TarpeiaField
	public Decimal precisionDAffichage;
	
	/** Mod�les matematiques d'affichage et de calcul **/
	private MapDeVecteurs mapAffichage;
	private MapDeVecteurs mapCalcul;

	/** Flottaison **/
	@TarpeiaField
	public Flottaison mer;
	
	/** Poids **/
	@TarpeiaField
	public ArrayList<Poids> poids;
	
	/** Gabarits **/
	@TarpeiaField
	public ArrayList<Gabarit> gabarit;
	
	/** Poutres **/
	@TarpeiaField
	public ArrayList<Poutre> poutres;
	
	public Cata () {
		
	}
}
