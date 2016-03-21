package model.calcul;

import java.util.ArrayList;

import model.Area;
import model.math.Bounds;
import model.math.Decimal;
import model.math.Droite2D;
import model.math.MapDeVecteurs;
import model.math.Plan3D;
import model.math.Vecteur;

public class CalculFormes {
	
	public class PointIndex{
		int pos;
		Vecteur inter;
	}

	/***
	 *  Calcul les intersections entre deux maps
	 *  
	 * @param myMap : Map à extruder
	 * @param myBounds : Contrours de la MAP à extruder
	 * @param hisMap : Map d'extrusion 
	 * 
	 * @return
	 */
	public static MapDeVecteurs getMapExtrusion(MapDeVecteurs myMap, Bounds myBounds, MapDeVecteurs hisMap) {
		// vérifie l'intersection
		Bounds hisBnds = Bounds.getBounds(hisMap);
		if (!myBounds.intersecs(hisBnds)) return myMap; // Pas d'intersection --> pas de'extrusion
		// Liste des Area resultantes :
		ArrayList<Area> liste = new ArrayList<Area>(); 
		
		// Parcours les lignes de la MAP pour déterminer les surfaces d'intersections
		for (int y = 0; y < myMap.ySize(); y++) {
			Area myArea = getArea(myMap, y); 

			Vecteur v = myMap.getPoint(0, y);		
			Decimal pos = v.getDecZ(); // position du plan d'intersection
			
			Area hisArea = hisMap.intersectionHorizontale(getPlan(pos));
			
			// Calcule la conjugaison des deux aires
			Area resArea = getExtrusion(myArea, hisArea);
			
			if (resArea != null) liste.add(resArea);
			
		}
		return myMap;
	}
  /***
   * Récupération des points d'une liste donnée
   * 
   */
	private static Area getArea(MapDeVecteurs map, int yPos) {
		Area ret = new Area();
		for (int x = 0; x < map.xSize(); x++) {
			ret.points.add(map.getPoint(x, yPos));
		}
		
		return ret;
	}
	
	
	private static Plan3D getPlan(Decimal pos) {
		Vecteur a = new Vecteur(Decimal.UN, Decimal.ZERO, pos);
		Vecteur b = new Vecteur(Decimal.ZERO, Decimal.ZERO, pos);
		Vecteur c = new Vecteur(Decimal.ZERO, Decimal.UN, pos);
		
		return new Plan3D(a, b, c);
	}

	
	
	
	/**
	 * Algorithme complexe de calcul d'intersection entre deux zones
	 * 
	 * @param myArea
	 * @param hisArea
	 * @return
	 */
	private static Area getExtrusion(Area myArea, Area hisArea) {
		int max = myArea.points.size()-1;
		// Pour permettre de déterminer si un point est dans la forme ou pas
		Droite2D[] droites = new Droite2D[max];
		for (int p = 0; p < max; p++) 
			droites[p] = new Droite2D(myArea.points.get(p), myArea.points.get(p+1));
		
		int posMyStart = -1;
		int posHisStart = -1;
		int posMyStop= -1;
		int poshisStop= -1;
		Vecteur interStart= null;
		Vecteur interStop = null;
		
		boolean lastIn = isInside (hisArea.points.get(max), droites);
		for (int pos = 0; pos <= max; pos ++) {
			boolean in = isInside (hisArea.points.get(pos), droites);
			if (lastIn) {
				if (!in) { // sortie de la forme
					
				}
			}
			
		}
		
		
		
		// TODO Auto-generated method stub
		return null;
	}
	

	

}
