package model.script.actions;

import java.awt.Color;
import java.util.ArrayList;

import model.concepts.math.InvalidGeomAction;
import model.concepts.math.Triangle;
import model.concepts.solid.Solide;
import model.concepts.math.Vecteur;
import model.solid.manage.Composant;
import model.solid.manage.SolidPrintStream;
import model.viewer.view.Scene;

public class ActionFusion extends ActionOperation {

	@Override
	public Solide execOperation(Solide sFrom, Solide sTo, SolidPrintStream log)
			throws InvalidGeomAction {
		
		// SI une des formes est vide --> retourne tous les triangles
		if (sFrom.size() == 0) {return sTo;}
		if (sTo.size() == 0) {return sFrom;}
		
		// Affichage du nombre de triangles et donc d'op�ration

		int nb = sFrom.size()* sTo.size();
		log.print("Split des formes : "+nb+" - ", SolidPrintStream.LEVEL_DEBUG);

		long start = System.currentTimeMillis(); 
		ArrayList<Solide> splitted = super.splitAll(sFrom, sTo, log);
		long end = System.currentTimeMillis(); 
		log.print(" to : "+splitted.get(0).size()+" - time = ", SolidPrintStream.LEVEL_DEBUG);

		Scene sc = new Scene();
		for (Triangle tri : splitted.get(0).getFacettes()) {
				sc.addForme(new Composant(tri, "Découpé", Color.YELLOW));
		}
/**		for (Triangle tri : splitted.get(1).getFacettes()) {
			sc.addForme(new Composant(tri, "Découpé", Color.WHITE));
     	}
**/		log.addScene(sc);

		log.println((end-start), SolidPrintStream.LEVEL_DEBUG);
		
		ArrayList<Solide> cleaned = this.dedoublonne(splitted.get(0).getFacettes(), splitted.get(1).getFacettes());

		ArrayList<Triangle>res = new ArrayList<Triangle>();
		res.addAll(cleaned.get(0).getFacettes());
		res.addAll(cleaned.get(1).getFacettes());

		ArrayList<Triangle>fus = new ArrayList<Triangle>();
		for (Triangle tri : res) {
			if (tri.getInside() != 1) fus.add(tri);
		}
		Solide sld = new Solide (fus);

		return sld;

/**		
		nb = cleaned.get(0).size()* sTo.size();
		nb += cleaned.get(1).size()* sFrom.size();
		log.println("Nettoyage : "+nb+" - ", SolidPrintStream.LEVEL_DEBUG);

		
		
		boolean detailed = false;
		int pos = 0;
		start = System.currentTimeMillis();
		// Netoyage du r�sultat
		Bounds bnds = new Bounds();
		bnds.setBounds(sTo);
		log.print("Nettoyage To avant : "+res.size(), SolidPrintStream.LEVEL_DEBUG);
		for (Triangle tri : cleaned.get(0).getFacettes()) {
			if (nettoieTriangle (tri, sTo, bnds)) 
				res.remove (tri);
			if (detailed) {
				pos += sTo.size();
				if (pos > 100000) {
					log.println("100k calculs : "+(System.currentTimeMillis() - start), SolidPrintStream.LEVEL_ALARM);
					pos = 0;
				}
			}
		}
		end = System.currentTimeMillis();
		log.print(" apr�s : "+res.size()+" - ", SolidPrintStream.LEVEL_DEBUG);
		log.println((end-start), SolidPrintStream.LEVEL_DEBUG);
		
		// Netoyage du r�sultat
		bnds = new Bounds();
		bnds.setBounds(sFrom);
		log.print("Nettoyage From avant : "+res.size(), SolidPrintStream.LEVEL_DEBUG);
		for (Triangle tri : cleaned.get(1).getFacettes()) {
			if (nettoieTriangle (tri, sFrom, bnds)) 
				res.remove (tri);
			if (detailed) {
				pos += sFrom.size();
				if (pos > 100000) {
					log.println("100k calculs : "+(System.currentTimeMillis() - start), SolidPrintStream.LEVEL_ALARM);
					pos = 0;
				}
			}
		}
		end = System.currentTimeMillis();
		log.print(" apr�s : "+res.size()+" - ", SolidPrintStream.LEVEL_DEBUG);
		log.println((end-start), SolidPrintStream.LEVEL_ALARM);

		Solide sld = new Solide (res);

		return sld;
**/	}

	
	/**
	 * Retourne true s'il est n�cessaire de nettoyer le triangle 
	 * 
	 * @param triangle
	 * @param other
	 * @param bnds
	 * @return
	 *
	private boolean nettoieTriangle (Triangle triangle, Solide other, Bounds bnds) {
		Vecteur[] pts = triangle.getSommets();
		// si un des points est � l'int�rieur du solide (pas contre une facette)
		boolean estDedans = false;
		for (Vecteur v : pts) {
			if (bnds.contains(v)) {
				if (other.estDedans(v, false)) estDedans = true;
			} 
		}
		return (estDedans);
	}
*/	
	protected ArrayList<Solide> dedoublonne (ArrayList<Triangle> me, ArrayList<Triangle> other) {
		ArrayList<Solide> ret = new ArrayList<Solide>();
		ArrayList<Triangle> myTrisToRemove = new ArrayList<Triangle>(); 
		ArrayList<Triangle> hisTrisToRemove = new ArrayList<Triangle>();
		
		for (Triangle triangle : me) {
			Vecteur norme = triangle.getNorme();
			for (Triangle o: other) {
				if (o.equals(triangle)) {
					if (o.getNorme().equals(norme)) {
						// -> si coll�s dans le m�me sens -> 1 seul � garder
						hisTrisToRemove.add(o);
					} else {
						// -> si coll�s dasn le sens inverse -> on supprime tout
						hisTrisToRemove.add(o);
						myTrisToRemove.add(triangle);
					}
				}
			}
		}
				
		ArrayList<Triangle> myTris= new ArrayList<Triangle>(me); 
		myTris.removeAll(myTrisToRemove);
		ArrayList<Triangle> hisTris = new ArrayList<Triangle>(other);
		myTris.removeAll(hisTrisToRemove);
		
		
		ret.add(new Solide(myTris));
		ret.add(new Solide(hisTris));
		return ret;
	}


}
