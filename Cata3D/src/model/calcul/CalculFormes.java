package model.calcul;

import java.util.ArrayList;

import model.Area;
import model.math.Bounds;
import model.math.Decimal;
import model.math.MapDeVecteurs;
import model.math.Plan3D;
import model.math.Segment;
import model.math.Vecteur;

public class CalculFormes {
	


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
		Segment[] droites = new Segment[max];
		for (int p = 0; p < max; p++) 
			droites[p] = new Segment(myArea.points.get(p), myArea.points.get(p+1));

		// Identifie la liste des points d'intersection
		ArrayList<Intersection> inters = new ArrayList<Intersection>(); 
		int last = hisArea.points.size()-1;
		// Pour chaque point de la courbe secondaire, identifie les intersections
		for (int pos = 0; pos < hisArea.points.size(); pos ++ ) {
			Segment his = new Segment(hisArea.points.get(last), hisArea.points.get(pos));
			int mypos = 0;
			for (Segment seg : droites) {
				Segment inter = seg.intersection(his); 
				if ( inter != null) {
					if (inter.getA().equals(inter.getB())) { // un seul point --> on enregistre une intersection
						if (!inter.getA().equals(seg.getB())) {  
							inters.add(new Intersection(mypos, last, inter.getA()));
						} // On ne prend pas le dernier point 
					} // Deux points diff�rents --> on ne fait rien						
				}
				mypos ++;
			}
			last = pos;
		}
		// Compte le nombre de positions
		if (inters.size() == 0) return null; // pas d'intersections
		// Il y a une seule intersection --> tangente --> pas d'int�ret
		if (inters.size() == 1) return null; // pas d'intersections
		if (inters.size() > 2) return null; // trop d'intersections
		if (inters.get(0).inter.equals(inters.get(1).inter)) return null; // pas d'intersections
		// --> Maintenant il faut parcourir la liste des points
		Intersection inter1 = inters.get(0);
		Intersection inter2 = inters.get(1);
		if ( (inter1.hisPos - inter2.hisPos == 1) || (inter1.hisPos - inter2.hisPos == -1) || (inter1.hisPos - inter2.hisPos == 0) )
		   return null; 		// Si les deux points sont cons�cutifs --> rien

		Intersection myStart = null;
		Intersection myEnd = null;
		if (inter1.myPos > inter2.myPos) { myStart = inter2; myEnd = inter1; }
		else { myStart = inter1; myEnd = inter2; }
		
		Intersection hisStart = null;
		Intersection hisEnd = null;
		if (inter1.hisPos > inter2.hisPos) { hisStart = inter2; hisEnd = inter1; }
		else { hisStart = inter1; hisEnd = inter2; }
		
		Area ret = new Area();
		if (hisStart == myStart) { // Meme sens
			
		} else { // Autre sens
			
		}
		return new Area();
	}

	public static void main (String[] args) {
		
		Area a1 = new Area();
		a1.points.add(new Vecteur("0;0;0"));
		a1.points.add(new Vecteur("0;4;0"));
		a1.points.add(new Vecteur("3;4;0"));
		a1.points.add(new Vecteur("3;2;0"));
		a1.points.add(new Vecteur("3;0;0"));

		Area a2 = new Area();
		a2.points.add(new Vecteur("8;0;0"));
		a2.points.add(new Vecteur("8;4;0"));
		a2.points.add(new Vecteur("5;4;0"));
		a2.points.add(new Vecteur("5;2;0"));
		a2.points.add(new Vecteur("5;0;0"));

		Area res = CalculFormes.getExtrusion(a1, a2);
		if (res != null)
			System.out.println("Resultat 1 : "+res.points.toString());
		else System.out.println("Resultat 1 : pas d'intersection");

		Area a3 = new Area();
		a3.points.add(new Vecteur("1;1;0"));
		a3.points.add(new Vecteur("1;3;0"));
		a3.points.add(new Vecteur("2;3;0"));
		a3.points.add(new Vecteur("2;1;0"));

		res = CalculFormes.getExtrusion(a1, a3);
		if (res != null)
			System.out.println("Resultat 2 : "+res.points.toString());
		else System.out.println("Resultat 2 : pas d'intersection");

		Area a4 = new Area();
		a4.points.add(new Vecteur("0;0;0"));
		a4.points.add(new Vecteur("1;4;0"));
		a4.points.add(new Vecteur("3;4;0"));
		a4.points.add(new Vecteur("3;2;0"));
		a4.points.add(new Vecteur("1;2;0"));
		a4.points.add(new Vecteur("1;0;0"));

		Area a5 = new Area();
		a5.points.add(new Vecteur("2;0;0"));
		a5.points.add(new Vecteur("5;0;0"));
		a5.points.add(new Vecteur("5;3;0"));
		a5.points.add(new Vecteur("4;3;0"));
		a5.points.add(new Vecteur("2;1;0"));

		res = CalculFormes.getExtrusion(a4, a5);
		if (res != null)
			System.out.println("Resultat 3 : "+res.points.toString());
		else System.out.println("Resultat 3 : pas d'intersection");

		Area a6 = new Area();
		a6.points.add(new Vecteur("3;4;0"));
		a6.points.add(new Vecteur("6;4;0"));
		a6.points.add(new Vecteur("6;1;0"));
		a6.points.add(new Vecteur("3;1;0"));

		res = CalculFormes.getExtrusion(a1, a6);
		if (res != null)
			System.out.println("Resultat 4 : "+res.points.toString());
		else System.out.println("Resultat 4 : pas d'intersection");

		Area a7 = new Area();
		a7.points.add(new Vecteur("3;4;0"));
		a7.points.add(new Vecteur("6;4;0"));
		a7.points.add(new Vecteur("6;3;0"));
		a7.points.add(new Vecteur("3;3;0"));

		res = CalculFormes.getExtrusion(a1, a7);
		if (res != null)
			System.out.println("Resultat 5 : "+res.points.toString());
		else System.out.println("Resultat 5 : pas d'intersection");

		Area a8 = new Area();
		a8.points.add(new Vecteur("5;4;0"));
		a8.points.add(new Vecteur("5;2.5;0"));
		a8.points.add(new Vecteur("2;2.5;0"));
		a8.points.add(new Vecteur("2;3.5;0"));

		res = CalculFormes.getExtrusion(a1, a8);
		if (res != null)
			System.out.println("Resultat 6 : "+res.points.toString());
		else System.out.println("Resultat 6 : pas d'intersection");
		
	}
	

}
