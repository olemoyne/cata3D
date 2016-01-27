package model.math.transfo;

import java.io.Serializable;

import model.math.Vecteur;

/**
 * Une transformation de type translation
 * 
 * @author olemoyne
 *
 */
public class Translation extends Transformation implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 890800828121902482L;
	public Vecteur trans;
	
	/**
	 * Positionne une translation
	 * 
	 * @param tr
	 */
	public Translation(Vecteur tr, Transformation u) {
		super(u);
		trans = tr;
	}

	public Vecteur getPoint(Vecteur ret) {
		if (ret == null) throw new NullPointerException ("Translation : point de départ nul");
		return new Vecteur (ret.getX()+trans.getX(), ret.getY()+trans.getY(), ret.getZ()+trans.getZ());
	}	
}
