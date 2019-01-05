package model.calcul;

import java.awt.Color;
import java.util.ArrayList;

import model.Area;
import model.Gabarit;
import model.Structure;
import model.composants.PatchVide;
import model.math.Axis;
import model.math.Bounds;
import model.math.Bounds3D;
import model.math.Decimal;
import model.math.MapDeVecteurs;
import model.math.Plan3D;
import model.math.Segment;
import model.math.Vecteur;
import model.math.transfo.Rotation;
import model.patch.Patch;
import view.view2D.PlanScene;
import view.view2D.PrintedGabarit;
import view.view2D.PrintedQuille;

public class CalculPlan {

	// Transformation pour extruder --> Z = 0
	public static Rotation rot = new Rotation(Axis.YAxis, new Decimal(-90), null);
	public static Rotation reverse = (Rotation) new Rotation(Axis.YAxis, new Decimal(-90), null).getReverse(null);

	
	public static class Intersection {
		public int indiceA;  // indice du premier point du segment de l'aire A
		public int indiceB;  // indice du premier point du segment de l'aire B
		public Segment inter;
		
		public Intersection (int a, int b, Segment s) {
			indiceA = a;
			indiceB = b;
			inter = s;
		}
	}
	
	/**
	 * Calcule les plans d'un patch vide : 
	 *     - dessin des gabarits
	 *     - dessin de la quille
	 *     
	 * @param boat
	 */
	public static PlanScene calculePlans (PatchVide boat, Plan3D mer) {
		PlanScene scene = new PlanScene(boat, mer);
		//
		if (boat == null) return scene;
		
		if (boat.structure.gabarits == null) return scene;
		if (boat.structure.gabarits.size() == 0) return scene;
		
		for (Gabarit g :boat.structure.gabarits) {
			PrintedGabarit pg = new PrintedGabarit(g, boat, mer, g.toString(), Color.YELLOW);
			scene.add(pg);
		}
		PrintedQuille pq = new PrintedQuille(boat.structure.quille, boat, Color.BLUE);
		
		scene.add(pq);
				
		return scene;
	}
	
	
	
	public static Segment setMer(Bounds bns, Plan3D pl) {
		Vecteur s = bns.getMin();
		Vecteur e = new Vecteur (bns.getMin().getDecX(), bns.getMax().getDecY(), bns.getMin().getDecZ());
		Vecteur mstart = pl.intersection(e,  s);

		s = new Vecteur (bns.getMax().getDecX(), bns.getMin().getDecY(), bns.getMin().getDecZ());
		e = new Vecteur (bns.getMax().getDecX(), bns.getMax().getDecY(), bns.getMin().getDecZ());
		Vecteur mend= pl.intersection(e,  s);

		Segment mer = new Segment (mstart, mend);
		return mer;
	}

	/**
	 * Calcule la zone d'un gabarit en fonction d'une map, d'une position en Z et d'une épaisseur 
	 * 
	 * @param mapAffichage
	 * @param i
	 * @param ep
	 * @return
	 */
	public static Area getMaximalIntersection(MapDeVecteurs map, Decimal i, Decimal ep) {
		// Calcule les deux plans d'intersection
		Plan3D pli = Plan3D.getPlan(Axis.ZAxis, i);
		Area fi = map.intersectionHorizontaleZ(pli);

		Plan3D plj = Plan3D.getPlan(Axis.ZAxis, i.add(ep));
		Area fj = map.intersectionHorizontaleZ(plj);

		if (fi.size() == 0) return fj;
		if (fj.size() == 0) return fi;
		
		// Calcule le max des deux plans
		// Récupère la liste des intersections
		ArrayList<Intersection> inters = new ArrayList<Intersection>();		
		for (int f = 0; f < fi.size(); f ++) {
			int lf = f-1;
			if (f == 0) lf = fi.size() -1;
			Segment sf = new Segment(fi.points.get(lf), fi.points.get(f));
			for (int j = 0; j < fj.size(); j ++) {
				int lj = j-1;
				if (j == 0) lj = fj.size() -1;
				Segment sj = new Segment(fj.points.get(lj), fj.points.get(j));
				
				Segment seg = sj.intersection(sf);
				if (seg != null) { // Pas d'intersection
					Intersection inter = new Intersection(f, j, seg);
					inters.add(inter);
				}
			}
		}
		// Parcours la liste des points pour determiner s'il faut les conserver ou pas
		// Commence par le premier point de croisement
		ArrayList<Vecteur> liste = new ArrayList<Vecteur>();
		int posi = 0;
		int posj = 0;
		// détermine qu'elle est la forme à l'extérieur
		boolean iIsUp = !fj.isInside(fi.points.get(0));
		// Pour chaque intersection
		for (Intersection inter : inters) {
			// Ajoute les points de la forme à l'extérieur
			if (iIsUp) {
				for (;posi < inter.indiceA; posi ++) 
					liste.add(fi.points.get(posi));
				posj  = inter.indiceB;
			} else {
				for (;posj < inter.indiceB; posj ++) 
					liste.add(fj.points.get(posj));				
				posi  = inter.indiceA;
			}
			// Ajoute les points d'intersection
			liste.add(inter.inter.getA());
			if (!inter.inter.getA().equals(inter.inter.getB())) {
				liste.add(inter.inter.getB());
			}
			// Détermine la poisiton pour le segment suivant
			iIsUp = !fj.isInside(fi.points.get(posi));
		}
		// termine la boucle
		if (iIsUp) {
			for (;posi < fi.size(); posi ++) 
				liste.add(fi.points.get(posi));
		} else {
			for (;posj < fj.size(); posj ++) 
				liste.add(fj.points.get(posj));				
		}
		
		return new Area(liste);
	}

	
	/**
	 * Calcule la forme du gabarit en gérant le trou de la quille
	 * 
	 * 
	 * @param fullInside
	 * @param epaisseurQuille
	 * @param position
	 */
	public static Area manageQuille(Area tour, Decimal epaisseurQuille, Decimal profQuille,  Decimal position) {
		
		// Parcours les points de l'Area pour déterminer les éléments de la quille
		Decimal epq = epaisseurQuille.divide(Decimal.DEUX);
		Decimal prof = profQuille.divide(Decimal.DEUX);
		
		Decimal bas =tour.getBounds().getMin().getDecY(); 
		
		Area quille = new Area();
		quille.points.add(new Vecteur(epq.negate(), bas.minus(Decimal.UN), position));
		quille.points.add(new Vecteur(epq.negate(), bas.add(prof), position));
		quille.points.add(new Vecteur(epq, bas.add(prof), position));
		quille.points.add(new Vecteur(epq, bas.minus(Decimal.UN), position));
		
		Area extrusion = CalculFormes.getExtrusion(tour, quille, position);
		tour.points.clear();
		tour.points.addAll(extrusion.points);
		
		return quille;
	}

