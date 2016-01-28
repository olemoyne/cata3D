package model.math.solide;

import java.util.ArrayList;

import model.math.Axis;
import model.math.Decimal;
import model.math.Droite3D;
import model.math.InvalidGeomAction;
import model.math.MapDeVecteurs;
import model.math.Triangle;
import model.math.Vecteur;
import model.patch.Patch;

public abstract class SolideMaker {


	/**
	 * Solide par défaut : un pavé
	 * @throws InvalidGeomAction 
	 */
	public static Solide getCube () throws InvalidGeomAction {
		return getPave(new Vecteur("0;0;0"), new Vecteur("1;1;1"));
	}

		
	/**
	 * Solide par défaut : un pavé
	 * @throws InvalidGeomAction 
	 */
	public static Solide getPyramide () throws InvalidGeomAction {
		ArrayList<Triangle> facs = new ArrayList<Triangle>(); 
		Vecteur[] pts = new Vecteur[4];
		
		//Base
		pts[0] = new Vecteur ("0;0;0");
		pts[1] = new Vecteur ("0;1;0");
		pts[2] = new Vecteur ("1;1;0");
		try {
			Triangle tr = Triangle.getTriangle(pts);
			facs.add(tr);
		} catch (InvalidGeomAction e) {};
		
		pts[0] = new Vecteur ("0;0;0");
		pts[1] = new Vecteur ("1;1;0");
		pts[2] = new Vecteur ("1;0;0");
		try {
			Triangle tr = Triangle.getTriangle(pts);
			facs.add(tr);
		} catch (InvalidGeomAction e) {};
		//Autres Triangles
		pts = new Vecteur[3];
		pts[0] = new Vecteur ("0;0;0");
		pts[1] = new Vecteur ("0,5;0,5;0,5");
		pts[2] = new Vecteur ("1;0;0");
		try {
			Triangle tr = Triangle.getTriangle(pts);
			facs.add(tr);
		} catch (InvalidGeomAction e) {};
		pts[0] = new Vecteur ("1;0;0");
		pts[1] = new Vecteur ("0,5;0,5;0,5");
		pts[2] = new Vecteur ("1;1;0");
		try {
			Triangle tr = Triangle.getTriangle(pts);
			facs.add(tr);
		} catch (InvalidGeomAction e) {};
		pts[0] = new Vecteur ("1;1;0");
		pts[1] = new Vecteur ("0,5;0,5;0,5");
		pts[2] = new Vecteur ("0;1;0");
		try {
			Triangle tr = Triangle.getTriangle(pts);
			facs.add(tr);
		} catch (InvalidGeomAction e) {};
		pts[2] = new Vecteur ("0;1;0");
		pts[1] = new Vecteur ("0,5;0,5;0,5");
		pts[2] = new Vecteur ("0;0;0");
		try {
			Triangle tr = Triangle.getTriangle(pts);
			facs.add(tr);
		} catch (InvalidGeomAction e) {};
		return new Solide(facs);
	}


