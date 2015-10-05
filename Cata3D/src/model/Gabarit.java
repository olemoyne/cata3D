package model;

import java.io.Serializable;
import java.util.ArrayList;

import model.math.Decimal;
import model.math.Vecteur;

/** Gabarit de construction **/
public class Gabarit implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	ArrayList<Vecteur> points;

	public Decimal position;
}
