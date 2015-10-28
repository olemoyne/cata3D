package model;

import java.util.ArrayList;

import model.math.Decimal;
import model.math.Vecteur;

/** 
 * Liste de points définissant une surface
 * 
 * @author olemoyne
 *
 */
public class Area {
	
	public ArrayList<Vecteur> points;
	
	public Area () {
		points = new ArrayList<Vecteur>();
	}

	/** Retourne le centre de la forme**/
	public Vecteur getCentre () {
		Vecteur ctr = new Vecteur();
		for (Vecteur pt : points) {
			ctr = ctr.add(pt);
		}
		Decimal dec = new Decimal(1d/points.size());
		ctr = ctr.multiply(dec);
		return ctr;
	}
	
	/**
	 * Retaille la forme en rognant la distance demandée
	 * 
	 * @param negate
	 * @return
	 */
	public Area resize(Decimal enPlus) {
		Area ret = new Area();
		Vecteur ctr = this.getCentre();
		for (Vecteur v : points) {
			// Calcule la nouvelle position du vecteur
			Decimal l = v.distance(ctr);
			Decimal coef = l.add(enPlus).divide(l); 
			// Applique le coeficient au point
			Vecteur pt = ctr.add(v.minus(ctr).multiply(coef));
			ret.points.add(pt);
		}
		return ret;
	}

}
