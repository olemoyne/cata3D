package view.gabarits;

import javax.swing.JTextArea;

import model.math.Decimal;
import model.math.Vecteur;

public class GabaritInformation extends JTextArea {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3103898284174275227L;
	
	
	public int id;
	/**
	 * Position du gabarit
	 */
	public Decimal position;
	
	/**
	 * Epaisseur du gabarit
	 */
	public Decimal epaisseur;
	
	public Vecteur mousePosition;

	public Vecteur fullPosition;

	public Vecteur insidePosition;
	
	
	public GabaritInformation () {
		super();
		this.mousePosition = new Vecteur();
	}
	
	public void show() {
		// Affiche la position en Z
		StringBuffer sb = new StringBuffer("Gabarit ");
		sb.append(id);
		sb.append(" de position ");
		sb.append(position.toString());
		sb.append(" d'épaisseur ");
		sb.append(epaisseur.toString());
		sb.append("\n");
		sb.append("Mouse position : ");
		sb.append(mousePosition.toString());
		if (fullPosition != null) {
			sb.append("\t");
			sb.append("Full nearest point position : ");
			sb.append(fullPosition.toString());
		}
		if (insidePosition != null) {
			sb.append("\t");
			sb.append("Inside nearest point position : ");
			sb.append(insidePosition.toString());
		}

		setText(sb.toString());
	}

}
