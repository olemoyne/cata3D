package model.patch;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import model.math.Decimal;
import model.math.Vecteur;

public class CourbeParametree3D extends Courbe3D{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7577281047556208321L;

	public static ArrayList<Vecteur> getCurvePoints (Vecteur d, Vecteur c1, Vecteur c2, Vecteur f, int max) {
		if (d == null) throw new NullPointerException();
		if (c1 == null) throw new NullPointerException();
		if (c2 == null) throw new NullPointerException();
		if (f == null) throw new NullPointerException();

		ArrayList<Vecteur> alf = new ArrayList<Vecteur>();
		// Récupère les points sur le plan X-Y
		ArrayList<Point2D.Double> al1 = Courbe2D.getCurvePoints(new Point2D.Double(d.getDecX().doubleValue(), d.getDecY().doubleValue()),
				new Point2D.Double(c1.getDecX().doubleValue(), c1.getDecY().doubleValue()), new Point2D.Double(c2.getDecX().doubleValue(), c2.getDecY().doubleValue()),
				new Point2D.Double(f.getDecX().doubleValue(), f.getDecY().doubleValue()), max);
		// Récupère les points sur le plan X-Z
		ArrayList<Point2D.Double> al2 = Courbe2D.getCurvePoints(new Point2D.Double(d.getDecX().doubleValue(), d.getDecZ().doubleValue()),
				new Point2D.Double(c1.getDecX().doubleValue(), c1.getDecZ().doubleValue()), new Point2D.Double(c2.getDecX().doubleValue(), c2.getDecZ().doubleValue()),
				new Point2D.Double(f.getDecX().doubleValue(), f.getDecZ().doubleValue()), max);
		for (int pos = 0; pos < al1.size(); pos ++) {
			Point2D.Double pt1 = (Point2D.Double)al1.get(pos);
			Point2D.Double pt2 = (Point2D.Double)al2.get(pos);
			Vecteur v = new Vecteur(new Decimal(pt1.getX()), new Decimal(pt1.getY()), new Decimal(pt2.getY()));
			alf.add(v);
		}
		return alf;
	}

}
