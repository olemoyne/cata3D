package model.math.transfo;

import model.math.Axis;
import model.math.Vecteur;

/**
 * Une transformation de type rotation
 * 
 * @author olemoyne
 *
 */
public class Reflexion extends Transformation {
	
	/**
	 * Axe de rotation
	 */
	public int axis;
	
	
	/**
	 * Positionne une reflexion
	 * 
	 * @param ax
	 * @param angle
	 */
	public Reflexion(int ax) {
		axis = ax;		
	}

	public Vecteur getPoint(Vecteur ret) {
		if (ret == null) throw new NullPointerException ("Reflexion : point de départ nul");
		if (axis == Axis.XAxis) 
			return new Vecteur(-1*ret.getX(), ret.getY(), ret.getZ());
		if (axis == Axis.YAxis) 
			return new Vecteur(ret.getX(), -1*ret.getY(), ret.getZ());
		if (axis == Axis.ZAxis) 
			return new Vecteur(ret.getX(), ret.getY(), -1*ret.getZ());
		return null;
	}	
}
