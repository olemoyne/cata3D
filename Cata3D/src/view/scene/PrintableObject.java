package view.scene;

import java.awt.Color;

import com.jogamp.opengl.GL2;

public abstract class PrintableObject {
	
	public Color color;
	
	public String name;
	
	public boolean toBePrinted;
	
	
	public PrintableObject (String n, Color c) {
		name = n;
		color = c;
		toBePrinted = true;
	}
	
	public abstract void drawObject (GL2 gl) ;

}
