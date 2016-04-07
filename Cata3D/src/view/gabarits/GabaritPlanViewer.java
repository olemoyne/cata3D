package view.gabarits;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Point2D;

import javax.swing.JPanel;

import model.Area;
import model.calcul.CalculSurface;
import model.math.Axis;
import model.math.Decimal;
import model.math.Vecteur;
import view.scene.PrintedGabarit;

public class GabaritPlanViewer extends JPanel implements MouseMotionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3990462677794567308L;

	/**
	 * Scene de visualisation d'un agbarit  
	 *    -> Dessin du gabarit en fonction des donnï¿½es 
	 *    -> Dessin de la mer
	 *    -> Dessin des trous liï¿½s aux poutres
	 *    
	 *    --> Possibilitï¿½ de positionner la souris sur un des ï¿½lï¿½ments 
	 */
	
	private PrintedGabarit gabarit;

	private double echelle;
	private int ctrX;
	private int ctrY;

	private Rectangle dim;
	
	public PrintingParameters params;
	
	private GabaritInformation logManager;
	
	/**
	 * Creation de la vue 2D
	 */
	public GabaritPlanViewer (GabaritInformation position, MouseWheelListener list) {
		super();
		params = new PrintingParameters();
		logManager = position;
		addMouseMotionListener(this);
		addMouseWheelListener(list);
	}

	
	private int getX (Vecteur v) {
		return (int)Math.round(ctrX + (v.minus(gabarit.bns.getMin())).getX()*echelle);
	}

	
	private int getY (Vecteur v) {
		return (int)Math.round((ctrY + (gabarit.bns.getMax().minus(v)).getY()*echelle));
	}

	private Vecteur getPosition(Point2D pt) {
		Decimal x = new Decimal( (pt.getX()-ctrX)/(echelle*Vecteur.LMETER)).add(gabarit.bns.getMin().getDecX());
		Decimal y = gabarit.bns.getMax().getDecY().minus(new Decimal( (pt.getY()-ctrY)/(echelle*Vecteur.LMETER)));
		return new Vecteur (x, y, gabarit.zPosition);
	}

	
	private void drawArea (Graphics gr, Area surf) {
		if (surf.points.size()== 0) return;
		Vecteur v = null;
		for (int p = 1; p < surf.points.size(); p ++) {
			v = surf.points.get(p);
			gr.drawLine(getX(surf.points.get(p-1)), getY(surf.points.get(p-1)),	getX(v), getY(v));
		}
		gr.drawLine(getX(surf.points.get(0)), getY(surf.points.get(0)),	getX(v), getY(v));
	}

	private void drawPoint (Graphics gr, Vecteur pt) {
		if (pt == null) return;
		
		int x = getX(pt);
		int y = getY(pt);
		
		gr.drawLine(x-5, y, x+5, y);
		gr.drawLine(x, y-5, x, y+5);
		
	}

	
	/** 
	 * Visualisation du gabarit
	 * 
	 */
	public void paintComponent(Graphics gr) {
		super.paintComponent(gr);
		if (gabarit == null) return;
		// Dessine le gabarit 
		dim = this.getBounds();
		double echelleX = (dim.getWidth()-20)/(gabarit.bns.getMax().getX() - gabarit.bns.getMin().getX()); 
		double echelleY = (dim.getHeight()-20) / (gabarit.bns.getMax().getY() - gabarit.bns.getMin().getY());
		
		echelle = Math.min(echelleX, echelleY);
		
		ctrX = 10 + (int)Math.round(( (dim.getWidth()-20) - (gabarit.bns.getMax().getX() - gabarit.bns.getMin().getX())*echelle ) /2);
		ctrY = 10 + (int)Math.round(( (dim.getHeight()-20) - (gabarit.bns.getMax().getY() - gabarit.bns.getMin().getY())*echelle ) /2);
		
		
		// Affiche le fond en blanc
		gr.setColor(Color.WHITE);
		gr.fillRect(dim.x, dim.y, dim.width, dim.height);
		
		
		showQuad(gr);

		// Affiche la limite de flottaison en bleu
		gr.setColor(Color.BLUE);
		gr.drawLine(getX(gabarit.mer.getA()), getY(gabarit.mer.getA()), getX(gabarit.mer.getB()), getY(gabarit.mer.getB()));

		// Affiche le gabarit non resizé en rouge
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

		// Affiche les éléments adjacents en orange
		gr.setColor(Color.ORANGE);
		for (Area a : gabarit.effacements) {
			drawArea(gr, a);
		}
		
		Vecteur pt = CalculSurface.getCentre(gabarit.full.points, Axis.ZAxis);
		drawPoint(gr, pt);
		
		if (logManager.fullPosition != null) {
			gr.setColor(Color.RED);
			drawPoint(gr, logManager.fullPosition);			
		}

		if (logManager.insidePosition != null) {
			gr.setColor(Color.CYAN);
			drawPoint(gr, logManager.insidePosition);			
		}

	}
	
	/***
	 * affiche le quadrillage 
	 * **/
	private void showQuad (Graphics gr) {
		
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

	/**
	 * Affiche le gabarit 
	 * 
	 * @param gab
	 * @param mer
	 * @param poutres
	 */
	public void setGabarit(PrintedGabarit gab) {
		// Calcule le gabarit
		gabarit = gab;
	}

	
	public void setMousePosition(Point2D pt) {
		if (this.gabarit == null) return;
		Vecteur pos= getPosition(pt);
		this.logManager.mousePosition = pos;
		
		// Détermine le point du FULL le plus proche
		int id = gabarit.full.getNearestPoint(pos);
		logManager.fullPosition = gabarit.full.points.get(id);
		if (gabarit.fullInside.points.size() > id)
			logManager.insidePosition = gabarit.fullInside.points.get(id);

		this.logManager.show();
		// Affiche les points
		repaint();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		
	}


	@Override
	public void mouseMoved(MouseEvent e) {
		Point2D pt= e.getPoint();
		this.setMousePosition(pt);
	}


}
