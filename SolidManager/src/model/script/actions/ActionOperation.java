package model.script.actions;

import java.awt.Color;
import java.util.ArrayList;

import model.concepts.math.InvalidGeomAction;
import model.solid.manage.Composant;
import model.solid.manage.Dessin;
import model.solid.manage.SolidPrintStream;
import model.viewer.view.Scene;
import model.concepts.solid.Bounds;
import model.concepts.solid.Solide;
import model.concepts.math.Triangle;
import model.script.ScriptAction;
import model.script.ScriptContext;
import model.script.TriangleSplitter;

/**
 * Fournis des boites � outils pour g�rer les op�rations d'op�ration binaire ente deux solides
 * 
 * @author olemoyne
 *
 */
public abstract class ActionOperation extends ScriptAction {
	
	/** fusion du solide 2 dans le solide 1 **/
	@Override
	public Dessin executeAction(ScriptContext dsn, String info, SolidPrintStream log) throws InvalidGeomAction {
		// source, target, plan 
		String [] data = info.split(":");
		if (data.length < 3) throw new InvalidGeomAction("Description d'action de fusion invalide");
		String nomFormeFrom = data[0];
		String nomFormeTo = data[1];
		String nomResultat = data[2];
		
		// R�cup�re la source 
		Dessin from = dsn.getDessin(nomFormeFrom);
		if (from == null) throw new InvalidGeomAction("Forme "+nomFormeFrom+" non définie dans le dessin");
		if (from.dessinType != Dessin.DESSIN_TYPE_SOLIDE) 
			throw new InvalidGeomAction("La forme à trancher n'est pas de type solide");

		// R�cup�re la destination
		Dessin to = dsn.getDessin(nomFormeTo);
		if (to == null) throw new InvalidGeomAction("Forme "+nomFormeTo+" non définie dans le dessin");
		if (to.dessinType != Dessin.DESSIN_TYPE_SOLIDE) 
			throw new InvalidGeomAction("La forme à trancher n'est pas de type solide");

		
		// D�coupe les deux solides en facettes d'intersection
		Solide sld = execOperation ((Solide)from.forme, (Solide)to.forme, log);
		
		Dessin dessin = new Dessin();
		dessin.forme = sld;
		dessin.color = from.color;
		dessin.densite = from.densite;
		dessin.dessinType = from.dessinType;
		dessin.matiere = from.matiere;
		
		dsn.setDessin(nomResultat,  dessin);
		return dessin;
	}

	/**
	 * Ex�cute l'op�ration
	 * 
	 * @param from
	 * @param to
	 * @return
	 */
	public abstract  Solide execOperation(Solide from, Solide to, SolidPrintStream log) throws InvalidGeomAction;
	
	protected ArrayList<Solide> splitAll (Solide from, Solide to, SolidPrintStream log) throws InvalidGeomAction {
		ArrayList<Solide> res = new ArrayList<Solide> ();

		boolean detailed = true;
		long start = System.currentTimeMillis();

		// D�coupe les triangles de to avec ceux de from
		// Si from est vide --> retourne to
		if ((from.getFacettes().size() == 0)||(to.getFacettes().size() == 0)) {
			res.add(to);
		} else {
			// Si to est vide --> retourne to
			int nb = 0;		
			Bounds bnds = new Bounds();
			bnds.setBounds(from);
			ArrayList<Triangle>  array = from.getFacettes();
			// Positionne la liste des o�prations
			for (Triangle tri : to.getFacettes()) {
				decoupe (tri, array, bnds, log);
				if (detailed) {
					nb += array.size();
					if (nb > 100000) {
						log.println("100k calculs : "+(System.currentTimeMillis() - start), SolidPrintStream.LEVEL_ALARM);
						nb = 0;
					}
				}
			}
			Scene sc = new Scene();
			for (Triangle t : array) {
				if (t.getInside() != -1) {
					sc.addForme(new Composant(t, "Découpeur", Color.RED));
				}
			}
			log.addScene(sc);

			log.println("        1° Solide : "+array.size()+" triangles", SolidPrintStream.LEVEL_ALARM);

			res.add(new Solide (array));
		}

		// D�coupe les triangles de from avec ceux de to
		// Si from ou to est vide --> retourne to
		if ((to.getFacettes().size() == 0)||(from.getFacettes().size() == 0)) {
			res.add(from);
		} else {
			Bounds bnds = new Bounds();
			bnds.setBounds(to);
			int nb = 0;
			ArrayList<Triangle> array = to.getFacettes();
			// Positionne la liste des o�prations
			for (Triangle tri : from.getFacettes()) {
				decoupe (tri, array, bnds, log);
				if (detailed) {
					nb += array.size();
					if (nb > 100000) {
						log.println("100k calculs : "+(System.currentTimeMillis() - start), SolidPrintStream.LEVEL_ALARM);
						nb = 0;
					}
				}
			}
			log.println("       2° Solide : "+array.size()+" triangles", SolidPrintStream.LEVEL_ALARM);
			
			res.add(new Solide (array));
		}
		
		// Récupération des 
		
		return res;
	}
	
	/**
	 * D�coupe le triangle en plusieurs elements de triangles
	 * 
	 * @param triangle
	 * @param other
	 * @param bounds
	 * @param ps
	 * @return
	 * @throws InvalidGeomAction
	 */
	public ArrayList<Triangle> decoupe(Triangle triangle, ArrayList<Triangle>other, Bounds bounds, SolidPrintStream ps) throws InvalidGeomAction {
		Bounds t = triangle.getBounds();
		if (t.intersecs(bounds)) {
			ArrayList<Triangle> tris = new ArrayList<Triangle>(); 
			boolean ok = TriangleSplitter.split(other, tris, triangle, ps);
			if (ok) {
				other.clear();
				other.addAll(tris);
			}
		} 
		return other;
	}

	public static Bounds getBounds(ArrayList<Triangle> tris) {
		Bounds bnds = new Bounds();
		for (Triangle t : tris) {bnds.setBounds(t);}
		return bnds;
	}
	
}