	/**
	 * Construit un pavé avec les dimensions et position définis
	 * 
	 * @param dim
	 * @param pos
	 * @return
	 * @throws InvalidGeomAction 
	 */
	public static Solide getPave(Vecteur dim, Vecteur pos) throws InvalidGeomAction {
		ArrayList<Triangle> facs = new ArrayList<Triangle>(); 

		Vecteur[] pts = new Vecteur[3];
		pts[0] = new Vecteur (pos.getX(), pos.getY(), pos.getZ());
		pts[1] = new Vecteur (pos.getX(), pos.getY()+dim.getY(), pos.getZ());
		pts[2] = new Vecteur (pos.getX()+dim.getX(), pos.getY()+dim.getY(), pos.getZ());
		try {
			Triangle tr = Triangle.getTriangle(pts);
			facs.add(tr);
		} catch (InvalidGeomAction e) {};
		pts[0] = new Vecteur (pos.getX(), pos.getY(), pos.getZ());
		pts[1] = new Vecteur (pos.getX()+dim.getX(), pos.getY()+dim.getY(), pos.getZ());
		pts[2] = new Vecteur (pos.getX()+dim.getX(), pos.getY(), pos.getZ()); 
		try {
			Triangle tr = Triangle.getTriangle(pts);
			facs.add(tr);
		} catch (InvalidGeomAction e) {};
		
		pts[0] = new Vecteur (pos.getX(), pos.getY(), pos.getZ()); 
		pts[1] = new Vecteur (pos.getX(), pos.getY(), pos.getZ()+dim.getZ()); 
		pts[2] = new Vecteur (pos.getX(), pos.getY()+dim.getY(), pos.getZ()+dim.getZ()); 
		try {
			Triangle tr = Triangle.getTriangle(pts);
			facs.add(tr);
		} catch (InvalidGeomAction e) {};
		pts[0] = new Vecteur (pos.getX(), pos.getY(), pos.getZ()); 
		pts[1] = new Vecteur (pos.getX(), pos.getY()+dim.getY(), pos.getZ()+dim.getZ()); 
		pts[2] = new Vecteur (pos.getX(), pos.getY()+dim.getY(), pos.getZ()); 
		try {
			Triangle tr = Triangle.getTriangle(pts);
			facs.add(tr);
		} catch (InvalidGeomAction e) {};

		pts[0] = new Vecteur (pos.getX(), pos.getY(), pos.getZ()); 
		pts[1] = new Vecteur (pos.getX()+dim.getX(), pos.getY(), pos.getZ()); 
		pts[2] = new Vecteur (pos.getX()+dim.getX(), pos.getY(), pos.getZ()+dim.getZ());
		try {
			Triangle tr = Triangle.getTriangle(pts);
			facs.add(tr);
		} catch (InvalidGeomAction e) {};
		pts[0] = new Vecteur (pos.getX()+dim.getX(), pos.getY(), pos.getZ()+dim.getZ());
		pts[1] = new Vecteur (pos.getX(), pos.getY(), pos.getZ()+dim.getZ()); 
		pts[2] = new Vecteur (pos.getX(), pos.getY(), pos.getZ()); 
		try {
			Triangle tr = Triangle.getTriangle(pts);
			facs.add(tr);
		} catch (InvalidGeomAction e) {};

		pts[0] = new Vecteur (pos.getX(), pos.getY()+dim.getY(), pos.getZ()); 
		pts[1] = new Vecteur (pos.getX(), pos.getY()+dim.getY(), pos.getZ()+dim.getZ()); 
		pts[2] = new Vecteur (pos.getX()+dim.getX(), pos.getY()+dim.getY(), pos.getZ()+dim.getZ()); 
		try {
			Triangle tr = Triangle.getTriangle(pts);
			facs.add(tr);
		} catch (InvalidGeomAction e) {};
		pts[0] = new Vecteur (pos.getX(), pos.getY()+dim.getY(), pos.getZ()); 
		pts[1] = new Vecteur (pos.getX()+dim.getX(), pos.getY()+dim.getY(), pos.getZ()+dim.getZ()); 
		pts[2] = new Vecteur (pos.getX()+dim.getX(), pos.getY()+dim.getY(), pos.getZ()); 
		try {
			Triangle tr = Triangle.getTriangle(pts);
			facs.add(tr);
		} catch (InvalidGeomAction e) {};

		pts[0] = new Vecteur (pos.getX(), pos.getY(), pos.getZ()+dim.getZ()); 
		pts[1] = new Vecteur (pos.getX()+dim.getX(), pos.getY(), pos.getZ()+dim.getZ()); 
		pts[2] = new Vecteur (pos.getX()+dim.getX(), pos.getY()+dim.getY(), pos.getZ()+dim.getZ()); 
		try {
			Triangle tr = Triangle.getTriangle(pts);
			facs.add(tr);
		} catch (InvalidGeomAction e) {};
		pts[0] = new Vecteur (pos.getX(), pos.getY(), pos.getZ()+dim.getZ()); 
		pts[1] = new Vecteur (pos.getX()+dim.getX(), pos.getY()+dim.getY(), pos.getZ()+dim.getZ()); 
		pts[2] = new Vecteur (pos.getX(), pos.getY()+dim.getY(), pos.getZ()+dim.getZ()); 
		try {
			Triangle tr = Triangle.getTriangle(pts);
			facs.add(tr);
		} catch (InvalidGeomAction e) {};

		pts[0] = new Vecteur (pos.getX()+dim.getX(), pos.getY(), pos.getZ()); 
		pts[1] = new Vecteur (pos.getX()+dim.getX(), pos.getY()+dim.getY(), pos.getZ()); 
		pts[2] = new Vecteur (pos.getX()+dim.getX(), pos.getY()+dim.getY(), pos.getZ()+dim.getZ()); 
		try {
			Triangle tr = Triangle.getTriangle(pts);
			facs.add(tr);
		} catch (InvalidGeomAction e) {};
		pts[0] = new Vecteur (pos.getX()+dim.getX(), pos.getY(), pos.getZ()); 
		pts[1] = new Vecteur (pos.getX()+dim.getX(), pos.getY()+dim.getY(), pos.getZ()+dim.getZ()); 
		pts[2] = new Vecteur (pos.getX()+dim.getX(), pos.getY(), pos.getZ()+dim.getZ()); 
		try {
			Triangle tr = Triangle.getTriangle(pts);
			facs.add(tr);
		} catch (InvalidGeomAction e) {};

		return new Solide(facs);
	}

