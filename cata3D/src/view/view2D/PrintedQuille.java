package view.view2D;

import java.awt.Color;
import java.util.ArrayList;

import model.Area;
import model.Quille;
import model.calcul.CalculArea;
import model.calcul.CalculSurface;
import model.composants.PatchVide;
import model.math.Axis;
import model.math.Decimal;
import model.math.Droite3D;
import model.math.Segment;
import model.math.Vecteur;
import model.math.transfo.Transformation;
import model.math.transfo.Translation;

public class PrintedQuille extends PrintedPlan {	
	
	/** 
	 * Construit les éléments du gabarit à afficher 
	 * **/
	public PrintedQuille(Quille q, PatchVide coque, Color c) {
		super(q.toString(), q.getAffichage().getBounds(), Axis.XAxis);

		setMer(coque.boat.mer.getPlan());
		
		// Ajoute les �l�ments � afficher de type area
		addSurface(q.getAffichage(), Color.RED);
		addLigne(q.surfaceZero, Color.GRAY);
		addLigne(q.surfaceEpaisseur, Color.GRAY);
		
		this.primaryPlotingTarget = q.getAffichage();
		this.secondaryPlotingTarget = null;
	}

	public String toString() {
		return name;
	}
	
	
	public String getPlanType() {
		return "Quille";
	}

	
	public String getStringDescr() {
		return "Quille";
	}

	
	public static Vecteur getNormal(Segment s, Decimal ep) {
        Vecteur d = s.getB().minus(s.getA());
        Vecteur norme = new Vecteur(Decimal.ZERO, d.getDecZ().negate(), d.getDecX());

    	if (norme.getNorme().isZero()) return new Vecteur(); 
        Decimal coef = ep.divide(norme.getNorme());
        return norme.multiply(coef);	
	}

	
    public Area reduceNormal(Area a, Decimal ep, int axis) {
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
        
        boolean sens = CalculArea.getTrigo(ctr, t);

        // Parcours la liste des points
        for (int i = 0; i < t.size(); i++) {
            Vecteur pt = t.points.get(i);

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
        } // Fin de la boucle sur les points
        
        // Check if the area is well oriented
        reduced.points.addAll(pts);
        Area clean = CalculArea.checkTrigo(ctr, reduced);

        Transformation back = trans.getReverse(null);
        if (clean != null) return clean.transform(back);
        return new Area();
    }


	
}
