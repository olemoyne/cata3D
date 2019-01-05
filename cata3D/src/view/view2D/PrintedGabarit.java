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

	public Area full;
	public Area fullInside;
	
	public Decimal position;

	public Area encoche;

	public ArrayList<Area> trous;

	public ArrayList<Area> effacements;
	
	public Decimal zPosition;
	public Decimal epaisseur;
		
	/** 
	 * Construit les Ã©lÃ©ments du gabarit Ã  afficher 
	 * **/
	public PrintedGabarit(Gabarit gab, PatchVide coque, Plan3D pl, String n, Color c) {
		super(n, gab.getArea(coque, null, false).getBounds(), Axis.ZAxis);

		position = gab.position;
		setMer(coque.boat.mer.getPlan());
		
		// Ajoute les éléments à afficher de type area
		addSurface(gab.getArea(coque, null, false), Color.BLACK);
		addSurface(gab.getContours(), Color.RED);
		// Ajoute les trous en vert
		if (gab.getTrous() != null) {
			for (Area t : gab.getTrous()) {
				addSurface(t, Color.GREEN);
			}
		}

		this.primaryPlotingTarget = gab.getContours();
		this.secondaryPlotingTarget = gab.getArea(coque, null, false);
		
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
		return "Tranche = "+position+" - "+zPosition+" - "+epaisseur;
	}
	
}
