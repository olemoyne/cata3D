package model.calcul;

import java.awt.geom.Point2D;
import java.io.PrintStream;
import java.util.ArrayList;

import math.geom2d.polygon.Polygon2D;
import math.geom2d.polygon.SimplePolygon2D;
import model.Area;
import model.math.Axis;
import model.math.Decimal;
import model.math.Vecteur;

public class CalculSurface {
	
	public static Vecteur getCentreGeometrique(Vecteur[] lst) {
		Vecteur c = new Vecteur();
		for (Vecteur v : lst) {
			c= c.add(v);
		}
		return c.multiply(Decimal.UN.divide(new Decimal(lst.length)));
	}

	public static Vecteur getCentreGeometrique(ArrayList<Vecteur> lst) {
		Vecteur c = new Vecteur();
		for (Vecteur v : lst) {
			c= c.add(v);
		}
		return c.multiply(Decimal.UN.divide(new Decimal(lst.size())));
	}

	
	public static Polygon2D getPoly(ArrayList<Vecteur> a, int ax) {
		SimplePolygon2D poly = new SimplePolygon2D();
		for (Vecteur v : a) {
			Point2D pt = v.get2D(ax);
			poly.addVertex(new math.geom2d.Point2D(pt.getX(), pt.getY()));
		}
		return poly;
	}
	
	/** 
	 * Calcul la surface 
	 * *
	public static Decimal getSurface(ArrayList<Vecteur> lst, int ax) {
		// FORMULE : Aire = 1/2 * somme[I=0 --> n-1] (XiYi+1 - YiXi+1)
		Decimal total = Decimal.ZERO;
		
		for (int i = 0; i < lst.size()-1; i++) {
			if (lst.get(i+1) == null) return Decimal.ZERO;
			Point2D.Double v = lst.get(i).get2D(ax);
			Point2D.Double vplusun = lst.get(i+1).get2D(ax);
			double dble = v.x*vplusun.y - v.y*vplusun.x;
//			Decimal sub = v.getDecY().multiply(vplusun.getDecZ()).minus(v.getDecZ().multiply(vplusun.getDecY()));
			total = total.add(new Decimal(dble));
		}
		
		return total.divide(Decimal.DEUX);		
	}


	/** 
	 * Calcul le centre de surface d'une aire
	 * *
	public static Vecteur getCentreSurface(ArrayList<Vecteur> lst, int ax) {
		// FORMULE : centre =(xg, yg) A = aire
		// xg = 1/(6*A) * somme[I=0 --> n-1] (Xi + Xi+1)*(XiYi+1 - YiXi+1)
		// yg = 1/(6*A) * somme[I=0 --> n-1] (Yi + Yi+1)*(XiYi+1 - YiXi+1)
		
		Decimal A = getSurface(lst, ax);
		Decimal abs = A.abs();
		if (abs.compareTo(new Decimal(0.000001)) <= 0) return new Vecteur();
		
		Decimal totX = Decimal.ZERO;
		Decimal totY = Decimal.ZERO;
		
		Decimal totZ = Decimal.ZERO;
		

		for (int i = 0; i < lst.size()-1; i++) {
			Point2D.Double v = lst.get(i).get2D(ax);
			Point2D.Double  vplusun = lst.get(i+1).get2D(ax);
			Decimal x = new Decimal(v.x+vplusun.x);
			Decimal y = new Decimal(v.y+vplusun.y);
			Decimal sub = new Decimal((v.x*vplusun.y) - (v.y*vplusun.x));
			totX = totX.add(sub.multiply(x));
			totY = totY.add(sub.multiply(y));
			totZ = totZ.add(lst.get(i).getDec(ax));
		}
		
		totZ = totZ.add(lst.get(lst.size()-1).getDec(ax));
		totZ = totZ.divide(new Decimal(lst.size()));
		
		Decimal delta = new Decimal(6).multiply(A);
		if (ax == Axis.XAxis) return new Vecteur (totZ, totX.divide(delta), totY.divide(delta));
		if (ax == Axis.YAxis) return new Vecteur (totX.divide(delta), totZ, totY.divide(delta));
		if (ax == Axis.ZAxis) return new Vecteur (totX.divide(delta), totY.divide(delta), totZ);
		return null;
	}

	public static Vecteur getCentreSurfaces(ArrayList<Area> surfs, int ax) {
		// FORMULE : moyenne pondérée des centre =(xg, yg) A = aire
		// xg = 1/(6*A) * somme[I=0 --> n-1] (Xi + Xi+1)*(XiYi+1 - YiXi+1)
		// yg = 1/(6*A) * somme[I=0 --> n-1] (Yi + Yi+1)*(XiYi+1 - YiXi+1)
		
		Decimal surfTotal = Decimal.ZERO;
		Decimal totalX = Decimal.ZERO;
		Decimal totalY = Decimal.ZERO;			
		Decimal totalZ = Decimal.ZERO;

		for (Area a : surfs) {
			ArrayList<Vecteur> lst = a.points;
			Decimal A = getSurface(lst, ax);
			Decimal abs = A.abs();
			if (abs.compareTo(new Decimal(0.000001)) > 0) {
				surfTotal = surfTotal.add(abs);

				Decimal totX = Decimal.ZERO;
				Decimal totY = Decimal.ZERO;			
				Decimal totZ = Decimal.ZERO;
		
				for (int i = 0; i < lst.size()-1; i++) {
					Point2D.Double v = lst.get(i).get2D(ax);
					Point2D.Double  vplusun = lst.get(i+1).get2D(ax);
					Decimal x = new Decimal(v.x+vplusun.x);
					Decimal y = new Decimal(v.y+vplusun.y);
					Decimal sub = new Decimal((v.x*vplusun.y) - (v.y*vplusun.x));
					totX = totX.add(sub.multiply(x));
					totY = totY.add(sub.multiply(y));
					totZ = totZ.add(lst.get(i).getDec(ax));
				}
				
				totZ = totZ.add(lst.get(lst.size()-1).getDec(ax));
				totZ = totZ.divide(new Decimal(lst.size()));
				
				Decimal delta = new Decimal(6);
				totalX = totalX.add(totX.divide(delta));
				totalY = totalY.add(totY.divide(delta));
				totalZ = totalZ.add(totZ);
			}
		}
		if (surfTotal.isZero()) return new Vecteur();
		
		if (ax == Axis.XAxis) return new Vecteur (totalZ, totalX.divide(surfTotal), totalY.divide(surfTotal));
		if (ax == Axis.YAxis) return new Vecteur (totalX.divide(surfTotal), totalZ, totalY.divide(surfTotal));
		if (ax == Axis.ZAxis) return new Vecteur (totalX.divide(surfTotal), totalY.divide(surfTotal), totalZ);
		return null;
	}

	
	/**
	 * Test des fonctions de calcule
	 * 
	 * @param args
	 */
	public static void main (String[] args) {
		ArrayList<Vecteur> pts = new ArrayList<Vecteur>();
		pts.add(new Vecteur("0;0;0.0019"));
		pts.add(new Vecteur("0;0;0.5575"));
		pts.add(new Vecteur("0;0;1.1132"));
		pts.add(new Vecteur("0;0;1.6687"));
		pts.add(new Vecteur("0;0;2.2244"));
		pts.add(new Vecteur("0;0;2.7799"));
		pts.add(new Vecteur("0;0;3.3356"));
		pts.add(new Vecteur("0;0;3.8911"));
		pts.add(new Vecteur("0;0;4.4468"));
		pts.add(new Vecteur("0;0;5.0024"));
		pts.add(new Vecteur("0;-0.1341;5.0043"));
		pts.add(new Vecteur("0;-0.195;4.4495"));
		pts.add(new Vecteur("0;-0.2303;3.8944"));
		pts.add(new Vecteur("0;-0.2525;3.3391"));
		pts.add(new Vecteur("0;-0.2645;2.7837"));
		pts.add(new Vecteur("0;-0.2696;2.2282"));
		pts.add(new Vecteur("0;-0.2681;1.6725"));
		pts.add(new Vecteur("0;-0.2582;1.1168"));
		pts.add(new Vecteur("0;-0.1356;0.5594"));
		pts.add(new Vecteur("0;-0.1039;0.0034"));
		

	}

