package view.scene;

import java.awt.Color;
import java.util.ArrayList;

import com.jogamp.opengl.GL2;

import model.Area;
import model.Gabarit;
import model.composants.PatchVide;
import model.math.Axis;
import model.math.Bounds;
import model.math.Decimal;
import model.math.Plan3D;
import model.math.Segment;
import model.math.Vecteur;

public class PrintedGabarit extends PrintableObject {

	public Area fond;
	public Area devant;

	public Area full;
	public Area fullInside;

	public Bounds bns;
	
	public ArrayList<Area> trous;

	public ArrayList<Area> effacements;

	public Segment mer; 
	
	public Decimal zPosition;
	public Decimal epaisseur;
	
	/** 
	 * Construit les éléments du gabarit à afficher 
	 * **/
	public PrintedGabarit(Gabarit gab, PatchVide coque, Plan3D pl, String n, Color c) {
		super(n, c, null);
		
		name = gab.toString();
		
		bns = Bounds.getBounds(coque.mapAffichage);
		
		zPosition = gab.position;
		epaisseur = gab.epaisseur;
		
//		Decimal delta = gab.epaisseur.divide(Decimal.DEUX);
		
		effacements = new ArrayList<Area>();
				
//		devant = gab.getArea(coque, gab.position.minus(delta), null, true);
//		fond = gab.getArea(coque, gab.position.add(delta), this.effacements, true);
		full = gab.getArea(coque, gab.position, null, false);
		fullInside =full.resize(coque.epaisseurDeBardage.negate(), Axis.ZAxis);
		
		trous = gab.getTrous(coque.structure.poutres);
		
						
		Vecteur s = bns.getMin();
		Vecteur e = new Vecteur (bns.getMin().getDecX(), bns.getMax().getDecY(), bns.getMin().getDecZ());
		Vecteur mstart = pl.intersection(e,  s);

		s = new Vecteur (bns.getMax().getDecX(), bns.getMin().getDecY(), bns.getMin().getDecZ());
		e = new Vecteur (bns.getMax().getDecX(), bns.getMax().getDecY(), bns.getMin().getDecZ());
		Vecteur mend= pl.intersection(e,  s);

		mer = new Segment (mstart, mend);
	}

	


	/**
	 * Pas d'implementation en 3D
	 */
	@Override
	public void drawObject(GL2 gl, int mode) {
	}
	
	
	public String toString() {
		return name;
	}
}
