package view.view2D;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Point2D;
import java.util.Hashtable;

import javax.swing.JPanel;

import view.gabarits.GabaritDrawer;
import view.quille.QuilleDrawer;
import view.scene.PrintableObject;

public class PrintablePlanViewer extends JPanel implements MouseMotionListener{

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
	
	protected PrintableObject gabarit;

	
	protected PrintableInformation logManager;
	
	protected Hashtable<String, ObjectDrawer> drawers;
	
	private ObjectDrawer activeDrawer;
	
	/**
	 * Creation de la vue 2D
	 */
	public PrintablePlanViewer (PrintableInformation position, MouseWheelListener list) {
		super();
		logManager = position;
		addMouseMotionListener(this);
		addMouseWheelListener(list);
		
		drawers = new Hashtable<String, ObjectDrawer>();
		drawers.put("Gabarit", new GabaritDrawer(this.getBounds()));
		drawers.put("Quille", new QuilleDrawer(this.getBounds()));
		
		activeDrawer = null;
	}

	
	
	/** 
	 * Visualisation du gabarit
	 * 
	 */
	public void paintComponent(Graphics gr) {
		super.paintComponent(gr);
		if (gabarit == null) return;
		// Dessine le gabarit 

		String type = gabarit.getPlanType();
		ObjectDrawer drawer = drawers.get(type);
		if (drawer == null) return;
		
		drawer.drawObject(gr, true, true);
		drawer.addComment(gr);
	}
	

	/**
	 * Affiche le gabarit 
	 * 
	 * @param gab
	 * @param mer
	 * @param poutres
	 */
	public void setObject(PrintableObject  gab) {
		// Calcule le gabarit
		gabarit = gab;
		String type = gabarit.getPlanType();
		ObjectDrawer drawer = drawers.get(type);

		if (drawer == null) 
			drawer = new ObjectDrawer(this.getBounds(), gabarit);
		else 
			drawer.setObject(gabarit, this.getBounds());
		
		activeDrawer = drawer;
		logManager.mouseData = drawer.getData(gab);

	}

	
	public void setMousePosition(Point2D pt) {
		
		if (logManager != null) {
			if (activeDrawer != null)
				logManager.mouseData = activeDrawer.getMouseData(null, pt);
			
			this.logManager.show();
		}
		
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



	public String getData(PrintableObject gab) {
		String type = gab.getPlanType();
		ObjectDrawer drawer = drawers.get(type);

		return drawer.getData(gab);
	}

	public String getMousePosition(PrintableObject gab) {
		String type = gab.getPlanType();
		ObjectDrawer drawer = drawers.get(type);

		return drawer.getMouseData(gab, this.getMousePosition());	
	}


}
