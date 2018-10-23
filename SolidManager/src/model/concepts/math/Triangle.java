package model.concepts.math;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

import model.concepts.solid.Bounds;
import model.concepts.transfo.Transformation;
import tarpeia.com.model.TarpeiaField;
import tarpeia.com.model.TarpeiaObject;

/**
 * Facette de solide d�finie comme �tant une liste de 3 points et d'une norme (de taille 1) d�finissant la direction vers l'ext�rieur
 * 
 * @author olemoyne
 *
 */
@TarpeiaObject
public class Triangle {
	
	@TarpeiaField
	private Vecteur A;
	@TarpeiaField
	private Vecteur B;
	@TarpeiaField
	private Vecteur C;
	
	@TarpeiaField
	private Vecteur norme;
	
	private Plan3D plan;
	
	/** 0 = Not defined, 1 : oui, -1 : non **/
	private int isInside;

	/**
	 * Fonction de creation de triangle vide par d�faut
	 */
	public Triangle () {
		plan = null;
		isInside = 0;
	}
	
	public int getInside () {
		return isInside;
	}
	
	public void setInside (int v) {
		isInside = v;
	}	

	public Triangle (Triangle t) throws InvalidGeomAction {
		A = t.A;
		B = t.B;
		C = t.C;
		// Calcul de la norme
		Vecteur u = A.minus(B);
		Vecteur v = C.minus(B);
			
		norme  = u.produitVectoriel(v);				
		Decimal dec = norme.getNorme();
		if (!dec.isZero()) 
			norme = norme.multiply(dec.inverse());
		else {
			throw new InvalidGeomAction("Norme du trinagle est nulle");
		}
		
		// Nom du plan
		plan = new Plan3D (A, B, C);
		isInside = 0;
	}
	
	/**
	 * Fonction de creation de triangle � partir de 3 vecteurs orient�s (ordre des aiguilles d'une montre, vu de l'ext�rieur)
	 * 
	 * @param a
	 * @param b
	 * @param c
	 * @throws InvalidGeomAction
	 */
	public Triangle (Vecteur a, Vecteur b, Vecteur c) throws InvalidGeomAction {
		A = a;
		B = b;
		C = c;
		// V�rifications que les vecteurs sont distincts 
		if (A.equals(B)) throw new InvalidGeomAction("Triangle invalide, deux points indentiques");
		if (A.equals(B)) throw new InvalidGeomAction("Triangle invalide, deux points indentiques");
		// V�rifications que les vecteurs sont non-colin�aires
		if (B.minus(A).estColineaire(B.minus(C))) throw new InvalidGeomAction("Triangle invalide, triangle plat "+a.toString()+" "+b.toString()+" "+c.toString());
		
		// Calcul de la norme
		Vecteur u = A.minus(B);
		Vecteur v = C.minus(B);
			
		norme  = u.produitVectoriel(v);				
		Decimal dec = norme.getNorme();
		if (!dec.isZero()) 
			norme = norme.multiply(dec.inverse());
		else {
			throw new InvalidGeomAction("Norme du trinagle est nulle");
		}
		
		// Nom du plan
		plan = new Plan3D (A, B, C);
		isInside = 0;
	}

	/**
	 * Creation d'un triangle � partir d'une chaine
	 *   (0,0.2338,-2.011);(0,0.2338,0);(0,0,0)
	 * @param pts
	 * @throws InvalidGeomAction 
	 */
	public static Triangle getTriangle (String str) throws InvalidGeomAction {
		String[] data = str.split(";");
		if (data.length == 3) {
			// g�n�re les points
			Vecteur[] pts = new Vecteur[3];
			int i = 0;
			for (String pt : data) {
				int start = pt.indexOf('(');
				int end = pt.indexOf(')');
				pts[i] = new Vecteur(pt.substring(start+1, end).replace(',', ';'));
				i ++;
			}
			return Triangle.getTriangle(pts);			
		}
		throw new InvalidGeomAction("Triangle invalide, Nombre de points d�finis incorrects, "+str);
	}


	/**
	 * Creation d'un triangle � partir d'un tableau
	 * @param pts
	 * @throws InvalidGeomAction 
	 */
	public static Triangle getTriangle (Vecteur[] pts) throws InvalidGeomAction {		
		if (pts.length == 3)
			return new Triangle (pts[0], pts[1], pts[2]);

		throw new InvalidGeomAction("Triangle invalide, Nombre de points d�finis incorrects");
	}

