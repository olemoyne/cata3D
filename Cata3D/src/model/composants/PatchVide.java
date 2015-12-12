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

	/**
	 * Creation d'un patch par defaut
	 * 
	 * @return
	 */
	public static Composant getDefaultPatch() {
		PatchVide ret = new PatchVide();
		ret.nom = "Flotteur";
		ret.patch= Patch.getPatch();
		ret.epaisseurDeBardage = new Decimal(0.01d);
		ret.densiteBardage = new Decimal(0.6d);
		ret.recalcule();
		return ret;
	}


}
