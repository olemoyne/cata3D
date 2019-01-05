package view.view2D;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import com.jogamp.opengl.GL2;

import model.Area;
import model.Position;
import model.math.Axis;
import model.math.Bounds3D;
import model.math.Decimal;
import model.math.Plan3D;
import model.math.Segment;
import model.math.Vecteur;
import view.scene.PrintableObject;

/**
 * Element de plan 
 * 
 * @author olemoyne
 *
 */
public class PrintedPlan extends PrintableObject{
	
	public class PrintedPlanElement {
		public Object element;
		public String type;
		public Color color;
		
		public PrintedPlanElement (Object o, String t, Color c) {
			element = o;
			type = t;
			color = c;
		}
		
	}

	/**
	 * Liste des elements affichables
	 */
	protected ArrayList<PrintedPlanElement> surfaces;
	protected ArrayList<PrintedPlanElement> lignes;
	protected Segment mer;
	
	/**
	 * Calcul des positions de le souris au survol
	 */
	protected Area primaryPlotingTarget;
	protected Area secondaryPlotingTarget;

	/** Axe de visualisation du plan **/
	protected int axis;
	
	/** zone d'affichage **/
	protected Bounds3D bnds;
	protected double echelle;
	protected int ctrX;
	protected int ctrY;

	protected Rectangle dim;

	
	/**
	 *  création d'un pan d'affichage
	 *   
	 * @param n
	 * @param c
	 * @param pos
	 */
	public PrintedPlan(String n, Bounds3D bounds, int ax) {
		super(n, null, new Position());
		bnds= bounds;
		axis = ax;
		
		surfaces = new ArrayList<PrintedPlanElement>();
		lignes = new ArrayList<PrintedPlanElement>();
	}
	
	
	/***
	 * Ajoute une surface dans le plan
	 * 
	 * @param a
	 * @param c
	 */
	public void addSurface (Area a, Color c) {
		surfaces.add(new PrintedPlanElement(a, "Surface", c));
	}

	/***
	 * Ajoute une ligne dans le plan
	 * 
	 * @param a
	 * @param c
	 */
	public void addLigne (Area a, Color c) {
		surfaces.add(new PrintedPlanElement(a, "Line", c));
	}


	/** Calcule la position de la mer **/
	public void setMer(Plan3D pl) {
		Vecteur s = bnds.getMin();
		Vecteur e = new Vecteur (bnds.getMin().getDecX(), bnds.getMax().getDecY(), bnds.getMin().getDecZ());
		Vecteur mstart = pl.intersection(e,  s);
	
		s = new Vecteur (bnds.getMax().getDecX(), bnds.getMin().getDecY(), bnds.getMin().getDecZ());
		e = new Vecteur (bnds.getMax().getDecX(), bnds.getMax().getDecY(), bnds.getMin().getDecZ());
		Vecteur mend= pl.intersection(e,  s);
	
		mer = new Segment (mstart, mend);
	}

	
	protected int getX (Vecteur v) {
		if (axis == Axis.ZAxis) {
			return (int)Math.round(ctrX + (v.minus(bnds.getMin())).getX()*echelle);
		} else {
			return (int)Math.round(ctrX + (v.minus(bnds.getMin())).getZ()*echelle);
		}
	}

	
	protected int getY (Vecteur v) {
		return (int)Math.round((ctrY + (bnds.getMax().minus(v)).getY()*echelle));
	}
	
	public Vecteur getPosition(Point2D pt) {
		if (bnds == null) return new Vecteur();
		if (pt == null) return new Vecteur();
		
		Decimal x = null; 
		Decimal y = bnds.getMax().getDecY().minus(new Decimal( (pt.getY()-ctrY)/(echelle*Vecteur.LMETER)));;
		if (axis == Axis.ZAxis) {
			x = new Decimal( (pt.getX()-ctrX)/(echelle*Vecteur.LMETER)).add(bnds.getMin().getDecX()); 
		} else {
			x = new Decimal( (pt.getY()-ctrX)/(echelle*Vecteur.LMETER)).add(bnds.getMin().getDecZ());
		}
		return new Vecteur (x, y, bnds.getMax().getDecZ());
	}

	
	public void drawArea (Graphics gr, Area surf) {
		if (surf == null) return;
		if (surf.points.size()== 0) return;
		Vecteur v = null;
		for (int p = 1; p < surf.points.size(); p ++) {
			v = surf.points.get(p);
			gr.drawLine(getX(surf.points.get(p-1)), getY(surf.points.get(p-1)),	getX(v), getY(v));
		}
		gr.drawLine(getX(surf.points.get(0)), getY(surf.points.get(0)),	getX(v), getY(v));
	}

	public void drawLine (Graphics gr, Area surf) {
		if (surf == null) return;
		if (surf.points.size()== 0) return;
		Vecteur v = null;
		for (int p = 1; p < surf.points.size(); p ++) {
			v = surf.points.get(p);
			gr.drawLine(getX(surf.points.get(p-1)), getY(surf.points.get(p-1)),	getX(v), getY(v));
		}
	}

	
	public void drawPoint (Graphics gr, Vecteur pt) {
		if (pt == null) return;
		
		int x = getX(pt);
		int y = getY(pt);
		
		gr.drawLine(x-5, y, x+5, y);
		gr.drawLine(x, y-5, x, y+5);
		
	}

	
	public void addComment (Graphics gr) {
/**		if (gr != null) {
			if (fullPosition != null) {
				gr.setColor(Color.RED);
				drawPoint(gr, fullPosition);			
			}
	
			if (insidePosition != null) {
				gr.setColor(Color.CYAN);
				drawPoint(gr, insidePosition);			
			}
		}
**/	}

