package view.scene;

import java.awt.Color;
import java.util.ArrayList;

import model.math.Vecteur;

import com.jogamp.opengl.GL2;

public class PrintedBlock extends PrintableObject {

	private Vecteur deb;
	private Vecteur fin;
	private boolean fillup;
	
	public PrintedBlock (Vecteur d, Vecteur f, String nom, boolean isFilled, Color col) {
		super(nom, col);
		deb = d;
		fin = f;
		fillup = isFilled;
	}
	
	public void setColor(GL2 gl, ArrayList<Vecteur> pts) {
        float r = color.getRed()/(float)256;
		float g = color.getGreen()/(float)256;
		float b = color.getBlue()/(float)256;
		
		gl.glColor3f(r, g, b);		
	}

	private void drawPoint(GL2 gl, Vecteur pt) {
		gl.glVertex3f(pt.getDecX().floatValue(), pt.getDecY().floatValue(), pt.getDecZ().floatValue());			
	}
	
	
	@Override
	public void drawObject(GL2 gl) {

		setColor (gl, null);
		
		if (!fillup) gl.glBegin(GL2.GL_LINE_STRIP);
		else gl.glBegin(GL2.GL_POLYGON);		
		drawPoint(gl, deb);
		drawPoint(gl, new Vecteur(fin.getDecX(), deb.getDecY(), deb.getDecZ()));
		drawPoint(gl, new Vecteur(fin.getDecX(), fin.getDecY(), deb.getDecZ()));
		drawPoint(gl, new Vecteur(deb.getDecX(), fin.getDecY(), deb.getDecZ()));
		drawPoint(gl, deb);
		gl.glEnd();

		if (!fillup) gl.glBegin(GL2.GL_LINE_STRIP);
		else gl.glBegin(GL2.GL_POLYGON);		
		drawPoint(gl, new Vecteur(deb.getDecX(), deb.getDecY(), fin.getDecZ()));
		drawPoint(gl, new Vecteur(fin.getDecX(), deb.getDecY(), fin.getDecZ()));
		drawPoint(gl, new Vecteur(fin.getDecX(), fin.getDecY(), fin.getDecZ()));
		drawPoint(gl, new Vecteur(deb.getDecX(), fin.getDecY(), fin.getDecZ()));
		drawPoint(gl, new Vecteur(deb.getDecX(), deb.getDecY(), fin.getDecZ()));
		gl.glEnd();

		if (!fillup) gl.glBegin(GL2.GL_LINE_STRIP);
		else gl.glBegin(GL2.GL_POLYGON);		
		drawPoint(gl, deb);
		drawPoint(gl, new Vecteur(deb.getDecX(), fin.getDecY(), deb.getDecZ()));
		drawPoint(gl, new Vecteur(deb.getDecX(), fin.getDecY(), fin.getDecZ()));
		drawPoint(gl, new Vecteur(deb.getDecX(), deb.getDecY(), fin.getDecZ()));
		drawPoint(gl, deb);
		gl.glEnd();

		if (!fillup) gl.glBegin(GL2.GL_LINE_STRIP);
		else gl.glBegin(GL2.GL_POLYGON);		
		drawPoint(gl, new Vecteur(fin.getDecX(), deb.getDecY(), deb.getDecZ()));
		drawPoint(gl, new Vecteur(fin.getDecX(), fin.getDecY(), deb.getDecZ()));
		drawPoint(gl, new Vecteur(fin.getDecX(), fin.getDecY(), fin.getDecZ()));
		drawPoint(gl, new Vecteur(fin.getDecX(), deb.getDecY(), fin.getDecZ()));
		drawPoint(gl, new Vecteur(fin.getDecX(), deb.getDecY(), deb.getDecZ()));
		gl.glEnd();


		if (!fillup) gl.glBegin(GL2.GL_LINE_STRIP);
		else gl.glBegin(GL2.GL_POLYGON);		
		drawPoint(gl, new Vecteur(deb.getDecX(), deb.getDecY(), deb.getDecZ()));
		drawPoint(gl, new Vecteur(deb.getDecX(), deb.getDecY(), fin.getDecZ()));
		drawPoint(gl, new Vecteur(fin.getDecX(), deb.getDecY(), fin.getDecZ()));
		drawPoint(gl, new Vecteur(fin.getDecX(), deb.getDecY(), deb.getDecZ()));
		drawPoint(gl, new Vecteur(deb.getDecX(), deb.getDecY(), deb.getDecZ()));
		gl.glEnd();

		if (!fillup) gl.glBegin(GL2.GL_LINE_STRIP);
		else gl.glBegin(GL2.GL_POLYGON);		
		drawPoint(gl, new Vecteur(deb.getDecX(), fin.getDecY(), deb.getDecZ()));
		drawPoint(gl, new Vecteur(deb.getDecX(), fin.getDecY(), fin.getDecZ()));
		drawPoint(gl, new Vecteur(fin.getDecX(), fin.getDecY(), fin.getDecZ()));
		drawPoint(gl, new Vecteur(fin.getDecX(), fin.getDecY(), deb.getDecZ()));
		drawPoint(gl, new Vecteur(deb.getDecX(), fin.getDecY(), deb.getDecZ()));
		gl.glEnd();

	}

}
