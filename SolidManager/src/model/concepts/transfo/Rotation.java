package model.concepts.transfo;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import model.concepts.math.Axis;
import model.concepts.math.Decimal;
import model.concepts.math.Vecteur;

/**
 * Une transformation de type rotation
 * 
 * @author olemoyne
 *
 */
public class Rotation extends Transformation {
	
	/**
	 * Axe de rotation
	 */
	public int axis;
	
	/**
	 * Transformation
	 */
	protected AffineTransform trans;
	
	private Decimal angle;


	/**
	 * Positionne une rotation
	 * 
	 * @param ax
	 * @param angle
	 */
	public Rotation(int ax, Decimal a, Transformation t) {
		super(t);
		angle = a;
		Decimal radAngle = angle.divide(new Decimal("180")).multiply(new Decimal(Math.PI));
		float flt = radAngle.floatValue();
		trans = AffineTransform.getRotateInstance(flt);
		axis = ax;
	}

	
	public Vecteur getPoint(Vecteur ret) {
		if (ret == null) throw new NullPointerException ("Rotation : point de d�part nul");
		if (axis == Axis.XAxis) {
			Point2D.Double pt = new Point2D.Double(ret.getY(), ret.getZ());
			Point2D.Double f = new Point2D.Double();
			trans.deltaTransform(pt, f);
			return new Vecteur(ret.getDecX(), new Decimal(f.getX()).divide(Vecteur.METER), new Decimal(f.getY()).divide(Vecteur.METER));
		}
		if (axis == Axis.YAxis) {
			Point2D.Double pt = new Point2D.Double(ret.getX(), ret.getZ());
			Point2D.Double f = new Point2D.Double();
			trans.deltaTransform(pt, f);
			return new Vecteur(new Decimal(f.getX()), ret.getDecY().divide(Vecteur.METER), new Decimal(f.getY()).divide(Vecteur.METER));
		}
		if (axis == Axis.ZAxis) {
			Point2D.Double pt = new Point2D.Double(ret.getX(), ret.getY());
			Point2D.Double f = new Point2D.Double();
			trans.deltaTransform(pt, f);
			return new Vecteur(new Decimal(f.getX()).divide(Vecteur.METER), new Decimal(f.getY()).divide(Vecteur.METER), ret.getDecZ());
		}
		return null;
	}
	
	
	public Transformation getReverse(Transformation tr) {
		Rotation ref = new Rotation(this.axis, angle.negate(), tr);
		return super.getReverse(ref);
	}

}
