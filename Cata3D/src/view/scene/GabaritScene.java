package view.scene;


import java.awt.Color;

import model.Cata;
import model.Gabarit;

public class GabaritScene extends PrintableScene {
	
	/**
	 * Cr�ation de la scene
	 */
	public GabaritScene (Cata boat){
		super();
		
		//Pour chaque gabarit, construit un Gabarit visualisable
		for (Gabarit gb : boat.structure.gabarits) {
			PrintedGabarit pg = new PrintedGabarit(gb, boat, gb.toString(), Color.YELLOW);
			this.allObjects.add(pg);
		}
	}
	
	public boolean is3DScene() {
		return false;
	}


}
