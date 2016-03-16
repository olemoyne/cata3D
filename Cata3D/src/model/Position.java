package model;

import java.io.Serializable;

import model.math.Axis;
import model.math.Decimal;
import model.math.Vecteur;
import model.math.transfo.Rotation;
import model.math.transfo.Transformation;
import model.math.transfo.Translation;

/*****
 * Gestion de la position d'un composant � partir de l'origine de l'espace
 * 
 * @author olemoyne
 *
 */
public class Position implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4435954668533317248L;

	/** Position dans le bateau **/
	public Vecteur position;
	
	/** Rotation dans chaque direction de l'espace **/
	public Vecteur rotation;
	
	public Decimal angle ;


	/**
	 * Par d�faut, pas de transformation 
	 */
	public Position () {
		position = new Vecteur();
		rotation = new Vecteur ();
		angle = Decimal.ZERO;
	}

	public Position(Vecteur pos) {
		this.position = pos;
		rotation = new Vecteur ();
		angle = Decimal.ZERO;
	}

	public Position(Vecteur pos, Decimal a, Vecteur rot) {
		this.position = pos;
		this.rotation = rot;
		angle = a;
	}

	/** Retourne la transformation induite par la position du composant **/
	public Transformation getTransformation (Transformation trans) {
		Transformation  trs = trans;
		if (rotation != null) {
			if (rotation.getX() != 0) trs = new Rotation (Axis.XAxis, rotation.getDecX(), trs);
			if (rotation.getY() != 0) trs = new Rotation (Axis.YAxis, rotation.getDecY(), trs);
			if (rotation.getZ() != 0) trs = new Rotation (Axis.ZAxis, rotation.getDecZ(), trs);
		}
		if (position != null) {
			if (!position.equals(new Vecteur(0, 0, 0))) {
				trs = new Translation (position, trs);
			}
		}
		return trs;
	}

	public String toString () {
		StringBuilder sb = new StringBuilder();
		sb.append(position.toString());
		sb.append("-");
		Decimal a = angle; if (a == null) a = Decimal.ZERO;
		sb.append(a.toString());
		sb.append("-");
		sb.append(rotation.toString());
		return sb.toString();
	}
	
}
