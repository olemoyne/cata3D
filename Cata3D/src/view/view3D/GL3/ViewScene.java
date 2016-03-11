package view.view3D.GL3;

import java.awt.Color;
import java.util.ArrayList;

import model.math.Vecteur;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;

import view.scene.PrintableObject;
import view.scene.PrintableScene;

/**
 * Gestion d'une scene a afficher avec 
 *    une liste de points [ Vertice ]
 *    une liste de formes [ premier Vertice, Nb Vertice, type ]
 *    une liste de couleurs  
 * 
 *  
 * @author olemoyne
 *
 */
public class ViewScene {
	// Vertex Array Objects Identifiers --> Permet d'identifier les buffers
	ArrayList<SceneObject> objects;
	
	public int vertexLoc, colorLoc;
	
	public ViewScene () {
		objects = new ArrayList<SceneObject>();
	}

	public void computeScene(PrintableScene scene) {
		objects.clear();
		// Setup the axes
		Vecteur[] pts = new Vecteur[2]; pts[0] = new Vecteur ("-20;0;0"); pts[1] = new Vecteur ("20;0;0");
		objects.add(new SceneObject(pts, GL.GL_LINES, Color.red));
		pts = new Vecteur[2]; pts[0] = new Vecteur ("0;-20;0"); pts[1] = new Vecteur ("0;20;0");
		objects.add(new SceneObject(pts, GL.GL_LINES, Color.green));
		pts = new Vecteur[2]; pts[0] = new Vecteur ("0;0;-20"); pts[1] = new Vecteur ("0;0;20");
		objects.add(new SceneObject(pts, GL.GL_LINES, Color.blue));
		for (PrintableObject po : scene.allObjects) {
			po.getSceneObjects(objects);
		}		
	}

	public void dispayObjects(GL3 gl) {
		for (SceneObject obj : objects) {
			gl.glBindVertexArray(obj.vertexArrayId);
			gl.glDrawArrays(obj.getForme(), 0, obj.points.length);
		}
	}

	public void generateBuffer(GL3 gl) {
		// Calcule les buffer arrays associés à la liste des objects
		int[] vertexArrayList = new int[objects.size()];

		gl.glGenVertexArrays(objects.size(), vertexArrayList, 0);
		
		int pos = 0;
		for (SceneObject obj : objects) {
			obj.getBuffer(gl, vertexArrayList[pos], vertexLoc, colorLoc);
			pos ++;
		}
	}
}
