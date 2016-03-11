package view.scene;

import java.awt.Color;
import java.util.ArrayList;

import view.view3D.GL3.SceneObject;
import model.math.Decimal;
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

	
	public void drawCarre(GL2 gl, Vecteur a, Vecteur b, Vecteur c, Vecteur d, Decimal echelle, int mode) {
		if ((!fillup)&&(mode == 0)) gl.glBegin(GL2.GL_LINE_STRIP);
		else gl.glBegin(GL2.GL_POLYGON);		
		setPoint(a, gl, echelle);
		setPoint(b, gl, echelle);
		setPoint(c, gl, echelle);
		setPoint(d, gl, echelle);
		setPoint(a, gl, echelle);
		gl.glEnd();
		
	}
	
	@Override
	public void drawObject(GL2 gl, Decimal echelle, int mode) {

		setColor (gl, null);
		drawCarre(gl, deb, new Vecteur(fin.getDecX(), deb.getDecY(), deb.getDecZ()),
				new Vecteur(fin.getDecX(), fin.getDecY(), deb.getDecZ()), 
				new Vecteur(deb.getDecX(), fin.getDecY(), deb.getDecZ()), echelle, mode);

		drawCarre(gl, new Vecteur(deb.getDecX(), deb.getDecY(), fin.getDecZ()),
				new Vecteur(fin.getDecX(), deb.getDecY(), fin.getDecZ()),
				new Vecteur(fin.getDecX(), fin.getDecY(), fin.getDecZ()), 
				new Vecteur(deb.getDecX(), fin.getDecY(), fin.getDecZ()), echelle, mode);

		drawCarre(gl, deb, new Vecteur(deb.getDecX(), fin.getDecY(), deb.getDecZ()), 
				new Vecteur(deb.getDecX(), fin.getDecY(), fin.getDecZ()),
				new Vecteur(deb.getDecX(), deb.getDecY(), fin.getDecZ()), echelle, mode);

		drawCarre(gl, new Vecteur(fin.getDecX(), deb.getDecY(), deb.getDecZ()), 
				new Vecteur(fin.getDecX(), fin.getDecY(), deb.getDecZ()), 
				new Vecteur(fin.getDecX(), fin.getDecY(), fin.getDecZ()),
				new Vecteur(fin.getDecX(), deb.getDecY(), fin.getDecZ()), echelle, mode);

		drawCarre(gl, new Vecteur(deb.getDecX(), deb.getDecY(), deb.getDecZ()), 
				new Vecteur(deb.getDecX(), deb.getDecY(), fin.getDecZ()), 
				new Vecteur(fin.getDecX(), deb.getDecY(), fin.getDecZ()),
				new Vecteur(fin.getDecX(), deb.getDecY(), deb.getDecZ()), echelle, mode);

		drawCarre(gl, new Vecteur(deb.getDecX(), fin.getDecY(), deb.getDecZ()),
				new Vecteur(deb.getDecX(), fin.getDecY(), fin.getDecZ()),
				new Vecteur(fin.getDecX(), fin.getDecY(), fin.getDecZ()),
				new Vecteur(fin.getDecX(), fin.getDecY(), deb.getDecZ()), echelle, mode);

	}

	
	@Override
	public void getSceneObjects(ArrayList<SceneObject> lst) {
		this.getCube(deb, fin, lst);
	}


}