	public void drawObject(Graphics gr, boolean quad, boolean fond ) {
		// Affiche le fond en blanc
		if (fond) {
			gr.setColor(Color.WHITE);
			gr.fillRect(dim.x, dim.y, dim.width, dim.height);
		}
		
		if (quad) showQuad(gr);

		// Affiche les surfaces
		for (PrintedPlanElement elem : surfaces) {
			gr.setColor(elem.color);
			this.drawArea(gr, (Area)elem.element);
		}
		
		// Affiche les lignes
		for (PrintedPlanElement elem : lignes) {
			gr.setColor(elem.color);
			this.drawLine(gr, (Area)elem.element);
		}
	}
	
	/***
	 * affiche le quadrillage 
	 * **/
	public void showQuad (Graphics gr) {
		
		Decimal gauche = bnds.getMin().getDecX();
		Decimal droite = bnds.getMax().getDecX();
		
		// affiche le quadrillage tous les CM 
		Decimal larg = bnds.getMax().getDecX().minus(bnds.getMin().getDecX()); 
		larg = larg.multiply(Decimal.CENT); // Centimetres
		
		int start = (int)Math.floor(gauche.multiply(Decimal.CENT).doubleValue());
		double nb = larg.doubleValue(); 
		
		Decimal haut = bnds.getMax().getDecY();
		Decimal bas = bnds.getMin().getDecY();
		
		gr.setColor(new Color (0.80f, 0.80f, 0.80f));
		for (int pos = start; pos < start + nb + 1; pos ++) {
			Vecteur h = new Vecteur (new Decimal(pos*0.01d), haut, Decimal.ZERO);
			Vecteur b = new Vecteur (new Decimal(pos*0.01d), bas, Decimal.ZERO);
			gr.drawLine(getX(h), getY(h), getX(b), getY(b));			
		}

		larg = haut.minus(bas); 
		larg = larg.multiply(Decimal.CENT); // Centimetres
		
		start = (int)Math.floor(bas.multiply(Decimal.CENT).doubleValue());
		nb = larg.doubleValue(); 
		
		for (int pos = start; pos < start + nb + 1; pos ++) {
			Vecteur g = new Vecteur (gauche, new Decimal(pos*0.01d), Decimal.ZERO);
			Vecteur d = new Vecteur (droite, new Decimal(pos*0.01d), Decimal.ZERO);
			gr.drawLine(getX(g), getY(g), getX(d), getY(d));			
		}
	}

	public void setSize(Rectangle d) {
		dim = d;
		double echelleX = 0; 
		if (axis == Axis.ZAxis) {
			echelleX = (dim.getWidth()-20)/(bnds.getMax().getX() - bnds.getMin().getX());
		} else {
			echelleX = (dim.getWidth()-20)/(bnds.getMax().getZ() - bnds.getMin().getZ());
		}
		double echelleY = (dim.getHeight()-20) / (bnds.getMax().getY() - bnds.getMin().getY());
		
		echelle = Math.min(echelleX, echelleY);
		
		if (axis == Axis.ZAxis) {
			ctrX = dim.x + 10 + (int)Math.round(( (dim.getWidth()-20) - (bnds.getMax().getX() - bnds.getMin().getX())*echelle ) /2);
		} else {
			ctrX = dim.x + 10 + (int)Math.round(( (dim.getWidth()-20) - (bnds.getMax().getZ() - bnds.getMin().getZ())*echelle ) /2);
		}
		ctrY = dim.y + 10 + (int)Math.round(( (dim.getHeight()-20) - (bnds.getMax().getY() - bnds.getMin().getY())*echelle ) /2);
	}

	public void setSize(Rectangle d, int dpi) {
		dim = d;
		echelle = 1.2d;

		if (axis == Axis.ZAxis) {
			ctrX = dim.x + 10 + (int)Math.round(( (dim.getWidth()-20) - (bnds.getMax().getX() - bnds.getMin().getX())*echelle ) /2);
		} else {
			ctrX = dim.x + 10 + (int)Math.round(( (dim.getWidth()-20) - (bnds.getMax().getZ() - bnds.getMin().getZ())*echelle ) /2);
		}
		ctrY = dim.y + 10 + (int)Math.round(( (dim.getHeight()-20) - (bnds.getMax().getY() - bnds.getMin().getY())*echelle ) /2);
	}


	public String getMouseData(Point2D pt) {
		if (pt != null) {
			StringBuilder sb = new StringBuilder();
			sb.append(name);
			sb.append("\n");
			sb.append("Position : ");
			Vecteur pos =this.getPosition(pt); 
			sb.append(pos.toString());
			
			if (this.primaryPlotingTarget != null) {
				Vecteur p = primaryPlotingTarget.getNearestPoint(pos);
				if (p != null) {
					sb.append("\n");
					sb.append("Position rouge : ");
					sb.append(p.toString());
				}
			}

			if (this.secondaryPlotingTarget != null) {
				Vecteur p = secondaryPlotingTarget.getNearestPoint(pos);
				if (p != null) {
					sb.append("\n");
					sb.append("Position noire : ");
					sb.append(p.toString());
				}
			}

			return sb.toString();
		} else {
			return name;
		}
	}

	
	
	
	
	/** Pas d'implementation de type 3D **/
	public void drawObject(GL2 gl, int mode) {
		
	}
	
	
	
}
