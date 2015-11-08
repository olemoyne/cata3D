package model;

import java.io.Serializable;
import java.util.ArrayList;

import model.math.Decimal;

public class Structure implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7416484307060964622L;
	
	/** epaisseur du bardage qui constitue la coque **/
	public Decimal epaisseurDeBardage; 
	
	/** Gabarits **/
	public ArrayList<Gabarit> gabarits;
	
	/** Poutres **/
	public ArrayList<Poutre> poutres;

	/** position en Z du mat **/
	public Decimal positionDuMat;

	public Structure () {

		gabarits = new ArrayList<Gabarit> ();
		poutres = new ArrayList<Poutre> ();
		epaisseurDeBardage = new Decimal(0.004f); // 4 millimetres 
	
	}
	
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (gabarits != null) for (Gabarit gab : gabarits) sb.append(gab.toString());
        if (poutres != null) for (Poutre poutre : poutres) sb.append(poutre.toString());
        return sb.toString();
    }

}
