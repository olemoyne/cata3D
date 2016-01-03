package model.composants;

import java.awt.Color;
import java.util.ArrayList;

import view.scene.PrintableObject;
import view.scene.PrintedForce;
import view.scene.PrintedMap;
import model.Cata;
import model.calcul.CalculCoque;
import model.math.transfo.Translation;
import model.patch.Patch;

public class PatchComposant extends Composant {
	
	/** Densit� de la mati�re de construction **/
	public static final double DENSITE_BOIS = 0.6d;

	public static final int PRECISION = 20;

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

	public ArrayList<PrintableObject> getSceneObjects() {
		ArrayList<PrintableObject> ret = new ArrayList<PrintableObject>();

		Translation trsl = new Translation(position, null); 
		
		//** D�caler le desssin **/
		ret.add(new PrintedMap (mapAffichage.transforme(trsl), "Coque", false, Color.darkGray));
		ret.add(new PrintedForce(gravite.transforme(trsl), Color.RED));

		return ret; 
	}


}
