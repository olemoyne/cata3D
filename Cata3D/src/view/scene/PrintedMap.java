package view.scene;

import java.awt.Color;
import java.util.ArrayList;

import view.view3D.GL3.SceneObject;
import model.math.Decimal;
import model.math.MapDeVecteurs;
import model.math.Vecteur;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.util.texture.Texture;
/**
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
	public void drawObject(GL2 gl, Decimal echelle, int mode) {

		if (map == null) return;
		
		if ((!fillup)&&(mode == 0)) {
			setColor (gl, null);
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
			// Gestion de la texture 
			Texture texture = this.getUniformTexture(gl, color);
			
			gl.glDisable(GL2.GL_TEXTURE_2D);
			
	        texture.enable(gl);
	        texture.bind(gl);
	        
			Vecteur[] lst = new Vecteur[4];
			// Trace tous les carrr�s
			for (int x = 1; x < map.xSize(); x ++) {
				for (int y = 1; y < map.ySize(); y ++) {
					lst[0] = map.getPoint(x-1,  y-1);
					lst[1] = map.getPoint(x,  y-1);
					lst[2] = map.getPoint(x,  y);
					lst[3] = map.getPoint(x-1,  y);
					
					this.drawPolygon(gl, lst, echelle, color, false);
/**					setColor (gl, lst);
			        gl.glBegin(GL2.GL_POLYGON);
						Vecteur pt = map.getPoint(x-1,  y-1);
						setPoint(pt, gl, echelle);
						pt = map.getPoint(x,  y-1);
						setPoint(pt, gl, echelle);
						pt = map.getPoint(x,  y);
						setPoint(pt, gl, echelle);
						pt = map.getPoint(x-1,  y);
						setPoint(pt, gl, echelle);
					gl.glEnd();
**/				}
			}			
		}
	}
	
	public Color getColor (Vecteur [] pts) {
		return this.color;
	}

	@Override
	public void getSceneObjects(ArrayList<SceneObject> lst) {
		if (map == null) return;
		Vecteur[] arr = new Vecteur[4];
		// Trace tous les carrr�s
		for (int x = 1; x < map.xSize(); x ++) {
			for (int y = 1; y < map.ySize(); y ++) {
				arr[0] = map.getPoint(x-1,  y-1);
				arr[1] = map.getPoint(x,  y-1);
				arr[2] = map.getPoint(x,  y);
				arr[3] = map.getPoint(x-1,  y);
				lst.add(new SceneObject(arr, GL3.GL_LINE_STRIP, this.getColor(arr)));	
			}
		}			
	}
}
