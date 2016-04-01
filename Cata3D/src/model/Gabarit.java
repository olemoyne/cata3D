package model;

import java.io.Serializable;
import java.util.ArrayList;

import model.calcul.CalculFormes;
import model.composants.Collision;
import model.composants.PatchVide;
import model.math.Axis;
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
	

	public Plan3D getPlan(Decimal i) {
		Plan3D pl = new Plan3D(new Vecteur(Decimal.UN, Decimal.ZERO, position.add(i)), 
				new Vecteur(Decimal.ZERO, Decimal.ZERO, position.add(i)), new Vecteur(Decimal.ZERO, Decimal.UN, position.add(i)));
		return pl;
	}
	
	/** 
	 * Retourne la surface (liste de points) corrrespondant au gabarit
	 * 
	 * @param bateau
	 * @param i 
	 * @return
	 */
	public Area getArea(PatchVide cmp, Decimal i) {
		// Plan Z = position;
		Plan3D pl = this.getPlan(i);
		Area coupe = cmp.mapAffichage.intersectionHorizontaleZ(pl);
		// SUppression des zones de collisison
		if (cmp.collisions != null) {
			for (Collision coll : cmp.collisions) {
				Area extrude = coll.collision.intersectionHorizontaleZ(pl);
				Area diff = CalculFormes.getExtrusion(coupe, extrude, position.add(i));
				if (diff != null) coupe = diff;
			}
		}
		
		return coupe.resize(cmp.epaisseurDeBardage.negate(), Axis.ZAxis);
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

		
		Vecteur start = ptr.getStart();
		Vecteur end = ptr.getEnd();
		
		long pStart = pl.donneCote(start);
		long pEnd = pl.donneCote(end);
		
		if (Long.signum(pStart)*Long.signum(pEnd) > 0) {
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
	
	public ArrayList<Area> getTrous (ArrayList<Poutre> poutres) {
		ArrayList<Area> trous = new ArrayList<Area>();
		
		// Liste des trous
		for (Poutre ptr : poutres) {
			// Définit la section d'intersection entre la poutre et le gabarit
			Area a = getArea(ptr, Decimal.ZERO);
			if (a != null) trous.add(a);
		}
		return trous;
	}
	
}
