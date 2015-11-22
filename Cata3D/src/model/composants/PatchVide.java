package model.composants;

import model.Structure;
import model.calcul.CalculCoque;
import model.math.Decimal;

/**
 * Composant généré à partir d'un patch 
 * 
 * @author lemoyne
 *
 */
public class PatchVide extends PatchComposant {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3254205662119556033L;

	/** Epaisseur du bardage nécessaire à la génération de la forme **/
	public Decimal epaisseurDeBardage;
	
	/** Densité du matériau utilisé pour le bardage **/
	public Decimal densiteBardage;

	/** Structure du bateau **/
	public Structure structure;


	public PatchVide () {
		super(); // Creation des données liées au patch
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
    	super.recalcule();
		this.gravite = CalculCoque.calculePoidsCoque(mapAffichage, epaisseurDeBardage.multiply(densiteBardage));
    }
    
	public int getType() {
		return Composant.PATCH_VIDE;
	}


}
