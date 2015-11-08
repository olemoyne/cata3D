package model.calcul;

import java.util.ArrayList;

import model.Area;
import model.Cata;
import model.Poids;
import model.math.Axis;
import model.math.Decimal;
import model.math.MapDeVecteurs;
import model.math.Plan3D;
import model.math.Vecteur;
import model.math.transfo.Reflexion;
import model.patch.Patch;

/**
 * Calcule les informations de coque
 * 
 * @author olemoyne
 *
 */
public class CalculCoque {

	/**
	 * Calcule la forme de la coque
	 * 
	 * @param patch
	 * @param precision
	 * @return
	 */
	public static MapDeVecteurs createCoque(Patch patch, int precision) {
		// Récupère la demiCoque
		MapDeVecteurs map = patch.getMap(precision);
		// Duplique la coque en créant un mirroir
		Reflexion ref = new Reflexion (Axis.XAxis, null);		
		MapDeVecteurs half = map.transforme(ref);
		
		MapDeVecteurs coque = map.addMap(half); 
		
		return coque;
	}


	public static void calculeCarene (Cata bateau) {
		Plan3D surface = bateau.mer.getPlan();
		bateau.mer.coque = bateau.mapAffichage.transforme(bateau.mer.getTransformation());
		bateau.mer.carene= bateau.mer.coque.truncate(surface);
	}
	
	/** Calcule la surface totale de la coque et multiplie par le coeficient 
	 * 
	 * @param bateau
	 * @return
	 */
	public static Poids calculePoidsCoque(Cata bateau) {
		MapDeVecteurs map = bateau.mapAffichage;
		ArrayList<Poids> pds = new ArrayList<Poids>();
		// Trace tous les carrrés
		for (int x = 1; x < map.xSize(); x ++) {
			for (int y = 1; y < map.ySize(); y ++) {
				Vecteur A = map.getPoint(x-1,  y-1);
				Vecteur B= map.getPoint(x,  y-1);
				Vecteur C = map.getPoint(x,  y);
				Vecteur D = map.getPoint(x-1,  y);
				Decimal aire = Vecteur.calculeSurface(A, B, C).add(Vecteur.calculeSurface(A, C, D));
				Decimal kg = aire.multiply(bateau.mer.densiteSurfaciqueCoque);
				// Centre du poids
				Vecteur centre = A.add(B).add(C).add(D).multiply(new Decimal(0.25f));
				Poids p = new Poids ("", centre, kg);
				pds.add(p);
			}
		}
		return CalculVolume.getCentreGravite("Poids de la coque", pds);
	}
	
	
	/**
	 * Calcule la flottaison en fonction des poids et du volume de carène
	 * 
	 * @param cata
	 */
	public static void calculeFlottaison(Cata cata) {
		cata.mer.pousseeArchimede = CalculVolume.getPoussee(cata.mer.carene);
		ArrayList<Poids> pds = new ArrayList<Poids>();
		pds.addAll(cata.poids);
		pds.add(cata.mer.poidsDeLaCoque);
		
		cata.mer.poidsTotal = CalculVolume.getCentreGravite("Poids total ", pds);
		// calcul si le bateau est stable
		
	}


	/** 
	 * TODO : Calcul du centre de dérive et de la surface anti-dérive
	 * 
	 * @param cata
	 */
	public static void calculeDerive(Cata cata) {
		// Création d'une projection
		Area surface = new Area();
		for (int y = 0; y < cata.mer.carene.ySize(); y++) {
			surface.points.add(cata.mer.carene.getPoint(0, y));
		}
		int max = cata.mer.carene.xSize()-1;
		for (int y = cata.mer.carene.ySize()-1; y > 0 ; y--) {
			surface.points.add(cata.mer.carene.getPoint(max, y));
		}
		
		cata.mer.centreAntiDerive = surface.getCentre();
		cata.mer.surfaceAntiDerive = surface.getSurface();
	}
	
}
