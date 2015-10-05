package view.scene;

import java.util.ArrayList;

import com.jogamp.opengl.GL2;

public class PrintableScene {

	public ArrayList<PrintableObject> allObjects;
	
	public PrintableScene () {
		allObjects = new ArrayList<PrintableObject> ();
	}
	
	public void printScene(GL2 gl) {
		for (PrintableObject obj : allObjects) {
			obj.drawObject(gl);
		}
	}

	public void add(PrintableObject obj) {
		allObjects.add(obj);
	}
	
}
