package view.scene;

import java.awt.Color;

import view.view3D.Printer;
import model.Poids;
import model.Position;
import model.math.Decimal;
import model.math.Vecteur;

import com.jogamp.opengl.GL2;

public class PrintedForce extends PrintableObject {

	private Poids pds;
	
	public PrintedForce (Poids p, Color col) {
		super(p.nom, col, new Position(p.position));
		pds = p;
	}

	@Override
	public void drawObject(GL2 gl, int mode) {
	    gl.glPushMatrix();       //equivalent to 'save current position'
	    
	    Printer.drawPoint(gl, pds.position, color);
    	Vecteur e = new Vecteur (Decimal.ZERO, pds.force.divide(new Decimal(20f)).negate(), Decimal.ZERO);
	    Printer.drawLine(gl, new Vecteur("0;0;0"), e, color);

	    gl.glPopMatrix();        //equivalent to 'load the last position saved'
	    
	}

/**	@Override
	public void getSceneObjects(ArrayList<SceneObject> lst) {
		Vecteur len = new Vecteur (new Decimal(0.01), new Decimal(0.01), new Decimal(0.01));
		Vecteur deb = pds.position.minus(len);
		Vecteur fin = pds.position.add(len);
		PrintedBlock.getCube(deb, fin, lst);
		// Ajout la flèche
		Vecteur[] arr = new Vecteur[2];
		arr[0] = pds.position;
		arr[1] = pds.position.add(new Vecteur (Decimal.ZERO, pds.force.divide(new Decimal(20f)).negate(), Decimal.ZERO));
		SceneObject fleche = new SceneObject(arr, GL3.GL_LINE_STRIP, this.color);
		lst.add(fleche);
	}
**/
}
