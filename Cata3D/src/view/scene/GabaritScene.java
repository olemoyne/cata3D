package view.scene;

import java.util.ArrayList;

import model.Cata;
import model.Gabarit;
import model.Poutre;
import model.math.Plan3D;

public class GabaritScene extends PrintableScene {

	Cata bateau;
	
	/**
	 * Création de la scene
	 */
	public GabaritScene (Cata boat){
		super();
		bateau = boat;
	}

	public ArrayList<Gabarit> getGabarits() {
		if (bateau == null) return new ArrayList<Gabarit>(); 
		return bateau.structure.gabarits;
	}

	public Plan3D getFlottaison() {
		if (bateau == null) return new Plan3D(); 
		return bateau.mer.getPlan();
	}

	public ArrayList<Poutre> getPoutres() {
		if (bateau == null) return new ArrayList<Poutre>(); 
		return bateau.structure.poutres;
	}
}
