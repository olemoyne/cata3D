package model.math.transfo;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.io.Serializable;

import model.math.Axis;
import model.math.Decimal;
import model.math.Vecteur;

/**
 * Une transformation de type rotation
 * 
 * @author olemoyne
 *
 */
public class Rotation extends Transformation implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8274275565953910235L;

	/**
	 * Axe de rotation
	 */
	public int axis;
	
	public Decimal angle;
	
	/**
	 * Transformation
	 */
	protected AffineTransform trans;


	/**
	 * Positionne une rotation
	 * 
	 * @param ax
	 * @param angle
	 */
	public Rotation(int ax, Decimal angl, Transformation u) {
		super(u);
		angle = angl;
		Decimal radAngle = angle.divide(new Decimal(180)).multiply(new Decimal(Math.PI));
		float flt = radAngle.floatValue();
		trans = AffineTransform.getRotateInstance(flt);
		axis = ax;
	}
	
	
	public Vecteur getPoint(Vecteur ret) {
		if (ret == null) throw new NullPointerException ("Rotation : point de d�part nul");
		if (axis == Axis.XAxis) {
			Point2D.Double pt = new Point2D.Double(ret.getDecY().doubleValue(), ret.getDecZ().doubleValue());
			Point2D.Double f = new Point2D.Double();
			trans.deltaTransform(pt, f);
			return new Vecteur(ret.getDecX(), new Decimal(f.getX()), new Decimal(f.getY()));
		}
		if (axis == Axis.YAxis) {
			Point2D.Double pt = new Point2D.Double(ret.getDecX().doubleValue(), ret.getDecZ().doubleValue());
			Point2D.Double f = new Point2D.Double();
			trans.deltaTransform(pt, f);
			return new Vecteur(new Decimal(f.getX()), ret.getDecY(), new Decimal(f.getY()));
		}
		if (axis == Axis.ZAxis) {
			Point2D.Double pt = new Point2D.Double(ret.getDecX().doubleValue(), ret.getDecY().doubleValue());
			Point2D.Double f = new Point2D.Double();
			trans.deltaTransform(pt, f);
			return new Vecteur(new Decimal(f.getX()), new Decimal(f.getY()), ret.getDecZ());
		}
		return null;
	}
	
	public Transformation getReverse(Transformation tr) {
		Rotation ref = new Rotation(this.axis, angle.negate(), tr);
		return super.getReverse(ref);
	}

}
