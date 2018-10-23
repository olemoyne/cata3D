package model.concepts.transfo;

import model.concepts.math.Vecteur;

/**
 * Une transformation de type translation
 * 
 * @author olemoyne
 *
 */
public class Translation extends Transformation {
	
	public Vecteur trans;
	
	/**
	 * Positionne une translation
	 * 
	 * @param tr
	 */
	public Translation(Vecteur tr, Transformation t) {
		super(t);
		trans = tr;
	}

	public Vecteur getPoint(Vecteur ret) {
		if (ret == null) throw new NullPointerException ("Translation : point de d�part nul");
		return new Vecteur (ret.getX()+trans.getX(), ret.getY()+trans.getY(), ret.getZ()+trans.getZ());
	}
	
	public Transformation getReverse(Transformation tr) {
		Translation ref = new Translation(trans.negat(), tr);
		return super.getReverse(ref);
	}

}
