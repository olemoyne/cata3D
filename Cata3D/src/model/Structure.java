package model;

import java.io.Serializable;
import java.util.ArrayList;

public class Structure implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7416484307060964622L;
	
	/** Gabarits **/
	public ArrayList<Gabarit> gabarits;
	
	/** Poutres **/
	public ArrayList<Poutre> poutres;

	public Structure () {

		gabarits = new ArrayList<Gabarit> ();
		poutres = new ArrayList<Poutre> ();
	}
	
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (gabarits != null) for (Gabarit gab : gabarits) sb.append(gab.toString());
        if (poutres != null) for (Poutre poutre : poutres) sb.append(poutre.toString());
        return sb.toString();
    }

}
