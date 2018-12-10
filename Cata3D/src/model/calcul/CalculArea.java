package model.calcul;

import java.util.ArrayList;

import model.Area;
import model.math.Axis;
import model.math.Decimal;
import model.math.Droite3D;
import model.math.Segment;
import model.math.Vecteur;
import model.math.transfo.Transformation;
import model.math.transfo.Translation;

public class CalculArea {
	
	
	/** r�ducing an AREA with an appropriate algorythme 
	 * 
	 */
	public static Area reduceVertical(Area a, Decimal ep) {
		Area ret = new Area();
		/***  Assume un r�tr�cissement sur le plan X = 0 ****/
		// Calcul du centre
		Vecteur ctr = CalculSurface.getCentre(a.points, Axis.ZAxis);
		// D�cale les points pour centrer en X
		if (ctr == null) return a;
		Translation trans = new Translation(ctr.negat(), null);

		
		// Translate la forme sur le centre
		ArrayList<Vecteur> pts = new ArrayList<Vecteur>();
		for (Vecteur pt : a.points)  {
			pts.add(trans.transforme(pt));
		}

		/* D�termine les points Hauts et BAS
		Bounds bnds = new Bounds();
		bnds.setBounds(pts);
		Decimal max = bnds.getMax().getDecY().minus(ep);
		Decimal min = bnds.getMin().getDecY().add(ep); **/

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
/**		
		ArrayList<Vecteur> less = new ArrayList<Vecteur>();
		for (Vecteur pt : reduced) {
			// Calcule le MAX et le MIN en fonction de la valeur du X 
			Segment seg = getIntersectionVerticale(pts, pt.getDecX());
			if (seg != null) {
				Decimal max = seg.getA().getDecY().minus(ep);
				Decimal min = seg.getB().getDecY().add(ep); 
				
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
		} **/
		
		// D�cale les points pour centrer en X
		Transformation back = trans.getReverse(null); 
		for (Vecteur pt : reduced)  ret.points.add(back.transforme(pt));

		return ret;
	}

	/** r�ducing an AREA with an appropriate algorythme 
	 * 
	 */
	public static Area reduceHorizontal(Area a, Decimal ep) {
		Area ret = new Area();
		/***  Assume un r�tr�cissement sur le plan X = 0 ****/
		// Calcul du centre
		Vecteur ctr = CalculSurface.getCentre(a.points, Axis.ZAxis);
		// D�cale les points pour centrer en Y
		Translation trans = new Translation(ctr.negat(), null);

		
		// Translate la forme sur le centre
		ArrayList<Vecteur> pts = new ArrayList<Vecteur>();
		for (Vecteur pt : a.points)  {
			pts.add(trans.transforme(pt));
		}

		/* D�termine les points Hauts et BAS
		Bounds bnds = new Bounds();
		bnds.setBounds(pts);
		Decimal max = bnds.getMax().getDecY().minus(ep);
		Decimal min = bnds.getMin().getDecY().add(ep); **/

		// Parcours les points pour r�duire leur taille en Y
		ArrayList<Vecteur> reduced = new ArrayList<Vecteur>();
		Vecteur decale = new Vecteur (Decimal.ZERO, ep, Decimal.ZERO);
		for (Vecteur pt : pts) {
			if (pt.getDecY().isPositive()) {
				Vecteur reduit = pt.minus(decale);
				if (reduit.getDecY().isPositive()) reduced.add(reduit);
			}  
			if (pt.getDecY().isNegative()) {
				Vecteur reduit = pt.add(decale);
				if (reduit.getDecY().isNegative()) reduced.add(reduit);
			}
		}
		
/**		ArrayList<Vecteur> less = new ArrayList<Vecteur>();
		for (Vecteur pt : reduced) {
			// Calcule le MAX et le MIN en fonction de la valeur du Y 
			Segment seg = getIntersectionHorizontale(pts, pt.getDecY());
			if (seg != null) {
				Decimal max = seg.getA().getDecY().minus(ep);
				Decimal min = seg.getB().getDecY().add(ep); 
				
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
		}
**/		
		// D�cale les points pour centrer en X
		Transformation back = trans.getReverse(null); 
		for (Vecteur pt : reduced)  ret.points.add(back.transforme(pt));

		return ret;
	}

	
	