	/**
	 * Donne le plan 3D dans lequel est inscrit le triangle
	 * 
	 */
	public Plan3D getPlan() {
		if (plan == null) plan = new Plan3D (A, B, C);
		return plan;
	}
	
	/**
	 * G�n�re les donn�es binaires d'un vecteur
	 * 
	 * @param v
	 * @param buffer
	 */
	private void printVecteur (Vecteur v, ByteBuffer buffer) {
		buffer.putFloat(v.getX());
		buffer.putFloat(v.getY());
		buffer.putFloat(v.getZ());
	}
	/**
	 * Retourne le triangle exprim� sous la forme de donn�es STL
	 *  
	 * @return
	 */
	public byte[] getSTLData (Decimal mul) {
		ByteBuffer buffer = ByteBuffer.allocate(50);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		printVecteur(this.norme.multiply(mul), buffer);
		printVecteur(this.A.multiply(mul), buffer);
		printVecteur(this.B.multiply(mul), buffer);
		printVecteur(this.C.multiply(mul), buffer);
		buffer.putShort((short) 0);
		return buffer.array();
	}
	
	/**
	 * D�termine si un point est � l'int�rieur du triangle
	 * 
	 * (vect(AB) ^ vect(AM)) . (vect(AM) ^ vect(AC)) >= 0
	 * (vect(BA) ^ vect(BM)) . (vect(BM) ^ vect(BC)) >= 0
	 * (vect(CA) ^ vect(CM)) . (vect(CM) ^ vect(CB)) >= 0
	 *
	 *   o� ^ d�signe le produit vectoriel de deux vecteurs
	 *		. d�signe le produit scalaire
	 *
	 * @param v
	 * @return
	 */
	public boolean contient (Vecteur M) {
		if (!this.getPlan().contains(M)) return false;
		
		Vecteur d1 = B.minus(A).produitVectoriel(M.minus(A)); 
		Vecteur d2 = M.minus(A).produitVectoriel(C.minus(A));
		
		Decimal d = d1.getProduitScalaire(d2);
		if (d.isZero()) return true;
		if (!d.isPositive()) return false;

		d = A.minus(B).produitVectoriel(M.minus(B)).getProduitScalaire(M.minus(B).produitVectoriel(C.minus(B)));
		if (d.isZero()) return true;
		if (!d.isPositive()) return false;

		d = A.minus(C).produitVectoriel(M.minus(C)).getProduitScalaire(M.minus(C).produitVectoriel(B.minus(C)));
		if (d.isZero()) return true;
		if (!d.isPositive()) return false;

		return true;
	}

	/** 
	 * Retourne la liste des sommets du triangle
	 * 
	 * @return
	 */
	public Vecteur[] getSommets() {
		return  new Vecteur[] {A, B, C};
	}
	
	/**
	 * Calcule la surface du triangle
	 * 
	 * @return
	 */
	public Decimal getSurface() {
		return Vecteur.calculeSurface(B, A, C);
	}

	/**
	 * Calcule la distance d'un vecteur avec le plan d�termin� par le triangle
	 * 
	 * @param ctr
	 * @return
	 */
	public Decimal distanceOrtho(Vecteur pt) {
		if (this.contient(pt)) return Decimal.ZERO;
		// Il faut utiliser la projection orthogonale !!
		return this.getPlan().distance(pt);
//		Decimal dec = plan.distance(ctr);
	}

