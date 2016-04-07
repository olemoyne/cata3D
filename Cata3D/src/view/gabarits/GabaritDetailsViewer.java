package view.gabarits;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Point2D;

import javax.swing.JPanel;

import appli.Logger;
import view.scene.GabaritScene;
import view.scene.PrintedGabarit;

public class GabaritDetailsViewer extends JPanel implements MouseWheelListener  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3990462677794567308L;

	/**
	 * Scene de visualisation des gabarits  
	 *    -> Liste des diff�rents gabarits (Choice / Select)
	 *    -> D�tail de visualisation
	 */
	
	private GabaritPlanViewer vue;
	private GabaritInformation position;
	
	private GabaritScene scene;
	private int element; 
	
	/**
	 * Creation de la vue 2D
	 */
	public GabaritDetailsViewer (Logger log) {
		super();
		setBackground(Color.WHITE);
		setLayout(new BorderLayout());
		// Ajoute les informations en haut
		position = new GabaritInformation();
		this.add(position, BorderLayout.NORTH);
		
		// Vue du plan du Gabarit
		vue = new GabaritPlanViewer (position, this);
		add(vue, BorderLayout.CENTER);
		
	}
	
	/**
	 * Affiche la scene demand�e
	 * 
	 * @param gab
	 */
	public void setScene (GabaritScene gab) {
		this.scene = gab;
		element = 0;
		showElement(null);
		repaint();
	}

	public void showElement(Point2D pt) {
		if (element >= this.scene.allObjects.size()) element = this.scene.allObjects.size()-1;
		if (element < 0) element = 0;

		PrintedGabarit gab = (PrintedGabarit) this.scene.allObjects.get(element);
		this.vue.setGabarit(gab);

		this.position.epaisseur = gab.epaisseur;
		this.position.id = element;
		this.position.position = gab.zPosition;
		
		if (pt != null) vue.setMousePosition(pt);
		position.show();

		vue.repaint();
	}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		int r = e.getWheelRotation();
		if (r < 0) element -= 1;
		if (r > 0) element += 1;

		showElement(e.getPoint());
		
	}

}
