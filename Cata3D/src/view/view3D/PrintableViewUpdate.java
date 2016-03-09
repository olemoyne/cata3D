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
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;

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
	}

	/** 
	 * Init de la vue : gestion du mode  
	 */
	public void init(GLAutoDrawable drawable) {
      GL2 gl = drawable.getGL().getGL2();
      // 3D enabling
      gl.glEnable( GL2.GL_DEPTH_TEST );
      gl.glDepthFunc( GL2.GL_LEQUAL );

      gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f); //erasing color
      gl.glColor3f(0.0f, 0.0f, 0.0f); // drawing color
      gl.glViewport(-400, -400, 800, 800);	      

      drawInside(gl);
      changeCameraPosition(gl);
    }

	/**
	 *  Affichage du CANVAS
	 *   
	 * @param gl
	 */
	public void display(GLAutoDrawable drawable) {
          GL2 gl = drawable.getGL().getGL2();
  		if (mode == PERSPECTIVE_MODE) {
          gl.glClear(GL.GL_COLOR_BUFFER_BIT|GL.GL_DEPTH_BUFFER_BIT);
  		
          changeCameraPosition(gl);
          gl.glMatrixMode(GL2.GL_MODELVIEW);

          drawInside(gl);
		}
		if (mode == ORTHO_MODE) {
		      changeCameraPosition(gl);
	          gl.glMatrixMode(GL2.GL_MODELVIEW);

	          gl.glShadeModel( GL2.GL_SMOOTH );
		      gl.glClearColor( 0f, 0f, 0f, 0f );
		      gl.glClearDepth( 1.0f );
		      gl.glEnable( GL2.GL_DEPTH_TEST );
		      gl.glDepthFunc( GL2.GL_LEQUAL );
		      gl.glHint( GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST );

		      gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT); 

		        float SHINE_ALL_DIRECTIONS = 1;
		        float[] lightPos = {-100, 0, 00, SHINE_ALL_DIRECTIONS};
		        float[] lightColorAmbient = {0.2f, 0.2f, 0.2f, 1f};
		        float[] lightColorSpecular = {0.8f, 0.8f, 0.8f, 0.5f};

		        // Set light parameters.
		        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_POSITION, lightPos, 0);
//		        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_AMBIENT, lightColorAmbient, 0);
		        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_SPECULAR, lightColorSpecular, 0);

		        // Enable lighting in GL.
		        gl.glEnable(GL2.GL_LIGHT1);
		        gl.glEnable(GL2.GL_LIGHTING);

		        // Set material properties.
		        float[] rgba = {1f, 1f, 1f};
		        gl.glMaterialfv(GL.GL_FRONT, GL2.GL_AMBIENT, rgba, 0);
		        gl.glMaterialfv(GL.GL_FRONT, GL2.GL_SPECULAR, rgba, 0);
		        gl.glMaterialf(GL.GL_FRONT, GL2.GL_SHININESS, 0.5f);
		        
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
	 *  Affichage des donn�es dans le CANVAS
	 *   
	 * @param gl
	 */
    public void drawInside(GL2 gl) {
//    	super.drawInside(gl);
		
    	if (scene == null) return ;
    	
    	scene.printScene(gl, echelle, mode);
	}

	/** 
	 * D�finit le composant � afficher :
	 *    --> label noir avec en fond blanc le nom de la forme
	 *      --> gras si selectionn�
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
