package model.calcul;

import java.util.ArrayList;

import math.geom2d.Point2D;
import math.geom2d.polygon.LinearRing2D;
import math.geom2d.polygon.Polygon2D;
import math.geom2d.polygon.Polygons2D;
import model.Area;
import model.math.Axis;
import model.math.Bounds;
import model.math.Decimal;
import model.math.MapDeVecteurs;
import model.math.Plan3D;
import model.math.Vecteur;
import model.patch.Patch;

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
			
			Area hisArea = hisMap.intersectionHorizontaleZ(getPlan(pos));
			
			// Calcule la conjugaison des deux aires
			Area resArea = getExtrusion(myArea, hisArea, pos);
			if (resArea == null) {
				System.err.println("Extruction error : "+pos.toString());
				return null;
			}
			
			if (resArea != null) liste.add(resArea);
			
		}

		int nbPoints = 30;
		MapDeVecteurs mapX = Patch.getMapDecoupe(myMap, nbPoints, Axis.XAxis);
//		MapDeVecteurs mapZ = Patch.getMapDecoupe(mapX, nbPoints, Axis.ZAxis);
		return mapX;
		
		/**		// Splitting the points in equal numbers of points 
		int nbPoints = myMap.xSize();
		MapDeVecteurs res = new MapDeVecteurs(nbPoints, myMap.ySize());
		int y = 0;
		for (Area a : liste) {
			int x = 0;
			for (Vecteur pt : a.split(nbPoints)) {
				res.setPoint(x, y, pt); x ++;
			}
			y ++;
		}
		return res;
**/
		/**		// Liste des intersections effectu�e
		// calul de la MAP --> nom max de points 
		int maxX = 0;
		for (Area a : liste) {
			if (a.points.size() > maxX) 
				maxX = a.points.size();
		}
		MapDeVecteurs res = new MapDeVecteurs(maxX, myMap.ySize());
		int y = 0;
		for (Area a : liste) {
			int x = 0;
			Vecteur last = null;
			for (Vecteur v : a.points) {
				res.setPoint(x, y, v); x ++;
				last = v;
			}
			while (x < maxX) {
				res.setPoint(x, y, last); x ++;
			}
			y ++; // Passe au niveau sup�rieur
		}
**/		
	}
	

	public static MapDeVecteurs getCollision(MapDeVecteurs myMap, Bounds bnds, MapDeVecteurs hisMap) {
		Bounds hisBnds = Bounds.getBounds(hisMap);
		if (!bnds.intersecs(hisBnds)) return null; // Pas d'intersection --> pas de'extrusion
		
		ArrayList<Area> lst = new ArrayList<Area>(); 
		// Parcours les lignes de la MAP pour déterminer les surfaces d'intersections
		for (int y = 0; y < myMap.ySize(); y++) {
			Area myArea = getArea(myMap, y); 

			Vecteur v = myMap.getPoint(0, y);		
			Decimal pos = v.getDecZ(); // position du plan d'intersection
			
			Area hisArea = hisMap.intersectionHorizontaleZ(getPlan(pos));

			Area a = getCollision(myArea, hisArea, pos);
			if (a != null) lst.add(a);
		}
		int min = 0;
		for (Area a : lst) if (a.points.size() > min) min = a.points.size();
		MapDeVecteurs ret = new MapDeVecteurs(min, lst.size());
		int y = 0;
		for (Area area : lst) {
			double step = (double)(area.points.size()) / (double)min;
			Vecteur last = area.points.get(0);
			int pos = 0;
			for (int x = 0; x < min; x++) {
				double p = step * x;
				if (p > pos) {
					pos ++;
					if (pos < area.points.size()) last = area.points.get(pos);
					ret.setPoint(x, y, last);
				} else {
					ret.setPoint(x, y, last);					
				}
			}
			y++;
		}

		return ret;
	}
	


