package model.composants;

import model.calcul.CalculCoque;
import model.calcul.CalculVolume;
import model.patch.Patch;

public class PatchComposant extends Composant {

	public static final int PRECISION = 10;

	/**
	 * 
	 */
	private static final long serialVersionUID = 3294205662119556033L;

	/** Patch permettant de stocker les données et calculer la forme **/
	public Patch patch;	
	
	public PatchComposant () {
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
    }

    

}
