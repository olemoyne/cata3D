package view.scene;

import java.awt.Color;

import model.math.Decimal;
import model.math.Vecteur;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;

public class PrintedPoint extends PrintableObject {

	private Vecteur point;
	
	public PrintedPoint (Vecteur mp, String nom, Color col) {
		super(nom, col);
		point = mp;
	}
	
	@Override
	public void drawObject(GL2 gl, Decimal echelle, int mode) {
	    gl.glPushMatrix();       //equivalent to 'save current position'

        float r = color.getRed()/(float)256;
		float g = color.getGreen()/(float)256;
		float b = color.getBlue()/(float)256;
		
		gl.glColor3f(r, g, b);


		GLU glu = new GLU();
	    GLUquadric quadric = glu.gluNewQuadric();    //In Jogl
	    glu.gluQuadricTexture(quadric, true);
        gl.glTranslatef(point.getDecX().multiply(echelle).floatValue(), 
        		point.getDecY().multiply(echelle).floatValue(), point.getDecZ().multiply(echelle).floatValue());
        glu.gluSphere(quadric, 0.01f, 5, 5);
	    gl.glPopMatrix();        //equivalent to 'load the last position saved'
			
	}

}
