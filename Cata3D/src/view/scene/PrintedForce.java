package view.scene;

import java.awt.Color;

import model.Poids;
import model.math.Decimal;
import model.math.Vecteur;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;

public class PrintedForce extends PrintableObject {

	private Poids pds;
	
	public PrintedForce (Poids p, Color col) {
		super(p.nom, col);
		pds = p;
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
        gl.glTranslatef(pds.position.getDecX().multiply(echelle).floatValue(), 
        		pds.position.getDecY().multiply(echelle).floatValue(), pds.position.getDecZ().multiply(echelle).floatValue());
        glu.gluSphere(quadric, 0.01f, 5, 5);
	    gl.glPopMatrix();        //equivalent to 'load the last position saved'
	    
        gl.glBegin(GL.GL_LINE_STRIP);
        	setPoint(pds.position, gl, echelle);
        	Vecteur e = pds.position.add(new Vecteur (Decimal.ZERO, pds.force.divide(new Decimal(20f)).negate(), Decimal.ZERO));
        	setPoint(e, gl, echelle);
        gl.glEnd();
	}

}
