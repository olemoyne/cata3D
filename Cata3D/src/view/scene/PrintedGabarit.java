package view.scene;

import java.awt.Color;
import java.util.ArrayList;

import com.jogamp.opengl.GL2;

import model.Area;
import model.Gabarit;
import model.Poutre;
import model.composants.PatchVide;
import model.math.Bounds3D;
import model.math.Decimal;
import model.math.Plan3D;
import model.math.Segment;
import model.math.Vecteur;

public class PrintedGabarit extends PrintableObject {

	public Area fond;
	public Area devant;
	
	public Bounds3D bns;
	
	public ArrayList<Area> trous;
	
	public Segment mer; 
	
	
	/** 
	 * Construit les éléments du gabarit à afficher 
	 * **/
	public PrintedGabarit(Gabarit gab, PatchVide coque, Plan3D pl, String n, Color c) {
		super(n, c);
		
		name = gab.toString();
		
		bns = new Bounds3D(); 
		
		devant = gab.getArea(coque, Decimal.ZERO);
		bns.add(devant);
		fond = gab.getArea(coque, gab.epaisseur);
		bns.add(fond);
		
		trous = new ArrayList<Area>();
		
		// Liste des trous
		for (Poutre ptr : coque.structure.poutres) {
			// Définit la section d'intersection entre la poutre et le gabarit
			Area a = gab.getArea(ptr, Decimal.ZERO);
			if (a != null) {
				trous.add(a);
				bns.add(devant);
			}
		}
				
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
	public void drawObject(GL2 gl) {
	}
	
	
	public String toString() {
		return name;
	}

}
