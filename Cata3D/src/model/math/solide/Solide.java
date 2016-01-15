package model.math.solide;

import java.util.ArrayList;

import model.math.Decimal;
import model.math.InvalidGeomAction;
import model.math.Triangle;
import model.math.Vecteur;
import model.math.transfo.Transformation;


/**
 * Représentation d'un solide comme étant une liste de triangles orientés définissant les facettes du solide
 * 
 * Le solide est stocké comme une liste de points + liste de triangles (référençant le point) 
 * 
 * @author olemoyne
 *
 */
public class Solide {
	
	/**
	 * 
	 * 
	 */
	private ArrayList<Triangle> triangles;
	
	/**
	 * Création par défaut
	 */
	public Solide () {
		triangles = new ArrayList<Triangle> ();
	}
	
	
	/**
	 * Création à partir d'une liste de triangles
	 */
	public Solide (ArrayList<Triangle> tri) {
		triangles = new ArrayList<Triangle> (tri);
	}

	/**
	 * Retourne le centre du solide
	 * 
	 * @return
	 */
	public Vecteur getCentre () {
		long x = 0;
		long y = 0;
		long z = 0;
		
		int nb = 0;
		for (Vecteur v : this.getSommets()) {
			x = x+v.getX(); y = y+v.getY(); z = z+v.getZ();
			nb ++;
		}
		
		Decimal div = new Decimal(nb);
		return new Vecteur (new Decimal(x).divide(div).divide(Vecteur.METER), new Decimal(y).divide(div).divide(Vecteur.METER), new Decimal(z).divide(div).divide(Vecteur.METER));
	}

	/**
	 * Retourne la liste des sommets du solide 
	 * 
	 * @return
	 */
	public ArrayList<Vecteur> getSommets() {
		return getSommets(triangles);
	}

	
	/**
	 * Retourne la liste des sommets d'une liste de triangles 
	 * 
	 * @return
	 */
	public static ArrayList<Vecteur> getSommets(ArrayList<Triangle> tris) {
		ArrayList<Vecteur> pts = new ArrayList<Vecteur>();
		
		for (Triangle facette : tris) {
			for (Vecteur pt : facette.getSommets() ) {
				if (!pts.contains(pt)) pts.add(pt);
			}
		}		
		return pts;
	}

	/**
	 * Calcule le volume du Polyedre .
	 *    Correspond au volume des polyedres composés par le cente et chaque facette
	 *    
	 * @return
	 */
	public Decimal getVolume() {
		Vecteur ctr = this.getCentre();
		Decimal vol = Decimal.ZERO;
		Decimal trois = Decimal.TROIS;
		// calcul du volume pour chaque facette
		for (Triangle facette : triangles) {
			Decimal s = facette.getSurface();
			Decimal dist = facette.distanceOrtho (ctr);
			vol = vol.add(s.multiply(dist));
		}
		return vol.divide(trois);
	}

	/**
	 * Calcule la surface du polyedre.
	 *    Correspond à la somme des surfaces de chaque facette
	 *    
	 * @return
	 */
	public Decimal getSurface() {
		Decimal vol = Decimal.ZERO;
		// calcul du volume pour chaque facette
		for (Triangle facette : triangles) vol = vol.add(facette.getSurface());

		return vol;
	}

	
	/**
	 * Détecte si une facette est inclue dans un polyedre
	 * Il faut que tous les points de la facette soient à l'intérieur du polyedre
	 * 
	 * @param fac
	 * @return
	 */
	public boolean contient(Triangle fac) {
		for (Vecteur v : fac.getSommets()) {
			if (!this.contient(v)) return false;
		}
		return true;
	}


	/**
	 * Retourne la facette la plus proche du point donné 
	 * 
	 * @param pt
	 * @return
	 */
	private Triangle facetteLaPlusProche(Vecteur pt) {
		Decimal distance = null;
		Triangle tri = null;
		
		for (Triangle tr : this.triangles) {
			Decimal dst = tr.distance(pt);
			if (distance == null) {
				distance = dst;
				tri = tr;
			} else {
				if (distance.compareTo(dst) > 0) {
					distance = dst;
					tri = tr;
				}
			}
		}
		return tri;
	}
	
	/**
	 * Valide si un point est dans le solide
	 *   il faut que ce point soit du coté intérieur du plan
	 * 
	 * @param v
	 * @return
	 */
	public boolean contient(Vecteur v) {
		Triangle facette = facetteLaPlusProche(v);
		
//		for (Triangle facette : triangles) 
			if (facette.getPlan().donneCote(v) < 0) 
				return false;
		return true;
	}

	/***
	 * détermine si un point est dans le solide 
	 *   mais pas sur les bords
	 * 
	 * @param v : coordonnées du point
	 * @return
	 */
	public boolean estDedans(Vecteur v, boolean border) {
		// Récupère la facette la plus proche
		Triangle facette = facetteLaPlusProche(v);
		// Est-ce que le point est hors de la	 
		if (facette.getPlan().donneCote(v) < 0) return false;
		if (facette.contient(v)) return border;
		
		return true;
	}


	public ArrayList<Triangle> getFacettes() {
		return this.triangles;
	}

	/**
	 * Effectue une transformation
	 * 
	 * @param trans
	 * @return
	 * @throws InvalidGeomAction 
	 */
	public Solide transfo(Transformation trans) throws InvalidGeomAction {
		ArrayList<Triangle> tr = new ArrayList<Triangle>();
		for (Triangle tri : this.triangles) {
			Vecteur[] smt = tri.getSommets(); 
				tr.add(new Triangle(trans.getPoint(smt[0]), trans.getPoint(smt[1]), trans.getPoint(smt[2])));
		}
		return new Solide(tr);
	}

	/**
	 * retourne le nombre de facettes
	 * @return
	 */
	public int size() {
		return this.triangles.size();
	}

}
