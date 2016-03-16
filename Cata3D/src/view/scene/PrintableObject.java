package view.scene;

import java.awt.Color;

import com.jogamp.opengl.GL2;

import model.Position;
import model.math.Decimal;
import model.math.Vecteur;

public abstract class PrintableObject {
	
	public Color color;
	public String name;
	public boolean toBePrinted;
	
	public Position position;
	
	
	public PrintableObject (String n, Color c, Position pos) {
		name = n;
		color = c;
		toBePrinted = true;
		position = pos;
	}
	
	public void setPosition (Position pos) {
		position = pos;
	}


	// Affichage des objects en mode OpenGL 2
	public abstract void drawObject (GL2 gl, int mode) ;
	
	
	public void drawScene(GL2 gl, int mode) {
		if (toBePrinted) {
			// Positionne la matrice
		    gl.glPushMatrix();       //equivalent to 'save current position'
		    if (position != null) {
		    	// Translation
			    Vecteur pos = position.position;
			    if (pos == null) pos = new Vecteur();
		        gl.glTranslatef(pos.getDecX().floatValue(), pos.getDecY().floatValue(), pos.getDecZ().floatValue());
		    	// Rotation 
		    	if (!position.rotation.isZero()) {
		    		pos = position.rotation; 
		    		Decimal a = position.angle; if (a==null) a = Decimal.ZERO;
			        gl.glRotatef(a.floatValue(), pos.getDecX().floatValue(), pos.getDecY().floatValue(), pos.getDecZ().floatValue());
		    	}
		    }

			drawObject(gl, mode);
		    
		    gl.glPopMatrix();        //equivalent to 'load the last position saved'
		}
	}
}