	/**
	 *  Calcule une liste de points en cercle des dimensions données et ajoute l'épaisseur
	 *  
	 * @param dim dimensions du disque (x, y, épaisseur)
	 * @param pos position du disque 
	 * @param nbPoints échantilloànage
	 * @param n nom de la forme
	 * @return
	 * @throws InvalidGeomAction 
	 */
	public static Solide getDisque(Vecteur dim, Vecteur pos, int nbPoints) throws InvalidGeomAction {
		ArrayList<Triangle> facs = new ArrayList<Triangle>(); 
		// Construit le premier crecle
		Vecteur[] pts = new Vecteur[nbPoints];
		double rad = 0; // Angle
		double delta = 2d*Math.PI/nbPoints;
		for (int id = 0; id < nbPoints; id ++) {
			Decimal x = dim.getDecX().multiply(new Decimal(Math.sin(rad)));
			Decimal y = dim.getDecY().multiply(new Decimal(Math.cos(rad)));
			pts[id] = new Vecteur (x, y, Decimal.ZERO);
			rad += delta;
		}
		// Parcours la liste des points et construit les Triangles de 4 points
		Vecteur trans = new Vecteur(0, 0, dim.getY());
		Vecteur[] fac = new Vecteur[3];
		for (int id = 0; id < nbPoints-1; id ++) {
			fac[0] = pts[id];
			fac[1] = pts[id+1];
			fac[2] = pts[id+1].add(trans);
			try {
				Triangle tr = Triangle.getTriangle(pts);
				facs.add(tr);
			} catch (InvalidGeomAction e) {};
			fac[0] = pts[id+1];
			fac[1] = pts[id].add(trans);
			fac[2] = pts[id+1].add(trans);
			try {
				Triangle tr = Triangle.getTriangle(pts);
				facs.add(tr);
			} catch (InvalidGeomAction e) {};
		}
		fac[0] = pts[nbPoints-1];
		fac[1] = pts[0];
		fac[2] = pts[0].add(trans);
		try {
			Triangle tr = Triangle.getTriangle(pts);
			facs.add(tr);
		} catch (InvalidGeomAction e) {};
		fac[0] = pts[0];
		fac[1] = pts[nbPoints-1].add(trans);
		fac[2] = pts[0].add(trans);
		try {
			Triangle tr = Triangle.getTriangle(pts);
			facs.add(tr);
		} catch (InvalidGeomAction e) {};
		// Triangle du haut et du bas
		Vecteur[] ptsRev = new Vecteur[nbPoints];
		for (int id = 0; id < nbPoints; id ++) ptsRev[id] = pts[nbPoints - id -1];
		facs.addAll(SolideMaker.splitFacette(pts));
		for (int id = 0; id < nbPoints; id ++) ptsRev[id] = pts[id].add(trans);
		facs.addAll(SolideMaker.splitFacette(ptsRev));

		return new Solide(facs);
	}

