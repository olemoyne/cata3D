package view.scene;

import java.awt.Color;

import view.view3D.Printer;
import model.Position;
import model.math.Axis;
import model.math.Decimal;
import model.math.MapDeVecteurs;
import model.math.Vecteur;

import com.jogamp.opengl.GL2;

public class PrintedFluidityMap extends PrintableObject {

	private MapDeVecteurs map;

	public PrintedFluidityMap (String nom, MapDeVecteurs mp, Position pos) {
		super(nom, Color.BLUE, pos);
		map = mp;
	}
	
	public Color getColor(Vecteur a, Vecteur b, Vecteur c, Vecteur d) {
		Vecteur v = Printer.getNormale(a, b, c, d);
		Vecteur dir = new Vecteur ("0;0;1"); //Direction du bateau
		
		Decimal val = Decimal.ZERO;
		Decimal dec = v.getProduitScalaire(dir);
		if (!dec.isNegative()) val = dec.multiply(Decimal.DEUX);

		val = Decimal.min(val, Decimal.UN);
		
		float r = val.floatValue();
		float g = 0f; // color.getGreen()/(float)256;
		float blue = 1f; //color.getBlue()/(float)256;
		
		return new Color(r, g, blue);
				
	}
	
	public void drawCarre (GL2 gl, Vecteur a, Vecteur b, Vecteur c, Vecteur d) {
		

		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE, Printer.getColor(getColor(a, b, c, d), GL2.GL_DIFFUSE), 0);
	    gl.glShadeModel(GL2.GL_SMOOTH);

		gl.glBegin(GL2.GL_QUADS);		

			Printer.drawCarre(gl, a, b, c, d);

		gl.glEnd();

	}
	
	@Override
	public void drawObject(GL2 gl, int mode) {

		if (map == null) return;
		
		Decimal centre = map.getCenter().getDecX();

			// Trace tous les carrr�s
			for (int x = 1; x < map.xSize(); x ++) {
				for (int y = 1; y < map.ySize(); y ++) {
					
					drawCarre(
							gl, map.getPoint(x-1,  y-1),
							map.getPoint(x-1,  y),
							map.getPoint(x,  y),
							map.getPoint(x,  y-1));
				}
				// Fermeture du bazard
				int y = map.ySize()-1;
				drawCarre(
						gl, map.getPoint(x-1,  y),
						map.getPoint(x-1,  y).setDec(Axis.XAxis, centre),
						map.getPoint(x,  y).setDec(Axis.XAxis, centre),
						map.getPoint(x,  y));
				drawCarre(
						gl, map.getPoint(x-1,  0),
						map.getPoint(x,  0), 
						map.getPoint(x,  0).setDec(Axis.XAxis, centre),
						map.getPoint(x-1,  0).setDec(Axis.XAxis, centre));

			}		
			// dessus de la forme
			int x = map.xSize()-1;
			for (int y = 1; y < map.ySize(); y ++) {
				drawCarre(
						gl, map.getPoint(x,  y-1),
						map.getPoint(x,  y),
						map.getPoint(0,  y),
						map.getPoint(0,  y-1));
			}

	}

	
}
