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

	public static int xMargin = 10;
	public static int yMargin = 10;

	
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
	private ArrayList<PrintedPlanElement> surfaces;
	private ArrayList<PrintedPlanElement> lignes;
	
	/**
	 * Calcul des positions de le souris au survol
	 */
	protected Area primaryPlotingTarget;
	protected Area secondaryPlotingTarget;

	/** Axe de visualisation du plan **/
	protected int planAxis;
	protected int xAxis;
	protected int yAxis;
	
	/** zone d'affichage **/
	protected Bounds3D bnds;
	protected double echelle;
	protected int ctrX;
	protected int ctrY;

	protected Rectangle dim;
	
	private Plan3D mer;

	
	/**
	 *  création d'un pan d'affichage
	 *   
	 * @param n
	 * @param c
	 * @param pos
	 */
	public PrintedPlan(String n, int ax) {
		super(n, null, new Position());
		bnds= new Bounds3D();
		planAxis = ax;
		if (planAxis == Axis.XAxis) {
			xAxis = Axis.ZAxis;
			yAxis = Axis.YAxis;
		}
		if (planAxis == Axis.ZAxis) {
			xAxis = Axis.XAxis;
			yAxis = Axis.YAxis;
		}
		if (planAxis == Axis.YAxis) {
			xAxis = Axis.ZAxis;
			yAxis = Axis.XAxis;
		}
		
		surfaces = new ArrayList<PrintedPlanElement>();
		lignes = new ArrayList<PrintedPlanElement>();
	}
	
	
	/**
	 *  Retourne le bounds en fonction de l'angle de vue 
	 * 
	 */
	public Bounds3D getBounds() {
		Bounds3D b = new Bounds3D();
		b.addPoint(new Vecteur(bnds.getMax().getDec(this.xAxis), bnds.getMax().getDec(yAxis), Decimal.ZERO));
		b.addPoint(new Vecteur(bnds.getMin().getDec(this.xAxis), bnds.getMin().getDec(yAxis), Decimal.ZERO));
		return b;
	}
	
	/***
	 * Ajoute une surface dans le plan
	 * 
	 * @param a
	 * @param c
	 */
	public void addSurface (Area a, Color c) {
		surfaces.add(new PrintedPlanElement(a, "Surface", c));
		bnds.add(a);
	}

	/***
	 * Ajoute une ligne dans le plan
	 * 
	 * @param a
	 * @param c
	 */
	public void addLigne (Area a, Color c) {
		surfaces.add(new PrintedPlanElement(a, "Line", c));
		bnds.add(a);
	}


	/** Calcule la position de la mer **/
	public void setMer(Plan3D pl) {
		mer = pl;
	}
	
	private Segment getMer() {
		Vecteur s = bnds.getMin();
		Vecteur e = new Vecteur (bnds.getMin().getDecX(), bnds.getMax().getDecY(), bnds.getMin().getDecZ());
		Vecteur mstart = mer.intersection(e,  s);
	
		s = new Vecteur (bnds.getMax().getDecX(), bnds.getMin().getDecY(), bnds.getMin().getDecZ());
		e = new Vecteur (bnds.getMax().getDecX(), bnds.getMax().getDecY(), bnds.getMin().getDecZ());
		Vecteur mend= mer.intersection(e,  s);
	
		return new Segment (mstart, mend);
	}

	
	protected int getX (Vecteur v) {
		double x = ctrX + (v.minus(bnds.getMin())).get(xAxis)*echelle;
		return ((int)Math.round(x));
	}

	
	protected int getY (Vecteur v) {
		return (int)Math.round((ctrY + (bnds.getMax().minus(v)).get(yAxis)*echelle));
	}
	
	public Vecteur getPosition(Point2D pt) {
		if (bnds == null) return new Vecteur();
		if (pt == null) return new Vecteur();
		
		Decimal y = bnds.getMax().getDec(yAxis).minus(new Decimal( (pt.getY()-ctrY)/(echelle*Vecteur.LMETER)));
		Decimal x = new Decimal( (pt.getX()-ctrX)/(echelle*Vecteur.LMETER)).add(bnds.getMin().getDec(xAxis)); 
		Decimal z = bnds.getMax().getDec(planAxis);
		
		return new Vecteur(x, y, z);
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
		
		if (mer != null) {
			Segment m = getMer();
			gr.setColor(Color.BLUE);
			this.drawSegment(gr, m);
		}
	}
	
	private void drawSegment(Graphics gr, Segment m) {
		gr.drawLine(getX(m.getA()), getY(m.getA()),	getX(m.getB()), getY(m.getB()));		
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
		
		double echelleX = (dim.getWidth()-2*xMargin)/(bnds.getMax().get(xAxis) - bnds.getMin().get(xAxis));
		double echelleY = (dim.getHeight()-2*yMargin) / (bnds.getMax().get(yAxis) - bnds.getMin().get(yAxis));
		
		echelle = Math.min(echelleX, echelleY);

		ctrX = dim.x + xMargin + (int)Math.round(( (dim.getWidth()-2*xMargin) - (bnds.getMax().get(xAxis) - bnds.getMin().get(xAxis))*echelle ) /2);
		ctrY = dim.y + yMargin + (int)Math.round(( (dim.getHeight()-2*yMargin) - (bnds.getMax().get(yAxis) - bnds.getMin().get(yAxis))*echelle ) /2);
	}

	public void setSize(Rectangle d, int dpi) {
		dim = d;
		echelle = 1;

		ctrX = dim.x + xMargin + (int)Math.round(( (dim.getWidth()-2*xMargin) - (bnds.getMax().get(xAxis) - bnds.getMin().get(xAxis))*echelle ) /2);
		ctrY = dim.y + yMargin + (int)Math.round(( (dim.getHeight()-2*yMargin) - (bnds.getMax().get(yAxis) - bnds.getMin().get(yAxis))*echelle ) /2);
	}


	public String getMouseData(Point2D pt) {
		if (pt != null) {
			StringBuilder sb = new StringBuilder();
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


	public Vecteur getPrimaryPosition(Point2D pt) {
		Vecteur pos =this.getPosition(pt); 
		if (this.primaryPlotingTarget != null) {
			Vecteur p = primaryPlotingTarget.getNearestPoint(pos);
			return p;
		}
		return null;
	}

	public Vecteur getSecondaryPosition(Point2D pt) {
		Vecteur pos =this.getPosition(pt); 
		if (this.secondaryPlotingTarget != null) {
			Vecteur p = secondaryPlotingTarget.getNearestPoint(pos);
			return p;
		}
		return null;
	}


	/**
	 * Fonction utilisée pour imprimer en 3D
	 * @return
	 */
	public Decimal getEpaisseur() {
		return null;
	}

	

	/**
	 * Fonction utilisée pour imprimer en 3D
	 * @return
	 */
	public Area getPrintable() {
		return null;
	}


	/**
	 * Fonction utilisée pour imprimer en 3D
	 * @return
	 */
	public ArrayList<Area> getTrous() {
		return null;
	}


	public int getAxis() {
		return this.planAxis;
	}


	public boolean is3DPrintable() {
		return true;
	}

	
	
}
