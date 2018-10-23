package model.concepts.solid;

import java.util.ArrayList;

import model.concepts.math.Decimal;
import model.concepts.math.InvalidGeomAction;
import model.concepts.math.Triangle;
import model.concepts.math.Vecteur;
import model.concepts.transfo.Transformation;
import tarpeia.com.model.TarpeiaField;
import tarpeia.com.model.TarpeiaObject;

/**
 * Repr�sentation d'un solide comme �tant une liste de triangles orient�s d�finissant les facettes du solide
 * 
 * Le solide est stock� comme une liste de points + liste de triangles (r�f�ren�ant le point) 
 * 
 * @author olemoyne
 *
 */
@TarpeiaObject
public class Solide {
	
	/**
	 * 
	 * 
	 */
	@TarpeiaField
	private ArrayList<Triangle> triangles;
	
	/**
	 * Cr�ation par d�faut
	 */
	public Solide () {
		triangles = new ArrayList<Triangle> ();
	}
	
	
	/**
	 * Cr�ation � partir d'une liste de triangles
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
	 *    Correspond au volume des polyedres compos�s par le cente et chaque facette
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
	 *    Correspond � la somme des surfaces de chaque facette
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
	 * D�tecte si une facette est inclue dans un polyedre
	 * Il faut que tous les points de la facette soient � l'int�rieur du polyedre
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
	 * Retourne la facette la plus proche du point donn� 
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
	 *   il faut que ce point soit du cot� int�rieur du plan
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
	 * d�termine si un point est dans le solide 
	 *   mais pas sur les bords
	 * 
	 * @param v : coordonn�es du point
	 * @return
	 */
	public boolean estDedans(Vecteur v, boolean border) {
		// R�cup�re la facette la plus proche
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
				tr.add(new Triangle(trans.transforme(smt[0]), trans.transforme(smt[1]), trans.transforme(smt[2])));
		}
		return new Solide(tr);
	}

	/**
	 * Test de fusion de patch
	 * 
	 * @param args
	 * @throws InvalidGeomAction
	 */
	public static void main(String[] args) throws InvalidGeomAction {
		
		// Test de est dedans 
		Solide pave = SolideMaker.getPave(new Vecteur ("1;1;1"), new Vecteur ("0;0;0"));
		Vecteur v = new Vecteur("1;1;1");
		
		if (pave.estDedans(v, false)) {
			System.out.println("Est dedans");
		} else {
			System.out.println("OK");
		}
	}


	/**
	 * retourne le nombre de facettes
	 * @return
	 */
	public int size() {
		return this.triangles.size();
	}


	public Bounds getBounds() {
		Bounds bnds = new Bounds();
		bnds.setBounds(this);
		return bnds;
	}


}
