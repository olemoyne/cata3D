package appli;

import model.Cata;

import com.jogamp.opengl.GL2;

import view.ViewUpdate;

public class CataViewUpdate extends ViewUpdate {

	private Cata dessin;
	
	public CataViewUpdate(String inc) {
		super(inc);
	}
	
	public void setCata (Cata dsn) {
		dessin = dsn;
	}
	
	/**
	 *  Affichage des données dans le CANVAS
	 *   
	 * @param gl
	 */
    public void drawInside(GL2 gl) {
    	super.drawInside(gl);
		
    	if (dessin == null) return ;
    	
    	drawCata(gl);
	}


    /** 
     * Doit être mise à jour en fonction du type de dessin à faire avec le Cata
     */
	public void drawCata(GL2 gl) {
	}



}