	public static Vecteur getNormal(Segment s, Decimal ep) {
    	Vecteur norme = s.getNormale();

    	if (norme.getNorme().isZero()) return new Vecteur(); 
        Decimal coef = ep.divide(norme.getNorme());
        return norme.multiply(coef);	
	}
    /**

     * Calcule une nouvelle surface correspondant à la surface initiale réduite de x

     * @param a

     * @param ep

     * @return

     */

    public static Area reduceNormal(Area a, Decimal ep) {
    	return reduceNormal(a, ep, Axis.ZAxis);
    }
	
    public static Area reduceNormal(Area a, Decimal ep, int axis) {
        // Tranformation of the area to the center of the area  

        // Calcul du centre
        Vecteur c = CalculSurface.getCentre(a.points, axis);

        if (c == null) return a;
        // Dï¿½cale les points pour centrer en Y
        Translation trans = new Translation(c.negat(), null);
        Area t = a.transform(trans);
		
        Area reduced = new Area();      
        ArrayList<Vecteur> pts = new ArrayList<Vecteur>();
   
        Vecteur ctr = new Vecteur();
        
        boolean sens = getTrigo(ctr, t);

        // Parcours la liste des points
        for (int i = 0; i < t.size(); i++) {
            Vecteur pt = t.points.get(i);

            if (pt.getDecX().abs().compareTo(ep) >= 0) {           
                Segment s1 = null; Segment s2 = null;
                if (i == 0 ) {
                    s1 = new Segment (t.lastPoint(), t.points.get(i));
                } else {
                    s1 = new Segment (t.points.get(i-1), t.points.get(i));               
                }
                if (i == t.size()-1) {
                    s2 = new Segment (t.points.get(i), t.points.get(0));
                } else {
                    s2 = new Segment (t.points.get(i), t.points.get(i+1));
                }
               
                Vecteur n1 = getNormal(s1, ep);
                Vecteur n2 = getNormal(s2, ep);
              
                Vecteur r1 = pt.add(n1);
                Vecteur r2 = pt.add(n2);

                // Calcul de l'angle formé par les segments pt - r1 / pt - r2
                Segment sg1 = new Segment(ctr, r1);
                Segment sg2 = new Segment(ctr, r2);
                Decimal angle = sg1.getAngle(sg2);

                if (angle.isPositive() == sens)  {
                    //Ajoute les deux points
                    pts.add(r1); pts.add(r2);
                } else {
                    // Calcule le point de césure
                    Vecteur v1 = s1.getA().add(n1);
                    Droite3D d1 = new Droite3D (r1.minus(v1), r1);
                    Vecteur v2 = s2.getB().add(n2);
                    Droite3D d2 = new Droite3D (r2.minus(v2), r2);
                    Vecteur inter = d1.getIntersection(d2);
                    if (inter == null) {
                        pts.add(r1);
                    } else {
                        pts.add(inter);
                    }
                }
            } // trop proche de l'axe
        } // Fin de la boucke sur les points
        
        // Check if the area is well oriented
        reduced.points.addAll(pts);
        Area clean = checkTrigo(ctr, reduced);

        Transformation back = trans.getReverse(null);
        if (clean != null) return clean.transform(back);
        return new Area();
    }

 

    /** Détermine si la forme évolue dans un sens trigonométrique

     * @param ctr **/

       public static boolean getTrigo(Vecteur ctr, Area t) {

             if (t.size() < 3) return false;

             Segment s1 = new Segment(ctr, t.points.get(0));
             Segment s2 = new Segment(ctr, t.points.get(1));
             Decimal angle = s1.getAngle(s2);
             return angle.isPositive();

       }

       
       public static Area checkTrigo(Vecteur ctr, Area t) {

           if (t.size() < 3) return null;
           
           Area ret = new Area();

           Segment s1 = new Segment(ctr, t.points.get(0));
           Segment s2 = new Segment(ctr, t.points.get(1));
           Segment lastSegment = s2;
    	   ret.points.add(t.points.get(0));
    	   ret.points.add(t.points.get(1));
           
           Decimal angle = s2.getAngle(s1);
           boolean sens =  angle.isPositive();
           
           for (int i = 2; i < t.points.size(); i++) {
               s1 = new Segment(ctr, t.points.get(i));
               angle = s1.getAngle(lastSegment);
               if (angle.isPositive() == sens) {
            	   ret.points.add(t.points.get(i));
               }
               lastSegment = s1;
           }
           
           return ret;
     }

 
}
