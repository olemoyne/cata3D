package model;

import java.io.Serializable;
import java.util.ArrayList;

import model.calcul.CalculCoque;
import model.math.MapDeVecteurs;
import model.patch.Patch;


/**
 * Modelisation simple d'un catamaran 
 *   * Design
 *   --> 16 points pour définir la forme de la coque
 *   --> 1 point de départ pour le roof
 *   --> une échelle de précision pour les calculs / affichage
 *   --> maillage de points pour afficher la coque
 *   --> maillage de points pour les calculs
 *   --> liste de poids associés
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
	
	/** Précision de calcul et d'affichage **/
	public int precisionDeCalcul;
	public int precisionDAffichage;
	
	/** Modèles matematiques d'affichage et de calcul **/
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
	 * Si les donneés de patch sont modifiées 
	 * 
	 * @param y 
	 * @param x 
	 */
	public void recalculePatch(int x, int y) {
		patch.recalcule(x, y);
		recalculeMaps();
		recalculeFlottaison();
		recalculeStructure();
	}

	/**
	 * Changement de patch sans resizing
	 */
	public void recalculeMaps() {
		// Recalcul des coques
		mapAffichage = CalculCoque.createCoque(patch, precisionDAffichage);
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


	public void recalculeFlottaison() {
		CalculCoque.calculeCarene(this);
		this.mer.poidsDeLaCoque = CalculCoque.calculePoidsCoque(this);
		CalculCoque.calculeFlottaison(this);
	}


	/** 
	 * En cas de modification de la structure du bateau 
	 * **/
	public void recalculeStructure() {
		
	}



	
}
