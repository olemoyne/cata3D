package view.scene;

import java.awt.Color;

import view.view3D.Printer;
import model.Position;
import model.math.Vecteur;

import com.jogamp.opengl.GL2;

public class PrintedPoint extends PrintableObject {

	private Vecteur point;
	
	public PrintedPoint (Vecteur mp, String nom, Color col) {
		super(nom, col, new Position(mp));
		point = mp;
	}
	
	@Override
	public void drawObject(GL2 gl, int mode) {
//	    gl.glPushMatrix();       //equivalent to 'save current position'

	    Printer.drawPoint(gl, point, color);
	    
//	    gl.glPopMatrix();        //equivalent to 'load the last position saved'
			
	}

}
