package view.scene;


import java.awt.Color;

import model.Gabarit;
import model.composants.PatchVide;
import model.math.Plan3D;

public class GabaritScene extends PrintableScene {
	
	/**
	 * Cr�ation de la scene
	 */
	public GabaritScene (PatchVide boat, Plan3D mer){
		super();
		
		//Pour chaque gabarit, construit un Gabarit visualisable
		for (Gabarit gb : boat.structure.gabarits) {
			PrintedGabarit pg = new PrintedGabarit(gb, boat, mer, gb.toString(), Color.YELLOW);
			this.allObjects.add(pg);
		}
	}
	
	public boolean is3DScene() {
		return false;
	}


}
