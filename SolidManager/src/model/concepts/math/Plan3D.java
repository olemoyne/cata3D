package model.concepts.math;


/*
 * Un plan est d�fini par trois points 3D 
 */
public class Plan3D {
	
	
	Decimal A;
	Decimal B;
	Decimal C;
	Decimal D;
	
	Vecteur O;
	
	Matrix mat;

	/*
	 * Construction d'un plan � partir de trois points
	 */
	public Plan3D (Vecteur a, Vecteur o, Vecteur b) {
	/* g�n�ration des co�ficients de l'�quation du plan � partir des donn�es  
	 *  des vecteurs
	 */
		compute (a, o, b);
		
		Vecteur n = getNorme().add(o);
		
		mat = new Matrix(a, b, n);
	}
	
	/**
	 * Retourne l'origine du plan
	 * @return
	 */
	public Vecteur getOrigine () {
		return O;
	}
	
	/**
	 * 
	 * 
	 * @param a
	 * @param o
	 * @param b
	 */
	public void compute (Vecteur a, Vecteur o, Vecteur b) {
		Vecteur u = a.minus(o);
		Vecteur v = b.minus(o);
			
		Vecteur produit = u.produitVectoriel(v);
		A = produit.getDecX();
		B = produit.getDecY();
		C = produit.getDecZ();
		
		//d = n1a1+n2a2+n3a3
		
		D = A.multiply(o.getDecX());
		D = D.add(B.multiply(o.getDecY()));
		D = D.add(C.multiply(o.getZ()));
		D = D.negate();
		
		O = new Vecteur(o);
	}
	
	
//	Attention les calculs doivent se faire en dec
	private Decimal calculePoint (Vecteur v) {
		Decimal p2 = A.multiply(v.getDecX());
		p2 = p2.add(B.multiply(v.getDecY()));
		p2 = p2.add(C.multiply(v.getDecZ()));
		p2 = p2.add(D);
		return p2;
	}

	/**
	 * Retourne (s'il existe) le point de contacte entre le plan et un segment de droite
	 *    retourn null si aucun point de contacte
	 * @param debut : point de d�part du segment de droite
	 * @param fin   : point de fin du segment de droite
	 * @return
	 */
	public Vecteur intersection (Vecteur debut, Vecteur fin) {
		if ((debut == null) && (fin == null)) return null;
		Decimal p1 = Decimal.ZERO;
		Decimal p2 = Decimal.ZERO;

		if (fin != null) p2 = calculePoint(fin);
		if (debut != null) p1 = calculePoint(debut);

		/** cas de droites incompletes **/
		if ((debut == null) || (fin == null)) {

			if (debut == null) {
				// long p1 = A * fin.getX() + B * fin.getY() + C * fin.getZ() + D;
				if (p2.isZero()) return new Vecteur(fin);
				else return null;
			}
			if (fin == null) {
	//			long p1 = A * debut.getX() + B * debut.getY() + C * debut.getZ() + D;
				if (p1.isZero()) return new Vecteur(debut);
				else return null;
			}
		}
		
		if (p2.isZero()) return fin;
		if (p1.isZero()) return debut;
		
		if ((p2.isPositive()) && (p1.isPositive())) return null;
		if ((p2.isNegative()) && (p1.isNegative())) return null;
		
		Vecteur U = fin.minus(debut);
//		Decimal prod = this.getNorme().produitVectoriel(U).getProduitScalaire(U);
		
		// si la droite est perpendiculaire au plan
//		if (prod.isZero()) return null;
		
		Decimal pu = this.calculePoint(U);
		
		Decimal k = p1.divide(D.minus(pu));
		
		Vecteur delta = U.multiply(k);
		return debut.add(delta);
	}
	
	// Retourne une norme quelconque
	public Vecteur getNorme() {
		
		
		Vecteur v = new Vecteur (A, B, C);
//		return v;
		Decimal dec= v.getNorme();
		return v.multiply(dec.inverse());
	}

	/**
	 * Retourne la position d'un point par rapport au plan
	 * la valeur du retour est positive ou negative selon la position du point par rapport au plan
	 * @param debut : point d'interrogation
	 * @return
	 */
	public long donneCote(Vecteur debut) {
		return this.calculePoint(debut).longValue();
	}
	
	
	/**
	 * Donne la distance entre un point et le plan
	 * @param point
	 * @return
	 */
	public Decimal distance (Vecteur p) {
		Decimal l = calculePoint(p);
		Decimal sub = A.square().add(B.square()).add(C.square());
//		long l = A * p.getX() + B * p.getY() + C* p.getZ() + D;
//		long sub = A*A + B*B + C*C; 
		Decimal dist = l.abs().divide(sub.racine());
		return dist;
	}

