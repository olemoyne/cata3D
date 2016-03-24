package model;

import java.io.Serializable;
import java.util.ArrayList;

import model.calcul.CalculSurface;
import model.math.Axis;
import model.math.Decimal;
import model.math.Droite3D;
import model.math.Plan3D;
import model.math.Segment;
import model.math.Vecteur;

/** 
 * Liste de points dï¿½finissant une surface
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
	
	
	private class Point  {
		int pos;
		Vecteur inter;
	}
	
	public Area () {
		points = new ArrayList<Vecteur>();
		situation = new Position();
	}

	/**
	 * Retaille la forme en rognant la distance demandï¿½e
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
	
	// Le premier point est le point la plus grande abcisse et la plus grande ordonnée 
	private Point getFirstPoint () {
		Point ret = new Point();
		// Point d'interconnexion entre le plan X = 0
		Plan3D pl = Plan3D.getPlan(Axis.XAxis, Decimal.ZERO);
		Decimal maxY = Decimal.MILLE.negate();
		int maxId = -1;
		Vecteur inter = null;
		int p = 1;
		for (Segment seg : getSegments()) {
			Vecteur v = pl.intersection(seg.getA(), seg.getB());
			if (v!= null) {
				if (v.getDecY().compareTo(maxY) > 0) {
					maxY = v.getDecY(); maxId = p;
					inter = v;
				}	
			}
			p++;
		}
		if (inter == null) {
			ret.inter = points.get(0);
			ret.pos = 0;
		} else {
			ret.inter = inter;
			ret.pos = maxId%points.size();
		}
		return ret;
	}

	// Eclate l'aire en liste de points equidistants Ã  partir du point le plus haut et le plus Ã  droite 
	public ArrayList<Vecteur> split(int nbPoints) {
		ArrayList<Vecteur> ret = new ArrayList<Vecteur>();
		// Calcule le point de dÃ©part
		Point pti = this.getFirstPoint();
		int firstPoint = pti.pos;
		// calcule le pÃ©rimetre
		Decimal p = getPerimetre();
		Decimal step = p.divide(new Decimal(nbPoints));
		// Ajoute le premier point
		Vecteur last = pti.inter;//points.get(firstPoint);
		System.out.println("First = "+last.toString()+ " - "+firstPoint);
		int nextId = (firstPoint + 1)%points.size();
		Vecteur next = points.get(nextId);
		ret.add(last); // Ajoute le premier point
		Decimal current = step; // position du pas en cours
		boolean finished = false;
		while (!finished) {
			// Récupère le prochain pas
			Decimal len = last.decDistance(next);
			int comp = len.compareTo(current) ;
			if (comp > 0) { // Ecart est plus grand
				Droite3D dtr = new Droite3D(next.minus(last), last);
				// identifie le point
				Vecteur pt = dtr.getPoint(current);
				ret.add(pt);
				last = pt;
				current = step;
			}  
			if (comp == 0) { // Ecart est exact
				last = next;
				ret.add(next);
				nextId = (nextId + 1)%points.size();
				next = points.get(nextId);
				current = step; 
			}
			if (comp < 0) { // Ecart est plus petit
				last = next;
				nextId = (nextId + 1)%points.size();
				next = points.get(nextId);
				current = current.minus(len); 
			}
			if (ret.size() >= nbPoints)
				finished = true; 
		}
		return ret;
	}

	private Decimal getPerimetre() {
		Decimal p = Decimal.ZERO;
		Vecteur last = points.get(points.size()-1);
		for (Vecteur v : points) {
			p = p.add(last.decDistance(v));
			last = v;
		}
		return p;
	}
	
	public static final void main(String[] a) {
		Area a1 = new Area();
		a1.points.add(new Vecteur("0;0;1"));
		a1.points.add(new Vecteur("0;4;1"));
		a1.points.add(new Vecteur("3;4;1"));
		a1.points.add(new Vecteur("3;2;1"));
		a1.points.add(new Vecteur("3;0;1"));

		ArrayList<Vecteur> res = a1.split(10);
		if (res != null)
			System.out.println("Resultat 1 : "+res.toString());
		else System.out.println("Resultat 1 : pas d'intersection");
	}

}
