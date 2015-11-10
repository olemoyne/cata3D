package model;

import java.io.Serializable;
import java.util.ArrayList;

import model.calcul.CalculCoque;
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
	public MapDeVecteurs mapAffichage;
	public MapDeVecteurs mapCalcul;

	/** Flottaison **/
	public Flottaison mer;
	
	/** Poids **/
	public ArrayList<Poids> poids;

	/** Structure du bateau **/
	public Structure structure;
		
	
	/**
	 * Constructeur --> intialisation des listes
	 */
	public Cata () {
		patch = new Patch();
		precisionDeCalcul = 20;
		precisionDAffichage = 80;
		mapAffichage = patch.getMap(precisionDAffichage);
		mapCalcul = patch.getMap(precisionDeCalcul);
		
		mer = new Flottaison();

		poids = new ArrayList<Poids> ();
		
		structure = new Structure();
	}
	

	/**
	 * Si les donne�s de patch sont modifi�es 
	 * 
	 * @param y 
	 * @param x 
	 */
	public void setPatch(int x, int y) {
		patch.recalcule(x, y);
		recalcule();
	}

	/**
	 * Changement de patch sans resizing
	 */
	public void recalcule() {
		// Recalcul des coques
		mapAffichage = CalculCoque.createCoque(patch, precisionDAffichage);
		CalculCoque.calculeCarene(this);
		this.mer.poidsDeLaCoque = CalculCoque.calculePoidsCoque(this);
		CalculCoque.calculeFlottaison(this);
		CalculCoque.calculeDerive(this);
		
	}

	
    public String toString() {
        StringBuilder sb = new StringBuilder("Cata = ");
        sb.append(patch.toString());
        sb.append(" ");
        if (mer != null) sb.append(mer.toString());
        if (poids != null) for (Poids pds : poids) sb.append(pds.toString());
        if (structure != null) sb.append(structure.toString());
        return sb.toString();
    }	
}
