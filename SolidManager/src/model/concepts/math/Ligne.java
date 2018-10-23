package model.concepts.math;

import java.util.ArrayList;

/**
 * Modélisation d'une ligne dans l'espace.
 * Une ligne est consituée de s'arretes dont les extrémités se rejoingnent
 * 
 * Une Ligne possède un départ et une fin
 * 
 * @author olemoyne
 *
 */
public class Ligne {
	
	/**
	 * Point de départ de la ligne
	 */
	private Vecteur A;
	
	/**
	 * Point de fin de la ligne
	 */
	private Vecteur B;
	
	/**
	 * Liste des arrètes constituant la ligne
	 */
	ArrayList<Segment> segments;
	
	
	public Ligne () {
		segments = new ArrayList<Segment>(); 
	}
	
	public void addArrete(Segment a) throws InvalidGeomAction {
		if (segments.size() == 0) {
			segments.add(a);
			A = a.getA();
			B = a.getB();
		} else {
			if (a.getA().equals(B)) {
				segments.add(a);
				B = a.getB();
			} 
			if (a.getB().equals(B)) {
				segments.add(new Segment(a.getB(), a.getA()));
				B = a.getA();
			} 
		}
	}
	
	public Vecteur getA() {
		return A;
	}
	
	public Vecteur getB() {
		return B;
	}

	public int size() {
		return this.segments.size();
	}
	
	public boolean estUneBoucle () {
		return A.equals(B);
	}

}
