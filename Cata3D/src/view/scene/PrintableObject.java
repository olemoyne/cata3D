package view.scene;

import java.awt.Color;

import com.jogamp.opengl.GL2;

import model.math.Decimal;
import model.math.Vecteur;

public abstract class PrintableObject {
	
	public Color color;
	
	public String name;
	
	public boolean toBePrinted;
	
	
	public PrintableObject (String n, Color c) {
		name = n;
		color = c;
		toBePrinted = true;
	}
	
	public abstract void drawObject (GL2 gl, Decimal echelle, int mode) ;
	
	// Ajoute un vecteur en appliquant l'echelle
	public void setPoint(Vecteur a, GL2 gl, Decimal echelle) {
		gl.glVertex3f(a.getDecX().multiply(echelle).floatValue(), a.getDecY().multiply(echelle).floatValue(), a.getDecZ().multiply(echelle).floatValue());
	}




}
