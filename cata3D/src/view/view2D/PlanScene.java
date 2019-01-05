package view.view2D;


import model.composants.PatchVide;
import model.math.Plan3D;
import view.scene.PrintableScene;

public class PlanScene extends PrintableScene {
	public PatchVide composant;
	/**
	 * Cr�ation de la scene
	 */
	public PlanScene (PatchVide boat, Plan3D mer){
		super(boat.boat.filename);
		
		composant = boat;
	}
	
	public boolean is3DScene() {
		return false;
	}


}
