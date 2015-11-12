package model.composants;

import model.Structure;
import model.calcul.CalculCoque;
import model.math.Decimal;
import model.patch.Patch;

/**
 * Composant généré à partir d'un patch 
 * 
 * @author lemoyne
 *
 */
public class PatchVide extends Composant {
	
	public static final int PRECISION = 10;

	/**
	 * 
	 */
	private static final long serialVersionUID = 3254205662119556033L;

	/** Patch permettant de stocker les données et calculer la forme **/
	public Patch patch;
	
	/** Epaisseur du bardage nécessaire à la génération de la forme **/
	public Decimal epaisseurDeBardage;
	
	/** Densité du matériau utilisé pour le bardage **/
	public Decimal densiteBardage;

	/** Structure du bateau **/
	public Structure structure;


	public PatchVide () {
		super(); // Creation des données liées au patch
		patch = new Patch();	
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
     * Recalcule les éléments essentiels de la pièce :
     *    Map affichage et calcul
     *    centre de gravité et poids
     *     
     */
    public void recalcule () {
    	mapAffichage = CalculCoque.createCoque(patch, PRECISION);
		this.gravite = CalculCoque.calculePoidsCoque(mapAffichage, epaisseurDeBardage.multiply(densiteBardage));
    }
    
	public int getType() {
		return Composant.PATCH_VIDE;
	}


}
