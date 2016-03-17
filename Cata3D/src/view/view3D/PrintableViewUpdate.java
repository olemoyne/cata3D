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
	private boolean updated;
	private int mode;

	private Decimal echelle;
    	
	public static int PERSPECTIVE_MODE = 0;
	public static int ORTHO_MODE = 1;
	public static int COUPE_MODE = 2;
	
	
	public PrintableViewUpdate(String inc, Decimal ech) {
		super(inc);
		echelle = ech;
		mode = 0;
		updated = true;
	}
	
	protected void setScene (PrintableScene scn) {
		scene = scn;
		updated = true;
	}

	protected void setEchelle (Decimal ech) {
		echelle = ech;
		updated = true;
	}
	
	
	protected void setMode (int md) {
		mode = md;
		updated = true;
		// Si mode = ORTHO Ajoute les lumières 
	}

    public void initDrawing(GL2 gl) {
    	// Positionne l'échelle 
		gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
        gl.glScalef(echelle.floatValue(), echelle.floatValue(), echelle.floatValue());

        this.initAxis(gl);
    	this.initScene(gl);
    }

	private void initScene(GL2 gl) {
		if (scene == null) return ;    	     
        scene.printScene(gl, echelle, mode);
	}

	/**
	 *  Affichage du CANVAS
	 *   
	 * @param gl
	 */
	public void display(GLAutoDrawable drawable) {

         GL2 gl = drawable.getGL().getGL2();
         /// Vide l'écran
         gl.glClear(GL.GL_COLOR_BUFFER_BIT|GL.GL_DEPTH_BUFFER_BIT);
         changeCameraPosition(gl);
        if (updated ) {
        	initDrawing(gl);
        	updated = false;
        }
    	gl.glCallList(axisId);
    	if (scene != null) scene.showScene(gl);
    }

	
	/**
	 *  Affichage des donnï¿½es dans le CANVAS
	 *   
	 * @param gl
	 */
    public void drawInside(GL2 gl) {
    	// Affiche les axes
    	gl.glCallList(axisId);
    	if (scene != null) scene.showScene(gl);
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

	public Decimal getEchelle() {
		return echelle;
	}

}
