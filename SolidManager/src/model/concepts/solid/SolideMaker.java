package model.concepts.solid;

import java.util.ArrayList;

import model.concepts.math.Decimal;
import model.concepts.math.Droite3D;
import model.concepts.math.InvalidGeomAction;
import model.concepts.math.Triangle;
import model.concepts.math.Vecteur;

public abstract class SolideMaker {


	/**
	 * Solide par d�faut : un pav�
	 * @throws InvalidGeomAction 
	 */
	public static Solide getCube () throws InvalidGeomAction {
		return getPave(new Vecteur("0;0;0"), new Vecteur("1;1;1"));
	}

		
	/**
	 * Solide par d�faut : un pav�
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
	 * Construit un pav� avec les dimensions et position d�finis
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
	 *  Calcule une liste de points en cercle des dimensions donn�es et ajoute l'�paisseur
	 *  
	 * @param dim dimensions du disque (x, y, �paisseur)
	 * @param pos position du disque 
	 * @param nbPoints �chantillo�nage
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


	private static ArrayList<Triangle> getSurface(Vecteur[][] map, int nbPoints, boolean ordre) throws InvalidGeomAction {
		ArrayList<Triangle> facs = new ArrayList<Triangle>(); 

		Vecteur[] pts = new Vecteur[3];
		// Calcul des Triangles
		for (int x = 0; x < nbPoints-1; x++) {
			for (int y = 0; y < nbPoints-1; y++) {
				if (ordre) {
					pts[0]= map[x][y];
					pts[1]= map[x][y+1];
					pts[2]= map[x+1][y+1];
					try {
						Triangle fac = Triangle.getTriangle(pts);
						facs.add(fac);
					} catch (InvalidGeomAction e) {
						System.err.println(e.getLocalizedMessage());
					}
					 
					pts[0]= map[x+1][y+1];
					pts[1]= map[x+1][y];
					pts[2]= map[x][y];
					try {
						Triangle fac = Triangle.getTriangle(pts);
						facs.add(fac);
					} catch (InvalidGeomAction e) {
						System.err.println(e.getLocalizedMessage());
					}
				} else {
					pts[0]= map[x][y];
					pts[1]= map[x+1][y];
					pts[2]= map[x+1][y+1];
					try {
						Triangle fac = Triangle.getTriangle(pts);
						facs.add(fac);
					} catch (InvalidGeomAction e) {
						System.err.println(e.getLocalizedMessage());
					}
					pts[0]= map[x+1][y+1];
					pts[1]= map[x][y+1];					
					pts[2]= map[x][y];
					try {
						Triangle fac = Triangle.getTriangle(pts);
						facs.add(fac);
					} catch (InvalidGeomAction e) {
						System.err.println(e.getLocalizedMessage());
					}
				}
			}
		}
		return facs;
	}
	
	/**
	 * Creation d'un Solide � partir d'un maillage de points
	 * 
	 * @param map
	 * @param nbPoints
	 * @return
	 * @throws InvalidGeomAction 
	 */
	public static Solide fromMap(Vecteur[][] map, int nbPoints) throws InvalidGeomAction {
		ArrayList<Triangle> facs = new ArrayList<Triangle>();
		facs.addAll(getSurface(map, nbPoints, true));

		// Ajoute les points de liaison
		Vecteur[] pts = new Vecteur[3];
		pts[0]= map[0][0];
		pts[1]= map[0][nbPoints-1];
		pts[2]= map[nbPoints-1][nbPoints-1];
		Triangle fac = Triangle.getTriangle(pts);
		facs.add(fac);
		pts[0]= map[nbPoints-1][nbPoints-1];	
		pts[1]= map[nbPoints-1][0];
		pts[2]= map[0][0];
		fac = Triangle.getTriangle(pts);
		facs.add(fac);

		// Creation des deux Triangles de fin de tube (x = 0 et x = nbPoints)
		Vecteur[] debut= new Vecteur[nbPoints];
		Vecteur[] fin= new Vecteur[nbPoints];
		for (int y = 0; y < nbPoints; y++) {
			debut[y]= map[nbPoints - y - 1][0];
			fin[y]= map[y][nbPoints-1];
		}
		facs.addAll(SolideMaker.splitFacette(debut));
		facs.addAll(SolideMaker.splitFacette(fin));
		
		return new Solide(facs);
	}

	
	/**
	 * Creation d'un Solide de type skin � partir d'un maillage de points
	 * 
	 * @param map
	 * @param nbPoints
	 * @param nom
	 * @param epaisseur
	 * 
	 * @return
	 * @throws InvalidGeomAction 
	 */
	public static Solide fromMap(Vecteur[][] map, int nbPoints, Decimal epaisseur) throws InvalidGeomAction {
		ArrayList<Triangle> facs = new ArrayList<Triangle>(); 
		facs.addAll(getSurface(map, nbPoints, true));

		// Creation des Triangles d�cal�es
		Vecteur[][] mapDecalee = new Vecteur[nbPoints][nbPoints];
		// Gestion du d�calage sur chaque point de la MAP
		for (int x = 0; x <= nbPoints-1; x++) {
			for (int y = 0; y <= nbPoints-1; y++) {
				Vecteur v = map[x][y];
				// Projection sur la droite
				Vecteur proj = new Vecteur(0, 0, v.getZ());

				Vecteur d =  Droite3D.getReduction(proj,  v, epaisseur);
				if (d == null) mapDecalee[x][y] = v;
				else mapDecalee[x][y] = d;
			}
		}
		facs.addAll(getSurface(mapDecalee, nbPoints, false));

		// Creation des Triangles de fin et d�but de tube (x = 0 et x = nbPoints)
		Vecteur[] debut= new Vecteur[nbPoints*2];
		Vecteur[] fin= new Vecteur[nbPoints*2];
		for (int y = 0; y < nbPoints; y++) {
			debut[y]= map[0][y];
			fin[y]= map[nbPoints-1][y];
		}
		for (int y = 0; y < nbPoints; y++) {
			debut[2*nbPoints - y - 1]= mapDecalee[0][y];
			fin[2*nbPoints - y - 1]= mapDecalee[nbPoints-1][y];
		}
		
		facs.addAll(SolideMaker.splitFacette(debut));
		facs.addAll(SolideMaker.splitFacette(fin));

		// Creation des Triangles de fin et d�but de tube (x = 0 et x = nbPoints)
		debut= new Vecteur[nbPoints*2];
		fin= new Vecteur[nbPoints*2];
		for (int x = 0; x < nbPoints; x++) {
			debut[x]= map[x][0];
			fin[x]= map[x][nbPoints-1];
		}
		for (int x = 0; x < nbPoints; x++) {
			debut[2*nbPoints - x - 1]= mapDecalee[x][0];
			fin[2*nbPoints - x - 1]= mapDecalee[x][nbPoints-1];
		}
		
		facs.addAll(SolideMaker.splitFacette(debut));
		facs.addAll(SolideMaker.splitFacette(fin));

		return new Solide(facs);
	}



