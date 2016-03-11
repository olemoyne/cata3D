package view.scene;

import java.util.ArrayList;

import com.jogamp.opengl.GL2;

import model.math.Decimal;

public class PrintableScene {

	public ArrayList<PrintableObject> allObjects;
	
	public PrintableScene () {
		allObjects = new ArrayList<PrintableObject> ();
	}
	
	/**
	 * Affichage de la scène avec l'échelle et le mode de visualisation.
	 *   Si mode != perspective --> affichage en ploygone systématique
	 * @param gl
	 * @param echelle
	 * @param mode
	 */
	public void printScene(GL2 gl, Decimal echelle, int mode) {
		for (PrintableObject obj : allObjects) {
			if (obj.toBePrinted) obj.drawObject(gl, echelle, mode);
		}
	}

	public void add(PrintableObject obj) {
		allObjects.add(obj);
	}

	public boolean is3DScene() {
		return true;
	}

}
