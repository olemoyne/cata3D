package model;

import java.awt.Color;
import java.util.ArrayList;

import model.composants.Composant;
import model.math.Vecteur;
import model.math.transfo.Transformation;
import view.scene.PrintableObject;
import view.scene.PrintableScene;
import view.scene.PrintedArea;
import view.scene.PrintedBlock;

public class CataScene {
	
	/**
	 * Affiche les composants d'un bateau sans autres �l�ments
	 * 
	 * @param boat
	 * @return
	 */
	public static PrintableScene getStandardScene(Cata boat) {
		if (boat == null) return new PrintableScene("");
		PrintableScene ret = new PrintableScene(boat.filename);

		
		// Affiche chaque composant
		for (Composant cmp : boat.composants) {
			for (PrintableObject obj : cmp.getSceneObjects(cmp.situation)) {
				ret.add(obj);
			}
		}
		return ret;
	}

	/**
	 * Affiche le bateau avec la mer
	 * 
	 * @param boat
	 * @return
	 */
	public static PrintableScene getNavigationScene(Cata bateau, boolean filled) {
		if (bateau == null) return new PrintableScene("");
		PrintableScene ret = new PrintableScene(bateau.filename);
		
		// Calcule la position de la mer 
//		Transformation mer = bateau.mer.getTransformation();
		
		// Affiche chaque composant
		for (Composant cmp : bateau.composants) {
			cmp.apparence.transparence = filled;
			for (PrintableObject obj : cmp.getSceneObjects(cmp.situation)) {
				ret.add(obj);
			}
		}
				
		Transformation trs = bateau.mer.getTransformation();
		// Affiche la mer ... Y = 0 
		ArrayList<Vecteur> mer = new ArrayList<Vecteur>();
		mer.add(trs.transforme(new Vecteur ("-8;0;-8")));
		mer.add(trs.transforme(new Vecteur ("8;0;-8")));
		mer.add(trs.transforme(new Vecteur ("8;0;8")));
		mer.add(trs.transforme(new Vecteur ("-8;0;8")));
		
		Area a = new Area();
		a.points.addAll(mer);
		
		Color sur = new Color(120, 150, 255);
		ret.add(new PrintedArea(a, "Mer dessus", true, true, sur, new Position()));
		// Seconde face -- mer vue du dessous
		mer = new ArrayList<Vecteur>();
		mer.add(trs.transforme(new Vecteur ("-8;0;-8")));
		mer.add(trs.transforme(new Vecteur ("-8;0;8")));
		mer.add(trs.transforme(new Vecteur ("8;0;8")));
		mer.add(trs.transforme(new Vecteur ("8;0;-8")));
		
		a = new Area();
		a.points.addAll(mer);
		
		Color sous = new Color(0, 0, 100);
		ret.add(new PrintedArea(a, "Mer dessous", true, true, sous, new Position()));
		
		return ret;
	}

	public static PrintableScene getHabitationScene(Cata bateau) {
		if (bateau == null) return new PrintableScene("");
		PrintableScene ret = getNavigationScene(bateau, false); //;new PrintableScene("Habitacle");
		
		Habitacle inside = bateau.habitacle;
		
		for (Compartiment comp : inside.compartiments) {
			ret.add(new PrintedBlock(comp.getPosition(), comp.getPosition().add(comp.getTaille()), comp.getNom(), true, comp.getCouleur(), new Position()));
		}
		
		return ret;
	}

}
