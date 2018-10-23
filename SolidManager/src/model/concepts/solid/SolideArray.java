package model.concepts.solid;

import java.util.ArrayList;

import model.concepts.math.Decimal;
import model.concepts.math.InvalidGeomAction;
import model.concepts.math.Plan3D;
import model.concepts.math.Triangle;
import model.concepts.math.Vecteur;
import model.script.TriangleSplitter;
import model.solid.manage.SolidPrintStream;
import tarpeia.com.model.TarpeiaField;
import tarpeia.com.model.TarpeiaObject;

/**
 * Repr�sentation d'un solide comme �tant une liste de triangles orient�s d�finissant les facettes du solide
 * 
 * Le solide est stock� comme une liste de points + liste de triangles (r�f�ren�ant le point) 
 * 
 * @author olemoyne
 *
 */
@TarpeiaObject
public class SolideArray {
	
	/**
	 * 
	 * 
	 */
	@TarpeiaField
	private ArrayList<Vecteur> vecteurs;

	@TarpeiaField
	private ArrayList<SolidTriangle> triangles;
	
	/** Bounds of the solid  **/
	private Bounds bounds;
	
	/**
	 * Cr�ation par d�faut
	 */
	public SolideArray () {
		vecteurs = new ArrayList<Vecteur> ();
		triangles = new ArrayList<SolidTriangle> ();
		bounds = new Bounds();
	}
	
	
	/**
	 * Cr�ation � partir d'une liste de triangles
	 */
	public SolideArray (ArrayList<Triangle> tri) {
		vecteurs = new ArrayList<Vecteur> ();
		triangles = new ArrayList<SolidTriangle> ();
		
		//** Enregistre les éléments des triangles **/
		for (Triangle tr : tri) {
			Vecteur[] lst = tr.getSommets();
			int pa = vecteurs.indexOf(lst[0]);
			if (pa == -1) { pa = vecteurs.size(); vecteurs.add(lst[0]);} 
			int pb = vecteurs.indexOf(lst[1]);
			if (pb == -1) { pb = vecteurs.size(); vecteurs.add(lst[1]);} 
			int pc = vecteurs.indexOf(lst[2]);
			if (pc == -1) { pc = vecteurs.size(); vecteurs.add(lst[2]);}
			SolidTriangle st = new SolidTriangle(pa, pb, pc); 
			triangles.add(st);
		}
		bounds = new Bounds();
		bounds.setBounds(vecteurs);
	}

	
	/**
	 * Détermine si un point est à l'intérieur du solide
	 * vérifie l'enveloppe du solide 
	 * Parcours la liste des triangle pour déterminer la position du point par rapport au solide
	 * @param v
	 * @return
	 */
	public boolean estDedans (Vecteur v){
		if (!bounds.contains(v)) return false;
		
		/* Parcours les triangles */
		for (SolidTriangle slt : triangles) {
			Plan3D pl = new Plan3D(vecteurs.get(slt.A), vecteurs.get(slt.B), vecteurs.get(slt.C));
			if (pl.estDessus(v) < 0) return false;
		}
		return true;
	}
	

	/**
	 * Détermine quels sont les points d'un solide à l'intérieur du solide
	 * vérifie l'enveloppe du solide 
     *
	 * @param v
	 * @return
	 */
	public ArrayList<Integer> estDedans (SolideArray sld){
		if (!bounds.intersecs(sld.getBounds())) return new ArrayList<Integer>();
		
		ArrayList<Integer> res = new ArrayList<Integer>();
		int pos = 0;
		for (Vecteur ptr : sld.vecteurs) {
			if (this.estDedans(ptr)) res.add(pos);
		}
		return res; 
	}
	

	
	/**
	 * Retourne le centre du solide
	 * 
	 * @return
	 */
	public Vecteur getCentre () {
		long x = 0;
		long y = 0;
		long z = 0;
		
		int nb = 0;
		for (Vecteur v : this.getSommets()) {
			x = x+v.getX(); y = y+v.getY(); z = z+v.getZ();
			nb ++;
		}
		
		Decimal div = new Decimal(nb);
		return new Vecteur (new Decimal(x).divide(div).divide(Vecteur.METER), new Decimal(y).divide(div).divide(Vecteur.METER), new Decimal(z).divide(div).divide(Vecteur.METER));
	}

	/**
	 * Retourne la liste des sommets du solide 
	 * 
	 * @return
	 */
	public ArrayList<Vecteur> getSommets() {
		return vecteurs;
	}
	
	/**
	 * retourne le nombre de facettes
	 * @return
	 */
	public int size() {
		return this.triangles.size();
	}


	public Bounds getBounds() {
		return bounds;
	}


	/*
	 * *
	 */
	public Solide getSolid () throws InvalidGeomAction {
		ArrayList<Triangle> tris = new ArrayList<Triangle>();
		for (SolidTriangle st : this.triangles) {
			Triangle tr = new Triangle (vecteurs.get(st.A), vecteurs.get(st.B), vecteurs.get(st.C));
			tris.add(tr);
		}
		return new Solide(tris);
	}
	
	
	public Bounds getBounds(SolidTriangle s) {
		Bounds bnds = new Bounds();
		bnds.setBounds(vecteurs.get(s.A));
		bnds.setBounds(vecteurs.get(s.B));
		bnds.setBounds(vecteurs.get(s.C));
		return bnds;
	}
	
	
	/**
	 * Retourne le solide splitté avec les éléments en contact avec ce solide
	 * 
	 * @param sol
	 * @return
	 * @throws InvalidGeomAction 
	 */
	public SolideArray splitSolid(SolideArray sol, SolidPrintStream ps) throws InvalidGeomAction {
		ArrayList<Integer> insiders  = this.estDedans(sol);
		if (insiders == null) return null;
		/* il y a des points à l'intérieur */ 
		ArrayList<SolidTriangle> totallyInside = new ArrayList<SolidTriangle>();
		ArrayList<SolidTriangle> borders = new ArrayList<SolidTriangle>();
		ArrayList<SolidTriangle> outside = new ArrayList<SolidTriangle>();
		for (SolidTriangle tr : sol.triangles) {
			int pos = 0;
			if (insiders.contains(tr.A)) pos ++;
			if (insiders.contains(tr.B)) pos ++;
			if (insiders.contains(tr.C)) pos ++;
			if (pos == 3) totallyInside.add(tr);
			else {
				if (pos == 0) outside.add(tr);
				else borders.add(tr);
			}
		}
		
		//découpe les triangles 
		ArrayList<Triangle> tris = new ArrayList<Triangle>();
		for (SolidTriangle st : this.triangles) {
			Triangle tr = new Triangle (vecteurs.get(st.A), vecteurs.get(st.B), vecteurs.get(st.C));
			tris.add(tr);
		}

		ArrayList<Triangle> result = new ArrayList<Triangle>(); 
		for (SolidTriangle brti : borders) {
			Bounds bdns = sol.getBounds(brti);
			for (Triangle cutter : tris) {
				Bounds bt = new Bounds();
				bt.setBounds(cutter);
				if (bt.intersecs(bdns)) {
					ArrayList<Triangle> splitted = TriangleSplitter.splitTriangle(cutter, sol.getTriangle(brti), ps);
					result.addAll(splitted);
				}
			}
		}
		// TODO : Gérer les résultats sous la forme d'un solide --> Ajoute les points au solide
	}


	private Triangle getTriangle(SolidTriangle tr) throws InvalidGeomAction {
		return new Triangle (vecteurs.get(tr.A), vecteurs.get(tr.B), vecteurs.get(tr.C));
	}
	
}
