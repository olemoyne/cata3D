package view.scene;

import java.awt.Color;

import view.view3D.GL3.SceneObject;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GL3;

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


	// Affichage des objects en mode OpenGL 2
	public abstract void drawObject (GL2 gl, Decimal echelle, int mode) ;

	// Ajoute un vecteur en appliquant l'echelle
	public void setPoint(Vecteur a, GL2 gl, Decimal echelle) {
		Vecteur p = a.multiply(echelle);
		gl.glVertex3fv(p.getFloats(), 0);
	}

	protected SceneObject getCarre(Vecteur v1, Vecteur v2, Vecteur v3, Vecteur v4) {
		// Calcul de la normale
		
		// liste des points
		Vecteur[] arr = new Vecteur[4];
		arr[0] = v1; arr[1] = v2; arr[2] = v3; arr[3] = v4;  
		SceneObject obj = new SceneObject(arr, GL3.GL_TRIANGLE_STRIP, this.color);
		
		return obj;
	}
	



}
