package view.gabarits;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Point2D;

import javax.swing.JPanel;

import model.math.Vecteur;
import view.scene.PrintedGabarit;

public class GabaritPlanViewer extends JPanel implements MouseMotionListener{

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

	
	private GabaritInformation logManager;
	
	private GabaritDrawer drawer;
	
	/**
	 * Creation de la vue 2D
	 */
	public GabaritPlanViewer (GabaritInformation position, MouseWheelListener list) {
		super();
		logManager = position;
		addMouseMotionListener(this);
		addMouseWheelListener(list);
	}

	
	
	/** 
	 * Visualisation du gabarit
	 * 
	 */
	public void paintComponent(Graphics gr) {
		super.paintComponent(gr);
		if (gabarit == null) return;
		// Dessine le gabarit 

		drawer.drawGabarit(gr, true, true);
		
		if (logManager.fullPosition != null) {
			gr.setColor(Color.RED);
			drawer.drawPoint(gr, logManager.fullPosition);			
		}

		if (logManager.insidePosition != null) {
			gr.setColor(Color.CYAN);
			drawer.drawPoint(gr, logManager.insidePosition);			
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
		if (drawer == null) 
			drawer = new GabaritDrawer(this.getBounds(), gabarit);
		else 
			drawer.setGabarit(gabarit, this.getBounds());
	}

	
	public void setMousePosition(Point2D pt) {
		if (this.gabarit == null) return;
		Vecteur pos= drawer.getPosition(pt);
		this.logManager.mousePosition = pos;
		
		// D�termine le point du FULL le plus proche
		int id = gabarit.full.getNearestPoint(pos);
		logManager.fullPosition = gabarit.full.points.get(id);
		if (gabarit.fullInside.points.size() > id) {
			id = gabarit.fullInside.getNearestPoint(pos);
			logManager.insidePosition = gabarit.fullInside.points.get(id);
		}

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
