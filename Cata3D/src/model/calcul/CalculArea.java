package model.calcul;

import java.util.ArrayList;

import model.Area;
import model.math.Axis;
import model.math.Bounds;
import model.math.Decimal;
import model.math.Vecteur;
import model.math.transfo.Transformation;
import model.math.transfo.Translation;

public class CalculArea {
	
	/** r�ducing an AREA with an appropriate algorythme 
	 * 
	 */
	public static Area reduce(Area a, Decimal ep) {
		Area ret = new Area();
		/***  Assume un r�tr�cissement sur le plan X = 0 ****/
		// Calcul du centre
		Vecteur ctr = CalculSurface.getCentre(a.points, Axis.ZAxis);
		// D�cale les points pour centrer en X
		Translation trans = new Translation(ctr.negat(), null);

		
		// Translate la forme sur le centre
		ArrayList<Vecteur> pts = new ArrayList<Vecteur>();
		for (Vecteur pt : a.points)  {
			pts.add(trans.transforme(pt));
		}

		// D�termine les points Hauts et BAS
		Bounds bnds = new Bounds();
		bnds.setBounds(pts);
		Decimal max = bnds.getMax().getDecY().minus(ep);
		Decimal min = bnds.getMin().getDecY().add(ep);

		// Parcours les points pour r�duire leur taille en X
		ArrayList<Vecteur> reduced = new ArrayList<Vecteur>();
		Vecteur decale = new Vecteur (ep, Decimal.ZERO, Decimal.ZERO);
		for (Vecteur pt : pts) {
			if (pt.getDecX().isPositive()) {
				Vecteur reduit = pt.minus(decale);
				if (reduit.getDecX().isPositive()) reduced.add(reduit);
			} 
			if (pt.getDecX().isNegative()) {
				Vecteur reduit = pt.add(decale);
				if (reduit.getDecX().isNegative()) reduced.add(reduit);
			}
		}
		
		ArrayList<Vecteur> less = new ArrayList<Vecteur>();
		for (Vecteur pt : reduced) {
			if (pt.getDecY().compareTo(max) > 0) {
				less.add(new Vecteur(pt.getDecX(), max, pt.getDecZ())); 
			} else {
				if (pt.getDecY().compareTo(min) < 0) {
					less.add(new Vecteur(pt.getDecX(), min, pt.getDecZ())); 
				} else { 
					less.add(pt);
				}
			}
		}
		
		// D�cale les points pour centrer en X
		Transformation back = trans.getReverse(null); 
		for (Vecteur pt : less)  ret.points.add(back.transforme(pt));

		return ret;
	}

}
