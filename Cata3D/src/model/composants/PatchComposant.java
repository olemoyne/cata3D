package model.composants;

import model.Cata;
import model.calcul.CalculCoque;
import model.patch.Patch;

public class PatchComposant extends Composant {
	
	/** Densit� de la mati�re de construction **/
	public static final double DENSITE_BOIS = 0.6d;

	public static final int PRECISION = 10;

	/**
	 * 
	 */
	private static final long serialVersionUID = 3294205662119556033L;

	/** Patch permettant de stocker les données et calculer la forme **/
	public Patch patch;	
	
	public PatchComposant (Cata bato) {
		super(bato); // Creation des données liées au patch
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
//		this.gravite = CalculVolume.getPoussee(mapAffichage);
		super.recalcule();
    }

    

}
