package model.math;

import java.util.ArrayList;

import model.math.transfo.Translation;


/**
 * Objet repr�sentant l'espace occup� par un solide 
 * Cet espace est repr�sent� par deux point (min et max) 
 * @author olemoyne
 *
 */
public class Bounds {
	
	private Vecteur min; 
	private Vecteur max; 
	
	
	public Bounds ()  {
		min = null;
		max = null;
	}
	
	public Bounds (Vecteur v1, Vecteur v2)  {
		min = v1;
		max = v2;
	}

	public Bounds(Bounds o) {
		min = new Vecteur(o.min);
		max = new Vecteur(o.max);
	}

	/**
	 * Retourne la position maximale du solide
	 * 
	 * @return
	 */
	public Vecteur getMax () {
		return max;
	}

	/**
	 * Retourne la position minimale du solide
	 * 
	 * @return
	 */
	public Vecteur getMin () {
		return min;
	}

	
	public void setBounds (Vecteur v) {
		if (min == null) min = v;
		else {
			boolean update = false;
			long x = min.getX();
			if (x > v.getX()) {x = v.getX(); update = true;}
			long y = min.getY();
			if (y > v.getY()) {y = v.getY(); update = true;}
			long z = min.getZ();
			if (z > v.getZ()) {z = v.getZ(); update = true;}
			if (update) min = new Vecteur(x, y, z); 
		}
		if (max == null) max = v;
		else {
			boolean update = false;
			long x = max.getX();
			if (x < v.getX()) {x = v.getX(); update = true;}
			long y = max.getY();
			if (y < v.getY()) {y = v.getY(); update = true;}
			long z = max.getZ();
			if (z < v.getZ()) {z = v.getZ(); update = true;}
			if (update) max = new Vecteur(x, y, z); 
		}
	}

	public void setBounds (Triangle tri) {
		for (Vecteur pt : tri.getSommets()) {
			setBounds(pt);
		}
	}
	
	/**
	 * 
	 * @param sol
	 */
	public void setBounds (ArrayList<Vecteur> sol) {
		long xMax = Long.MIN_VALUE;
		long yMax = Long.MIN_VALUE;
		long zMax = Long.MIN_VALUE;

		long xMin = Long.MAX_VALUE;
		long yMin = Long.MAX_VALUE;
		long zMin = Long.MAX_VALUE;
		for (Vecteur v : sol) {
			if (xMax < v.getX()) 
				xMax = v.getX();
			if (yMax < v.getY()) 
				yMax = v.getY();
			if (zMax < v.getZ()) 
				zMax = v.getZ();
			if (xMin > v.getX()) 
				xMin = v.getX();
			if (yMin > v.getY()) 
				yMin = v.getY();
			if (zMin > v.getZ()) 
				zMin = v.getZ();
		}
		this.max = new Vecteur(xMax, yMax, zMax);
		this.min = new Vecteur(xMin, yMin, zMin);
	}

	/**
	 * 
	 * @param sol
	 */
	public static Bounds getBounds (MapDeVecteurs map) {
		long xMax = Long.MIN_VALUE;
		long yMax = Long.MIN_VALUE;
		long zMax = Long.MIN_VALUE;

		long xMin = Long.MAX_VALUE;
		long yMin = Long.MAX_VALUE;
		long zMin = Long.MAX_VALUE;
		for (int x= 0; x < map.xSize; x++) {
			for (int y= 0; y < map.ySize; y++) {
				Vecteur v = map.getPoint(x, y);
				if (xMax < v.getX()) 
					xMax = v.getX();
				if (yMax < v.getY()) 
					yMax = v.getY();
				if (zMax < v.getZ()) 
					zMax = v.getZ();
				if (xMin > v.getX()) 
					xMin = v.getX();
				if (yMin > v.getY()) 
					yMin = v.getY();
				if (zMin > v.getZ()) 
					zMin = v.getZ();
			}
		}
		return new Bounds(new Vecteur(xMin, yMin, zMin), new Vecteur(xMax, yMax, zMax));
	}

	/**
	 * Retourne si deux bounds sont en intersection
	 * 
	 * @param other
	 * @return
	 */
	public boolean intersecs (Bounds other) {
		if (other == null) return false;
		
		if  ( (other.max.getX()<this.min.getX()) || (other.min.getX()>this.max.getX()) )
			return false;
		
		if  ( (other.max.getY()<this.min.getY()) || (other.min.getY()>this.max.getY()) )
			return false;

		if  ( (other.max.getZ()<this.min.getZ()) || (other.min.getZ()>this.max.getZ()) )
			return false;

		return true;
	}

