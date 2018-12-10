package model;

import java.io.Serializable;

import model.math.Decimal;

public class Quille implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6072384585613619939L;

	/** épaisseur de la quille **/
	public Decimal epaisseur;

	/** Profondeur de la quille  **/
	public Decimal profondeur;
	
	/** Creation de la quille **/
	public Quille() {
		epaisseur = new Decimal("0.02");
		profondeur = new Decimal("0.06");
	}

	
	public String toString() {
		return "Quille";
	}

}
