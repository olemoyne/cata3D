package view.scene;

import java.awt.Color;
import java.util.ArrayList;

import com.jogamp.opengl.GL2;

import model.Area;
import model.Cata;
import model.Gabarit;
import model.Poutre;
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
	public PrintedGabarit(Gabarit gab, Cata coque, String n, Color c) {
		super(n, c);
		
		bns = new Bounds3D(); 
		
		devant = gab.getArea(coque, Decimal.ZERO);
		bns.add(devant);
		fond = gab.getArea(coque, gab.epaisseur);
		bns.add(devant);
		
		// Liste des trous
		for (Poutre ptr : coque.structure.poutres) {
			// Définit la section d'intersection entre la poutre et le gabarit
			Area a = gab.getArea(ptr, Decimal.ZERO);
			if (a != null) {
				trous.add(a);
				bns.add(devant);
			}
		}
		
		// Ajoute le niveau de la mer
		Plan3D pl = coque.mer.getPlan();
		
		Vecteur s = bns.getMin();
		Vecteur e = new Vecteur (start.getDecX(), start.getDecY(), end.getDecZ());
		Vecteur c = pl.intersection(e,  s);
		if (c!= null) ret.points.add(c);

	}


	/**
	 * Pas d'implementation en 3D
	 */
	@Override
	public void drawObject(GL2 gl) {
	}

}