	/**
	 * Retourne si un bounds est contenu dans celui-ci
	 * 
	 * @param other
	 * @return
	 */
	public boolean contains (Bounds other) {
		if (other == null) return false;
		if (max == null) return false;
		if (min == null) return false;

		if ((other.max.getX() < this.min.getX()) || (other.max.getX() > this.max.getX()))
			return false;
		if ((other.max.getY() < this.min.getY()) || (other.max.getY() > this.max.getY()))
			return false;
		if ((other.max.getZ() < this.min.getZ()) || (other.max.getZ() > this.max.getZ()))
			return false;

		if ((other.min.getX() < this.min.getX()) || (other.min.getX() > this.max.getX()))
			return false;
		if ((other.min.getY() < this.min.getY()) || (other.min.getY() > this.max.getY()))
			return false;
		if ((other.min.getZ() < this.min.getZ()) || (other.min.getZ() > this.max.getZ()))
			return false;
		
		return true;
	}

	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(min.toString());
		sb.append (" - ");
		sb.append(max.toString());
		return sb.toString();
	}

	public boolean contains(Vecteur v) {
		if ((v.getX() < min.getX()) || (v.getX() > max.getX())) return false;
		if ((v.getY() < min.getY()) || (v.getY() > max.getY())) return false;
		if ((v.getZ() < min.getZ()) || (v.getZ() > max.getZ())) return false;
		return true;
	}

	/** V�rifie que le vecteur est dans la zone sans les bords**/
	public boolean inside(Vecteur v) {
		if ((v.getX() <= min.getX()) || (v.getX() >= max.getX())) return false;
		if ((v.getY() <= min.getY()) || (v.getY() >= max.getY())) return false;
		if ((v.getZ() <= min.getZ()) || (v.getZ() >= max.getZ())) return false;
		return true;
	}

	/**
	 * Creation 
	 * @param axe
	 * @param position
	 * @param l
	 */
	public Bounds truncate(int axe, long debut, long fin) {
		Bounds ret = new Bounds(this);
		if (min.get(axe) > fin) return null;
		if (max.get(axe) < debut) return null;
		
		if (min.get(axe) < debut) ret.min = ret.min.set(axe, debut);
		if (max.get(axe) > fin) ret.max = ret.max.set(axe, fin);
		
		return ret;
	}

	public long getLength(int axe) {
		return max.get(axe) - min.get(axe);
	}

	/**
	 * On consid�re qu'un triangle est dans la zone si au moins un des points est inclu dans la zone;
	 * 
	 * @param tr
	 * @return
	 */
	public boolean contains(Triangle tr) {
		for (Vecteur v : tr.getSommets()) {
			if (this.contains(v)) return true;
		}
		return false;
	}
	
	public boolean containsTotally(Triangle tr) {
		for (Vecteur v : tr.getSommets()) {
			if (!this.contains(v)) return false;
		}
		return true;
	}


	/**
	 * V�rifie qu'un triangle est strictement dans la zone (pas sur les bords) ;
	 * 
	 * @param tr
	 * @return
	 */
	public boolean inside(Triangle tr) {
		for (Vecteur v : tr.getSommets()) {
			if (this.inside(v)) return true;
		}
		return false;
	}

	
	/** 
	 * D�oupe une zone en 8 sous-zones selon un point central
	 * @param centre
	 * @return
	 */
	public Bounds[] splitZone(Vecteur centre) {
		if (!this.contains(centre)) return null;
		
		Bounds[] ret = new Bounds[8];
		ret[0] = new Bounds(new Vecteur(min.getX(), min.getY(), min.getZ()), new Vecteur (centre.getX(), centre.getY(), centre.getZ()));
		ret[1] = new Bounds(new Vecteur(centre.getX(), min.getY(), min.getZ()), new Vecteur (max.getX(), centre.getY(), centre.getZ()));
		ret[2] = new Bounds(new Vecteur(min.getX(), centre.getY(), min.getZ()), new Vecteur (centre.getX(), max.getY(), centre.getZ()));
		ret[3] = new Bounds(new Vecteur(centre.getX(), centre.getY(), min.getZ()), new Vecteur (max.getX(), max.getY(), centre.getZ()));
		ret[4] = new Bounds(new Vecteur(min.getX(), min.getY(), centre.getZ()), new Vecteur (centre.getX(), centre.getY(), max.getZ()));
		ret[5] = new Bounds(new Vecteur(centre.getX(), min.getY(), centre.getZ()), new Vecteur (max.getX(), centre.getY(), max.getZ()));
		ret[6] = new Bounds(new Vecteur(min.getX(), centre.getY(), centre.getZ()), new Vecteur (centre.getX(), max.getY(), max.getZ()));
		ret[7] = new Bounds(new Vecteur(centre.getX(), centre.getY(), centre.getZ()), new Vecteur (max.getX(), max.getY(), max.getZ()));
		
		return ret;
	}

	/**
	 * Applique une transformation � la zone
	 * 
	 * @param trans
	 * @return
	 */
	public Bounds transfo(Translation trans) {
		Vecteur mini = trans.getPoint(this.min);
		Vecteur maxi = trans.getPoint(this.max);
		return new Bounds (mini, maxi);
	}

}
