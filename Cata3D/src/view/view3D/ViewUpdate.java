package view.view3D;

import java.awt.Color;

import model.math.Decimal;
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
      GL2 gl = drawable.getGL().getGL2();
      gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f); //erasing color
      gl.glColor3f(0.0f, 0.0f, 0.0f); // drawing color
      gl.glViewport(-400, -400, 800, 800);
      
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
	 *  Affichage des donn�es dans le CANVAS
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
	 *  Position de la cam�ra
	 *   
	 * @param gl
	 */
    public void changeCameraPosition (GL2 gl) {
      if (camera.isUpdated()) {
            gl.glMatrixMode(GL2.GL_PROJECTION);
            gl.glLoadIdentity();

            GLU glu=new GLU();
            glu.gluPerspective(90, 1, 1, 12);
            camera.lookAt(glu);
      }
    }

    

	/**
	 *  positionnement de la taille de l'�cran
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
	
	// Ajoute un vecteur en appliquant l'echelle
	public void setPoint(Vecteur a, GL2 gl) {
		gl.glVertex3f(a.getDecX().multiply(echelle).floatValue(), a.getDecY().multiply(echelle).floatValue(), a.getDecZ().multiply(echelle).floatValue());
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
        gl.glTranslatef(v.getDecX().multiply(echelle).floatValue(), v.getDecY().multiply(echelle).floatValue(), v.getDecZ().multiply(echelle).floatValue());
        glu.gluSphere(quadric, 0.005f, 2, 2);
	    gl.glPopMatrix();        //equivalent to 'load the last position saved'
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

		setPoint(a, gl);
		setPoint(b, gl);
		
        gl.glEnd();
	}
	

	@Override
	public void dispose(GLAutoDrawable drawable) {
	      GL2 gl = drawable.getGL().getGL2();
	      drawInside(gl);
	}

}
