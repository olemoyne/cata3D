package view.scene;

import java.awt.Color;
import java.util.ArrayList;

import view.view3D.Printer;
import model.Area;
import model.Position;
import model.math.Vecteur;

import com.jogamp.opengl.GL2;

public class PrintedArea extends PrintableObject {

	private Area zone;
	private boolean fillup;
	private boolean transparent;
	
	public PrintedArea (Area mp, String nom, boolean isFilled, boolean isTransp, Color col, Position pos) {
		super(nom, col, pos);
		zone = mp;
		fillup = isFilled;
		transparent = isTransp;
	}
	
	public void setColor(GL2 gl, ArrayList<Vecteur> pts) {
        float r = color.getRed()/(float)256;
		float g = color.getGreen()/(float)256;
		float b = color.getBlue()/(float)256;
		
		gl.glColor3f(r, g, b);		
	}
	
	@Override
	public void drawObject(GL2 gl, int mode) {

		if (zone == null) return;

		float[] rgba = Printer.getColor(color, GL2.GL_DIFFUSE);
		if (transparent) {
			gl.glEnable(GL2.GL_BLEND) ;
			gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA) ; 
			rgba[3] = 0.5f;
		}
		
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE, rgba, 0);
	    gl.glShadeModel(GL2.GL_SMOOTH);

		if ((!fillup)&&(mode == 0)) gl.glBegin(GL2.GL_LINE_STRIP);
		else gl.glBegin(GL2.GL_POLYGON);
		
		for (int i = zone.points.size()-1; i>= 0; i--) {
			Vecteur pt = zone.points.get(i); 
			Printer.setPoint(pt, gl);
		}
		Vecteur pt = zone.points.get(0);
		Printer.setPoint(pt, gl);
		gl.glEnd();

		// Double face si non tranparent
		if (!this.transparent) {
			if ((!fillup)&&(mode == 0)) gl.glBegin(GL2.GL_LINE_STRIP);
			else gl.glBegin(GL2.GL_POLYGON);
			
			for (int i = 0; i < zone.points.size(); i++) {
				pt = zone.points.get(i); 
				Printer.setPoint(pt, gl);
			}
			pt = zone.points.get(0);
			Printer.setPoint(pt, gl);

			gl.glEnd();
		}

		
		if (transparent) {
			gl.glDisable(GL2.GL_BLEND) ;
		}			
	}
}
