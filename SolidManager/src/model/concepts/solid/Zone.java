package model.concepts.solid;

import java.util.ArrayList;

import model.concepts.math.Decimal;
import model.concepts.math.InvalidGeomAction;
import model.concepts.math.Triangle;
import model.concepts.math.Vecteur;

/**
 * Une zone est un morceau d'espace délimité par des bounds dans laquelle sont inscrits des triangles.
 * Permet d'identifier une liste de triangles appartenant à une zone donnée
 * 
 * En cas de dépassement d'une taille donnée, la zone est splittée en 4
 * 
 * @author olemoyne
 *
 */
public class Zone {
	
	
	private static final int SPLIT_SIZE = 25;

	
	/**
	 * Sous classe de stockage des données de triangle
	 * 
	 * @author olemoyne
	 *
	 */
	public class ZonedTriangle {
		private Bounds bounds;
		private Triangle triangle;

		public ZonedTriangle(Triangle tr, Bounds bnds) {
			bounds = bnds;
			triangle = tr;
		}

	}
	

	/** Délimitation de la zone **/
	private Bounds bounds;
	
	/** niveau de zone **/
	private int level;
	
	/** Liste des triangles de la zone **/
	private ArrayList<ZonedTriangle> triangles;

	/** Liste des zones intégrées dans cette zone **/
	private ArrayList<Zone> sousZones;
	
	public Zone (int lvl) {
		level = lvl;
		bounds = new Bounds ();
		triangles = new ArrayList<ZonedTriangle> ();
		sousZones = new ArrayList<Zone> ();
	}
	
	public Zone (int lvl, Bounds bnds) {
		level = lvl;
		bounds = bnds;
		triangles = new ArrayList<ZonedTriangle> ();
		sousZones = new ArrayList<Zone> ();
	}

	public Zone (int lvl, Vecteur v1, Vecteur v2) {
		level = lvl;
		bounds = new Bounds(v1, v2);
		triangles = new ArrayList<ZonedTriangle> ();
		sousZones = new ArrayList<Zone> ();
	}

	public Bounds getBounds () {
		return bounds;
	}
	
	/**
	 * Calcule le centre de tous les éléments de la zone
	 */
	public Vecteur computeCenter() {
		Vecteur max = new Vecteur();
//		Vecteur min = new Vecteur();
		for (ZonedTriangle trs : triangles) {
			max = max.add(trs.bounds.getMax());
//			min = min.add(trs.bounds.getMin());
		}
		max = max.multiply(new Decimal (Integer.toString(triangles.size())).inverse());
//		min = min.multiply(new Decimal (Integer.toString(triangles.size())).inverse());
	
		return max;
//		return min.add(max.minus(min).multiply(Decimal.DEUX.inverse()));
	}

	/**
	 * Fonction d'ajout d'un triangle dans la zone.
	 * retourne True si l'ajout est possible.
	 * 
	 * Si le nombre de triangle excède 20 -> la zone est splittée
	 * 
	 * @param tr
	 * @param bnds
	 * @return
	 */
	public boolean addTriangle (Triangle tr, Bounds bnds) {
		// calcul des bounds du triangle
		if (bnds == null) {
			bnds = new Bounds();
			bnds.setBounds(tr);
		}
		if (!this.bounds.contains(bnds)) 
			return false;

		return triangles.add(new ZonedTriangle(tr, bnds));
	}
	