	/**
	 * Calcule la distance d'un vecteur avec un des points du triangle
	 * 
	 * @param ctr
	 * @return
	 */
	public Decimal distance(Vecteur pt) {
		if (this.contient(pt)) return Decimal.ZERO;
		
		// retourne la distance entre le point et le cente du triangle
		Vecteur ctr = new Vecteur((A.getX()+B.getX()+C.getX())/3, (A.getY()+B.getY()+C.getY())/3, (A.getZ()+B.getZ()+C.getZ())/3);
		return pt.distance(ctr);
/*		
		// Si la projection est dans le triangle
		Vecteur proj = getPlan().projection(pt);
		if (this.contient(proj)) return proj.distance(pt);
		
		// Sinon, il faut d�terminer le point du triangle le plus proche
		// Pour chaque point du triangle d�terminer la distance avec la projection / intersection avec chaque segment
		Segment s = new Segment (A, B);
		Segment inter = s.intersection(new Segment (proj, C));
		Decimal dist  =null;
		if (inter != null) 
		 dist = inter.getA().distance(proj);
		
		s = new Segment (B, C);
		inter = s.intersection(new Segment (proj, A));
		if (inter != null) 
			dist  = Decimal.min (dist, inter.getA().distance(proj));
		
		
		s = new Segment (C, A);
		inter = s.intersection(new Segment (proj, B));
		if (inter != null) 
			dist  = Decimal.min (dist, inter.getA().distance(proj));

		return dist;
*/	}

	
	/**
	 * Fonction de d�tection d'intersection entre deux facettes. 
	 * 
	 * @param tr
	 * @return
	 * @throws InvalidGeomAction 
	 */
	public Segment intersection(Triangle other) throws InvalidGeomAction {
		// Si les deux facettes sont parall�les 
		if (norme.estColineaire(other.norme)) return null;
/**			// Si les facettes sont coll�es retourne un segment NaN
			if (this.estColle(other)) {
				return Segment.getEmptySegment();
			}
		}
		
**/		
		
		Segment seg = this.intersection(other.getPlan());
		Segment segOther = other.intersection(this.getPlan());

		if (seg == null) return null;
		if (segOther == null) return null;

		return seg.intersection(segOther);
		
/**		Droite3D drt = this.plan.getIntersection(other.getPlan());
		if (drt == null) throw new InvalidGeomAction("Intersection de plan incorrecte");

		// cr�ation du segment comme �tant l'intersection des deux segments
		Segment mySeg = intersection(drt);
		if (mySeg == null) return null;
		Segment hisSeg = other.intersection(drt);
		if (hisSeg == null) return null;
		
		return mySeg.intersection(hisSeg); **/
	}


	/** Deux triangles sont coplanaires si leur normes sont identiques ou oppos�es 
	 *    
	 * 
	 * @param other : autre triangle � tester
	 * @return
	 */
	public boolean estColplanaire (Triangle other) {
		if (other == null) return false;
		
		if (other.norme.equals(this.norme.multiply(Decimal.UN.negate()))) return true;
		if (other.norme.equals(this.norme)) return true;
		return false;
	}
	
	/**
	 * les deux triangles sont identiques si les deux normes sont inverses et que les points sont �quivalents
	 * 
	 * @param other
	 * @return
	 */
	public boolean estColle(Triangle other) {
		if (other == null) return false;
		// Compare les normes
		if (!other.norme.equals(this.norme.multiply(Decimal.UN.negate()))) return false;

		// Compare les sommets
		if (!this.contient(other.A)) return false;
		if (!this.contient(other.B)) return false;
		if (!this.contient(other.C)) return false;

		return true;
	}

