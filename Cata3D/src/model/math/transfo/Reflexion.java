package model.math.transfo;

import java.io.Serializable;

import model.math.Axis;
import model.math.Vecteur;

/**
 * Une transformation de type rotation
 * 
 * @author olemoyne
 *
 */
public class Reflexion extends Transformation implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 416690175014402643L;
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
	public Reflexion(int ax, Transformation u) {
		super(u);
		axis = ax;		
	}

	public Vecteur getPoint(Vecteur ret) {
		if (ret == null) throw new NullPointerException ("Reflexion : point de d�part nul");
		if (axis == Axis.XAxis) 
			return new Vecteur(-1*ret.getX(), ret.getY(), ret.getZ());
		if (axis == Axis.YAxis) 
			return new Vecteur(ret.getX(), -1*ret.getY(), ret.getZ());
		if (axis == Axis.ZAxis) 
			return new Vecteur(ret.getX(), ret.getY(), -1*ret.getZ());
		return null;
	}	
	
	public Transformation getReverse(Transformation tr) {
		Reflexion ref = new Reflexion(this.axis, tr);
		return super.getReverse(ref);
	}

}
