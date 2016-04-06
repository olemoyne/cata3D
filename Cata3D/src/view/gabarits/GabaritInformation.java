package view.gabarits;

import javax.swing.JLabel;

import model.math.Decimal;

public class GabaritInformation extends JLabel {

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
	
	public double mouseX;
	public double mouseY;
	
	
	public GabaritInformation () {
		super();
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
		sb.append(mouseX);
		sb.append(" ; ");
		sb.append(mouseY);

		setText(sb.toString());
	}

}
