package model.math.solide;

import java.util.ArrayList;

import appli.CataAppliException;
import appli.CataFileManager;
import model.Cata;
import model.composants.Composant;
import model.composants.PatchComposant;
import model.math.Axis;
import model.math.Decimal;
import model.math.InvalidGeomAction;
import model.math.MapDeVecteurs;
import model.math.Triangle;
import model.math.Vecteur;

/**
 * Permet de transformer une MAP en solide pour générer un STL
 * 
 * @author olemoyne
 *
 */
public class MapConversion {

	
	// AJoute les triangles associés à un paralelépipède
	public static void addTriangles (ArrayList<Triangle> tris, Vecteur a, Vecteur b, Vecteur c, Vecteur d) {
		try {
			// Triangle 1 
			Triangle tr1 = new Triangle(a, b, c);
			tris.add(tr1);
		} catch (InvalidGeomAction e) {};
		try {
			// Triangle 2 
			Triangle tr2 = new Triangle(a, c, d);		
			tris.add(tr2);
		} catch (InvalidGeomAction e) {};
		
	}
	
	
	/**
	 * Génération d'un solide sur la base d'une MAP de vecteurs
	 * 
	 * @param map
	 * @return
	 */
	public static Solide getSolide (MapDeVecteurs map, boolean isSymetrique) {
		// Liste de triangles
		ArrayList<Triangle> tris = new ArrayList<Triangle>(); 

		for (int x = 1; x < map.xSize(); x ++) {
			for (int y = 1; y < map.ySize(); y ++) {
				addTriangles(tris, map.getPoint(x-1, y-1), map.getPoint(x-1, y), map.getPoint(x, y), map.getPoint(x, y-1));
			}
		}
		
		// Parcours tous les X pour prendre les premiers et derniers points et les relier
		for (int x = 1; x < map.xSize(); x ++) {
			int y = 0;
			addTriangles(tris, map.getPoint(x-1, y), map.getPoint(x, y), map.getPoint(x, y).set(Axis.XAxis, 0), map.getPoint(x-1, y).set(Axis.XAxis, 0));
			y = map.ySize()-1;
			addTriangles(tris, map.getPoint(x-1, y), map.getPoint(x-1, y).set(Axis.XAxis, 0), map.getPoint(x, y).set(Axis.XAxis, 0), map.getPoint(x, y));
		}

		// Parcours tous les Y pour prendre les premiers et derniers points et les relier
		for (int y = 1; y < map.ySize(); y ++) {
			int x = 0;
			addTriangles(tris, map.getPoint(x, y-1), map.getPoint(x, y-1).set(Axis.XAxis, 0), map.getPoint(x, y).set(Axis.XAxis, 0), map.getPoint(x, y));
			x = map.xSize()-1;
			addTriangles(tris, map.getPoint(x, y-1), map.getPoint(x, y), map.getPoint(x, y).set(Axis.XAxis, 0), map.getPoint(x, y-1).set(Axis.XAxis, 0));
		}

		// retourne le solide avec les triangles
		return new Solide (tris);
	}
	
	/**
	 * Génération d'un solide creusé sur une épaisseur donnée sur la base d'une MAP de vecteurs
	 * 
	 * @param map
	 * @return
	 */
	public static Solide getSolideVide (MapDeVecteurs map, Decimal epaiss, boolean isSymetrique) {
		Solide sol = getSolide (map, isSymetrique);
		// Récupère la liste des triangles du dessus 
		ArrayList<Triangle> tris = sol.getFacettes();
		
		MapDeVecteurs sub = map.rezise(epaiss.negate());

		for (int x = 1; x < sub.xSize(); x ++) {
			for (int y = 1; y < sub.ySize(); y ++) {
				addTriangles(tris, sub.getPoint(x-1, y-1), sub.getPoint(x, y-1), sub.getPoint(x, y), sub.getPoint(x-1, y));
			}
		}
		
		// Parcours tous les X pour prendre les premiers et derniers points et les relier
		for (int x = 1; x < sub.xSize(); x ++) {
			int y = 0;
			addTriangles(tris, sub.getPoint(x-1, y), sub.getPoint(x-1, y).set(Axis.XAxis, 0), sub.getPoint(x, y).set(Axis.XAxis, 0), sub.getPoint(x, y));
			y = sub.ySize()-1;
			addTriangles(tris, sub.getPoint(x-1, y), sub.getPoint(x, y), sub.getPoint(x, y).set(Axis.XAxis, 0), sub.getPoint(x-1, y).set(Axis.XAxis, 0));
		}

		// Parcours tous les Y pour prendre les premiers et derniers points et les relier
		for (int y = 1; y < sub.ySize(); y ++) {
			int x = 0;
			addTriangles(tris, sub.getPoint(x, y-1), sub.getPoint(x, y), sub.getPoint(x, y).set(Axis.XAxis, 0), sub.getPoint(x, y-1).set(Axis.XAxis, 0));
			x = map.xSize()-1;
			addTriangles(tris, sub.getPoint(x, y-1), sub.getPoint(x, y-1).set(Axis.XAxis, 0), sub.getPoint(x, y).set(Axis.XAxis, 0), sub.getPoint(x, y));
		}

		// retourne le solide avec les triangles
		return new Solide (tris);
	}
	
	
	
	
	public static void main (String[] args) {
		try {
			Cata boat = CataFileManager.getCataFile("D:/dev/GIT/Cata3D/Cata3D/data/OMCata.03022016.dbf");
			for (Composant comp : boat.composants) {
				if (comp.nom.equals("Safran")) {
					PatchComposant pv = (PatchComposant) comp;
					Solide sol = MapConversion.getSolide(pv.mapAffichage, pv.reflexive);
					STLFile f = new STLFile(1);
					f.createBinaryFile("D:/dev/GIT/Cata3D/Cata3D/data/safranBloc.stl", sol, pv.nom, Decimal.CENT);
					sol = MapConversion.getSolideVide(pv.mapAffichage, new Decimal (0.002d), pv.reflexive);
					f.createBinaryFile("D:/dev/GIT/Cata3D/Cata3D/data/safranVide.stl", sol, pv.nom, Decimal.CENT);
				}
			}
		} catch (CataAppliException e) {
			e.printStackTrace();
		} catch (InvalidGeomAction e) {
			e.printStackTrace();
		}
	}
}
