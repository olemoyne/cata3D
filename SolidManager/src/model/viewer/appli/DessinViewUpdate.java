package model.viewer.appli;

import com.jogamp.opengl.GL2;

import model.concepts.math.Segment;
import model.concepts.math.Triangle;
import model.solid.manage.Composant;
import model.viewer.view.Scene;
import model.viewer.view.ViewUpdate;

public class DessinViewUpdate extends ViewUpdate {

	private Scene dessin;
	
	public DessinViewUpdate(String inc) {
		super(inc);
	}
	
	public void setDessin (Scene dsn) {
		dessin = dsn;
		this.camera.update();
	}
	
	/**
	 *  Affichage des donn�es dans le CANVAS
	 *   
	 * @param gl
	 */
    public void drawInside(GL2 gl) {
    	super.drawInside(gl);
		
    	if (dessin == null) return ;
    	
    	// Affiche le dessin
    	for (Composant cmp : dessin.composants) {
    		if (cmp.type == Composant.TYPE_TRIANGLE)
        		this.printFacette((Triangle)cmp.forme, cmp.color, gl);
    		if (cmp.type == Composant.TYPE_SEGMENT) {
    			Segment seg = (Segment)cmp.forme;
        		this.printSegment(seg.getA(), seg.getB(), cmp.color, gl);
        	}
    	}
    	System.out.println(dessin.comment);
	}



}
