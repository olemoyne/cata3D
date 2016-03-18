package view.view3D;

import java.awt.Color;

import model.math.Decimal;
import model.math.Vecteur;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;

public class Printer {
	
	
	// Ajoute un vecteur en appliquant l'echelle
	public static void setPoint(Vecteur p, GL2 gl) {
		gl.glVertex3fv(p.getFloats(), 0);
	}


	public static float[] getColor(Color col, int mode) {
        float[] rgba = new  float[4];
        float coef = 1;
		if (mode == GL2.GL_AMBIENT) coef = 0.6f;
		if (mode == GL2.GL_DIFFUSE) coef = 1f;
		if (mode == GL2.GL_SPECULAR) coef = 1f;

		rgba[0] = col.getRed()/(float)256*coef;
        rgba[1] = col.getGreen()/(float)256*coef;
        rgba[2] = col.getBlue()/(float)256*coef;
        rgba[3] = 0.7f;
		
        return rgba; 
	}
	
	// Affiche un carré rempli
	public static void drawCarre(GL2 gl, Vecteur a, Vecteur b, Vecteur c, Vecteur d) {
		Vecteur n = Printer.getNormale(a, b, c, d);
	    gl.glNormal3fv(n.getFloats(), 0);
	    
		setPoint(a, gl);
		setPoint(b, gl);
		setPoint(c, gl);
		setPoint(d, gl);
	}
	
	public static void drawLineCarre(GL2 gl, Vecteur a, Vecteur b, Vecteur c, Vecteur d) {
		setPoint(a, gl);
		setPoint(b, gl);
		setPoint(b, gl);
		setPoint(c, gl);
		setPoint(c, gl);
		setPoint(d, gl);		
		setPoint(d, gl);		
		setPoint(a, gl);
	}
	

	public static void drawLinePave (GL2 gl, Vecteur deb, Vecteur fin, Color color) {
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE, getColor(color, GL2.GL_DIFFUSE), 0);
	    gl.glShadeModel(GL2.GL_SMOOTH);
	    
	    gl.glBegin(GL2.GL_LINES);

	    drawLineCarre(gl,  // face arrière
				deb, 
				new Vecteur(fin.getDecX(), deb.getDecY(), deb.getDecZ()),
	    		new Vecteur(fin.getDecX(), fin.getDecY(), deb.getDecZ()),
	    		new Vecteur(deb.getDecX(), fin.getDecY(), deb.getDecZ()));

		drawLineCarre(gl,  // face avant
				new Vecteur(deb.getDecX(), deb.getDecY(), fin.getDecZ()),
				new Vecteur(deb.getDecX(), fin.getDecY(), fin.getDecZ()),
				new Vecteur(fin.getDecX(), fin.getDecY(), fin.getDecZ()),
				new Vecteur(fin.getDecX(), deb.getDecY(), fin.getDecZ()));

		// Drawing all the lines
		setPoint(new Vecteur(deb.getDecX(), deb.getDecY(), deb.getDecZ()), gl);
		setPoint(new Vecteur(deb.getDecX(), deb.getDecY(), fin.getDecZ()), gl);

		setPoint(new Vecteur(deb.getDecX(), fin.getDecY(), deb.getDecZ()), gl);
		setPoint(new Vecteur(deb.getDecX(), fin.getDecY(), fin.getDecZ()), gl);

		setPoint(new Vecteur(fin.getDecX(), fin.getDecY(), deb.getDecZ()), gl);
		setPoint(new Vecteur(fin.getDecX(), fin.getDecY(), fin.getDecZ()), gl);

		setPoint(new Vecteur(fin.getDecX(), deb.getDecY(), deb.getDecZ()), gl);
		setPoint(new Vecteur(fin.getDecX(), deb.getDecY(), fin.getDecZ()), gl);
		
