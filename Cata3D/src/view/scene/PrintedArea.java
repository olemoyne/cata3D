package view.scene;

import java.awt.Color;
import java.util.ArrayList;

import model.Area;
import model.math.Decimal;
import model.math.Vecteur;

import com.jogamp.opengl.GL2;

public class PrintedArea extends PrintableObject {

	private Area zone;
	private boolean fillup;
	
	public PrintedArea (Area mp, String nom, boolean isFilled, Color col) {
		super(nom, col);
		zone = mp;
		fillup = isFilled;
	}
	
	public void setColor(GL2 gl, ArrayList<Vecteur> pts) {
        float r = color.getRed()/(float)256;
		float g = color.getGreen()/(float)256;
		float b = color.getBlue()/(float)256;
		
		gl.glColor3f(r, g, b);		
	}
	
	@Override
	public void drawObject(GL2 gl, Decimal echelle, int mode) {

		if (zone == null) return;
		setColor (gl, null);
		
		if ((!fillup)&&(mode == 0)) gl.glBegin(GL2.GL_LINE_STRIP);
		else gl.glBegin(GL2.GL_POLYGON);
		
		for (Vecteur pt : zone.points) {
			setPoint(pt, gl, echelle);
		}
		Vecteur pt = zone.points.get(0);
		setPoint(pt, gl, echelle);

		gl.glEnd();
			
	}

}
