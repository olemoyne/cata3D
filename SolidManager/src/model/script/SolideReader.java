package model.script;

import java.awt.Color;
import java.util.ArrayList;

import model.solid.manage.Dessin;
import model.concepts.math.Decimal;
import model.concepts.math.InvalidGeomAction;
import model.concepts.math.Vecteur;
import model.concepts.solid.Patch;
import model.concepts.solid.Solide;
import model.concepts.solid.SolideMaker;
import model.concepts.math.Triangle;


/**
 * Outil de lecture de solide à partir des données en entrée
 * 
 * @author olemoyne
 *
 */
public class SolideReader {

	/**
	 * Convertie une chaine de caractères en solides
	 * 
	 * @return
	 */
	public static Solide readSolide(String data) throws InvalidGeomAction {
		// Forme : type -- PAVE / DISQUE / PATCH / PYRAMIDE / DISCRETE
		// Dimension : integer
		// vecteurs -> Séparateur -> :
		String infos[] = data.split(":");

		if (infos[0].equals("PAVE")) {
			if (infos.length < 4) throw new InvalidGeomAction("Description de solide invalide, trop courte : "+data);
			return SolideMaker.getPave(new Vecteur(infos[2]), new Vecteur(infos[3]));
		}

		if (infos[0].equals("PATCH")) {
			if (infos.length < 18) throw new InvalidGeomAction("Description de solide invalide, trop courte");
			int nb = Integer.parseInt(infos[1]);
			
			Vecteur[] pts = new Vecteur[16];
			for (int pos = 0; pos < 16; pos++) pts[pos] = new Vecteur(infos[pos+2]);
			Patch patch = new Patch(pts, nb);
			Solide pol = patch.calculeForme(nb);
			return pol;
		}

		if (infos[0].equals("DISQUE")) {
			if (infos.length < 4) throw new InvalidGeomAction("Description de solide invalide, trop courte");
			int nb = Integer.parseInt(infos[1]);
			return SolideMaker.getDisque(new Vecteur(infos[1]), new Vecteur(infos[2]), nb);
		}

		if (infos[0].equals("DISCRETE")) {
			if (infos.length < 4) throw new InvalidGeomAction("Description de solide invalide, trop courte");
			int nb = Integer.parseInt(infos[1]); // Nombre de triangles

			if (infos.length < nb*3+2) throw new InvalidGeomAction("Description de solide invalide, incohérence");
			
			ArrayList<Triangle> tris = new ArrayList<Triangle>(nb);
			for (int pos = 0; pos < nb; pos++) 
				tris.add(new Triangle(new Vecteur(infos[pos*3+2]), new Vecteur(infos[pos*3+3]), new Vecteur(infos[pos*3+4])));
			return new Solide (tris);
		}

		throw new InvalidGeomAction("Description de solide invalide, nom inconnu : "+infos[0]);

	}

	
	/**
	 * Convertie une chaine de caractères en composant 
	 * 
	 * @return
	 */
	public static Dessin readDessin(String data) throws InvalidGeomAction {
		int p = data.indexOf("{");
		if (p == -1) throw new InvalidGeomAction("Description de composant invalide, pas de solide");
		String infos[] = data.substring(0,  p).split(":");

		if (infos.length != 3) throw new InvalidGeomAction("Description de composant invalide, trop courte");
		
		Dessin comp = new Dessin();
		if (infos[0].contains(",")) {
			String[] cols = infos[0].split(",");
			Color col = new Color(Integer.parseInt(cols[0]), Integer.parseInt(cols[1]), Integer.parseInt(cols[2]));
			comp.color = col.getRGB();
		} else {
			comp.color = Integer.parseInt(infos[0]);
		}
		comp.densite = Decimal.parse(infos[1]);
		comp.matiere = infos[2];
		
		int f = data.indexOf("}", p+1);
		if (f != -1) {
			comp.forme = readSolide(data.substring(p+1, f));		
		} else {
			comp.forme = readSolide(data.substring(p+1));
		}
		
		return comp;
	}

	/**
	 * Test de fusion de patch
	 * 
	 * @param args
	 * @throws InvalidGeomAction
	 */
	public static void main(String[] args) throws InvalidGeomAction {
		
	}



}
