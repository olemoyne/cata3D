package model.composants;

import model.calcul.CalculCoque;
import model.calcul.CalculVolume;
import model.math.Decimal;
import model.patch.Patch;

/**
 * Composant généré à partir d'un patch 
 * 
 * @author lemoyne
 *
 */
public class PatchPlein extends Composant {
	
	public static final int PRECISION = 10;

	/**
	 * 
	 */
	private static final long serialVersionUID = 3294205662119556033L;

	/** Patch permettant de stocker les données et calculer la forme **/
	public Patch patch;
	
	/** Densité du matériau utilisé pour ola forme **/
	public Decimal densite;
	

	public PatchPlein () {
		super(); // Creation des données liées au patch
		patch = new Patch();	
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
		this.gravite = CalculVolume.getPoussee(mapAffichage);
		this.gravite.force = this.gravite.force.multiply(densite); 
    }

    
	public int getType() {
		return Composant.PATCH_PLEIN;
	}

}
