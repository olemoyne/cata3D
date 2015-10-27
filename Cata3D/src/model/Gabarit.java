package model;

import java.io.Serializable;

import model.math.Decimal;
import model.math.Plan3D;
import model.math.Vecteur;

/** Gabarit de construction **/
public class Gabarit implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Decimal position;
	
	/** Epaisseur du bois constituant le gabarit **/
	public Decimal epaisseur;
	
	/** Elements calculés **/
	public Decimal surface;
	public Decimal poids;

	
	public Gabarit (){
		position = new Decimal(1d);
		epaisseur = new Decimal(0.01d);
	}
	
	/** 
	 * Retourne la surface (liste de points) corrrespondant au gabarit
	 * 
	 * @param bateau
	 * @return
	 */
	public Area getArea(Cata bateau) {
		// Plan Z = position;
		Plan3D pl = new Plan3D(new Vecteur(Decimal.UN, Decimal.ZERO, position), 
				new Vecteur(Decimal.ZERO, Decimal.ZERO, position), new Vecteur(Decimal.ZERO, Decimal.UN, position));
		
		Area coupe = bateau.mapAffichage.intersectionHorizontale(pl);
		
		return coupe.resize(bateau.structure.epaisseurDeBardage.negate());
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(position.toString());
		sb.append(" - ");
		sb.append(epaisseur.toString());
		return sb.toString();
	}
}