	/**
	 * Donne la projection entre un point et le plan
	 * 
	 * @param point
	 * @return
	 */
	public Vecteur projection (Vecteur p) {
		Decimal u = calculePoint(p);
		Decimal d = A.square().add(B.square()).add(C.square());;
		
//		long up = A * p.getX() + B * p.getY() + C* p.getZ() + D;
//		long down = A*A + B*B + C*C; 
		Decimal lambda  = u.negate().divide(d);
		Vecteur v = new Vecteur (A, B, C);
		Vecteur pt = v.multiply(lambda);
		return pt.add(p);
	}

	
	/** 
	 * Détermine si un point V d'un plan est au dessus de du plan (orienté) ou pas
	 *    - Calcule la projection du point par rapport au plan (P)
	 *    - Si la distance PV est nulle --> retourne 0 
	 *    - Calcule le coefficient entre la norme du plan et le vecteur PV
	 *    
	 *    Si le coefficient est > 0 - retourne 1
	 *    Si le coefficient est < 0 - retourne 0
	 *     
	 * **/
	public int estDessus (Vecteur v) {
				
		Vecteur P = projection (v);
		if (P.equals(v)) return 0;
		
		Vecteur VP  = v.minus(P);
		Vecteur N = this.getNorme();

		Decimal dx = Decimal.UN;
		Decimal dy = Decimal.UN;
		Decimal dz = Decimal.UN;

		if (!N.getDecX().isZero()) {dx = VP.getDecX().divide(N.getDecX());}
		if (!N.getDecY().isZero()) {dy = VP.getDecY().divide(N.getDecY());}
		if (!N.getDecZ().isZero()) {dz = VP.getDecZ().divide(N.getDecZ());}
		
		
		if (dx.isPositive() & dy.isPositive() & dz.isPositive() )
			return 1;
		
		return -1;
	}

	/** 
	 * D�termine si le vecteur est coplanaire � ce plan
	 * 
	 * @param p
	 * @return
	 */
	public boolean contains(Vecteur p) {
		long l = calculePoint(p).longValue();
		return (l <= 1) && (l > -1);
	}
	

