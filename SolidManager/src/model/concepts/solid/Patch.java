package model.concepts.solid;

import java.util.ArrayList;

import model.concepts.math.InvalidGeomAction;
import model.concepts.math.Vecteur;
import tarpeia.com.model.TarpeiaField;
import tarpeia.com.model.TarpeiaObject;

// Ce patch est construit à partir de 4 x 4 points 
@TarpeiaObject
public class Patch {

	/**
	 * Liste des 16 points constituant le patch
	 */
	@TarpeiaField
	private Vecteur[] points;

	/**
	 * Taille des points calculés
	 */
	private int taille;
	
	public Patch () {
		taille = 0;
		points = new Vecteur[16];
	}
	
	public Patch(Vecteur[] pts, int nbPoints) {
		taille = pts.length/4;
		points = new Vecteur[taille*4];
		// Prise en compte des projections 
		points = pts;
	}

	public Patch(Patch pts, int nbPoints) {
		taille = pts.taille;
		points = new Vecteur[taille*4];
		// Prise en compte des projections 
		points = new Vecteur[pts.points.length];
		for (int pos = 0; pos < pts.points.length; pos ++) 
			points[pos] = new Vecteur(pts.points[pos]);
	}

	public void setPoints (Vecteur[] pts) throws InvalidGeomAction {
		if (pts.length != 16) throw new InvalidGeomAction("la liste de points ne correspond pas à la taille d'un patch");
		for (int i = 0; i< 16; i++) {
			points[i] = pts[i];
		}
	}
	
	private Vecteur[] getPoints(int pos, int nbPoints) {
		Vecteur[] pts = new Vecteur[taille];
		for (int l= 0; l<taille; l++) {
			pts[l] = points [l*4+pos];
		}
		// Calcule la spline définie par le point
		ArrayList<Vecteur> al = CourbeParametree3D.getCurvePoints(pts[0], pts[1], pts[2], pts[3], nbPoints-1);
		// recopie les points du arrayList vers le tableau
		Vecteur[] ret = new Vecteur[nbPoints];
		for (int i = 0; i < Math.min(al.size(), nbPoints); i ++) {
			ret[i] = (Vecteur)al.get(i);
		}
		return ret;
	}
	
	
	/**
	 * Calcule la forme définitive du volume déterminé par le patch
	 *   Consiste à décliner horizontalement des tracés à partir des points définis
	 * @throws InvalidGeomAction 
	 *   
	 */
	public Solide calculeForme (int nbPoints) throws InvalidGeomAction {
		// Calcul de la position des points
		Vecteur [][] map = new Vecteur [nbPoints][nbPoints];
		
		// Calcul des points du haut
		Vecteur[] haut = getPoints(0, nbPoints);
		Vecteur[] flott = getPoints(1, nbPoints);
		Vecteur[] flanc = getPoints(2, nbPoints);
		Vecteur[] quille = getPoints(3, nbPoints);

		for (int pos = 0; pos < nbPoints; pos ++) {
			ArrayList<Vecteur> al = CourbeParametree3D.getCurvePoints(haut[pos], flott[pos], flanc[pos], quille[pos], nbPoints-4);
			for (int h = 0; h < al.size(); h ++) {
				map [pos][h+1] = (Vecteur)al.get(h);
			}
			Vecteur v = map[pos][1];
			
			// Gestion de la SKIN
			map[pos][0] = new Vecteur(0, v.getY(), v.getZ());
			v = map[pos][al.size()];
			// Rajoute des points vides équivalents au dernier point
			for (int h = al.size()+1; h < nbPoints; h++ ) {
				// Gestion de la SKIN
				map[pos][h] = new Vecteur(0, v.getY(), v.getZ());
			}
		}
		return SolideMaker.fromMap(map, nbPoints);//, new Decimal(0.01d));
	}
	
	
	

	public static void main(String[] args) throws InvalidGeomAction {
		
		Solide pol = SolideMaker.getPatch(20);
		
		System.out.println("Nb facettes : "+pol.getFacettes().size());
		System.out.println("Nb sommets : "+pol.getSommets().size());

		System.out.println("Start cleaning "+System.currentTimeMillis());

		Bounds bnds = new Bounds();
		bnds.setBounds(pol);
		System.out.println("Bounds : "+bnds.toString());
	}
}
