package model.calcul;

import java.util.ArrayList;

import model.Area;
import model.Cata;
import model.Poids;
import model.composants.Composant;
import model.composants.PatchComposant;
import model.math.Axis;
import model.math.Bounds;
import model.math.Decimal;
import model.math.MapDeVecteurs;
import model.math.Plan3D;
import model.math.Vecteur;
import model.math.transfo.Reflexion;
import model.math.transfo.Transformation;
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
	public static MapDeVecteurs createCoque(Patch patch, int precision, boolean sym) {
		// Récupère la demiCoque
		MapDeVecteurs map = patch.getMap(precision, sym);
		if (!sym) {
			return map;
		}
		// Duplique la coque en créant un mirroir
		Reflexion ref = new Reflexion (Axis.XAxis, null);		
		MapDeVecteurs half = map.transforme(ref);
		
		MapDeVecteurs coque = map.addMap(half); 
		return coque;		
	}


	/** 
	 * Calcul des carènes de tous les composants 
	 * **/
	public static void calculeCarene (Cata bateau) {
		Plan3D surface = bateau.mer.getPlan();
		bateau.mer.carenes.clear();
        for (Composant cmp : bateau.composants) {
        	if (cmp.mapAffichage != null) {
	        	Transformation trs = cmp.situation.getTransformation(null);
	        	MapDeVecteurs mdv = cmp.mapAffichage.transforme(trs);
	        	MapDeVecteurs crn = mdv.truncate(surface);
	        	if (crn != null) bateau.mer.carenes.add(crn);
        	}
        }
	}
	
	public static Decimal calculeSurfaceCoque(MapDeVecteurs map) {
		Decimal surf = Decimal.ZERO;
		for (int x = 1; x < map.xSize(); x ++) {
			for (int y = 1; y < map.ySize(); y ++) {
				Vecteur A = map.getPoint(x-1,  y-1);
				Vecteur B= map.getPoint(x,  y-1);
				Vecteur C = map.getPoint(x,  y);
				Vecteur D = map.getPoint(x-1,  y);
				Decimal aire = Vecteur.calculeSurface(A, B, C).add(Vecteur.calculeSurface(A, C, D));
				surf = surf.add(aire);
			}
		}
		return surf;
	}
	
	/** Calcule la surface totale de la coque et multiplie par le coeficient 
	 * 
	 * @param bateau
	 * @return
	 */
	public static Poids calculePoidsCoque(MapDeVecteurs map, Decimal densiteSurfacique) {
		ArrayList<Poids> pds = new ArrayList<Poids>();
		if (map == null) return new Poids();
		// Trace tous les carrrés
		for (int x = 1; x < map.xSize(); x ++) {
			for (int y = 1; y < map.ySize(); y ++) {
				Vecteur A = map.getPoint(x-1,  y-1);
				Vecteur B= map.getPoint(x,  y-1);
				Vecteur C = map.getPoint(x,  y);
				Vecteur D = map.getPoint(x-1,  y);
				Decimal aire = Vecteur.calculeSurface(A, B, C).add(Vecteur.calculeSurface(A, C, D));
				Decimal kg = aire.multiply(densiteSurfacique).multiply(Decimal.MILLE);
				
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
		ArrayList<Poids> poussees = new ArrayList<Poids>(); 
		// Les carènes intègrent bien la position des composants et du bateau lui-même
        for (MapDeVecteurs cmp : cata.mer.carenes) {
        	poussees.add(CalculVolume.getPoussee(cmp));
        }
        cata.mer.pousseeArchimede = CalculVolume.getCentreGravite("Poussée", poussees);
        
        // Intégration du repositionnement du poids des composants
        Transformation mer = cata.mer.getTransformation();
		ArrayList<Poids> pds = new ArrayList<Poids>();
        for (Composant cmp : cata.composants) {
            Transformation trans = cmp.situation.getTransformation(mer);
        	pds.add(cmp.gravite.transforme(trans));
        }
		
		cata.mer.poidsTotal = CalculVolume.getCentreGravite("Poids total ", pds);
		// TODO : calcul si le bateau est stable
		
	}

	
	/** 
	 * Calcul du centre de dérive et de la surface anti-dérive
	 * 
	 * @param cata
	 */
	public static void calculeDerive(Cata cata) {
		// Création d'une projection
		cata.mer.surfaceAntiDerive = new ArrayList<Area>(); 
        for (MapDeVecteurs cmp : cata.mer.carenes) {
        	if (cmp.isValid())
        		cata.mer.surfaceAntiDerive.add(cmp.getProjection());
        }

        Decimal surf = Decimal.ZERO;
        for (Area a : cata.mer.surfaceAntiDerive) surf = surf.add(CalculSurface.getSurface(a.points, Axis.XAxis));
		cata.mer.surfaceTotale = surf;

    	Vecteur v = CalculSurface.getCentreSurfaces(cata.mer.surfaceAntiDerive, Axis.XAxis);

       	cata.mer.centreAntiDerive = v;
	}


	/** 
	 * modifie la MAP d'affichage d'un composant pour extruder avec les composants imbriqués
	 * 
	 * @param pc
	 * @param cata
	 */
	public static void extrudeMap(PatchComposant pc, Cata cata) {
		// Sanity check
		if (pc.reduction == false) return;
		if (pc.mapAffichage == null) return;
		
		// Working map : map d'affichage repositionnée
		Transformation trans = pc.situation.getTransformation(null);
		Transformation back = trans.getReverse(null);
		MapDeVecteurs myMap = pc.mapAffichage;
		
		pc.mapNonReduite = new MapDeVecteurs(pc.mapAffichage);
		
		// Calcul les intersections
		Bounds bnds = Bounds.getBounds(myMap);
		for (Composant cmp : cata.composants) {
			// Ne retient que les patchs ou recopie de patch
			if (cmp.isPatch()) {
				PatchComposant he = (PatchComposant)cmp;
				//positionne la MAP dans le même repère que la MAP de calcul
				MapDeVecteurs hisMap = he.mapAffichage.transforme(he.situation.getTransformation(null)).transforme(back);
				if (he.mapNonReduite != null) {
					myMap = CalculFormes.getMapExtrusion(myMap, bnds, hisMap);
				}
			}
		}
		// Gestion du retour dans le repère initial
		pc.mapAffichage = myMap.transforme(back);
	}
	
}
