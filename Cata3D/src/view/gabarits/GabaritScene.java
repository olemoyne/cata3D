package view.gabarits;


import java.awt.Color;

import model.Gabarit;
import model.Quille;
import model.composants.PatchVide;
import model.math.Plan3D;
import view.quille.PrintedQuille;
import view.scene.PrintableScene;
import view.scene.PrintedGabarit;

public class GabaritScene extends PrintableScene {
	public PatchVide composant;
	/**
	 * Cr�ation de la scene
	 */
	public GabaritScene (PatchVide boat, Plan3D mer){
		super(boat.boat.filename);
		
		composant = boat;
		//Pour chaque gabarit, construit un Gabarit visualisable
		for (Gabarit gb : boat.structure.gabarits) {
			PrintedGabarit pg = new PrintedGabarit(gb, boat, mer, gb.toString(), Color.YELLOW);
			this.allObjects.add(pg);
		}
		
		Quille q = boat.structure.quille;
		if (q != null) {
			PrintedQuille pg = new PrintedQuille(q, boat, Color.YELLOW);
			this.allObjects.add(pg);
		}
	}
	
	public boolean is3DScene() {
		return false;
	}


}
