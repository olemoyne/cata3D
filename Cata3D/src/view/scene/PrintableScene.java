package view.scene;

import java.util.ArrayList;

import com.jogamp.opengl.GL2;

import model.math.Decimal;

public class PrintableScene {

	public ArrayList<PrintableObject> allObjects;
	
	public PrintableScene () {
		allObjects = new ArrayList<PrintableObject> ();
	}
	
	public void printScene(GL2 gl, Decimal echelle) {
		for (PrintableObject obj : allObjects) {
			if (obj.toBePrinted) obj.drawObject(gl, echelle);
		}
	}

	public void add(PrintableObject obj) {
		allObjects.add(obj);
	}

	public boolean is3DScene() {
		return true;
	}
	
}
