package appli;

import com.jogamp.opengl.GL2;

import view.ViewUpdate;
import view.scene.PrintableScene;

public class CataViewUpdate extends ViewUpdate {

	private PrintableScene scene;
	
	public CataViewUpdate(String inc) {
		super(inc);
	}
	
	public void setScene (PrintableScene scn) {
		scene = scn;
	}
	
	/**
	 *  Affichage des données dans le CANVAS
	 *   
	 * @param gl
	 */
    public void drawInside(GL2 gl) {
    	super.drawInside(gl);
		
    	if (scene == null) return ;
    	
    	scene.printScene(gl);
	}


}