	private static void getSurface(MapDeVecteurs map, ArrayList<Triangle> facs , boolean ordre) throws InvalidGeomAction {
		Vecteur[] pts = new Vecteur[3];
		// Calcul des Triangles
		try {
			for (int x = 0; x < map.xSize()-1; x++) {
				for (int y = 0; y < map.ySize()-1; y++) {
					if (ordre) {
						pts[0]= map.getPoint(x, y);
						pts[1]= map.getPoint(x, y+1);
						pts[2]= map.getPoint(x+1, y+1);
						facs.add(Triangle.getTriangle(pts));
						 
						pts[0]= map.getPoint(x+1, y+1);
						pts[1]= map.getPoint(x+1, y);
						pts[2]= map.getPoint(x, y);
						facs.add(Triangle.getTriangle(pts));
					} else {
						pts[0]= map.getPoint(x, y);
						pts[1]= map.getPoint(x+1, y);
						pts[2]= map.getPoint(x+1, y+1);
						facs.add(Triangle.getTriangle(pts));

						pts[0]= map.getPoint(x+1, y+1);
						pts[1]= map.getPoint(x, y+1);					
						pts[2]= map.getPoint(x, y);
						facs.add(Triangle.getTriangle(pts));
					}
				}
			}
		} catch (InvalidGeomAction e) {
			System.err.println(e.getLocalizedMessage());
		}
		return;
	}
	
	/**
	 * Creation d'un Solide à partir d'un maillage de points
	 * 
	 * @param map
	 * @param nbPoints
	 * @return
	 * @throws InvalidGeomAction 
	 */
	public static Solide fromMap(MapDeVecteurs map) throws InvalidGeomAction {
	
		ArrayList<Triangle> facs = new ArrayList<Triangle>();
		getSurface(map, facs, true);
		
		getProjection (map, facs, 0, -1, Axis.XAxis);
		getProjection (map, facs, -1, 0, Axis.XAxis);
		getProjection (map, facs, -1, map.ySize()-1, Axis.XAxis);

		return new Solide(facs);
	}

	


	/**
	 * Creation de triangles entre des points d'une ligne ou colonne et de leur projection 
	 * 
	 * @param map
	 * @param facs
	 * @param i
	 * @param j
	 * @param xaxis
	 */
	private static void getProjection(MapDeVecteurs map,
			ArrayList<Triangle> facs, int i, int j, int axis) {
		Vecteur[] pts = new Vecteur[3];
		try {
			if (i != -1) { // X est constant
				for (int y = 1; y < map.ySize(); y++) { // Y flottant
					pts[0] = map.getPoint(i, y-1);
					pts[1] = map.getPoint(i, y);
					pts[2] = map.getPoint(i, y-1).set(axis, 0);
					facs.add(Triangle.getTriangle(pts));
					pts[1] = map.getPoint(i, y);
					pts[2] = map.getPoint(i, y-1).set(axis, 0);
					pts[0] = map.getPoint(i, y).set(axis, 0);
					facs.add(Triangle.getTriangle(pts));
				}
			}
			if (j != -1) { // Y est constant
				for (int x = 1; x < map.xSize(); x++) { // X flottant
					pts[0] = map.getPoint(x-1, j);
					pts[1] = map.getPoint(x, j);
					pts[2] = map.getPoint(x-1, j).set(axis, 0);
					facs.add(Triangle.getTriangle(pts));
					pts[1] = map.getPoint(x, j);
					pts[2] = map.getPoint(x-1, j).set(axis, 0);
					pts[0] = map.getPoint(x, j).set(axis, 0);
					facs.add(Triangle.getTriangle(pts));
				}
			}
		} catch (InvalidGeomAction e) {
			System.err.println(e.getLocalizedMessage());
		}		
	}


