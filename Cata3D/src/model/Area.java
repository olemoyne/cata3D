package model;

import java.io.Serializable;
import java.util.ArrayList;

import model.calcul.CalculSurface;
import model.math.Decimal;
import model.math.Segment;
import model.math.Vecteur;

/** 
 * Liste de points d�finissant une surface
 * 
 * @author olemoyne
 *
 */
public class Area implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4445241224165587862L;
	public ArrayList<Vecteur> points;
	
	public Position situation;
	
	public Area () {
		points = new ArrayList<Vecteur>();
		situation = new Position();
	}

	/**
	 * Retaille la forme en rognant la distance demand�e
	 * 
	 * @param negate
	 * @return
	 */
	public Area resize(Decimal enPlus, int ax) {
		Area ret = new Area();
		Vecteur ctr = CalculSurface.getCentre(points, ax);
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

	public boolean isInside(Vecteur v) {
		Segment seg = new Segment(v, this.points.get(0));
		Decimal angle = Decimal.ZERO;
		for (int i = 0; i< points.size(); i++) {
			Segment seg2 = new Segment(v, this.points.get(i));
			angle = angle.add(seg.getAngle(seg2));
		}
		if (angle.equals(new Decimal(360f))) return true;
		return false;
	}	
	
	public ArrayList<Segment> getSegments (){
		ArrayList<Segment> segs = new ArrayList<Segment>();
		int last = points.size()-1;
		for (int pos = 0; pos < points.size(); pos ++) {
			Segment seg = new Segment(points.get(last), points.get(pos));
			segs.add(seg);
			last = pos;
		}
		return segs;
	}

	
	public int getIntersectionCount(Area other) {
		int nb = 0;
		ArrayList<Segment> mySegs = getSegments();
		for (Segment hisSeg : other.getSegments()) {
			for (Segment mySeg : mySegs) {
				Segment seg = hisSeg.intersection(mySeg);
				if (seg != null) {
					if ( (!seg.getA().equals(mySeg.getA())) && ((!seg.getB().equals(mySeg.getA())))) 
						nb ++;
				}
			}
		}
		return nb;
	}
}
