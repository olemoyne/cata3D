package model.composants;

import java.awt.Color;
import java.util.ArrayList;

import view.scene.PrintableObject;
import view.scene.PrintedForce;
import view.scene.PrintedMap;
import model.Apparence;
import model.Cata;
import model.Poids;
import model.Position;
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
	/** Précision d'affichage des données du patch **/
	public int precision;
	/** Définit si la forme est symétriques ou pas **/
	public Boolean reflexive;
	/** Définit si la forme doit être réduite en cas de collision avec un autre composant **/
	public Boolean reduction;

	/** Définit si la forme est en collision avec un autre composant **/
	public Boolean collision;
	
	public ArrayList<Collision> collisions;
	
	public PatchComposant() {
		super(); // Creation des données liées au patch
		patch = new Patch();	
		precision = 20;
		reflexive = Boolean.TRUE;
		collision= Boolean.FALSE;
		collisions = new ArrayList<Collision>();
	}
	
	
	public PatchComposant (Cata bato) {
		super(bato); // Creation des données liées au patch
		patch = new Patch();	
		precision = 20;
		collision = Boolean.FALSE;
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
	 * Reclacul des éléments hors MAP d'affichage
	 */
	public void calculeElements (){
		// Par déaut, ne fait rien
	}

	
    /**
     * Recalcule les éléments essentiels de la pièce :
     *    Map affichage et calcul
     *    centre de gravité et poids
     *     
     */
    public void recalcule () {
		if (reflexive == null) reflexive = Boolean.TRUE;
		if (reduction == null) reduction = Boolean.FALSE;
    	if (precision <= 0) precision = 20;
    	// calcule la MAP de vecteur de base
    	this.mapAffichage= CalculCoque.createCoque(patch, precision, this.reflexive);
    	calculeElements();
    }

	public ArrayList<PrintableObject> getSceneObjects(Position trans) {
		ArrayList<PrintableObject> ret = new ArrayList<PrintableObject>();
		Apparence app = this.getApparence();
		if (trans != null) {
			ret.add(new PrintedMap (mapAffichage, this.nom, !app.transparence, app.couleur, trans));
			if (gravite != null) {
				Transformation trs = trans.getTransformation(null);
				if (trs != null) {
					Poids pds = new Poids (gravite.nom, trs.transforme(gravite.position), gravite.force);
					ret.add(new PrintedForce(pds, Color.RED));
				}
			}
		} else {
			ret.add(new PrintedMap (mapAffichage, this.nom, false, Color.darkGray, this.situation));
			if (gravite != null) {
				ret.add(new PrintedForce(gravite, Color.RED));
			}
			
		}

		return ret; 
	}
	
	public boolean isPatch() {
		return true;
	}
}
