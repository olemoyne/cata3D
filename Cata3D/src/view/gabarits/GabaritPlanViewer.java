package view.gabarits;

import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;

import model.Area;
import model.Gabarit;
import model.Poutre;
import model.math.Droite3D;
import model.math.Plan3D;

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
	
	private Area gabarit;
	private ArrayList<Area> poutres;
	private Droite3D limite; 
	
	
	/**
	 * Creation de la vue 2D
	 */
	public GabaritPlanViewer () {
		super();
		
	}
	
	/** 
	 * Visualisation du gabarit
	 * 
	 */
	public void repaint(Graphics gr) {
		super.repaint(); // Effectue le repaint d�j� pr�vu
		// Dessine le gabarit 
	}

	/**
	 * Affiche le gabarit 
	 * 
	 * @param gab
	 * @param mer
	 * @param poutres
	 */
	public void setGabarit(Gabarit gab, Plan3D mer, ArrayList<Poutre> poutres) {
		// Calcule le gabarit
		
	}
}
