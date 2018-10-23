package math;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;


/**
 * Facette de solide d�finie comme �tant une liste de 3 points et d'une norme (de taille 1) d�finissant la direction vers l'ext�rieur
 * 
 * @author olemoyne
 *
 */
public class Triangle {
	
	private Vecteur A;
	private Vecteur B;
	private Vecteur C;
	
	private Vecteur norme;
	

	/**
	 * Fonction de creation de triangle vide par d�faut
	 */
	public Triangle () {
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
		else throw new InvalidGeomAction("Norme du trinagle est nulle");
		
	}
	
	public Triangle(Triangle orig) throws InvalidGeomAction {
		A = orig.A;
		B = orig.B;
		C = orig.C;
		// V�rifications que les vecteurs sont distincts 
		if (A.equals(B)) throw new InvalidGeomAction("Triangle invalide, deux points indentiques");
		if (A.equals(B)) throw new InvalidGeomAction("Triangle invalide, deux points indentiques");
		// V�rifications que les vecteurs sont non-colin�aires
		if (B.minus(A).estColineaire(B.minus(C))) throw new InvalidGeomAction("Triangle invalide, triangle plat "+orig.toString());
		
		// Calcul de la norme
		Vecteur u = A.minus(B);
		Vecteur v = C.minus(B);
			
		norme  = u.produitVectoriel(v);				
		Decimal dec = norme.getNorme();
		if (!dec.isZero()) 
			norme = norme.multiply(dec.inverse());
		else throw new InvalidGeomAction("Norme du trinagle est nulle");
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
	 * Creation d'un triangle � partir d'un tableau
	 * @param pts
	 * @throws InvalidGeomAction 
	 */
	public static Triangle getTriangle (String str) throws InvalidGeomAction {
		int start = str.indexOf('[');
		int end = str.indexOf(']');
		
		String message = str.substring(start+1, end);
		String[] pts= message.split(",");
		
		if (pts.length != 3)
			throw new InvalidGeomAction("Triangle invalide, Nombre de points d�finis incorrects "+str);

		Vecteur[] points = new Vecteur[3];
		int i = 0;
		for (String pt : pts) {
			start = pt.indexOf('(');
			end = pt.indexOf(')');
			points[i] = new Vecteur(pt.substring(start+1, end));
			i ++;
		}
		
		return new Triangle (points[0], points[1], points[2]);

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




	/** Deux triangles sont coplanaires si leur normes sont identiques ou oppos�es 
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
	

	public Vecteur getNorme() {
		return this.norme;
	}

}
