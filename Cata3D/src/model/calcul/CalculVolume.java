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
	private static Decimal getVolume(Vecteur centre, ArrayList<Vecteur> lst) { // tableau de 8 points 
		
		Vecteur[] v = new Vecteur[8];
		lst.toArray(v);
		
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
		ArrayList<Vecteur> lst = new ArrayList<Vecteur>(8);
		// Liste des poussées
		ArrayList<Poids> pds = new ArrayList<Poids>();
		// Trace tous les carrrés
		for (int x = 1; x < map.xSize(); x ++) {
			for (int y = 1; y < map.ySize(); y ++) {
				lst.add(map.getPoint(x-1,  y-1));
				lst.add(map.getPoint(x,  y-1));
				lst.add(map.getPoint(x,  y));
				lst.add(map.getPoint(x-1,  y));
				lst.add(map.getPoint(x-1,  y-1).set(Axis.ZAxis, 0));
				lst.add(map.getPoint(x,  y-1).set(Axis.ZAxis, 0));
				lst.add(map.getPoint(x,  y).set(Axis.ZAxis, 0));
				lst.add(map.getPoint(x-1,  y).set(Axis.ZAxis, 0));
				// Centre sde poussée
				Vecteur centre = CalculSurface.getCentreSurface (lst, Axis.ZAxis);
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
	 *   par les Y
	 * 
	 * @param map
	 * @return
	 */
	public static Poids getPoussee(MapDeVecteurs map) { 
		ArrayList<Vecteur> lst = new ArrayList<Vecteur>();
		// Calcule les surfaces de chaque section
		ArrayList<Decimal> surfaces= new ArrayList<Decimal>();
		ArrayList<Vecteur> centresSurf = new ArrayList<Vecteur>();
		for (int y = 0; y < map.ySize(); y ++) {
			lst.clear();
			for (int x = 0; x < map.xSize(); x ++) 
				lst.add(map.getPoint(x, y));
				// Centre sde poussée
			centresSurf.add(CalculSurface.getCentreSurface (lst, Axis.ZAxis));
			surfaces.add(CalculSurface.getSurface(lst, Axis.ZAxis).abs());
		}

		
		// Calcule le volume et le centre de poussée de chaque bloc
		ArrayList<Poids> poussees = new ArrayList<Poids>();
		for (int y = 1; y < map.ySize(); y ++) {
			// Centre de poussée 
			Decimal dx = centresSurf.get(y-1).getDecX().multiply(surfaces.get(y-1)).add(centresSurf.get(y).getDecX().multiply(surfaces.get(y)));
			Decimal dy = centresSurf.get(y-1).getDecY().multiply(surfaces.get(y-1)).add(centresSurf.get(y).getDecY().multiply(surfaces.get(y)));
			Decimal dz = centresSurf.get(y-1).getDecZ().multiply(surfaces.get(y-1)).add(centresSurf.get(y).getDecZ().multiply(surfaces.get(y)));
			
			Decimal surf = surfaces.get(y-1).add(surfaces.get(y));
			if (!surf.isZero()) {
				dx = dx.divide(surf);
				dy = dy.divide(surf);
				dz = dz.divide(surf);
			}
			
			Decimal dist = centresSurf.get(y).getDecZ().minus(centresSurf.get(y-1).getDecZ());
			Decimal vol = ((surfaces.get(y-1).add(surfaces.get(y))).multiply(Decimal.DEMI)).multiply(dist);
			
			poussees.add(new Poids("", new Vecteur(dx, dy, dz), vol));			
		}
		
		Poids p = getCentreGravite("Poussée ", poussees);
		p.force = p.force.multiply(Decimal.MILLE).negate();
		return p;
	}

	
	/** 
	 * Calcule le volume induit par une forme symétrique
	 *  
	 * @param v
	 * @return
	 */
	public static Poids getVolumeSymetrique (MapDeVecteurs v, String nom, Decimal densite) {
		ArrayList<Poids> liste= new ArrayList<Poids>();

		for (int x = 1; x < v.xSize(); x ++) {
			for (int y = 1; y < v.ySize(); y ++) {
				Decimal surf = Vecteur.calculeSurface(v.getPoint(x-1, y-1), v.getPoint(x, y-1), v.getPoint(x, y));
				surf = surf.add(Vecteur.calculeSurface(v.getPoint(x-1, y-1), v.getPoint(x, y), v.getPoint(x-1, y)));
				Decimal h = v.getPoint(x-1, y-1).getDecX();
				h = h.add(v.getPoint(x-1, y).getDecX());
				h = h.add(v.getPoint(x, y).getDecX());
				h = h.add(v.getPoint(x, y-1).getDecX());
				h = h.divide(Decimal.QUATRE).abs();
				Decimal vol = h.multiply(surf).multiply(densite);
				Vecteur c = v.getPoint(x-1, y-1);
				c = c.add(v.getPoint(x-1, y));
				c = c.add(v.getPoint(x, y-1));
				c = c.add(v.getPoint(x, y));
				c = c.multiply(Decimal.QUATRE.inverse());
//				c = c.set(Axis.XAxis, 0);
				
				liste.add(new Poids ("pds", c, vol));
			}
		}
		return CalculVolume.getCentreGravite(nom, liste);
	}
	
	
	/**
	 * Calcule le centre de gravité
	 * @param pds
	 * @return
	 */
	static public Poids getCentreGravite (String n, ArrayList<Poids> pds){
		
		// calcule le Centre de gravité
		Vecteur centre = new Vecteur (0, 0, 0);
		Decimal poidsTotal = new Decimal(0);
		
		if (pds.size() == 0) return new Poids (n, centre, poidsTotal); 
		
		// Parcours les poids
		for (Poids p : pds) {
			poidsTotal = poidsTotal.add(p.force);
		}
		if (poidsTotal.isZero()) return new Poids (n, centre, poidsTotal); 

		for (Poids p : pds) {
			Decimal coef = p.force.divide(poidsTotal);
			centre = centre.add(p.position.multiply(coef));
		}

		return new Poids (n, centre, poidsTotal);
	}




}
