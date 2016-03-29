package model;

import java.io.Serializable;
import java.util.ArrayList;

import math.geom2d.polygon.Polygon2D;
import model.calcul.CalculSurface;
import model.calcul.CalculVolume;
import model.composants.PatchVide;
import model.math.Axis;
import model.math.Decimal;
import model.math.Vecteur;

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

	public ArrayList<Poids> getAllPoids(PatchVide ptch, double densiteBois) {
		Decimal dens = new Decimal(densiteBois);
		ArrayList<Poids> ret = new ArrayList<Poids> ();
		for (Gabarit gab : gabarits){ // ajoute le poids des gabarits
			Vecteur ep = new Vecteur(Decimal.ZERO, Decimal.ZERO, gab.epaisseur.divide(Decimal.DEUX));
			ArrayList<Poids> pds = new ArrayList<Poids> ();
			Area a = gab.getArea(ptch, Decimal.ZERO);
			if (a.points.size() != 0) {
				Polygon2D pol = CalculSurface.getPoly(a.points, Axis.ZAxis);
				Decimal vol = new Decimal(pol.area()).multiply(gab.epaisseur).multiply(dens).multiply(Decimal.MILLE);
				Vecteur ctr = CalculSurface.getCentre(pol, Decimal.ZERO, Axis.ZAxis).add(ep);
				pds.add(new Poids("Surf", ctr, vol));
				for (Area sub : gab.getTrous(poutres)) {
					pol = CalculSurface.getPoly(sub.points, Axis.ZAxis);
					vol = new Decimal(pol.area()).multiply(gab.epaisseur).multiply(dens).multiply(Decimal.MILLE);
					ctr = CalculSurface.getCentre(pol, Decimal.ZERO, Axis.ZAxis).add(ep);
					pds.add(new Poids("Trou", ctr, vol.negate()));
				}
				ret.add(CalculVolume.getCentreGravite("Gab", pds));
			}
		}
		for (Poutre ptr : poutres){ // ajoute le poids des poutres
			Decimal vol = ptr.epaisseur.multiply(ptr.hauteur).multiply(ptr.longueur).multiply(dens);
			Vecteur ctr = ptr.getStart().add(new Vecteur (Decimal.ZERO, Decimal.ZERO, ptr.longueur.divide(Decimal.DEUX)));
			ret.add(new Poids("Poutre", ctr, vol));
		}
		return ret;
	}

}
