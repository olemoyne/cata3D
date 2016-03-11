package view.view3D.GL3;

import java.awt.Color;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL3;

import model.math.Vecteur;

/**
 * Objet de scene à afficher.
 *    - liste de points
 *    - type de forme
 *    - couleur
 *    - texture // a terme
 * @author olemoyne
 *
 */
public class SceneObject {

	public Vecteur[] points;
	public int forme; // Héritée de OpenGL
	public Color couleur;
	
	public int vertexArrayId;
	
	public SceneObject (Vecteur[] pts, int f, Color c) {
		points = pts;
		forme = f;
		couleur = c;
	}
	
	public int countVertices () {
		return points.length*4;
	}
	
	
	public float[] getVertices() {
		float[] tab = new float[countVertices()]; 
		int pos = 0;
		for (Vecteur v : points) {
			tab[pos] = v.getDecX().floatValue();
			tab[pos+1] = v.getDecY().floatValue();
			tab[pos+2] = v.getDecZ().floatValue();
			tab[pos+3] = 1f;
			pos += 4;
		}
		return tab;
	}

	public float[] getColors() {
		float[] tab = new float[countVertices()]; 
		int pos = 0;
		for (int p = 0; p < points.length; p++) {
			tab[pos] = couleur.getRed();
			tab[pos+1] = couleur.getGreen();
			tab[pos+2] = couleur.getBlue();
			tab[pos+3] = 1f;//couleur.getAlpha();
			pos += 4;
		}
		return tab;
	}

	
	public int getForme() {
		return forme;
	}

	public void getBuffer(GL3 gl, int vao, int vertexLoc, int colorLoc) {
		this.vertexArrayId = vao;
		
		int buffers[] = new int[2];
		
		float[] vertices = this.getVertices();
		float[] colors = this.getColors();

		gl.glBindVertexArray(vao);
		// Generate two slots for the vertex and color buffers
		gl.glGenBuffers(2, buffers, 0);
		// bind buffer for vertices and copy data into buffer
		gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, buffers[0]);
		gl.glBufferData(GL3.GL_ARRAY_BUFFER, vertices.length * Float.SIZE / 8,
		Buffers.newDirectFloatBuffer(vertices), GL3.GL_STATIC_DRAW);
		gl.glEnableVertexAttribArray(vertexLoc);
		gl.glVertexAttribPointer(vertexLoc, 4, GL3.GL_FLOAT, false, 0, 0);

		// bind buffer for colors and copy data into buffer
		gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, buffers[1]);
		gl.glBufferData(GL3.GL_ARRAY_BUFFER, colors.length * Float.SIZE / 8,
		Buffers.newDirectFloatBuffer(colors), GL3.GL_STATIC_DRAW);
		gl.glEnableVertexAttribArray(colorLoc);
		gl.glVertexAttribPointer(colorLoc, 4, GL3.GL_FLOAT, false, 0, 0);
		
	}

}