	public static Vecteur getCentre(ArrayList<Vecteur>a, int axis) {
		Polygon2D poly = CalculSurface.getPoly(a, axis);
		return getCentre (poly, a.get(0).getDec(axis), axis);
	}

	public static Vecteur getCentreSurfaces(ArrayList<Area> l, int axis) {
		Decimal x = Decimal.ZERO; Decimal y= Decimal.ZERO; Decimal z= Decimal.ZERO;;
		Decimal aireTotale = Decimal.ZERO;
		for (Area a : l) {
			Polygon2D poly = CalculSurface.getPoly(a.points, axis);
			Vecteur v =getCentre (poly, a.points.get(0).getDec(axis), axis);
			Decimal aire = new Decimal(poly.area());
			
			aireTotale = aireTotale.add(aire);
			x = x.add(v.getDecX().multiply(aire));
			y = y.add(v.getDecY().multiply(aire));
			z = z.add(v.getDecZ().multiply(aire));
		}
		
		x = x.divide(aireTotale);
		y = y.divide(aireTotale);
		z = z.divide(aireTotale);
		return new Vecteur(x, y, z);
	}

	
	public static Vecteur getCentre(Polygon2D pol, Decimal val, int axis) {
		if (pol == null) return null;
		math.geom2d.Point2D pt = pol.centroid();
		if ( (Double.isNaN(pt.getX())) || (Double.isNaN(pt.getY())) ) {
			pt = pol.vertex(0);
		}
		if (axis == Axis.XAxis) return new Vecteur (val, new Decimal(pt.getX()), new Decimal(pt.getY()));
		if (axis == Axis.YAxis) return new Vecteur (new Decimal(pt.getX()), val, new Decimal(pt.getY()));
		if (axis == Axis.ZAxis) return new Vecteur (new Decimal(pt.getX()), new Decimal(pt.getY()), val);
		return null;
	}

	public static void writePol(PrintStream out, String msg, Polygon2D pol) {
		StringBuffer sb = new StringBuffer();
		sb.append(msg);
		sb.append(" ");
		for (math.geom2d.Point2D pt : pol.vertices()) {
			sb.append(pt.toString());
			sb.append(";");
		}
		out.println(sb.toString());
	}

}
