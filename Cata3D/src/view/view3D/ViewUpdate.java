package view.view3D;

import java.awt.Color;
import model.math.Vecteur;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;


/**
 * Vision des formes en 3D
 * 
 * @author olemoyne
 *
 */
public class ViewUpdate implements GLEventListener{
	
    protected PositionCamera camera;
    
    protected int precision;
    
    /**
     * Initialisation de la vue 3D
     */
	public ViewUpdate(String inc) {
		camera = new PositionCamera(inc);
		precision = 2;
	}

	/** 
	 * Init de la vue 
	 */
	public void init(GLAutoDrawable drawable) {
      GL2 gl = drawable.getGL().getGL2();
      gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f); //erasing color
      gl.glColor3f(0.0f, 0.0f, 0.0f); // drawing color
      gl.glViewport(-100, -100, 200, 200);
      
      drawInside(gl);
      changeCameraPosition(gl);
    }

	
    private void drawAxis (GL2 gl) {
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE);
        gl.glEnable(GL.GL_BLEND);
        
        gl.glBegin(GL.GL_LINES);
        gl.glColor3d(1, 0, 0);
        gl.glVertex3f(0, 0, 0);
        gl.glVertex3f(8f, 0, 0);

        gl.glColor3d(0, 1, 0);
        gl.glVertex3f(0, 0, 0);
        gl.glVertex3f(0, 8f, 0);

        gl.glColor3d(0, 0, 1);
        gl.glVertex3f(0, 0, 0);
        gl.glVertex3f(0, 0.0f, 8f);

        gl.glEnd();
        gl.glDisable(GL.GL_BLEND);

    }

	/**
	 *  Affichage des données dans le CANVAS
	 *   
	 * @param gl
	 */
    public void drawInside(GL2 gl) {
    	gl.glEnable(GL.GL_BLEND);        
	
		this.drawAxis(gl);
		
	}

    
	/**
	 *  Affichage du CANVAS
	 *   
	 * @param gl
	 */
	public void display(GLAutoDrawable drawable) {
          GL2 gl = drawable.getGL().getGL2();
          gl.glClear(GL.GL_COLOR_BUFFER_BIT);
          gl.glMatrixMode(GL2.GL_MODELVIEW);
          
          changeCameraPosition(gl);
          drawInside(gl);
    }

	/**
	 *  Position de la caméra
	 *   
	 * @param gl
	 */
    public void changeCameraPosition (GL2 gl) {
      if (camera.isUpdated()) {
            gl.glMatrixMode(GL2.GL_PROJECTION);
            gl.glLoadIdentity();

            GLU glu=new GLU();
            glu.gluPerspective(90, 1, 1, 120);
            camera.lookAt(glu);
      }
    }

    

	/**
	 *  positionnement de la taille de l'écran
	 *   
	 * @param gl
	 */
    public void reshape(GLAutoDrawable drawable,
                        int x,
                        int y,
                        int width,
                        int height) {
    	GL gl = drawable.getGL();
    	this.changeTailleEcran(gl, width, height);
/*         
      gl.glViewport(0, 0, width, height);
      gl.glMatrixMode(GL.GL_PROJECTION);
      gl.glLoadIdentity();
      gl.glOrtho(-width, width, -height, height, -1, 1);
*/       }

    public void changeTailleEcran(GL gl, int width, int height) {
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
	

	/**
	 * Affichage d'un point
	 * 
	 * @param o
	 * @param col
	 * @param gl
	 */
	public void printPoint (Vecteur v, Color col, GL2 gl) {
	    gl.glPushMatrix();       //equivalent to 'save current position'

	    float r = col.getRed()/(float)256;
		float g = col.getGreen()/(float)256;
		float b = col.getBlue()/(float)256;
		
		gl.glColor3f(r, g, b);

		GLU glu = new GLU();
	    GLUquadric quadric = glu.gluNewQuadric();    //In Jogl
	    glu.gluQuadricTexture(quadric, true);
        gl.glTranslatef(v.getDecX().floatValue(), v.getDecY().floatValue(), v.getDecZ().floatValue());
        glu.gluSphere(quadric, 0.01f, 5, 5);
	    gl.glPopMatrix();        //equivalent to 'load the last position saved'
	}

	/**
	 * Affiche une Facette **
	 *
	 * 
	 * @param fac
	 * @param col
	 * @param gl
	 *
	public void printFacette (Triangle fac, Color col, GL2 gl) {
        gl.glBegin(GL2.GL_LINE_STRIP);

        float r = col.getRed()/(float)256;
		float g = col.getGreen()/(float)256;
		float b = col.getBlue()/(float)256;
		
		gl.glColor3f(r, g, b);

		for (Vecteur pt : fac.getSommets()) {
			gl.glVertex3f(pt.getDecX().floatValue(), pt.getDecY().floatValue(), pt.getDecZ().floatValue());			
		}
		Vecteur pt = fac.getSommets()[0];
		gl.glVertex3f(pt.getDecX().floatValue(), pt.getDecY().floatValue(), pt.getDecZ().floatValue());			
        gl.glEnd();
	}

	/**
	 * Affiche une Arrete 
	 * 
	 * @param arr
	 * @param col
	 * @param gl
	 */
	public void printSegment (Vecteur a, Vecteur b, Color col, GL2 gl) {
        gl.glBegin(GL.GL_LINE_STRIP);

        float r = col.getRed()/(float)256;
		float g = col.getGreen()/(float)256;
		float bl = col.getBlue()/(float)256;
		
		gl.glColor3f(r, g, bl);

		gl.glVertex3f(a.getDecX().floatValue(), a.getDecY().floatValue(), a.getDecZ().floatValue());
		gl.glVertex3f(b.getDecX().floatValue(), b.getDecY().floatValue(), b.getDecZ().floatValue());
        gl.glEnd();
	}
	
	/**
	 * Affiche un polyedre
	 *
	public void printForme (Triangle facette, Color col, GL2 gl, int type) {
		if (facette== null) return;
		
		// Affiche chaque arrête du polyedre
		if (type == 0) { 
			Vecteur[] pts = facette.getSommets();
			printSegment(pts[0], pts [1], col, gl);
			printSegment(pts[1], pts [2], col, gl);
			printSegment(pts[2], pts [0], col, gl);
		} else {
			printFacette(facette, col, gl);
			Vecteur[] pts = facette.getSommets();
			printSegment(pts[0], pts [1], col.darker(), gl);
			printSegment(pts[1], pts [2], col.darker(), gl);
			printSegment(pts[2], pts [0], col.darker(), gl);
		}
	}
*/


	@Override
	public void dispose(GLAutoDrawable drawable) {
	      GL2 gl = drawable.getGL().getGL2();
	      drawInside(gl);
	}

}
