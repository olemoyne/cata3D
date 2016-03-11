package view.scene;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import view.view3D.GL3.SceneObject;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;

import model.math.Decimal;
import model.math.Vecteur;

public abstract class PrintableObject {
	
	public Color color;
	
	public String name;
	
	public boolean toBePrinted;
	
	
	public PrintableObject (String n, Color c) {
		name = n;
		color = c;
		toBePrinted = true;
	}

	// Affichage des objects en mode OpenGL 2
	public abstract void drawObject (GL2 gl, Decimal echelle, int mode) ;

	// Affichage des objects en mode OpenGL 3
	public abstract void getSceneObjects (ArrayList<SceneObject> lst);

	// Ajoute un vecteur en appliquant l'echelle
	public void setPoint(Vecteur a, GL2 gl, Decimal echelle) {
		gl.glVertex3f(a.getDecX().multiply(echelle).floatValue(), a.getDecY().multiply(echelle).floatValue(), a.getDecZ().multiply(echelle).floatValue());
	}

	protected SceneObject getCarre(Vecteur v1, Vecteur v2, Vecteur v3, Vecteur v4) {
		// liste des points
		Vecteur[] arr = new Vecteur[4];
		arr[0] = v1; arr[1] = v2; arr[2] = v3; arr[3] = v4;  
		SceneObject obj = new SceneObject(arr, GL3.GL_TRIANGLE_STRIP, this.color);
		
		return obj;
	}
	
	protected void getCube (Vecteur deb, Vecteur fin, ArrayList<SceneObject> lst) {
		lst.add(getCarre(deb, new Vecteur(fin.getDecX(), deb.getDecY(), deb.getDecZ()),
				new Vecteur(fin.getDecX(), fin.getDecY(), deb.getDecZ()), 
				new Vecteur(deb.getDecX(), fin.getDecY(), deb.getDecZ())));

		lst.add(getCarre(new Vecteur(deb.getDecX(), deb.getDecY(), fin.getDecZ()),
				new Vecteur(fin.getDecX(), deb.getDecY(), fin.getDecZ()),
				new Vecteur(fin.getDecX(), fin.getDecY(), fin.getDecZ()), 
				new Vecteur(deb.getDecX(), fin.getDecY(), fin.getDecZ())));

		lst.add(getCarre(deb, new Vecteur(deb.getDecX(), fin.getDecY(), deb.getDecZ()), 
				new Vecteur(deb.getDecX(), fin.getDecY(), fin.getDecZ()),
				new Vecteur(deb.getDecX(), deb.getDecY(), fin.getDecZ())));

		lst.add(getCarre(new Vecteur(fin.getDecX(), deb.getDecY(), deb.getDecZ()), 
				new Vecteur(fin.getDecX(), fin.getDecY(), deb.getDecZ()), 
				new Vecteur(fin.getDecX(), fin.getDecY(), fin.getDecZ()),
				new Vecteur(fin.getDecX(), deb.getDecY(), fin.getDecZ())));

		lst.add(getCarre(new Vecteur(deb.getDecX(), deb.getDecY(), deb.getDecZ()), 
				new Vecteur(deb.getDecX(), deb.getDecY(), fin.getDecZ()), 
				new Vecteur(fin.getDecX(), deb.getDecY(), fin.getDecZ()),
				new Vecteur(fin.getDecX(), deb.getDecY(), deb.getDecZ())));

		lst.add(getCarre(new Vecteur(deb.getDecX(), fin.getDecY(), deb.getDecZ()),
				new Vecteur(deb.getDecX(), fin.getDecY(), fin.getDecZ()),
				new Vecteur(fin.getDecX(), fin.getDecY(), fin.getDecZ()),
				new Vecteur(fin.getDecX(), fin.getDecY(), deb.getDecZ())));
		
	}

	
	
	
	public void drawPolygon (GL2 gl, Vecteur[] pts, Decimal echelle, Color col, boolean transp) {
		gl.glEnable(GL2.GL_TEXTURE_2D);
        gl.glEnable(GL2.GL_BLEND);
        gl.glBlendFunc(GL2.GL_ONE, GL2.GL_ONE_MINUS_SRC_ALPHA);
        gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL2.GL_COLOR_ARRAY);
        gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        gl.glLoadIdentity();

        Texture mTest = this.getUniformTexture(gl, col);
        float[] coordData = {
                    0, 0, //
                    0, 1, //
                    1, 0, //
                    1, 1,
         };
        float[] vertices = new float[pts.length*3];
        for (int pos = 0; pos < pts.length; pos++) {
        	Vecteur v= pts[pos];
        	vertices[pos*3] = v.getDecX().multiply(echelle).floatValue();
        	vertices[pos*3+1] = v.getDecY().multiply(echelle).floatValue();
        	vertices[pos*3+2] = v.getDecZ().multiply(echelle).floatValue();
        }
            // Setup the vertices into the buffer
        FloatBuffer verts = Buffers.newDirectFloatBuffer(vertices.length);
        verts.put(vertices).position(0);

            // Setup the texture coordinates
        FloatBuffer coords = Buffers.newDirectFloatBuffer(coordData.length);
        coords.put(coordData).position(0);

        mTest.enable(gl);
        mTest.bind(gl);
        gl.glLoadIdentity();
        gl.glTranslatef(100, 100, 0);
        gl.glVertexPointer(3, GL2.GL_FLOAT, 0, verts);
        gl.glTexCoordPointer(2, GL2.GL_FLOAT, 0, coords);
        gl.glDrawArrays(GL2.GL_POLYGON, 0, pts.length);

        gl.glDisableClientState(GL2.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL2.GL_COLOR_ARRAY);
	}

	/** Creation d'une texture d'une seule couleur **/
	protected Texture getUniformTexture(GL2 gl, Color color) {
		BufferedImage off_Image =
				  new BufferedImage(8, 8,
				                    BufferedImage.TYPE_INT_ARGB);

		Graphics2D g2 = off_Image.createGraphics();
		g2.setBackground(color);
		g2.fillRect(0, 0, 8, 8);
		
		Texture txt = AWTTextureIO.newTexture(GLProfile.getDefault (), off_Image, true);
		
		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_BASE_LEVEL, 0);
		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAX_LEVEL, 5);
		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_GENERATE_MIPMAP, GL2.GL_TRUE);
		
		return txt;
	}



}