	/**
	 * Creation d'un Solide de type skin à partir d'un maillage de points
	 * 
	 * @param map
	 * @param nbPoints
	 * @param nom
	 * @param epaisseur
	 * 
	 * @return
	 * @throws InvalidGeomAction 
	 */
	public static Solide fromMap(MapDeVecteurs map, Decimal epaisseur) throws InvalidGeomAction {
		ArrayList<Triangle> facs = new ArrayList<Triangle>(); 
		getSurface(map, facs, true);

		// Creation des Triangles décalées
		MapDeVecteurs mapDecalee = new MapDeVecteurs(map.xSize(), map.ySize());
		// Gestion du décalage sur chaque point de la MAP
		for (int x = 0; x <= map.xSize()-1; x++) {
			for (int y = 0; y <= map.ySize()-1; y++) {
				Vecteur v = map.getPoint(x, y);
				// Projection sur la droite
				Vecteur proj = new Vecteur(0, 0, v.getZ());

				Vecteur d =  Droite3D.getReduction(proj,  v, epaisseur);
				if (d == null) mapDecalee.setPoint(x, y, v);
				else mapDecalee.setPoint(x, y, d);
			}
		}
		getSurface(mapDecalee, facs, false);

		// Creation des Triangles de fin et début de tube (x = 0 et x = nbPoints)
		Vecteur[] debut= new Vecteur[map.ySize()*2];
		Vecteur[] fin= new Vecteur[map.ySize()*2];
		for (int y = 0; y < map.ySize(); y++) {
			debut[y]= map.getPoint(0, y);
			fin[y]= map.getPoint(map.ySize()-1, y);
		}
		for (int y = 0; y < map.ySize(); y++) {
			debut[2*map.ySize() - y - 1]= mapDecalee.getPoint(0, y);
			fin[2*map.ySize() - y - 1]= mapDecalee.getPoint(map.xSize()-1, y);
		}
		
		facs.addAll(SolideMaker.splitFacette(debut));
		facs.addAll(SolideMaker.splitFacette(fin));

		// Creation des Triangles de fin et début de tube (x = 0 et x = nbPoints)
		debut= new Vecteur[map.xSize()*2];
		fin= new Vecteur[map.xSize()*2];
		for (int x = 0; x < map.xSize(); x++) {
			debut[x]= map.getPoint(x, 0);
			fin[x]= map.getPoint(x, map.ySize()-1);
		}
		for (int x = 0; x < map.xSize(); x++) {
			debut[2*map.xSize() - x - 1]= mapDecalee.getPoint(x, 0);
			fin[2*map.xSize() - x - 1]= mapDecalee.getPoint(x, map.ySize()-1);
		}
		
		facs.addAll(SolideMaker.splitFacette(debut));
		facs.addAll(SolideMaker.splitFacette(fin));

		return new Solide(facs);
	}



	public static Solide getPatch(int nb) throws InvalidGeomAction {
		Patch patch = new Patch();
		MapDeVecteurs map = patch.getMap(nb, true);
		Solide pol = SolideMaker.fromMap(map);
		return pol;
	}

	
	/**
	 * Split un triangle en plusieurs triangles
	 *    utilisation de la méthode optimisée si la facette est convexe
	 * 
	 * @return
	 * @throws InvalidGeomAction 
	 */
	public static ArrayList<Triangle> splitFacette(Vecteur[] pts) throws InvalidGeomAction {
		// Si la facette est composée de 3 sommets -> retour;
		if (pts.length == 3) {
			ArrayList<Triangle> ret = new ArrayList<Triangle> ();
			try {
				Triangle tr = Triangle.getTriangle(pts);
				ret.add(tr);
			} catch (InvalidGeomAction e) {};
			return ret;
		}		
		ArrayList<Triangle> ret = new ArrayList<Triangle> ();
		// Construction de plusieurs facettes en triangle de 3 arrètes jointes
		// tous les triangles ont pour sommet le centre de la acette
		Vecteur c = getCentre(pts);			
		Vecteur[] data = new Vecteur[3];
		for (int pos = 0; pos < pts.length -1 ; pos ++) {
			data[0] = pts[pos]; data[1] = pts[pos+1]; data[2] = c;
			try {
				Triangle tr = Triangle.getTriangle(data);
				ret.add(tr);
			} catch (InvalidGeomAction e) {};
		}
		data[0] = pts[pts.length-1]; data[1] = pts[0]; data[2] = c;
		try {
			Triangle tr = Triangle.getTriangle(data);
			ret.add(tr);
		} catch (InvalidGeomAction e) {};
		return ret;
	}

	
	/**
	 * Retourne le centre de la facette
	 * Le centre est composé de la moyenne des coordonnées de tous les sommets
	 * 
	 * @return
	 */
	public static Vecteur getCentre (Vecteur[] pts) {
		long x = 0;
		long y = 0;
		long z = 0;
		
		int nb = 0;
		for (Vecteur v : pts) {
			x = x+v.getX();
			y = y+v.getY();
			z = z+v.getZ();
			nb ++;
		}
		
		Decimal div = new Decimal(nb);
		return new Vecteur (new Decimal(x).divide(div).divide(Vecteur.METER), new Decimal(y).divide(div).divide(Vecteur.METER), new Decimal(z).divide(div).divide(Vecteur.METER));
	}

}
