package view.gabarits;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JPanel;

import model.Area;
import model.math.Vecteur;
import view.scene.PrintedGabarit;

public class GabaritPlanViewer extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3990462677794567308L;

	/**
	 * Scene de visualisation d'un agbarit  
	 *    -> Dessin du gabarit en fonction des donn�es 
	 *    -> Dessin de la mer
	 *    -> Dessin des trous li�s aux poutres
	 *    
	 *    --> Possibilit� de positionner la souris sur un des �l�ments 
	 */
	
	private PrintedGabarit gabarit;

	private double echelle;

	private Rectangle dim;
	
	
	/**
	 * Creation de la vue 2D
	 */
	public GabaritPlanViewer () {
		super();
		
	}

	
	private int getX (Vecteur v) {
		return (int)Math.round((v.minus(gabarit.bns.getMin())).getX()*echelle);
	}

	private int getY (Vecteur v) {
		return (int)Math.round((gabarit.bns.getMax().minus(v)).getY()*echelle);
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
		
		// Affiche le fond en blanc
		gr.setColor(Color.WHITE);
		gr.fillRect(dim.x, dim.y, dim.width, dim.height);
		

		// Affiche la limite de flottaison en bleu
		gr.setColor(Color.BLUE);
		gr.drawLine(getX(gabarit.mer.getA()), getY(gabarit.mer.getA()), getX(gabarit.mer.getB()), getY(gabarit.mer.getB()));

		// Affiche le gabarit en darkGray
		gr.setColor(Color.DARK_GRAY);
		drawArea(gr, gabarit.fond);

		// Affiche le gabarit en noir
		gr.setColor(Color.BLACK);
		drawArea(gr, gabarit.devant);

		// Affiche les trous en vert
		gr.setColor(Color.GREEN);
		for (Area a : gabarit.trous) {
			drawArea(gr, a);
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
}