		gl.glEnd();
	}

	
	public static void drawFilledPave (GL2 gl, Vecteur deb, Vecteur fin, Color col) {
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE, getColor(col, GL2.GL_DIFFUSE), 0);
	    gl.glShadeModel(GL2.GL_SMOOTH);
	    
		gl.glBegin(GL2.GL_QUADS);		

	    drawCarre(gl,  // face arrière
				deb, 
				new Vecteur(deb.getDecX(), fin.getDecY(), deb.getDecZ()),
	    		new Vecteur(fin.getDecX(), fin.getDecY(), deb.getDecZ()),
	    		new Vecteur(fin.getDecX(), deb.getDecY(), deb.getDecZ()));

		drawCarre(gl,  // face avant
				new Vecteur(deb.getDecX(), deb.getDecY(), fin.getDecZ()),
				new Vecteur(fin.getDecX(), deb.getDecY(), fin.getDecZ()),
				new Vecteur(fin.getDecX(), fin.getDecY(), fin.getDecZ()),
				new Vecteur(deb.getDecX(), fin.getDecY(), fin.getDecZ()));
		
		drawCarre(gl, // face de gauche
				deb, 
				new Vecteur(deb.getDecX(), deb.getDecY(), fin.getDecZ()), 
				new Vecteur(deb.getDecX(), fin.getDecY(), fin.getDecZ()),
				new Vecteur(deb.getDecX(), fin.getDecY(), deb.getDecZ()));

		drawCarre(gl, // Face de droite
				new Vecteur(fin.getDecX(), deb.getDecY(), deb.getDecZ()), 
				new Vecteur(fin.getDecX(), fin.getDecY(), deb.getDecZ()), 
				new Vecteur(fin.getDecX(), fin.getDecY(), fin.getDecZ()),
				new Vecteur(fin.getDecX(), deb.getDecY(), fin.getDecZ()));

		drawCarre(gl, // Fond du bas
				new Vecteur(deb.getDecX(), deb.getDecY(), deb.getDecZ()), 
				new Vecteur(fin.getDecX(), deb.getDecY(), deb.getDecZ()), 
				new Vecteur(fin.getDecX(), deb.getDecY(), fin.getDecZ()),
				new Vecteur(deb.getDecX(), deb.getDecY(), fin.getDecZ()));

		drawCarre(gl, // Fond du haut
				new Vecteur(deb.getDecX(), fin.getDecY(), deb.getDecZ()),
				new Vecteur(deb.getDecX(), fin.getDecY(), fin.getDecZ()),
				new Vecteur(fin.getDecX(), fin.getDecY(), fin.getDecZ()),
				new Vecteur(fin.getDecX(), fin.getDecY(), deb.getDecZ()));
		
		gl.glEnd();
	}


	public static void drawPoint(GL2 gl, Vecteur point, Color color) {
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE, getColor(color, GL2.GL_DIFFUSE), 0);
	    gl.glShadeModel(GL2.GL_SMOOTH);

		GLU glu = new GLU();
	    GLUquadric quadric = glu.gluNewQuadric();    //In Jogl
	    glu.gluQuadricTexture(quadric, true);
//        gl.glTranslatef(point.getDecX().floatValue(), point.getDecY().floatValue(), point.getDecZ().floatValue());
        glu.gluSphere(quadric, 0.005f, 5, 5);
	}


	public static void drawLine(GL2 gl, Vecteur b, Vecteur e, Color color) {
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE, getColor(color, GL2.GL_DIFFUSE), 0);
	    gl.glShadeModel(GL2.GL_SMOOTH);

	    gl.glBegin(GL2.GL_LINES);
	    	setPoint(b, gl);
	    	setPoint(e, gl);
	    gl.glEnd();
		
	}


	public static void drawCylindre(GL2 gl, Decimal rayon, Decimal hauteur, Color color) {
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE, getColor(color, GL2.GL_DIFFUSE), 0);
	    gl.glShadeModel(GL2.GL_SMOOTH);

		GLU glu = new GLU();
	    GLUquadric quadric = glu.gluNewQuadric();    //In Jogl
	    glu.gluQuadricTexture(quadric, true);
//        gl.glTranslatef(point.getDecX().floatValue(), point.getDecY().floatValue(), point.getDecZ().floatValue());
        glu.gluCylinder(quadric, rayon.doubleValue(), rayon.doubleValue(), hauteur.doubleValue(), 10, 20);
	}


	public static Vecteur getNormale(Vecteur a, Vecteur b, Vecteur c, Vecteur d) {
		// Calcul de la normale
		Vecteur n = Vecteur.getNormale(a.minus(b), c.minus(b)).negat();
		if (n.isZero()) {
			if (a.equals(b))
				n = Vecteur.getNormale(b.minus(c), d.minus(c)).negat();
			if (b.equals(c))
				n = Vecteur.getNormale(c.minus(d), a.minus(d)).negat();
		}
		return n;
	}
	

}
