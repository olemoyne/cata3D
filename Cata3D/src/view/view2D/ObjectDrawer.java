package view.view2D;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Point2D;

import model.Area;
import model.math.Axis;
import model.math.Bounds;
import model.math.Decimal;
import model.math.Vecteur;
import view.scene.PrintableObject;

public class ObjectDrawer {
	protected double echelle;
	protected int ctrX;
	protected int ctrY;

	protected Rectangle dim;
	private PrintableObject object;
	
	protected int axis;
	

	public ObjectDrawer (Rectangle d){
		axis = Axis.ZAxis;
	}

	
	public ObjectDrawer (Rectangle d, PrintableObject g){
		this.setObject(g, d);
		axis = Axis.ZAxis;
	}

	public ObjectDrawer (Rectangle d, PrintableObject g, int dpi){
		this.setObject(g, d, dpi);
	}

	protected int getX (Vecteur v) {
		Bounds bns = object.getBounds();

		if (axis == Axis.ZAxis) {
			return (int)Math.round(ctrX + (v.minus(bns.getMin())).getX()*echelle);
		} else {
			return (int)Math.round(ctrX + (v.minus(bns.getMin())).getZ()*echelle);
		}
	}

	
	protected int getY (Vecteur v) {
		Bounds bns = object.getBounds();

		return (int)Math.round((ctrY + (bns.getMax().minus(v)).getY()*echelle));
	}
	
	public Vecteur getPosition(Point2D pt) {
		Bounds bns = object.getBounds();

		Decimal x = null; 
		Decimal y = bns.getMax().getDecY().minus(new Decimal( (pt.getY()-ctrY)/(echelle*Vecteur.LMETER)));;
		if (axis == Axis.ZAxis) {
			x = new Decimal( (pt.getX()-ctrX)/(echelle*Vecteur.LMETER)).add(bns.getMin().getDecX()); 
		} else {
			x = new Decimal( (pt.getY()-ctrX)/(echelle*Vecteur.LMETER)).add(bns.getMin().getDecZ());
		}
		return new Vecteur (x, y, object.getZPosition());
	}

	
	public void drawArea (Graphics gr, Area surf) {
		if (surf.points.size()== 0) return;
		Vecteur v = null;
		for (int p = 1; p < surf.points.size(); p ++) {
			v = surf.points.get(p);
			gr.drawLine(getX(surf.points.get(p-1)), getY(surf.points.get(p-1)),	getX(v), getY(v));
		}
		gr.drawLine(getX(surf.points.get(0)), getY(surf.points.get(0)),	getX(v), getY(v));
	}

	public void drawPoint (Graphics gr, Vecteur pt) {
		if (pt == null) return;
		
		int x = getX(pt);
		int y = getY(pt);
		
		gr.drawLine(x-5, y, x+5, y);
		gr.drawLine(x, y-5, x, y+5);
		
	}

	
	public void addComment (Graphics gr) {
	}

	public void drawObject(Graphics gr, boolean quad, boolean fond ) {
	}
	
	/***
	 * affiche le quadrillage 
	 * **/
	public void showQuad (Graphics gr) {
		
		Bounds bns = object.getBounds();

		
		Decimal gauche = bns.getMin().getDecX();
		Decimal droite = bns.getMax().getDecX();
		
		// affiche le quadrillage tous les CM 
		Decimal larg = bns.getMax().getDecX().minus(bns.getMin().getDecX()); 
		larg = larg.multiply(Decimal.CENT); // Centimetres
		
		int start = (int)Math.floor(gauche.multiply(Decimal.CENT).doubleValue());
		double nb = larg.doubleValue(); 
		
		Decimal haut = bns.getMax().getDecY();
		Decimal bas = bns.getMin().getDecY();
		
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

	public void setObject(PrintableObject g, Rectangle d) {
		dim = d;
		this.object = g;
		
		Bounds bns = object.getBounds();

		double echelleX = 0; 
		if (axis == Axis.ZAxis) {
			echelleX = (dim.getWidth()-20)/(bns.getMax().getX() - bns.getMin().getX());
		} else {
			echelleX = (dim.getWidth()-20)/(bns.getMax().getZ() - bns.getMin().getZ());
		}
		double echelleY = (dim.getHeight()-20) / (bns.getMax().getY() - bns.getMin().getY());
		
		echelle = Math.min(echelleX, echelleY);
		
		if (axis == Axis.ZAxis) {
			ctrX = dim.x + 10 + (int)Math.round(( (dim.getWidth()-20) - (bns.getMax().getX() - bns.getMin().getX())*echelle ) /2);
		} else {
			ctrX = dim.x + 10 + (int)Math.round(( (dim.getWidth()-20) - (bns.getMax().getZ() - bns.getMin().getZ())*echelle ) /2);
		}
		ctrY = dim.y + 10 + (int)Math.round(( (dim.getHeight()-20) - (bns.getMax().getY() - bns.getMin().getY())*echelle ) /2);
	}

	public void setObject(PrintableObject g, Rectangle d, int dpi) {
		dim = d;
		this.object = g;

		echelle = 1.2d;

		Bounds bns = object.getBounds();

		if (axis == Axis.ZAxis) {
			ctrX = dim.x + 10 + (int)Math.round(( (dim.getWidth()-20) - (bns.getMax().getX() - bns.getMin().getX())*echelle ) /2);
		} else {
			ctrX = dim.x + 10 + (int)Math.round(( (dim.getWidth()-20) - (bns.getMax().getZ() - bns.getMin().getZ())*echelle ) /2);
		}
		ctrY = dim.y + 10 + (int)Math.round(( (dim.getHeight()-20) - (bns.getMax().getY() - bns.getMin().getY())*echelle ) /2);
	}


	public String getData(PrintableObject gab) {
		/**		
		this.position.epaisseur = gab.epaisseur;
		this.position.id = element;
		this.position.position = gab.zPosition;
		 **/		
		return null;
	}

	public String getMouseData(PrintableObject gab, Point2D pt) {
		/**		
		this.position.epaisseur = gab.epaisseur;
		this.position.id = element;
		this.position.position = gab.zPosition;
		 **/		
		return null;
	}


}