	/**
	 * Identifie les points des extrémités de la quille en fonction du patch
	 * 
	 * @param patch
	 * @return
	 */
	public static Segment getExtermites(Patch patch) {
		if (patch == null) return new Segment();
		
		// Dans le patch on récupère les points du premier niveau
		Vecteur pt1 = patch.points[0][patch.x-1];
		Vecteur pt2 = patch.points[patch.x-1][patch.y-1];
		
		return new Segment(pt1, pt2);
	}

	/** Extruction de la structure de la quille 
	 * 
	 * @param surface
	 * @param structure
	 * @param epaisseur
	 * @param profondeur
	 * @return
	 */
	public static Area extrudeStructure(Area surface, Decimal pointBas, Structure structure, Decimal epaisseur, Decimal profondeur) {
		Area ret = new Area(surface.points);
		Decimal limiteY = profondeur.add(pointBas);
		
		/** Sanaty checks **/
		if (structure== null) return ret;
		
		if (structure.gabarits == null) return ret;
		if (structure.gabarits.size() == 0) return ret;

		Bounds3D bnds = surface.getBounds(); 

		// Transformation pour extrusion
		Area retour = surface.transform(CalculPlan.rot);

		// Extrusion du tableau
		for (Gabarit g : structure.gabarits) {
			if (g.getPos() == 0) {
				Area extrude = new Area();
				extrude.points.add(new Vecteur (Decimal.ZERO, bnds.getMin().getDecY().minus(Decimal.UN), Decimal.UN.negate())); 
				extrude.points.add(new Vecteur (Decimal.ZERO, bnds.getMin().getDecY().minus(Decimal.UN), g.epaisseur)); 
				extrude.points.add(new Vecteur (Decimal.ZERO, bnds.getMax().getDecY().add(Decimal.UN), g.epaisseur)); 
				extrude.points.add(new Vecteur (Decimal.ZERO, bnds.getMax().getDecY().add(Decimal.UN), Decimal.UN.negate())); 
				// Soustrait l'encoche à la surface
				retour = CalculFormes.getExtrusion(retour, extrude.transform(CalculPlan.rot), Decimal.ZERO);	
			}
		}
		
		
		
		// Traitement de la partie extérieur : détermination des limites
		Decimal firstZLimit = new Decimal(20000); 
		Decimal lastZLimit = new Decimal(-20000);
		for (Gabarit g : structure.gabarits) {
			if (g.getPos() != 0) {
				if (g.position.compareTo(firstZLimit) < 0) {
					firstZLimit = g.position;
				}
				if (g.position.compareTo(lastZLimit) > 0) {
					lastZLimit = g.position.add(g.epaisseur);
				}
				// Construction de la liste des encoches
				Decimal posEncoche = g.getEncoche().getBounds().getMax().getDecY();
				// Calcul de la taille
				Area encoche = new Area();
				encoche.points.add(new Vecteur(Decimal.ZERO, limiteY.add(Decimal.UN), g.position)); 
				encoche.points.add(new Vecteur(Decimal.ZERO, posEncoche, g.position)); 
				encoche.points.add(new Vecteur(Decimal.ZERO, posEncoche, g.position.add(g.epaisseur))); 
				encoche.points.add(new Vecteur(Decimal.ZERO, limiteY.add(Decimal.UN), g.position.add(g.epaisseur))); 
				// Soustrait l'encoche à la surface
				retour = CalculFormes.getExtrusion(retour, encoche.transform(CalculPlan.rot), Decimal.ZERO);	
			}

		}
		
		Area exclusion = new Area();
		exclusion.points.add(new Vecteur(Decimal.ZERO, bnds.getMax().getDecY().add(Decimal.UN), firstZLimit)); 
		exclusion.points.add(new Vecteur(Decimal.ZERO, limiteY, firstZLimit)); 
		exclusion.points.add(new Vecteur(Decimal.ZERO, limiteY, lastZLimit)); 
		exclusion.points.add(new Vecteur(Decimal.ZERO, bnds.getMax().getDecY().add(Decimal.UN), lastZLimit)); 
		retour = CalculFormes.getExtrusion(retour, exclusion.transform(CalculPlan.rot), Decimal.ZERO);	

		ret = retour.transform(CalculPlan.reverse);

		return ret;
	}

	
}
