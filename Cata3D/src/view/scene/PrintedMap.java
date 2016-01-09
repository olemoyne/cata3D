package view.scene;

import java.awt.Color;
import java.util.ArrayList;

import model.math.Decimal;
import model.math.MapDeVecteurs;
import model.math.Vecteur;

import com.jogamp.opengl.GL2;
/**
 * TODO : nettoyage de la coque
 *  
 * @author lemoyne
 *
 */
public class PrintedMap extends PrintableObject {

	private MapDeVecteurs map;
	private boolean fillup;
	
	public PrintedMap (MapDeVecteurs mp, String nom, boolean isFilled, Color col) {
		super(nom, col);
		map = mp;
		fillup = isFilled;
	}
	
	public void setColor(GL2 gl, ArrayList<Vecteur> pts) {
        float r = color.getRed()/(float)256;
		float g = color.getGreen()/(float)256;
		float b = color.getBlue()/(float)256;
		
		gl.glColor3f(r, g, b);		
	}
	
	@Override
	public void drawObject(GL2 gl, Decimal echelle) {

		if (map == null) return;
		setColor (gl, null);
		
		if (!fillup) {
			Vecteur pt = null;
			// Trace tous les trais verticaux
			for (int y = 0; y < map.ySize(); y ++) {
		        gl.glBegin(GL2.GL_LINE_STRIP);
				for (int x = 0; x < map.xSize(); x ++) {
					pt = map.getPoint(x,  y);
					setPoint(pt, gl, echelle);
				}
				pt = map.getPoint(0,  y);
				setPoint(pt, gl, echelle);
				gl.glEnd();
			}
			// Trace tous les trais horizontaux
			for (int x = 0; x < map.xSize(); x ++) {
		        gl.glBegin(GL2.GL_LINE_STRIP);
				for (int y = 0; y < map.ySize(); y ++) {
					pt = map.getPoint(x,  y);
					setPoint(pt, gl, echelle);
				}
				pt = map.getPoint(x,  0);
//				gl.glVertex3f(pt.getDecX().floatValue(), pt.getDecY().floatValue(), pt.getDecZ().floatValue());			
				gl.glEnd();
			}
		} else {
			Vecteur pt= null;
			ArrayList<Vecteur>lst = new ArrayList<Vecteur>();
			// Trace tous les carrr�s
			for (int x = 1; x < map.xSize(); x ++) {
				for (int y = 1; y < map.ySize(); y ++) {
					lst.clear();
					lst.add(map.getPoint(x-1,  y-1));
					lst.add(map.getPoint(x,  y-1));
					lst.add(map.getPoint(x,  y));
					lst.add(map.getPoint(x-1,  y));
					
					setColor (gl, lst);
			        gl.glBegin(GL2.GL_POLYGON);
						pt = map.getPoint(x-1,  y-1);
						setPoint(pt, gl, echelle);
						pt = map.getPoint(x,  y-1);
						setPoint(pt, gl, echelle);
						pt = map.getPoint(x,  y);
						setPoint(pt, gl, echelle);
						pt = map.getPoint(x-1,  y);
						setPoint(pt, gl, echelle);
					gl.glEnd();
				}
			}
			
		}
			
	}

}
