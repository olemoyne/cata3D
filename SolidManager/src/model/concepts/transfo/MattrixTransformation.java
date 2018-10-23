package model.concepts.transfo;


import model.concepts.math.Matrix;
import model.concepts.math.Vecteur;

/**
 * Une transformation de type rotation
 * 
 * @author olemoyne
 *
 */
public class MattrixTransformation extends Transformation {
	
	/**
	 * Matrice de transformation
	 */
	public Matrix mat;
	
	
	/**
	 * Positionne une reflexion
	 * 
	 * @param ax
	 * @param angle
	 */
	public MattrixTransformation(Matrix ax, Transformation t) {
		super(t);
		mat = ax;
	}

	public Vecteur getPoint(Vecteur ret) {
		if (ret == null) throw new NullPointerException ("Reflexion : point de d�part nul");

		return mat.multiply(ret);
	}
	
	
	public Transformation getReverse(Transformation tr) {
		MattrixTransformation ref = new MattrixTransformation(mat.inverse(), tr);
		return super.getReverse(ref);
	}

}
