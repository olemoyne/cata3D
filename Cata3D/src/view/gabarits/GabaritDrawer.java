package view.gabarits;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Point2D;

import model.Area;
import model.calcul.CalculSurface;
import model.math.Axis;
import model.math.Decimal;
import model.math.Vecteur;
import view.scene.PrintedGabarit;

public class GabaritDrawer {
	private double echelle;
	private int ctrX;
	private int ctrY;

	private Rectangle dim;
	private PrintedGabarit gabarit;

	
	public GabaritDrawer (Rectangle d, PrintedGabarit g){
		this.setGabarit(g, d);
	}

	public GabaritDrawer (Rectangle d, PrintedGabarit g, int dpi){
		this.setGabarit(g, d, dpi);
	}

	private int getX (Vecteur v) {
		return (int)Math.round(ctrX + (v.minus(gabarit.bns.getMin())).getX()*echelle);
	}

	
	private int getY (Vecteur v) {
		return (int)Math.round((ctrY + (gabarit.bns.getMax().minus(v)).getY()*echelle));
	}

	public Vecteur getPosition(Point2D pt) {
		Decimal x = new Decimal( (pt.getX()-ctrX)/(echelle*Vecteur.LMETER)).add(gabarit.bns.getMin().getDecX());
		Decimal y = gabarit.bns.getMax().getDecY().minus(new Decimal( (pt.getY()-ctrY)/(echelle*Vecteur.LMETER)));
		return new Vecteur (x, y, gabarit.zPosition);
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


	public void drawGabarit(Graphics gr, boolean quad, boolean fond) {
		
		// Affiche le fond en blanc
		if (fond) {
			gr.setColor(Color.WHITE);
			gr.fillRect(dim.x, dim.y, dim.width, dim.height);
		}
		
		if (quad) showQuad(gr);

		// Affiche la limite de flottaison en bleu
		gr.setColor(Color.BLUE);
		gr.drawLine(getX(gabarit.mer.getA()), getY(gabarit.mer.getA()), getX(gabarit.mer.getB()), getY(gabarit.mer.getB()));

		// Affiche le gabarit non resiz� en rouge
		gr.setColor(Color.RED);
		drawArea(gr, gabarit.full);

		// Affiche le gabarit en noir
		gr.setColor(Color.BLACK);
		drawArea(gr, gabarit.fullInside);

		// Affiche les trous en vert
		gr.setColor(Color.GREEN);
		for (Area a : gabarit.trous) {
			drawArea(gr, a);
		}

		// Affiche les �l�ments adjacents en orange
		gr.setColor(Color.ORANGE);
		for (Area a : gabarit.effacements) {
			drawArea(gr, a);
		}
		
		Vecteur pt = CalculSurface.getCentre(gabarit.full.points, Axis.ZAxis);
		drawPoint(gr, pt);

		
	}
	
	/***
	 * affiche le quadrillage 
	 * **/
	public void showQuad (Graphics gr) {
		
		Decimal gauche = gabarit.bns.getMin().getDecX();
		Decimal droite = gabarit.bns.getMax().getDecX();
		
		// affiche le quadrillage tous les CM 
		Decimal larg = gabarit.bns.getMax().getDecX().minus(gabarit.bns.getMin().getDecX()); 
		larg = larg.multiply(Decimal.CENT); // Centimetres
		
		int start = (int)Math.floor(gauche.multiply(Decimal.CENT).doubleValue());
		double nb = larg.doubleValue(); 
		
		Decimal haut = gabarit.bns.getMax().getDecY();
		Decimal bas = gabarit.bns.getMin().getDecY();
		
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

	public void setGabarit(PrintedGabarit g, Rectangle d) {
		dim = d;
		this.gabarit = g;

		double echelleX = (dim.getWidth()-20)/(gabarit.bns.getMax().getX() - gabarit.bns.getMin().getX()); 
		double echelleY = (dim.getHeight()-20) / (gabarit.bns.getMax().getY() - gabarit.bns.getMin().getY());
		
		echelle = Math.min(echelleX, echelleY);
		
		ctrX = dim.x + 10 + (int)Math.round(( (dim.getWidth()-20) - (gabarit.bns.getMax().getX() - gabarit.bns.getMin().getX())*echelle ) /2);
		ctrY = dim.y + 10 + (int)Math.round(( (dim.getHeight()-20) - (gabarit.bns.getMax().getY() - gabarit.bns.getMin().getY())*echelle ) /2);
	}

	public void setGabarit(PrintedGabarit g, Rectangle d, int dpi) {
		dim = d;
		this.gabarit = g;

		echelle = 1.2d;
		
		ctrX = dim.x + 10 + (int)Math.round(( (dim.getWidth()-20) - (gabarit.bns.getMax().getX() - gabarit.bns.getMin().getX())*echelle ) /2);
		ctrY = dim.y + 10 + (int)Math.round(( (dim.getHeight()-20) - (gabarit.bns.getMax().getY() - gabarit.bns.getMin().getY())*echelle ) /2);
	}



}
