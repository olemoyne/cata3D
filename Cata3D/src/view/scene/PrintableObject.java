package view.scene;

import java.awt.Color;

import com.jogamp.opengl.GL2;

public abstract class PrintableObject {
	
	public Color color;
	
	
	public abstract void drawObject (GL2 gl);

}
