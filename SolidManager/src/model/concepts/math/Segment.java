package model.concepts.math;

import model.concepts.transfo.Transformation;

/**
 * Segment dans l'espace d�fini par deux points 3D
 * 
 * @author olemoyne
 *
 */
public class Segment {
	
	private Vecteur A;
	private Vecteur B;
	
	/**
	 * Cr�ation de Segment
	 * 
	 * @param a
	 * @param b
	 */
	public Segment(Vecteur a, Vecteur b) {
		A = a;
		B = b;
	}
	
	/**
	 * R�cup�ration du point de d�part
	 * 
	 * @return
	 */
	public Vecteur getA() {
		return A;
	}
	
	/**
	 * R�cup�ration du point d'arriv�e
	 * 
	 * @return
	 */
	public Vecteur getB() {
		return B;
	}

	/**
	 * D�termine l'intersection de deux segements dans l'espace 
	 * 
	 * @param hisSeg
	 * @return
	 */
	public Segment intersection(Segment other) {
		// Gestion de deux droites collin�aires (deux vecteurs directeurs colin�aires)
		if (this.B.minus(A).estColineaire(other.B.minus(other.A))) {
			// Identifie l'arr�te d�finie par l'intersection
			// Si les deux points appartienent � l'autre arrete -> retourne moi
			if ((other.contient(A))&&(other.contient(B))) {
				return this;
			}
			// Si les deux points appartienent � mon arrete -> retourne lui
			if ((contient(other.A))&&(contient(other.B))) {
				return other;
			}
			// Pas de point communs 
			if ((!other.contient(A))&&(!other.contient(B))) {
				return null;
			}
			if (A.equals(other.B)) return new Segment (A, A);
			if (A.equals(other.A)) return new Segment (A, A);
			if (B.equals(other.A)) return new Segment (B, B);
			if (B.equals(other.B)) return new Segment (B, B);

			// Identification des points
			if (other.contient(A)) {
				if (this.contient(other.B)) return new Segment(A, other.B);
				else return new Segment(A, other.A);
			}
			if (other.contient(B)) {
				if (this.contient(other.B))  return new Segment(other.B, B);
				else return new Segment(other.A, B);
			}
		} else {
			Droite3D drt = new Droite3D(other.B.minus(other.A), other.A);
			Vecteur v = drt.getIntersection(this);
			if (v == null) return null;
			// V�rifie que inter est bien entre D et F
			// La distance entre D et V < celle entre D et F
			Decimal d = v.distance(other.A);
			Decimal t = other.A.distance(other.B);
			if (t.doubleValue() >= d.doubleValue()) {
				d = v.distance(other.A);
				if (t.doubleValue() >= d.doubleValue()) return new Segment(v, v);
			}
		}
		// Pas d'intersection
		return null;
	}

	/**
	 *  Teste si un point est compris dans l'intervalle du segment 
	 *   
	 * @param v
	 * @return
	 */
	public boolean contient(Vecteur v) {
		
		// v�rifie que V appetient � la droite du segment
		Vecteur dir = A.minus(v);
		Vecteur directeur = B.minus(A);

		if (!dir.estColineaire(directeur)) return false;

		Decimal d = v.distance(A);
		Decimal taille = B.distance(A);
		if (taille.compareTo(d) >= 0) { //.doubleValue() >= d.doubleValue()) {
			d = v.distance(B);
			if (taille.compareTo(d) >= 0) //.doubleValue() >= d.doubleValue()) 
				return true;
		}
		return false;
	}

	/**
	 * G�n�re une repr�sentation en chaine de caract�res du segment
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		sb.append(A.toString());
		sb.append(";");
		sb.append(B.toString());
		sb.append("]");
		return sb.toString();
	}
	
	/**
	 * Validation de la similitude de position
	 * 
	 * @param other
	 * @return
	 */
	public boolean equals(Object other) {
		if (other.getClass() == Segment.class) {
			Segment seg = (Segment) other;
			if ((A.equals(seg.A))&&(B.equals(seg.B))) return true;
			if ((A.equals(seg.B))&&(B.equals(seg.A))) return true;
			return false;
		}
		return false;
	}

	public Segment transforme(Transformation trans) {
		if (trans == null) return null;
		return new Segment(trans.transforme(A), trans.transforme(B));
	}


}
