package model.calcul;

import java.util.ArrayList;

import model.Poids;
import model.math.Axis;
import model.math.Decimal;
import model.math.MapDeVecteurs;
import model.math.Vecteur;

public class CalculVolume {
	
	/**
	 * Calcul le volume du tetraedre
	 * volume du teraedre = (x1y2z3 + x2y3z1 + x3y2z2 − z1y2x3 − z2y3x1 − z3y1x2)/6
	 * 
	 * @param A
	 * @param B
	 * @param C
	 * @param C
	 * @return
	 */
	private static Decimal getVolume(Vecteur A, Vecteur B, Vecteur C, Vecteur D) { // 4 points
		Vecteur v1 = B.minus(A);
		Vecteur v2 = C.minus(A);
		Vecteur v3 = D.minus(A);
		
		long prod = v1.getX()*v2.getY()*v3.getZ() + v2.getX()*v3.getY()*v1.getZ() + v3.getX()*v2.getY()*v2.getZ() -
				    v3.getX()*v2.getY()*v1.getZ() - v1.getX()*v3.getY()*v2.getZ() - v2.getX()*v1.getY()*v3.getZ();
		
		return new Decimal(prod).divide(new Decimal(6)).divide(Vecteur.METER).divide(Vecteur.METER).divide(Vecteur.METER).multiply(Decimal.MILLE);
	}
	
	/**
	 * Calcul du volume d'un paralelépipède --> 6 face = 12 tetraedres
	 * 
	 * @param v
	 * @return
	 */
	private static Decimal getVolume(Vecteur centre, Vecteur[] v) { // tableau de 8 points 
		
		/* --> 12 facette :
		 *  ABC, EFG, ABE, BCF, CDG, DAH
		 *  BCD, FGH, BEF, CFG, DGH, AHE 
		 */
		Decimal volume = getVolume(centre, v[0], v[1], v[2]);
		volume.add(getVolume(centre, v[1], v[2], v[3]));
		volume.add(getVolume(centre, v[4], v[5], v[6]));
		volume.add(getVolume(centre, v[5], v[6], v[7]));
		volume.add(getVolume(centre, v[0], v[1], v[4]));
		volume.add(getVolume(centre, v[1], v[4], v[5]));
		volume.add(getVolume(centre, v[1], v[2], v[5]));
		volume.add(getVolume(centre, v[2], v[5], v[6]));
		volume.add(getVolume(centre, v[2], v[3], v[6]));
		volume.add(getVolume(centre, v[3], v[6], v[7]));
		volume.add(getVolume(centre, v[3], v[0], v[7]));
		volume.add(getVolume(centre, v[0], v[7], v[4]));

		return volume;
	}

	/**
	 * 
	 * @param map
	 * @return
	 */
	public static Poids getPoussee2(MapDeVecteurs map) { 
		Vecteur[] lst = new Vecteur[8];
		// Liste des poussées
		ArrayList<Poids> pds = new ArrayList<Poids>();
		// Trace tous les carrrés
		for (int x = 1; x < map.xSize(); x ++) {
			for (int y = 1; y < map.ySize(); y ++) {
				lst[0] = map.getPoint(x-1,  y-1);
				lst[1] = map.getPoint(x,  y-1);
				lst[2] = map.getPoint(x,  y);
				lst[3] = map.getPoint(x-1,  y);
				lst[4] = map.getPoint(x-1,  y-1).set(Axis.ZAxis, 0);
				lst[5] = map.getPoint(x,  y-1).set(Axis.ZAxis, 0);
				lst[6] = map.getPoint(x,  y).set(Axis.ZAxis, 0);
				lst[7] = map.getPoint(x-1,  y).set(Axis.ZAxis, 0);
				// Centre sde poussée
				Vecteur centre = CalculSurface.getCentre (lst);
				Decimal vol = getVolume(centre, lst);
				pds.add(new Poids (" ", centre, vol));
			}
		}
		Poids p = getCentreGravite("Poussée ", pds);
		p.force = p.force.negate();
		return p;
	}

	
	/**
	 * Calcul la poussée en saussissonnant la MAP
	 * 
	 * @param map
	 * @return
	 */
	public static Poids getPoussee(MapDeVecteurs map) { 
		ArrayList<Vecteur> lst = new ArrayList<Vecteur>();
		// Calcule les surfaces de chaque section
		ArrayList<Decimal> surfaces= new ArrayList<Decimal>();
		ArrayList<Vecteur> centresSurf = new ArrayList<Vecteur>();
		for (int x = 0; x < map.xSize(); x ++) {
			lst.clear();
			for (int y = 0; y < map.ySize(); y ++) 
				lst.add(map.getPoint(x, y));
				// Centre sde poussée
			centresSurf.add(CalculSurface.getCentre (lst));
			surfaces.add(CalculSurface.getSurface(lst));
		}

		// Calcule le volume et le centre de poussée de chaque bloc
		ArrayList<Poids> poussees = new ArrayList<Poids>();
		for (int x = 1; x < map.xSize(); x ++) {
			// Centre de poussée 
			Vecteur ctr = centresSurf.get(x-1).multiply(surfaces.get(x-1)).add(centresSurf.get(x).multiply(surfaces.get(x))) ;
			Decimal surf = surfaces.get(x-1).add(surfaces.get(x));
			if (!surf.isZero())
				ctr = ctr.multiply(surf.inverse());
			
			Decimal dist = centresSurf.get(x).getDecZ().minus(centresSurf.get(x-1).getDecZ());
			Decimal vol = ((surfaces.get(x-1).add(surfaces.get(x))).multiply(Decimal.DEMI)).multiply(dist);
			
			poussees.add(new Poids("", ctr, vol));			
		}
		
		Poids p = getCentreGravite("Poussée ", poussees);
		p.force = p.force.multiply(Decimal.MILLE).negate();
		return p;
	}

	
	
	/**
	 * Calcule le centre de gravité
	 * @param pds
	 * @return
	 */
	static Poids getCentreGravite (String n, ArrayList<Poids> pds){
		
		// calcule le Centre de gravité
		Vecteur centre = new Vecteur (0, 0, 0);
		Decimal poidsTotal = new Decimal(0);
		
		if (pds.size() == 0) return new Poids (n, centre, poidsTotal); 
		
		// Parcours les poids
		for (Poids p : pds) {
			centre = centre.add(p.position.multiply(p.force));
			poidsTotal = poidsTotal.add(p.force);
		}
		if (poidsTotal.isZero()) return new Poids (n, centre, poidsTotal); 

		return new Poids (n, centre.multiply(poidsTotal.inverse()), poidsTotal);
	}




}