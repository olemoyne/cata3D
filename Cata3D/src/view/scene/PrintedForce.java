package view.scene;

import java.awt.Color;

import model.Poids;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;

public class PrintedForce extends PrintableObject {

	private Poids pds;
	
	public PrintedForce (Poids p, Color col) {
		super();
		pds = p;
		color = col;
	}
	
	@Override
	public void drawObject(GL2 gl) {
	    gl.glPushMatrix();       //equivalent to 'save current position'

        float r = color.getRed()/(float)256;
		float g = color.getGreen()/(float)256;
		float b = color.getBlue()/(float)256;
		
		gl.glColor3f(r, g, b);


		GLU glu = new GLU();
	    GLUquadric quadric = glu.gluNewQuadric();    //In Jogl
	    glu.gluQuadricTexture(quadric, true);
        gl.glTranslatef(pds.position.getDecX().floatValue(), pds.position.getDecY().floatValue(), pds.position.getDecZ().floatValue());
        glu.gluSphere(quadric, 0.01f, 5, 5);
	    gl.glPopMatrix();        //equivalent to 'load the last position saved'
	    
        gl.glBegin(GL.GL_LINE_STRIP);
			gl.glVertex3f(pds.position.getDecX().floatValue(), pds.position.getDecY().floatValue(), pds.position.getDecZ().floatValue());
			gl.glVertex3f(pds.position.getDecX().floatValue(), pds.position.getDecY().floatValue()-pds.force.floatValue()/20f, pds.position.getDecZ().floatValue());
        gl.glEnd();

			
	}

}
