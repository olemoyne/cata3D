package view.scene;

import java.util.ArrayList;

import model.math.Decimal;

import com.jogamp.opengl.GL2;

public class PrintableScene {

	
	public String cataFileName;
	
	public ArrayList<PrintableObject> allObjects;
	
	public PrintableScene (String cat) {
		cataFileName = cat;
		allObjects = new ArrayList<PrintableObject> ();
	}
	
	/**
	 * Affichage de la sc�ne avec l'�chelle et le mode de visualisation.
	 *   Si mode != perspective --> affichage en ploygone syst�matique
	 * @param gl
	 * @param echelle 
	 * @param echelle
	 * @param mode
	 */
	public void printScene(GL2 gl, Decimal echelle, int mode) {
		for (PrintableObject obj : allObjects) {
			obj.drawScene(gl, echelle, mode);
		}
	}
	
	public void showScene (GL2 gl) {
		for (PrintableObject obj : allObjects) {
			obj.showObject(gl);
		}		
	}

	public void add(PrintableObject obj) {
		allObjects.add(obj);
	}

	public boolean is3DScene() {
		return true;
	}

}
