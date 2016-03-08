package model.composants;

import java.awt.Color;
import java.util.ArrayList;

import view.scene.PrintableObject;
import view.scene.PrintedForce;
import view.scene.PrintedMap;
import model.Apparence;
import model.Cata;
import model.Poids;
import model.math.Decimal;
import model.math.MapDeVecteurs;
import model.math.Vecteur;
import model.math.transfo.Transformation;

/**
 * Composant généré à partir d'un patch 
 * 
 * @author lemoyne
 *
 */
public class Tube extends Composant {
	
	public static final int PRECISION = 10;

	/**
	 * 
	 */
	private static final long serialVersionUID = 3254205662119556033L;

	/** Dimensions **/
	public Decimal diametre;
	
	/** Epaisseur du tube  */ 
	public Decimal epaisseur;
	
	/** Logueur du tube **/
	public Decimal longueur;
	
	/** Densité du tube **/
	public Decimal densite;
	
	public Tube (Cata bato) {
		super(bato); // Creation des données liées au patch
		
		initData();
	}
	
	private void initData() {
		if (this.diametre == null) this.diametre = new Decimal(0.01);
		if (this.epaisseur == null) this.epaisseur = new Decimal(0.001);
		if (this.longueur == null) this.longueur = new Decimal(0.2);
		if (this.densite == null) this.densite = new Decimal (0.2);
	}
	
    /**
     *  Recalcule les éléments essentiels de la pièce :
     *    Map affichage et calcul
     *    centre de gravité et poids
     *     
     */
    public void recalcule () {
    	initData();
    	mapAffichage = createMap();
		Decimal pds = new Decimal(Math.PI).multiply(diametre.square().minus(diametre.minus(Decimal.DEUX.multiply(epaisseur)).square())).multiply(densite).multiply(this.longueur);
		// TODO : Revoir la position du poids
		Vecteur ctr = this.situation.getTransformation(null).getPoint(new Vecteur(Decimal.ZERO, longueur.divide(Decimal.DEUX), Decimal.ZERO));
		this.gravite = new Poids ("Centre de gravité", ctr, pds);
		super.recalcule();
    }

	private MapDeVecteurs createMap() {
		int nbPoints = 8;
		// Creation d"un saucisson de la longueur du tube
		MapDeVecteurs map = new MapDeVecteurs(10, nbPoints);
		// Construit le premier cercle
		Vecteur[] pts = new Vecteur[nbPoints];
		double rad = 0; // Angle
		double delta = 2d*Math.PI/nbPoints;
		for (int id = 0; id < nbPoints; id ++) {
			Decimal x = this.diametre.divide(Decimal.DEUX).multiply(new Decimal(Math.sin(rad)));
			Decimal y = this.diametre.divide(Decimal.DEUX).multiply(new Decimal(Math.cos(rad)));
			pts[id] = new Vecteur (x, y, Decimal.ZERO);
			rad += delta;
		}

		// Calcule la position des points en fonction de la direction en Y
		Decimal d = longueur.divide(new Decimal(9));
		Decimal y = Decimal.ZERO;
		for (int pos = 0; pos < 10; pos ++) {
			for (int p = 0; p < nbPoints; p++) {
				map.setPoint(pos, p, new Vecteur (pts[p].getDecX(), y, pts[p].getDecY()));
			}
			y = y.add(d);
		}
		return map;
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


	
	public int getType() {
		return Composant.TUBE;
	}


}
