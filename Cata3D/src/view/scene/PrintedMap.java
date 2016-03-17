package view.scene;

import java.awt.Color;

import view.view3D.Printer;
import model.Position;
import model.math.Axis;
import model.math.Decimal;
import model.math.MapDeVecteurs;

import com.jogamp.opengl.GL2;
/**
 *  
 * @author lemoyne
 *
 */
public class PrintedMap extends PrintableObject {

	private MapDeVecteurs map;
	private boolean fillup;
	
	public PrintedMap (MapDeVecteurs mp, String nom, boolean isFilled, Color col, Position pos) {
		super(nom, col, pos);
		map = mp;
		fillup = isFilled;
		position = pos;
	}

	
	
	@Override
	public void drawObject(GL2 gl, int mode) {

		if (map == null) return;
		
		Decimal centre = map.getCenter().getDecX();

		if ((!fillup)&&(mode == 0)) {
					
			gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE, Printer.getColor(color, GL2.GL_DIFFUSE), 0);
		    gl.glShadeModel(GL2.GL_SMOOTH);
		    
			gl.glBegin(GL2.GL_QUADS);		
	
			// Trace tous les carrr�s
			for (int x = 1; x < map.xSize(); x ++) {
				for (int y = 1; y < map.ySize(); y ++) {
					Printer.drawCarre(
							gl, map.getPoint(x-1,  y-1),
							map.getPoint(x-1,  y),
							map.getPoint(x,  y),
							map.getPoint(x,  y-1));
				}
				// Fermeture du bazard
				int y = map.ySize()-1;
				Printer.drawCarre(
						gl, map.getPoint(x-1,  y),
						map.getPoint(x-1,  y).setDec(Axis.XAxis, centre),
						map.getPoint(x,  y).setDec(Axis.XAxis, centre),
						map.getPoint(x,  y));
				Printer.drawCarre(
						gl, map.getPoint(x-1,  0),
						map.getPoint(x,  0), 
						map.getPoint(x,  0).setDec(Axis.XAxis, centre),
						map.getPoint(x-1,  0).setDec(Axis.XAxis, centre));
			}		
			// Dessus de la forme
			int x = map.xSize()-1;
			for (int y = 1; y < map.ySize(); y ++) {
				Printer.drawCarre(
						gl, map.getPoint(x,  y-1),
						map.getPoint(x,  y),
						map.getPoint(0,  y),
						map.getPoint(0,  y-1));
			}
			gl.glEnd();
		} else {
			gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE, Printer.getColor(color, GL2.GL_DIFFUSE), 0);
		    gl.glShadeModel(GL2.GL_SMOOTH);
		    
			gl.glBegin(GL2.GL_LINES);		
	
			// Trace tous les carrr�s
			for (int x = 1; x < map.xSize(); x ++) {
				for (int y = 1; y < map.ySize(); y ++) {
					Printer.drawLineCarre(
							gl, map.getPoint(x-1,  y-1),
							map.getPoint(x-1,  y),
							map.getPoint(x,  y),
							map.getPoint(x,  y-1));
				}
				// Fermeture du bazard
				int y = map.ySize()-1;
				Printer.drawLineCarre(
						gl, map.getPoint(x-1,  y),
						map.getPoint(x-1,  y).setDec(Axis.XAxis, centre),
						map.getPoint(x,  y).setDec(Axis.XAxis, centre),
						map.getPoint(x,  y));
				Printer.drawLineCarre(
						gl, map.getPoint(x-1,  0),
						map.getPoint(x,  0), 
						map.getPoint(x,  0).setDec(Axis.XAxis, centre),
						map.getPoint(x-1,  0).setDec(Axis.XAxis, centre));
			}		
			int x = map.xSize()-1;
			for (int y = 1; y < map.ySize(); y ++) {
				Printer.drawLineCarre(
						gl, map.getPoint(x,  y-1),
						map.getPoint(x,  y),
						map.getPoint(0,  y),
						map.getPoint(0,  y-1));
			}
			gl.glEnd();
		}
	}
	
}