	/**
	 * D�termine les points d'intersection entre le triangle et une droite
	 */
	public Segment intersection(Droite3D drt) {
		if (drt == null) return null;
		/** Et recherche si un des cot�s est inscrit dans la droite **/
		Segment s1 = new Segment(A, B);
		if (drt.contient(s1)) return s1;
		Segment s2 = new Segment(B, C);
		if (drt.contient(s2)) return s2;
		Segment s3 = new Segment(C, A);
		if (drt.contient(s3)) return s3;
		
		/** D�termine les points de d�but et fin **/
		ArrayList<Vecteur> pts = new ArrayList<Vecteur>(); 
		Vecteur ab = drt.getIntersection(s1);
		if (ab != null) pts.add(ab);
		Vecteur bc = drt.getIntersection(s2);
		if (bc != null) { if (!pts.contains(bc)) pts.add(bc); }
		Vecteur ac = drt.getIntersection(s3);
		if (ac != null) { if (!pts.contains(ac)) pts.add(ac); }

		// Si tous les points sont nulls, pas d'intersection
		if (pts.size() == 0) return null;
		
		// un seul point sur la droite, un sommet
		if (pts.size() == 1) return new Segment(pts.get(0), pts.get(0));
		
		// Deux points -> retourne le segment 
		return new Segment(pts.get(0), pts.get(1));
	}
	
	
	/**
	 * G�n�re une repr�sentation en chaine de caract�res du triangle
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		sb.append(A.toString());
		sb.append(";");
		sb.append(B.toString());
		sb.append(";");
		sb.append(C.toString());
		sb.append("]");
		return sb.toString();
	}

	public Bounds getBounds() {
		Bounds bnds = new Bounds();
		bnds.setBounds(this);
		return bnds;
	}


	public boolean equals(Object o) {
		if (o.getClass() == Triangle.class) {
			Triangle other = (Triangle)o;
			Vecteur[] pts = other.getSommets();
			boolean found = false;
			for (Vecteur v : pts) if (v.equals(A)) found = true;
			if (!found) return false;

			found = false;
			for (Vecteur v : pts) if (v.equals(B)) found = true;
			if (!found) return false;

			found = false;
			for (Vecteur v : pts) if (v.equals(C)) found = true;
			if (!found) return false;
			
			return true;
		}
		return false;
	}
	
	/**
	 * Test de contenance de point
	 * 
	 * @param args
	 * @throws InvalidGeomAction
	 */
	public static void main(String[] args) throws InvalidGeomAction {
		
		Triangle tr = new Triangle(new Vecteur("1;1;0.5"), new Vecteur("1;1;1"), new Vecteur("1;0;0"));
		if (tr.contient(new Vecteur("0.8;1;1"))) System.out.println("OK");
		else System.out.println("Out");
		
		tr = new Triangle (new Vecteur("1;1;0.5"),new Vecteur("1;1;1"),new Vecteur("1;0;0"));
		Triangle tri = new Triangle (new Vecteur("0.5;0.5;0.5"),new Vecteur("1.5;1.5;0.5"),new Vecteur("1.5;0.5;0.5"));
		
		Segment seg = tri.intersection(tr);
		if (seg != null)
			System.out.println(seg.toString());
		else System.out.println("No intersection");
	}

	public Vecteur getNorme() {
		return this.norme;
	}

	/**
	 * Calcule l'intersection entre le plan et le triangle
	 * 
	 * 
	 * @param plan : Plan3D � tester
	 * @return
	 * @throws InvalidGeomAction 
	 */
	public Segment intersection(Plan3D pl) throws InvalidGeomAction {
		if (pl == null) return null;
		if( pl.getNorme().estColineaire(this.plan.getNorme())) return null;
		
		/* Si un des sommets est dans le plan
		 * Portnawak 
		 */
		if (pl.contains(A)) {
			if (pl.contains(B)) return new Segment(A, B);
			if (pl.contains(C)) return new Segment(A, C);
			Vecteur v = pl.intersection(B, C);
			if (v == null) return new Segment(A, A);
			else return new Segment (A, v);
		}
		if (pl.contains(B)) {
			if (pl.contains(C)) return new Segment(B, C);
			Vecteur v = pl.intersection(A, C);
			if (v == null) return new Segment(B, B);
			else return new Segment (B, v);
		}
		if (pl.contains(C)) {
			Vecteur v = pl.intersection(A, B);
			if (v == null) return new Segment(C, C);
			else return new Segment (C, v);
		}

		/** D�termine les points de d�but et fin **/
		ArrayList<Vecteur> pts = new ArrayList<Vecteur>(); 
		Vecteur pt = pl.intersection(A, B);
		if (pt != null) pts.add(pt);
		pt = pl.intersection(B, C);
		if (pt != null) { if (!pts.contains(pt)) pts.add(pt); }
		pt = pl.intersection(C, A);
		if (pt != null) { if (!pts.contains(pt)) pts.add(pt); }

		// Si tous les points sont nulls, pas d'intersection
		if (pts.size() == 0) return null;
		
		// un seul point sur la droite, un sommet
		if (pts.size() == 1) return new Segment(pts.get(0), pts.get(0));
		
		// Deux points -> retourne le segment 
		return new Segment(pts.get(0), pts.get(1));
	}

	public Triangle transforme(Transformation trans) throws InvalidGeomAction {
		if (trans == null) return this;
		Triangle t = new Triangle (trans.transforme(A), trans.transforme(B), trans.transforme(C));
		t.isInside = this.isInside;
		return t;
	}
		

}
