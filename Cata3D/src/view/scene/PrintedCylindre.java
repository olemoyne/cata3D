package view.scene;

import java.awt.Color;

import view.view3D.Printer;
import model.Position;
import model.math.Decimal;

import com.jogamp.opengl.GL2;

public class PrintedCylindre extends PrintableObject {

	private Decimal rayon;
	private Decimal hauteur;
	
	
	public PrintedCylindre (Decimal r, Decimal h, String nom, Color col, Position pos) {
		super(nom, col, pos);
		rayon = r;
		hauteur = h;
	}
	
	@Override
	public void drawObject(GL2 gl, int mode) {
	    gl.glPushMatrix();       //equivalent to 'save current position'

	    Printer.drawCylindre(gl, rayon, hauteur, color);
	    
	    gl.glPopMatrix();        //equivalent to 'load the last position saved'
			
	}

}
