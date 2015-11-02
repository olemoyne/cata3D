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
	
	/** Elements calcul�s **/
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
	 * @param i 
	 * @return
	 */
	public Area getArea(Cata bateau, Decimal i) {
		// Plan Z = position;
		Plan3D pl = new Plan3D(new Vecteur(Decimal.UN, Decimal.ZERO, position.add(i)), 
				new Vecteur(Decimal.ZERO, Decimal.ZERO, position), new Vecteur(Decimal.ZERO, Decimal.UN, position.add(i)));
		
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

	/**
	 * Retourne l'intersection entre le gabarit et la poutre
	 * 
	 * @param ptr
	 * @return
	 */
	public Area getArea(Poutre ptr, Decimal i) {
		Area ret = new Area();
		// Parcours les deux faces d'abord

		Plan3D pl = new Plan3D(new Vecteur(Decimal.UN, Decimal.ZERO, position.add(i)), 
				new Vecteur(Decimal.ZERO, Decimal.ZERO, position), new Vecteur(Decimal.ZERO, Decimal.UN, position.add(i)));

		
		Vecteur start = ptr.depart;
		Vecteur end = ptr.getEnd();
		
		long pStart = pl.donneCote(start);
		long pEnd = pl.donneCote(end);
		
		if (pStart*pEnd > 0) {
			// Sont du même coté --> Pas d'intersection
			return null;
		}
		// Ne sont pas du même côté
		
		Vecteur s = start;
		Vecteur e = new Vecteur (start.getDecX(), start.getDecY(), end.getDecZ());
		Vecteur c = pl.intersection(e,  s);
		if (c!= null) ret.points.add(c);

		s = new Vecteur (end.getDecX(), start.getDecY(), start.getDecZ());
		e = new Vecteur (end.getDecX(), start.getDecY(), end.getDecZ());
		c = pl.intersection(e,  s);
		if (c!= null) ret.points.add(c);


		s = new Vecteur (end.getDecX(), end.getDecY(), start.getDecZ());
		e = new Vecteur (end.getDecX(), end.getDecY(), end.getDecZ());
		c = pl.intersection(e,  s);
		if (c!= null) ret.points.add(c);


		s = new Vecteur (start.getDecX(), end.getDecY(), start.getDecZ());
		e = new Vecteur (start.getDecX(), end.getDecY(), end.getDecZ());
		c = pl.intersection(e,  s);
		if (c!= null) ret.points.add(c);
		
		return ret;
	}
	
}
