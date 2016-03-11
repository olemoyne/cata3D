package view.view3D;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import model.math.Decimal;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;

import view.scene.PrintableObject;
import view.scene.PrintableScene;

public class PrintableViewUpdate extends ViewUpdate implements ListCellRenderer<PrintableObject> {

	private PrintableScene scene;
	
	private int mode;
	
	public static int PERSPECTIVE_MODE = 0;
	public static int ORTHO_MODE = 1;
	public static int COUPE_MODE = 2;
	
	
	public PrintableViewUpdate(String inc, Decimal ech) {
		super(inc);
		echelle = ech;
		mode = 0;
	}
	
	protected void setScene (PrintableScene scn) {
		scene = scn;
	}

	
	
	protected void setMode (int md) {
		mode = md;
		// Si mode = ORTHO Ajoute les lumières 
	}

	/**
	 *  Affichage du CANVAS
	 *   
	 * @param gl
	 */
	public void display(GLAutoDrawable drawable) {
         GL2 gl = drawable.getGL().getGL2();
  		if (mode == PERSPECTIVE_MODE) {
  			// Nettoie les buffers
          gl.glClear(GL.GL_COLOR_BUFFER_BIT|GL.GL_DEPTH_BUFFER_BIT);  		
			// positionne la caméra
          changeCameraPosition(gl);

          drawInside(gl);
		}
		if (mode == ORTHO_MODE) {
  			// Nettoie les buffers
	          gl.glClear(GL.GL_COLOR_BUFFER_BIT|GL.GL_DEPTH_BUFFER_BIT);
				// positionne la caméra
		      changeCameraPosition(gl);


//		        http://malgouyres.org/cours/data/jogl.pdf
		        
/*	            GLU glu=new GLU();

		        // Draw sphere (possible styles: FILL, LINE, POINT).
		        gl.glColor3f(0.3f, 0.5f, 1f);
		        GLUquadric earth = glu.gluNewQuadric();
		        glu.gluQuadricDrawStyle(earth, GLU.GLU_FILL);
		        glu.gluQuadricNormals(earth, GLU.GLU_FLAT);
		        glu.gluQuadricOrientation(earth, GLU.GLU_OUTSIDE);
		        final float radius = 1f;
		        final int slices = 16;
		        final int stacks = 16;
		        glu.gluSphere(earth, radius, slices, stacks);
		        glu.gluDeleteQuadric(earth);
*/	          drawInside(gl);
		      
		}
    }

	
	/**
	 *  Affichage des donnï¿½es dans le CANVAS
	 *   
	 * @param gl
	 */
    public void drawInside(GL2 gl) {
    	// Affiche les axes
    	super.drawInside(gl);
		
    	if (scene == null) return ;
    	
    	scene.printScene(gl, echelle, mode);
	}

	/** 
	 * Dï¿½finit le composant ï¿½ afficher :
	 *    --> label noir avec en fond blanc le nom de la forme
	 *      --> gras si selectionnï¿½
	 */
	@Override
	public Component getListCellRendererComponent(JList<? extends PrintableObject> list,
			PrintableObject value, int index, boolean isSelected, boolean cellHasFocus) {

		JLabel lab = new JLabel (value.name);
		lab.setBackground(Color.black);
		lab.setForeground(Color.white);
		
		if (value.toBePrinted) lab.setFont(new Font("Arial", Font.BOLD, 12));
		else lab.setFont(new Font("Arial", Font.PLAIN, 12));
		return lab;
	}

}
