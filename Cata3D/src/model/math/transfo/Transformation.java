package model.math.transfo;

import java.io.Serializable;

import model.math.Axis;
import model.math.Decimal;
import model.math.Vecteur;


/**
 * Une transformation permet de modifier un point dans l'espace
 * 
 * @author olemoyne
 *
 */
public abstract class Transformation implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6429020222059152387L;
	/**
	 * Cas d'une transformation imbriqu�e
	 */
	private Transformation upper;
	

	public Transformation (Transformation u) {
		upper = u;
	}

	/**
	 * Transformation d'un point unitaire
	 * @param pt
	 * @return
	 */
	public Vecteur transforme(Vecteur pt) {
		Vecteur v = null;
		if (upper != null) v = upper.transforme(pt);
		else v = pt;
		
		return getPoint(v);
	}

	
	/**
	 * Application de la transformation
	 * 
	 * @param ret
	 * @return
	 */
	protected abstract Vecteur getPoint(Vecteur ret);
	
	
	public static void main (String[] args){
		Rotation rot = new Rotation(Axis.XAxis, Decimal.UN, null);
		Transformation trans = new Translation (new Vecteur ("0;-2;0"), rot);
		
		System.out.println("init :"+new Vecteur ("0;0;1").toString());
		
		Vecteur res = trans.transforme(new Vecteur ("0;0;1"));
		System.out.println("trans = "+res);

		res = trans.getReverse(null).transforme(res);
		System.out.println("trans = "+res);
	}

	public Transformation getReverse(Transformation tr) {
		// Gestion de la transformation supérieure
		if (this.upper != null) {
			Transformation ret = upper.getReverse(tr);			
			return ret;
		}
		return tr;
	}
}
