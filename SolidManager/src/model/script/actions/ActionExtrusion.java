package model.script.actions;

import java.util.ArrayList;

import model.concepts.math.InvalidGeomAction;
import model.concepts.math.Triangle;
import model.concepts.solid.Bounds;
import model.concepts.solid.Solide;
import model.concepts.math.Vecteur;
import model.solid.manage.SolidPrintStream;

public class ActionExtrusion extends ActionOperation {

	@Override
	public Solide execOperation(Solide sFrom, Solide sTo, SolidPrintStream log)
			throws InvalidGeomAction {
		
		ArrayList<Solide> splitted = super.splitAll(sFrom, sTo, log);

		ArrayList<Solide> cleaned = this.dedoublonne(splitted.get(0).getFacettes(), splitted.get(1).getFacettes());

		ArrayList<Triangle>res = new ArrayList<Triangle>();
		res.addAll(cleaned.get(0).getFacettes());
		res.addAll(cleaned.get(1).getFacettes());

		// Netoyage du r�sultat
		Bounds bnds = new Bounds();
		bnds.setBounds(sTo);
		for (Triangle tri : cleaned.get(0).getFacettes()) {
			if (nettoieTriangle (tri, sTo, bnds)) 
				res.remove (tri);
		}
		
		// Netoyage du r�sultat
		bnds = new Bounds();
		bnds.setBounds(sFrom);
		for (Triangle tri : cleaned.get(1).getFacettes()) {
			if (nettoieTriangle (tri, sFrom, bnds)) 
				res.remove (tri);
		}

		Solide sld = new Solide (res);

		return sld;
	}

	
	/**
	 * Retourne true s'il est n�cessaire de nettoyer le triangle 
	 * 
	 * @param triangle
	 * @param other
	 * @param bnds
	 * @return
	 */
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
	
	protected ArrayList<Solide> dedoublonne (ArrayList<Triangle> me, ArrayList<Triangle> other) {
		ArrayList<Solide> ret = new ArrayList<Solide>();
		ArrayList<Triangle> myTrisToRemove = new ArrayList<Triangle>(); 
		ArrayList<Triangle> hisTrisToRemove = new ArrayList<Triangle>();
		
		for (Triangle triangle : me) {
			Vecteur norme = triangle.getNorme();
			for (Triangle o: other) {
				if (o.equals(triangle)) {
					if (o.getNorme().equals(norme)) {
						// -> si coll�s dans le m�me sens -> on supprime tout
						hisTrisToRemove.add(o);
						myTrisToRemove.add(triangle);
					} else {
						// -> si coll�s dasn le sens inverse -> 1 seul � garder
						hisTrisToRemove.add(o);
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
