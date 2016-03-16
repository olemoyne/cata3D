package view.view3D;

import java.awt.Color;

import model.math.Decimal;
import model.math.Vecteur;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLU;


/**
 * Vision des formes en 3D
 * 
 * @author olemoyne
 *
 */
public class ViewUpdate implements GLEventListener{
	
    protected PositionCamera camera;
    
    protected int precision;
    
    protected Decimal echelle;
    
    /**
     * Initialisation de la vue 3D
     */
	public ViewUpdate(String inc) {
		camera = new PositionCamera(inc);
		precision = 2;
		echelle = Decimal.UN;
	}

	/** 
	 * Init de la vue 
	 */
	public void init(GLAutoDrawable drawable) {
      // Positionne les éléments valables dans toutes les vues 3D 
      GL2 gl = drawable.getGL().getGL2();
      gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f); //erasing color
      gl.glColor3f(0.0f, 0.0f, 0.0f); // drawing color
      gl.glViewport(-400, -400, 800, 800);
      // Gestion de la profondeur --> less_or_equals affiche les elements qui sont devant
      gl.glEnable( GL2.GL_DEPTH_TEST );
      gl.glDepthFunc( GL2.GL_LEQUAL );
      // Model de coloration : avec dégradé
      gl.glShadeModel(GL2.GL_SMOOTH);
      // Gestion de la perspective
      gl.glHint( GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST );
      gl.glHint( GL2.GL_LINE_SMOOTH_HINT, GL2.GL_NICEST );

      // Gestion de la lumière
      float pos[] = { 5.0f, 5.0f, 0.0f, 0.0f };
      float ambiant[] = { 0.5f, 0.5f, 0.5f, 0.7f };
      gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, pos, 0);
      gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, ambiant, 0);
      
      gl.glEnable(GL2.GL_CULL_FACE);
      gl.glEnable(GL2.GL_LIGHTING);
      gl.glEnable(GL2.GL_LIGHT0);
      gl.glEnable(GL2.GL_DEPTH_TEST);

    }
	
	public float[] getColor(Color color) {
        float[] rgba = new  float[4];
		rgba[0] = color.getRed()/(float)256;
        rgba[1] = color.getGreen()/(float)256;
        rgba[2] = color.getBlue()/(float)256;
        rgba[3] = 0.7f;
		
        return rgba; 
	}


// Pas de transparence	
    private void drawAxis (GL2 gl) {
        // Mode de gestion de la tranparence (ALPHA) --> Les axes sont tranparents à 80% 
        gl.glEnable(GL.GL_BLEND);
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE);
        
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE, getColor(Color.red), 0);
	    gl.glShadeModel(GL2.GL_SMOOTH);

        gl.glBegin(GL.GL_LINES);
        gl.glVertex3f(-8f, 0, 0);
        gl.glVertex3f(8f, 0, 0);
        gl.glEnd();

		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE, getColor(Color.green), 0);
        gl.glBegin(GL.GL_LINES);
        gl.glVertex3f(0, -8f, 0);
        gl.glVertex3f(0, 8f, 0);
        gl.glEnd();

		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE, getColor(Color.blue), 0);
        gl.glBegin(GL.GL_LINES);
        gl.glVertex3f(0, 0, -8f);
        gl.glVertex3f(0, 0.0f, 8f);
        gl.glEnd();
        
        gl.glDisable(GL.GL_BLEND);

    }

	/**
	 *  Affichage des donnï¿½es dans le CANVAS
	 *   
	 * @param gl
	 */
    public void drawInside(GL2 gl) {
    	// Passe en mode de calcul de type model
        gl.glMatrixMode(GL2.GL_MODELVIEW);          
		this.drawAxis(gl);
	}

    
	/**
	 *  Affichage du CANVAS
	 *   
	 * @param gl
	 */
	public void display(GLAutoDrawable drawable) {
          GL2 gl = drawable.getGL().getGL2();
          gl.glClear(GL.GL_COLOR_BUFFER_BIT|GL.GL_DEPTH_BUFFER_BIT);
          drawInside(gl);
          changeCameraPosition(gl);
    }

	/**
	 *  Position de la camï¿½ra
	 *   
	 * @param gl
	 */
    public void changeCameraPosition (GL2 gl) {
      if (camera.isUpdated()) {
            gl.glMatrixMode(GL2.GL_PROJECTION);
            gl.glLoadIdentity();

            GLU glu=new GLU();
            glu.gluPerspective(70, 4.0/3.0, 0.1, 12);
            camera.lookAt(glu);
      }
    }

    

	/**
	 *  positionnement de la taille de l'ï¿½cran
	 *   
	 * @param gl
	 */
    public void reshape(GLAutoDrawable drawable,
                        int x,
                        int y,
                        int width,
                        int height) {
    	GL2 gl = drawable.getGL().getGL2();
    	this.changeTailleEcran(gl, width, height);
/*         
      gl.glViewport(0, 0, width, height);
      gl.glMatrixMode(GL.GL_PROJECTION);
      gl.glLoadIdentity();
      gl.glOrtho(-width, width, -height, height, -1, 1);
*/       }

    public void changeTailleEcran(GL2 gl, int width, int height) {
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrtho(-width, width, -height, height, -1, 1);
     }

	public void displayChanged(GLAutoDrawable drawable,
                               boolean modeChanged,
                               boolean deviceChanged) {
    }
	
	public void changeCameraPosition(String action) {
		if (action.equals("moveUp")) this.camera.moveUp();
		if (action.equals("moveDown")) this.camera.moveDown();
		if (action.equals("moveRight")) this.camera.moveRight();
		if (action.equals("moveLeft")) this.camera.moveLeft();
		if (action.equals("moveInto")) this.camera.moveInto();
		if (action.equals("moveOut")) this.camera.moveOut();
		if (action.equals("lookUp")) this.camera.lookUp();
		if (action.equals("lookDown")) this.camera.lookDown();
		if (action.equals("lookRight")) this.camera.lookRight();
		if (action.equals("lookLeft")) this.camera.lookLeft();
		if (action.equals("lookInto")) this.camera.lookInto();
		if (action.equals("lookOut")) this.camera.lookOut();
   	}

	public Vecteur getPosition() {
		return camera.getPosition();
	}

	public void centrer() {
		camera.reset();
	}
	
	// Ajoute un vecteur en appliquant l'echelle
	public void setPoint(Vecteur a, GL2 gl) {
		gl.glVertex3f(a.getDecX().multiply(echelle).floatValue(), a.getDecY().multiply(echelle).floatValue(), a.getDecZ().multiply(echelle).floatValue());
	}


	@Override
	public void dispose(GLAutoDrawable drawable) {
	      GL2 gl = drawable.getGL().getGL2();
	      drawInside(gl);
	}

}