	/**
	 * L'intersection entre ce plan et le plan en param�tre.
	 * Cette intersection est la droite de vecteur directeur = prod vectoriel des deux normes de ce plan
	 * 
	 *  
	 * @param pl
	 * @return
	 * @throws InvalidGeomAction 
	 */
	public Droite3D getIntersection (Plan3D pl) throws InvalidGeomAction {
		Vecteur myNorme = this.getNorme();
		Vecteur hisNorme = pl.getNorme();
		// Si les deux normes sont colineaires, les plans sont paralleles 
		if (myNorme.estColineaire(hisNorme)) return null;
		
		// Calcule la norme de la droite
		Vecteur dir = myNorme.produitVectoriel(hisNorme);
		Decimal nm = dir.getNorme();
		dir = dir.multiply(nm.inverse());
		
		// d�finit un point commun aux deux plans. R�solution des �quations
		Vecteur pt = null;
		
		// X = 0
		Decimal s = pl.B.multiply(C).minus(pl.C.multiply(B));
//		double s = (double)pl.B*(double)C-(double)pl.C*(double)B;
		if (!s.isZero() ) {
			if (!pl.B.isZero()) {
//				double z = (double)pl.D*(double)B; z -= (double)pl.B*(double)D; z = z/s;
				Decimal z = pl.D.multiply(B); z = z.minus(pl.B.multiply(D)); z = z.divide(s);
				Decimal y = (pl.C.multiply(z).add(pl.D)).divide(pl.B).negate();
				pt = new Vecteur(Decimal.ZERO, y, z);
				return new Droite3D (dir, pt);	
			}
			if (!pl.C.isZero()) {
//				double y = (double)pl.D*(double)C; y -= (double)pl.C*(double)D; y = -1.0*y/s;
				Decimal y = pl.D.multiply(C).minus(pl.C.multiply(D)).divide(s).negate();
//				double z = -1.0*((double)pl.B*y+(double)pl.D)/pl.C;
				Decimal z = (pl.B.multiply(y).add(pl.D)).divide(pl.C).negate();
				pt = new Vecteur(Decimal.ZERO, y, z);
				return new Droite3D (dir, pt);	
			}
		}

		// Y = 0
//		s = (double)A*(double)pl.C-(double)C*(double)pl.A;
		s = A.multiply(pl.C).minus(C.multiply(pl.A));
		if (!s.isZero()) {
			if (!pl.C.isZero()) {
//				double x = (double)pl.D*(double)C; x -= (double)D*(double)pl.C; x = x/s;
				Decimal x = pl.D.multiply(C).minus(D.multiply(pl.C)).divide(s);
//				double z = -1.0*((double)pl.A*x+(double)pl.D)/pl.C;
				Decimal z = (pl.A.multiply(x).add(pl.D)).divide(pl.C).negate();
				pt = new Vecteur(x, Decimal.ZERO, z);
				return new Droite3D (dir, pt);	
			}
			if (!pl.A.isZero()) {
//				double z = (double)pl.D*(double)A; z -= (double)D*(double)pl.A; z = -1.0*z/s;
				Decimal z = pl.D.multiply(A).minus(D.multiply(pl.A)).divide(s).negate();
//				double x = -1.0*((double)pl.C*z+(double)pl.D)/pl.A;
				Decimal x = (pl.C.multiply(z).add(pl.D)).divide(pl.A).negate();
				pt = new Vecteur(x, Decimal.ZERO, z);
				return new Droite3D (dir, pt);	
			}
		}

		// Z = 0
		s = pl.A.multiply(B).minus(pl.B.multiply(A));
		if (!s.isZero()) {
			if (!pl.A.isZero()) {
//				double y = ((double)pl.D*(double)A); y-= (double)pl.A*(double)D; y = y/s;
				Decimal y = pl.D.multiply(A).minus(D.multiply(pl.A)).divide(s);
//				double x = -1.0*((double)pl.B*y + (double)pl.D)/pl.A;
				Decimal x = (pl.B.multiply(y).add(pl.D)).divide(pl.A).negate();
				pt = new Vecteur(x, y, Decimal.ZERO);
				return new Droite3D (dir, pt);	
			}
			if (!pl.B.isZero()) {
//				double x = ((double)pl.D*(double)B); x-= (double)pl.B*(double)D; x = -1.0*x/s;
				Decimal x = pl.D.multiply(B).minus(D.multiply(pl.B)).divide(s).negate();
//				double y = -1.0*((double)pl.A*x + (double)pl.D)/pl.B;
				Decimal y = (pl.A.multiply(x).add(pl.D)).divide(pl.B).negate();
				pt = new Vecteur(x, y, Decimal.ZERO);
				return new Droite3D (dir, pt);	
			}
		}
		return null;
	}
	
	
	/**
	 *  Cr�ation d'un polyedre par d�faut, calcul du centre, de la surface et du volume
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		Plan3D pl = new Plan3D (new Vecteur (Decimal.UN, Decimal.ZERO, Decimal.ZERO),
								new Vecteur (Decimal.UN, Decimal.ZERO, Decimal.UN),
								new Vecteur (Decimal.UN, Decimal.UN, Decimal.UN));
		
		Vecteur A = new Vecteur (new Decimal("0.2"), new Decimal("0.2"), new Decimal("0.2"));
		Vecteur B = new Vecteur (new Decimal("10.2"), new Decimal("0.2"), new Decimal("0.2"));

		Decimal d = pl.distance(A);
		System.out.println("Distance A - Plan = "+d.toString());

		d = pl.distance(B);
		System.out.println("Distance B - Plan = "+d.toString());

		Vecteur v = pl.intersection(A, B);
		if (v == null) System.out.println("Pas d'intersection [A, B] - Plan");
		else System.out.println("Intersection [A, B] - Plan = "+v.toString());
		
		/**          --------------------------------------
		 *        test de calcul de la position d'un point dans l'espace par rapport au plan
		 *               
		 * **/
		Vecteur a = new Vecteur ("3;5;0");
		Vecteur b = new Vecteur ("2;1;0");
		Vecteur c = new Vecteur ("6;3;0");
		Plan3D pla = new Plan3D(a, b, c);
		
		Vecteur p = new Vecteur ("3;5;0");
		int i = pla.estDessus(p);
		System.out.println("Position 1 = "+i);

		p = new Vecteur ("13;15;1.7");
		i = pla.estDessus(p);
		System.out.println("Position 2 = "+i);

		p = new Vecteur ("12;3;-1.4");
		i = pla.estDessus(p);
		System.out.println("Position 3 = "+i);
		
	}
	
	
	public String toString () {
		StringBuilder sb = new StringBuilder();
		sb.append("Norme : ");
		sb.append(this.getNorme());
		sb.append(" - Point : ");
		sb.append(this.getOrigine());
		return sb.toString();
	}

	public static Plan3D getPlan(int axis, long pos) {
		Vecteur orig = new Vecteur(0, 0, 0).set(axis, pos);
		if (axis == Axis.XAxis) return new Plan3D (new Vecteur(pos, 0, 1), orig, new Vecteur(pos, 1, 0));
		if (axis == Axis.YAxis) return new Plan3D (new Vecteur(0, pos, 1), orig, new Vecteur(1, pos, 0));
		if (axis == Axis.ZAxis) return new Plan3D (new Vecteur(0, 1, pos), orig, new Vecteur(1, 0, pos));
		return null;
	}

	
	// Retourne la matrice du plan 
	public Matrix getMatrix() {
		return mat;
	}
}