	public static Solide getPatch(int nb) throws InvalidGeomAction {
		Vecteur[]pts = new Vecteur[16];
		pts[0] = new Vecteur("0.12;0.2;0");
		pts[1] = new Vecteur("0.2;0.1;0");
		pts[2] = new Vecteur("0.16;-0.08;0");
		pts[3] = new Vecteur("0.05;-0.15;0");
		pts[4] = new Vecteur("0.16;0.2;0.75");
		pts[5] = new Vecteur("0.28;0.12;0.75");
		pts[6] = new Vecteur("0.2;-0.25;0.75");
		pts[7] = new Vecteur("0.08;-0.32;0.75");
		pts[8] = new Vecteur("0.05;0.15;3");
		pts[9] = new Vecteur("0.2;0.08;3");
		pts[10] = new Vecteur("0.16;-0.12;3");
		pts[11] = new Vecteur("0.02;-0.4;3");
		pts[12] = new Vecteur("0.004;0.1;5");
		pts[13] = new Vecteur("0.01;0;5");
		pts[14] = new Vecteur("0.01;-0.1;5");
		pts[15] = new Vecteur("0.004;-0.22;5");
		
		Patch patch = new Patch(pts, nb);
		Solide pol = patch.calculeForme(nb);
		return pol;
	}

	
	/**
	 * Split un triangle en plusieurs triangles
	 *    utilisation de la m�thode optimis�e si la facette est convexe
	 * 
	 * @return
	 * @throws InvalidGeomAction 
	 */
	public static ArrayList<Triangle> splitFacette(Vecteur[] pts) throws InvalidGeomAction {
		// Si la facette est compos�e de 3 sommets -> retour;
		if (pts.length == 3) {
			ArrayList<Triangle> ret = new ArrayList<Triangle> ();
			try {
				Triangle tr = Triangle.getTriangle(pts);
				ret.add(tr);
			} catch (InvalidGeomAction e) {};
			return ret;
		}		
		ArrayList<Triangle> ret = new ArrayList<Triangle> ();
		// Construction de plusieurs facettes en triangle de 3 arr�tes jointes
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
	 * Le centre est compos� de la moyenne des coordonn�es de tous les sommets
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
