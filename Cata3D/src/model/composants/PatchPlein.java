package model.composants;

import model.calcul.CalculVolume;
import model.math.Decimal;

/**
 * Composant généré à partir d'un patch 
 * 
 * @author lemoyne
 *
 */
public class PatchPlein extends PatchComposant {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3294205662119556033L;

	/** Densité du matériau utilisé pour ola forme **/
	public Decimal densite;
	

	public PatchPlein () {
		super(); // Creation des données liées au patch
	}
	
	/**
	 * Si les donne�s de patch sont modifi�es 
	 * 
	 * @param y 
	 * @param x 
	 */
	public void setPatch(int x, int y) {
		super.setPatch(x, y);
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

		this.gravite = CalculVolume.getPoussee(mapAffichage);
		this.gravite.force = this.gravite.force.multiply(densite); 
    }

    
	public int getType() {
		return Composant.PATCH_PLEIN;
	}

}
