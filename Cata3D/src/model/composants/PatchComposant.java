package model.composants;

import java.awt.Color;
import java.util.ArrayList;

import view.scene.PrintableObject;
import view.scene.PrintedForce;
import view.scene.PrintedMap;
import model.Apparence;
import model.Cata;
import model.Poids;
import model.calcul.CalculCoque;
import model.math.transfo.Transformation;
import model.patch.Patch;

public class PatchComposant extends Composant {
	
	/** Densit� de la mati�re de construction **/
	public static final double DENSITE_BOIS = 0.6d;

	/**
	 * 
	 */
	private static final long serialVersionUID = 3294205662119556033L;

	/** Patch permettant de stocker les données et calculer la forme **/
	public Patch patch;	
	public int precision;
	public Boolean reflexive;
	
	public PatchComposant() {
		super(); // Creation des données liées au patch
		patch = new Patch();	
		precision = 20;
		reflexive = Boolean.TRUE;
	}
	
	
	public PatchComposant (Cata bato) {
		super(bato); // Creation des données liées au patch
		patch = new Patch();	
		precision = 20;
		reflexive = Boolean.TRUE;
	}
	
	/**
	 * Si les donne�s de patch sont modifi�es 
	 * 
	 * @param y 
	 * @param x 
	 */
	public void setPatch(int x, int y) {
		if (reflexive == null) reflexive = Boolean.TRUE;

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
		if (reflexive == null) reflexive = Boolean.TRUE;
    	if (precision <= 0) precision = 20;
    	mapAffichage = CalculCoque.createCoque(patch, precision, this.reflexive);
//		this.gravite = CalculVolume.getPoussee(mapAffichage);
		super.recalcule();
    }

	public ArrayList<PrintableObject> getSceneObjects(Transformation trans) {
		ArrayList<PrintableObject> ret = new ArrayList<PrintableObject>();
		Apparence app = this.getApparence();
		if (trans != null) {
			//** D�caler le desssin **/
			ret.add(new PrintedMap (mapAffichage.transforme(trans), this.nom, !app.transparence, app.couleur));
			if (gravite != null) {
				Poids pds = new Poids (gravite.nom, trans.transforme(gravite.position), gravite.force);
				ret.add(new PrintedForce(pds, Color.RED));
			}
		} else {
			ret.add(new PrintedMap (mapAffichage, this.nom, false, Color.darkGray));
			if (gravite != null) {
				Poids pds = new Poids (gravite.nom, gravite.position, gravite.force);
				ret.add(new PrintedForce(pds, Color.RED));
			}
			
		}

		return ret; 
	}

}
