package model.concepts.btree;

import java.util.ArrayList;

import model.concepts.math.Triangle;
import model.concepts.math.Vecteur;
import model.concepts.solid.Bounds;

/**
 * Noeud de gestion des arbres binaires
 * 
 * @author olemoyne
 *
 */
public class SolidBinaryNode {

	private static final int NB_MAX_TRIANGLES = 10;

	/**
	 * Zone du noeud
	 */
	private Bounds bnds;
	
	/**
	 * Liste des triangles de la zone - vide si le noeud poss�de des sous-noeuds
	 */
	private ArrayList<Triangle> trianglesDeLaZone;
	
	/**
	 * Liste des noeuds d�finissant 8 sous-zones
	 */
	private ArrayList<SolidBinaryNode> subs;
	
	private boolean estSubdivise;
	
	/**
	 * Cr�ation de l'arbre avec une liste de triangles
	 * 
	 * @param tris
	 */
	public SolidBinaryNode (ArrayList<Triangle> tris, Bounds area) {
		trianglesDeLaZone = tris;
		bnds = area;
		
		subs = new ArrayList<SolidBinaryNode> ();
		estSubdivise = false;
	}
	
	/**
	 *  Ajoute un triangle dans la zone
	 *    TODO : Comment g�rer un triangle qui est compl�tement au-dessus du bounds ?
	 *  
	 * @throws SolidBinaryTreeException
	 */
	public void addTriangle (Triangle tr) throws SolidBinaryTreeException {
		if (!bnds.contains (tr)) throw new SolidBinaryTreeException("Le triangle "+tr.toString()+" n'est pas dans la zone");
		
		if (!estSubdivise) {
			this.trianglesDeLaZone.add(tr);
		} else {
			// Ajoute le triangle dans la/les zones 
			for (SolidBinaryNode sub : subs) {
				if (sub.bnds.contains(tr)) sub.addTriangle(tr);
			}
		}
	}
	
	/**
	 * D�coupe la zone en 8 parties et r�partie les diff�rents triangles
	 * 
	 * @throws SolidBinaryTreeException
	 */
	public void SplitNode () throws SolidBinaryTreeException {
		if (estSubdivise) return;
		
		// D�termine le point central de la zone
		Vecteur centre = getCentre();
		
		//A partir du centre, d�finit une liste de 8 sous-zones
		Bounds[] zones = bnds.splitZone(centre);
		
		for (Bounds bnd: zones) {
			ArrayList<Triangle> tris = new ArrayList<Triangle>();
			for (Triangle tr : this.trianglesDeLaZone) {
				if (bnd.contains(tr)) tris.add(tr);
			}
 			this.subs.add(new SolidBinaryNode(tris, bnd));
		}
		
		// Controle si une des zones contient autant de triangles que moi j'arr�te la dychotomie
		boolean isOk = true;
		int mySize = this.trianglesDeLaZone.size();
		for (SolidBinaryNode sub : subs) {
			if (sub.trianglesDeLaZone.size() == mySize) isOk = false;
		}
		
		if (!isOk) {
			subs.clear();
		} else {
			// Ca y est, mon noeud est subdivis�
			trianglesDeLaZone.clear();
			estSubdivise = true;
			
			// Recursivit�
			for (SolidBinaryNode sub : subs) {
				if (sub.trianglesDeLaZone.size() > NB_MAX_TRIANGLES) sub.SplitNode();
			}
		}
	}

	/** 
	 * D�termine le centre de la zone
	 * 
	 * @return
	 */
	private Vecteur getCentre() {
		long x = 0, y = 0, z = 0;
		int nb = 0;
		// Pour chaque triangle, d�termine le centre de tous les points
		for (Triangle tr : this.trianglesDeLaZone) {
			for (Vecteur pt : tr.getSommets()) {
				if (this.bnds.contains(pt)) {
					x+= pt.getX();
					y+= pt.getY();
					z+= pt.getZ();
					
					nb ++;
				}
			}
		}
		return new Vecteur (x/nb, y/nb, z/nb);
	}


	/** Affiche les donn�es de la zone
	 * 
	 * @param sb
	 * @param i
	 */
	public void toString(StringBuilder sb, int level) {
		for (int i = 0; i <level; i++) sb.append(" ");
		
		sb.append(bnds.toString());
		sb.append(" ");
		sb.append(trianglesDeLaZone.size());
		sb.append("-");
		sb.append(subs.size());
		sb.append("\n");
		
		for (SolidBinaryNode sub : subs) sub.toString (sb, level+1);
	}
	
	
	
}
