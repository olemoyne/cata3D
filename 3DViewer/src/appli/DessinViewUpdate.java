package appli;

import com.jogamp.opengl.GL2;

import math.Composant;
import math.Dessin;
import view.ViewUpdate;

public class DessinViewUpdate extends ViewUpdate {

	private Dessin dessin;
	
	public DessinViewUpdate(String inc) {
		super(inc);
	}
	
	public void setDessin (Dessin dsn) {
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
    	
    	// Affiche le dessin
    	for (Composant cmp : dessin.getFormes()) {
    		this.printFacette(cmp.forme, cmp.color, gl);
    	}
	}



}