/**	private static boolean getCollision(Area myArea, Area hisArea) {
		Polygon2D myPoly = CalculSurface.getPoly(myArea.points, Axis.ZAxis);
		Polygon2D hisPoly = CalculSurface.getPoly(hisArea.points, Axis.ZAxis);

		Polygon2D res = Polygons2D.intersection(myPoly, hisPoly); 
		if ( res == null ) return false;
		if (res.vertices().size() == 0) return false;
		return true;
	}
**/

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
	public static Area getCollision(Area myArea, Area hisArea, Decimal z) {
		Polygon2D myPoly = CalculSurface.getPoly(myArea.points, Axis.ZAxis);
		Polygon2D hisPoly = CalculSurface.getPoly(hisArea.points, Axis.ZAxis);
		
		Polygon2D resPoly = Polygons2D.intersection(myPoly, hisPoly); 
		if (resPoly == null) {
			return null;
		}
		if (resPoly.edgeNumber() == 0) {
			return null;
		}
		if (resPoly.contours().size() > 1) {
			return null;
		}
		Area ret = new Area();
		for (int p = resPoly.vertexNumber()-1; p >= 0; p--) {
			Point2D pt  = resPoly.vertex(p);
			ret.points.add(new Vecteur(new Decimal(pt.x()), new Decimal(pt.y()), z));
		}
		return ret;
	}

	
	/**
	 * Algorithme complexe de calcul d'intersection entre deux zones
	 * 
	 * @param myArea
	 * @param hisArea
	 * @return
	 */
	public static Area getExtrusion(Area myArea, Area hisArea, Decimal z) {
		if( myArea.points.size() == 0) return myArea;
		Polygon2D myPoly = CalculSurface.getPoly(myArea.points, Axis.ZAxis);
		Polygon2D hisPoly = CalculSurface.getPoly(hisArea.points, Axis.ZAxis);

		Polygon2D resPoly = Polygons2D.difference(myPoly, hisPoly); 
		if (resPoly == null) {
			System.err.println("Extruction error : Non polygon");
			return null;
		}
		if (resPoly.edgeNumber() == 0) {
			System.err.println("Extruction error : Empty polygon");
			Area ret = new Area();
			ret.points.add(myArea.points.get(0));
			return ret;
		}
		if (resPoly.contours().size() > 1) {
			System.err.println("Extruction error : Multiple polygon");
			Area ret = new Area();
			LinearRing2D rg = resPoly.contours().iterator().next();
			for (Point2D pt : rg.vertices()) {
				ret.points.add(new Vecteur(new Decimal(pt.x()), new Decimal(pt.y()), z));
			}
			return ret;
		}
		Area ret = new Area();
		for (int p = resPoly.vertexNumber()-1; p >= 0; p--) {
			Point2D pt  = resPoly.vertex(p);
			ret.points.add(new Vecteur(new Decimal(pt.x()), new Decimal(pt.y()), z));
		}
		return ret;
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

		Area res = CalculFormes.getExtrusion(a1, a2, Decimal.ZERO);
		if (res != null)
			System.out.println("Resultat 1 : "+res.points.toString());
		else System.out.println("Resultat 1 : pas d'intersection");

		Area a3 = new Area();
		a3.points.add(new Vecteur("1;1;0"));
		a3.points.add(new Vecteur("1;3;0"));
		a3.points.add(new Vecteur("2;3;0"));
		a3.points.add(new Vecteur("2;1;0"));

		res = CalculFormes.getExtrusion(a1, a3, Decimal.ZERO);
		if (res != null)
			System.out.println("Resultat 2 : "+res.points.toString());
		else System.out.println("Resultat 2 : pas d'intersection");

		Area a4 = new Area();
		a4.points.add(new Vecteur("0;0;0"));
		a4.points.add(new Vecteur("0;4;0"));
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

		res = CalculFormes.getExtrusion(a4, a5, Decimal.ZERO);
		if (res != null)
			System.out.println("Resultat 3 : "+res.points.toString());
		else System.out.println("Resultat 3 : pas d'intersection");

		Area a6 = new Area();
		a6.points.add(new Vecteur("3;4;0"));
		a6.points.add(new Vecteur("6;4;0"));
		a6.points.add(new Vecteur("6;1;0"));
		a6.points.add(new Vecteur("3;1;0"));

		res = CalculFormes.getExtrusion(a1, a6, Decimal.ZERO);
		if (res != null)
			System.out.println("Resultat 4 : "+res.points.toString());
		else System.out.println("Resultat 4 : pas d'intersection");

		Area a7 = new Area();
		a7.points.add(new Vecteur("3;4;0"));
		a7.points.add(new Vecteur("6;4;0"));
		a7.points.add(new Vecteur("6;3;0"));
		a7.points.add(new Vecteur("3;3;0"));

		res = CalculFormes.getExtrusion(a1, a7, Decimal.ZERO);
		if (res != null)
			System.out.println("Resultat 5 : "+res.points.toString());
		else System.out.println("Resultat 5 : pas d'intersection");

		Area a8 = new Area();
		a8.points.add(new Vecteur("5;4;0"));
		a8.points.add(new Vecteur("5;2.5;0"));
		a8.points.add(new Vecteur("2;2.5;0"));
		a8.points.add(new Vecteur("2;3.5;0"));

		res = CalculFormes.getExtrusion(a1, a8, Decimal.ZERO);
		if (res != null)
			System.out.println("Resultat 6 : "+res.points.toString());
		else System.out.println("Resultat 6 : pas d'intersection");

		Area a9 = new Area();
		a9.points.add(new Vecteur("0;0;0"));
		a9.points.add(new Vecteur("0;3;0"));
		a9.points.add(new Vecteur("3;3;0"));
		a9.points.add(new Vecteur("3;2;0"));
		a9.points.add(new Vecteur("5;2;0"));
		a9.points.add(new Vecteur("5;0;0"));

		Area a10 = new Area();
		a10.points.add(new Vecteur("4;4;0"));
		a10.points.add(new Vecteur("4;1;0"));
		a10.points.add(new Vecteur("6;1;0"));
		a10.points.add(new Vecteur("6;4;0"));

		res = CalculFormes.getExtrusion(a9, a10, Decimal.ZERO);
		if (res != null)
			System.out.println("Resultat 7 : "+res.points.toString());
		else System.out.println("Resultat 7 : pas d'intersection");

		Area a11 = new Area();
		a11.points.add(new Vecteur("5;4;0"));
		a11.points.add(new Vecteur("6;4;0"));
		a11.points.add(new Vecteur("6;1;0"));
		a11.points.add(new Vecteur("1;1;0"));
		a11.points.add(new Vecteur("1;2;0"));
		a11.points.add(new Vecteur("5;2;0"));

		res = CalculFormes.getExtrusion(a1, a11, Decimal.ZERO);
		if (res != null)
			System.out.println("Resultat 8 : "+res.points.toString());
		else System.out.println("Resultat 8 : pas d'intersection");
		
		Area a12 = new Area();
		a12.points.add(new Vecteur("2;2;0"));
		a12.points.add(new Vecteur("6;2;0"));
		a12.points.add(new Vecteur("6;0;0"));
		a12.points.add(new Vecteur("2;0;0"));

		res = CalculFormes.getExtrusion(a1, a12, Decimal.ZERO);
		if (res != null)
			System.out.println("Resultat 9 : "+res.points.toString());
		else System.out.println("Resultat 9 : pas d'intersection");

		Area a13 = new Area();
		a13.points.add(new Vecteur("2;3;0"));
		a13.points.add(new Vecteur("5;3;0"));
		a13.points.add(new Vecteur("5;1;0"));
		a13.points.add(new Vecteur("2;1;0"));

		res = CalculFormes.getExtrusion(a1, a13, Decimal.ZERO);
		if (res != null)
			System.out.println("Resultat 10 : "+res.points.toString());
		else System.out.println("Resultat 10 : pas d'intersection");

		Area a14 = new Area();
		a14.points.add(new Vecteur("4;3;0"));
		a14.points.add(new Vecteur("4;1;0"));
		a14.points.add(new Vecteur("-1;1;0"));
		a14.points.add(new Vecteur("-1;3;0"));

		res = CalculFormes.getExtrusion(a1, a14, Decimal.ZERO);
		if (res != null)
			System.out.println("Resultat 11 : "+res.points.toString());
		else System.out.println("Resultat 11 : pas d'intersection");

	}

}
