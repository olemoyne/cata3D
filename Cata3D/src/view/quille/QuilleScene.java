package view.quille;


import java.awt.Color;

import model.composants.PatchVide;
import model.math.Plan3D;
import view.scene.PrintableScene;

public class QuilleScene extends PrintableScene {
	public PatchVide composant;
	/**
	 * Cr�ation de la scene
	 */
	public QuilleScene (PatchVide boat, Plan3D mer){
		super(boat.boat.filename);
		
		composant = boat;
		PrintedQuille pq = new PrintedQuille(boat.structure.quille, boat, Color.YELLOW);
		this.allObjects.add(pq);
	}
	
	public boolean is3DScene() {
		return false;
	}


}