	/**
	 * Eclate la zone en sous-zones pour gérer la profondeur d'historique
	 */
	public void splitZone() {
		// Si la zone est de taille suffisemment petite ...
		if (triangles.size() < SPLIT_SIZE) 
			return;
		
		// Calcule du centre de la zone	
		Vecteur center = computeCenter();// this.bounds.getMin().add(this.bounds.getMax().minus(bounds.getMin()).multiply(Decimal.DEUX.inverse()));
		// Split de la zone en 8 zones - par défaut division de la zone en 8
		sousZones.add(new Zone(level+1, bounds.getMin(), center));
		sousZones.add(new Zone(level+1, new Vecteur(center.getX(), bounds.getMin().getY(), bounds.getMin().getZ()), new Vecteur(bounds.getMax().getX(), center.getY(), center.getZ())));
		sousZones.add(new Zone(level+1, new Vecteur(bounds.getMin().getX(), center.getY(), bounds.getMin().getZ()), new Vecteur(center.getX(), bounds.getMax().getY(), center.getZ())));
		sousZones.add(new Zone(level+1, new Vecteur(center.getX(), center.getY(), bounds.getMin().getZ()), new Vecteur(bounds.getMax().getX(), bounds.getMax().getY(), center.getZ())));

		sousZones.add(new Zone(level+1, new Vecteur(bounds.getMin().getX(), bounds.getMin().getY(), center.getZ()), new Vecteur(center.getX(), center.getY(), bounds.getMax().getZ())));
		sousZones.add(new Zone(level+1, new Vecteur(center.getX(), bounds.getMin().getY(), center.getZ()), new Vecteur(bounds.getMax().getX(), center.getY(), bounds.getMax().getZ())));
		sousZones.add(new Zone(level+1, new Vecteur(bounds.getMin().getX(), center.getY(), center.getZ()), new Vecteur(center.getX(), bounds.getMax().getY(), bounds.getMax().getZ())));
		sousZones.add(new Zone(level+1, new Vecteur(center.getX(), center.getY(), center.getZ()), new Vecteur(bounds.getMax().getX(), bounds.getMax().getY(), bounds.getMax().getZ())));

		ArrayList<ZonedTriangle> reste = new ArrayList<ZonedTriangle>();
		// Affectation des triangles dans chaque sous zones
		for (ZonedTriangle trs : this.triangles) {
			
			boolean found = false;
			System.out.println("Adding triangle to zone "+level);
			for (Zone zne: sousZones) {
				if (!found)
					found = zne.addTriangle(trs.triangle, trs.bounds);
			}
			// Si le triangle n'est dans aucune zone
			// Il reste au niveau de cette zone
			
			if (!found) {
				System.out.println("Adding triangle to my zone "+level);
				reste.add(trs);
			}
		}
		this.triangles.clear();
		this.triangles.addAll(reste);
		// Split les sous zones
		for (Zone zne : sousZones) {
			zne.splitZone();
		}
	}
	
	/**
	 * Retourne une liste de triangles en interférence avec une zone donnée
	 * 
	 * @return
	 */
	public ArrayList<Triangle> getTriangles(Bounds bnds) {
		ArrayList<Triangle> ret = new ArrayList<Triangle> ();
		// Pas d'intersection avec cette zone, sortie immédiate
		if (!this.bounds.intersecs(bnds)) return ret;
		// Recherche parmi les sous-zones
		for (Zone zne : sousZones)	ret.addAll(zne.getTriangles(bnds));
		// Recherche dans la zone
		for (ZonedTriangle ztr : triangles)
			if (ztr.bounds.intersecs(bnds)) ret.add(ztr.triangle);
		
		return ret;
	}
	
	/**
	 * Affiche les données de la chaine de caractères
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(level);
		sb.append (" --> ");
		sb.append(bounds.toString());
		sb.append (" : ");
		sb.append(triangles.size());
		sb.append (" - ");
		sb.append(sousZones.size());
		sb.append ("\n");
		for (Zone zne : sousZones) {
			sb.append ("sous-Zone ");
			sb.append (" - ");
			sb.append(zne.toString());
		}
		return sb.toString();
	}
	
	
	public static Zone getZone(Solide pol) {
		Bounds bnds = new Bounds();
		bnds.setBounds(pol);
		Zone zne = new Zone(0, bnds);
		for (Triangle tr : pol.getFacettes()) {
			zne.addTriangle(tr, null);
		}
		zne.splitZone();
		return zne;
	}

	public static void main(String[] args) {
		try {
			Solide pol = SolideMaker.getPatch(20);
			Bounds bnds = new Bounds();
			bnds.setBounds(pol);
			System.out.println("Adding triangles : "+pol.getFacettes().size());
			Zone zne = new Zone(0, bnds);
			for (Triangle tr : pol.getFacettes()) {
				zne.addTriangle(tr, null);
			}
			zne.splitZone();
			System.out.println(zne.toString());
		} catch (InvalidGeomAction e1) {
			e1.printStackTrace();
			return;
		}
	}

}
