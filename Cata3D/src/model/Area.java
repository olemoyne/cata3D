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
	
	public Area(ArrayList<Vecteur> pts) {
		points = pts;
		situation = new Position();
	}

	public static Vecteur getMediatrice (Vecteur pt1, Vecteur pt2) {
		Vecteur dir = pt2.minus(pt1);
		Decimal n = dir.getNorme();
		Decimal x= dir.getDecY().negate().divide(n);
		Decimal y= dir.getDecX().divide(n);
		dir = new Vecteur (x, y, dir.getDecZ());
		return dir;
	}
	
	
	private static Vecteur getResizedPoint(Vecteur last, Vecteur my, Vecteur next, Vecteur ctr, Decimal delta) {
		Vecteur dir = null;
		if (!next.equals(last)) {
			if (next.minus(my).estColineaire(my.minus(last))) {
				// Dans ce cas on prends la droite perpendiculaire
				dir = getMediatrice(last, next);
			} else {
				Droite3D drt1 = new Droite3D(last.minus(my), my);
				Droite3D drt2 = new Droite3D(next.minus(my), my);
				Vecteur pt1 = drt1.getPoint(Decimal.UN);
				Vecteur pt2 = drt2.getPoint(Decimal.UN);
				Segment seg = new Segment (pt1, pt2);
				dir = seg.getCenter().minus(my);
			}

			if (!dir.getNorme().isZero()) {
				Droite3D dr = new Droite3D(dir, my);
				Vecteur p = dr.getPoint(delta.negate());
				Decimal dist = my.decDistance(ctr);
				Decimal dpt = p.decDistance(ctr);
				if (dpt.compareTo(dist) > 0) {
					p = dr.getPoint(delta);
				}
				return p;
			} else {
				System.err.println("Erreur : "+last.toString()+" "+my.toString()+" "+next.toString());
				return null;
			}
		}		
		return null;
	}

	/**
	 * Retaille la forme en rognant la distance demandï¿½e
	 * 
	 * @param negate
	 * @return
	 */
	public Area resizeNormale(Decimal enPlus, int ax) {
		Area ret = new Area();
		if (enPlus.isZero()) return this;
		if (points.size() == 0) return this;
		Vecteur ctr = CalculSurface.getCentre(points, ax);

		ArrayList<Vecteur> pts = getUnitedPoints();
		Decimal delta = enPlus;//.negate();
		Vecteur last = pts.get(pts.size()-1);
		for (int pos =0; pos < pts.size(); pos ++) {
			Vecteur my = pts.get(pos);
			Vecteur next = null;
			if (pos < pts.size() - 1) next = pts.get(pos + 1);
			else next = pts.get(0);
			
			Vecteur p = getResizedPoint(last, my, next, ctr, delta);
			if (p != null) ret.points.add(p);
			if (my.getY() == -186) {
				System.out.println(last.toString()+" ; "+my.toString()+" ; "+next.toString()+" : Resized = "+p.toString()+" + "+delta.toString());
			}
			last = my;
		}
		return ret;
	}

	private ArrayList<Vecteur> getUnitedPoints() {
		ArrayList<Vecteur> ret = new ArrayList<Vecteur> ();
		Vecteur last = points.get(points.size()-1);
		for (Vecteur v : points) {
			if (!v.equals(last)) ret.add(v); 
			last = v;
		}
		return ret;
	}

	/**
	 * Retaille la forme en rognant la distance demandï¿½e
	 * 
	 * @param negate
	 * @return
	 */
	public Area resizeCentre(Decimal enPlus, int ax) {
		Area ret = new Area();
		Vecteur ctr = CalculSurface.getCentre(points, ax);
		Decimal delta = enPlus.multiply(Vecteur.METER);
		for (Vecteur v : points) {
			// Calcule la nouvelle position du vecteur
			Decimal l = v.distance(ctr);
			if (!l.isZero()) {
				Decimal coef = l.add(delta).divide(l); 
			// Applique le coeficient au point
				Decimal x = ctr.getDecX().add(v.getDecX().minus(ctr.getDecX()).multiply(coef));
				Decimal y = ctr.getDecY().add(v.getDecY().minus(ctr.getDecY()).multiply(coef));
				Decimal z = ctr.getDecZ().add(v.getDecZ().minus(ctr.getDecZ()).multiply(coef));
				Vecteur pt = new Vecteur (x, y, z);
				ret.points.add(pt);
			} else {
				ret.points.add(v);
			}
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
		
/**		// (-0.0309;-0.0249;0.001) ; (-0.0314;-0.0219;0.001) ; (-0.032;-0.0186;0.001) : Resized = (-0.0354;-0.0219;0.001)
		// Calcul de resize point avec trois points 
		Vecteur n = new Vecteur ("-0.0320;-0.0186;0.13");
		Vecteur m = new Vecteur ("-0.0314;-0.0219;0.13");
		Vecteur l = new Vecteur ("-0.0309;-0.0249;0.13");
		
		Vecteur pt = Area.getResizedPoint(l, m, n, new Decimal(-0.004d));
		if (pt != null) {
			System.out.println(l.toString()+" ; "+m.toString()+" ; "+n.toString()+" : Resized = "+pt.toString());
			System.out.println("Dist = "+pt.decDistance(m).toString());
		}

		n = new Vecteur ("-0.0326;-0.0151;0.13");
		m = new Vecteur ("-0.0320;-0.0186;0.13");
		l = new Vecteur ("-0.0314;-0.0219;0.13");
		
		pt = Area.getResizedPoint(l, m, n, new Decimal(-0.004d));
		if (pt != null) {
			System.out.println(l.toString()+" ; "+m.toString()+" ; "+n.toString()+" : Resized = "+pt.toString());
			System.out.println("Dist = "+pt.decDistance(m).toString());
		}
*/
	}

	/**
	 * Rtetourne le point de la forme le plus proche
	 * 
	 * @param pos
	 */
	
	public int getNearestPoint(Vecteur pos) {
		int id = 0;
		Decimal dist = Decimal.MILLE;
		
		for (int p = 0; p < points.size(); p++) {
			Vecteur pt = points.get(p);
			Decimal d = pos.distance(pt).abs();
			if (d.compareTo(dist) < 0) {
				id = p;
				dist = d;
			}
		}
		return id;
	}

}
