package view.scene;

import java.awt.Color;
import java.util.ArrayList;

import model.calcul.CalculSurface;
import model.math.MapDeVecteurs;
import model.math.Vecteur;

import com.jogamp.opengl.GL2;

public class PrintedFluidityMap extends PrintedMap {

	public PrintedFluidityMap (String nom, MapDeVecteurs mp) {
		super(mp, nom, true, Color.blue);
	}
	
	public void setColor(GL2 gl, ArrayList<Vecteur> pts) {

		
		float r = CalculSurface.getCoeficient(pts).floatValue();
		float g = 0.5f; // color.getGreen()/(float)256;
		float b = 0.5f; //color.getBlue()/(float)256;
		
		gl.glColor3f(r, g, b);		
	}
	
}
