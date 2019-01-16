package view.view2D;

import java.awt.Color;
import java.util.ArrayList;

import model.Area;
import model.Gabarit;
import model.composants.PatchVide;
import model.math.Axis;
import model.math.Decimal;
import model.math.Plan3D;

public class PrintedGabarit extends PrintedPlan {

	public Decimal position;

	public ArrayList<Area> trous;

	public ArrayList<Area> effacements;
	
	public Decimal zPosition;
	public Decimal epaisseur;
		
	/** 
	 * Construit les Ã©lÃ©ments du gabarit Ã  afficher 
	 * **/
	public PrintedGabarit(Gabarit gab, PatchVide coque, Plan3D pl, String n, Color c) {
		super(n, Axis.ZAxis);

		position = gab.position;
		setMer(coque.boat.mer.getPlan());
		
		// Ajoute les éléments à afficher de type area
		addSurface(gab.getTranche(), Color.BLACK);
		addSurface(gab.getContours(), Color.RED);
		// Ajoute les trous en vert
		if (gab.getTrous() != null) {
			for (Area t : gab.getTrous()) {
				addSurface(t, Color.GREEN);
			}
		}

		this.primaryPlotingTarget = gab.getContours();
		this.secondaryPlotingTarget = gab.getTranche();
		
		zPosition = gab.position;
		epaisseur = gab.epaisseur;		
	}
	
	public Decimal getZPosition() {
		return zPosition;
	}


	public String toString() {
		return name;
	}

	public String getPlanType() {
		return "Gabarit";
	}

	
	public String getStringDescr() {
		return "Tranche = "+position+" - "+epaisseur;
	}

	public ArrayList<Area> getTrous() {
		return trous;
	}

	/** Retourne le contours d'impression 3D **/
	public Area getPrintable() {
		return this.primaryPlotingTarget;
	}

	/**
	 * Fonction utilisée pour imprimer en 3D
	 * @return
	 */
	public Decimal getEpaisseur() {
		return this.epaisseur;
	}

	
}
