package model.math;


/*
 * Un plan est défini par trois points 3D 
 */
public class Plan3D {
	
	
	long A;
	long B;
	long C;
	long D;
	
	Vecteur O;
	

	/*
	 * Construction d'un plan à partir de trois points
	 */
	public Plan3D (Vecteur a, Vecteur o, Vecteur b) {
	/* génération des coéficients de l'équation du plan à partir des données  
	 *  des vecteurs
	 */
		compute (a, o, b);
	}
	
	/** Plan Y = 0**/
	public Plan3D() {
		
		compute (new Vecteur(1, 0, 0), new Vecteur(0, 0, 0), new Vecteur(0, 0, 1));
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
		A = produit.getX();
		B = produit.getY();
		C = produit.getZ();
		
		//d = n1a1+n2a2+n3a3
		
		D = A*o.getX();
		D = D+(B*o.getY());
		D = D+(C*o.getZ());
		D = -1*D;
		
		O = new Vecteur(o);
	}
	
	
	private long calculePoint (Vecteur v) {
		double p2 = (double)A*(double)v.getX();
		p2 = p2+(double)B*(double)v.getY();
		p2 = p2+(double)C*(double)v.getZ();
		p2 = p2+(double)D;
		return (long)p2;
	}

	/**
	 * Retourne (s'il existe) le point de contacte entre le plan et un segment de droite
	 *    retourn null si aucun point de contacte
	 * @param debut : point de départ du segment de droite
	 * @param fin   : point de fin du segment de droite
	 * @return
	 */
	public Vecteur intersection (Vecteur debut, Vecteur fin) {
		if ((debut == null) && (fin == null)) return null;
		long p1 = 0;
		long p2 = 0;

		if (fin != null) p2 = calculePoint(fin);
		if (debut != null) p1 = calculePoint(debut);

		/** cas de droites incompletes **/
		if ((debut == null) || (fin == null)) {

			if (debut == null) {
				// long p1 = A * fin.getX() + B * fin.getY() + C * fin.getZ() + D;
				if (p2==0) return new Vecteur(fin);
				else return null;
			}
			if (fin == null) {
	//			long p1 = A * debut.getX() + B * debut.getY() + C * debut.getZ() + D;
				if (p1==0) return new Vecteur(debut);
				else return null;
			}
		}
		
		if (p2==0) return fin;
		if (p1==0) return debut;
		
		if ((p2>0) && (p1>0)) return null;
		if ((p2<0) && (p1<0)) return null;
		
		Vecteur U = fin.minus(debut);
//		Decimal prod = this.getNorme().produitVectoriel(U).getProduitScalaire(U);
		
		// si la droite est perpendiculaire au plan
//		if (prod.isZero()) return null;
		
		long pu = this.calculePoint(U);
		
		Decimal k = new Decimal(p1).divide(new Decimal(D-pu));
		
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
		return this.calculePoint(debut);
	}
	
	
	/**
	 * Donne la distance entre un point et le plan
	 * @param point
	 * @return
	 */
	public Decimal distance (Vecteur p) {
		Decimal l = new Decimal(calculePoint(p));
		Decimal sub = new Decimal(A*A+B*B+C*C);
//		long l = A * p.getX() + B * p.getY() + C* p.getZ() + D;
//		long sub = A*A + B*B + C*C; 
		Decimal dist = l.abs().divide(sub.racine());
		return dist.divide(Vecteur.METER);
	}

	/**
	 * Donne la projection entre un point et le plan
	 * 
	 * @param point
	 * @return
	 */
	public Vecteur projection (Vecteur p) {
		long u = calculePoint(p);
		long d = A*A+B*B+C*C;
		
		Decimal dU = new Decimal(-1*u);
		Decimal dd = new Decimal(d);
		
//		long up = A * p.getX() + B * p.getY() + C* p.getZ() + D;
//		long down = A*A + B*B + C*C; 
		Decimal lambda  = dU.divide(dd);
		Vecteur v = new Vecteur (A, B, C);
		Vecteur pt = v.multiply(lambda);
		return pt.add(p);
	}


	/** 
	 * Détermine si le vecteur est coplanaire à ce plan
	 * 
	 * @param p
	 * @return
	 */
	public boolean contains(Vecteur p) {
		long l = calculePoint(p);
		return (l <= 1) && (l > -1);
	}
	

	/**
	 * L'intersection entre ce plan et le plan en paramètre.
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
		
		// définit un point commun aux deux plans. Résolution des équations
		Vecteur pt = null;
		
		// X = 0
//		Decimal s = new Decimal(pl.B).multiply(new Decimal(C)).minus(new Decimal(pl.C).multiply(B));
		double s = (double)pl.B*(double)C-(double)pl.C*(double)B;
		if (s != 0) {
			if (pl.B != 0) {
				double z = (double)pl.D*(double)B; z -= (double)pl.B*(double)D; z = z/s;
				double y = -1.0*((double)pl.C*z+(double)pl.D)/pl.B;
				pt = new Vecteur(0, (long)y, (long)z);
				return new Droite3D (dir, pt);	
			}
			if (pl.C != 0) {
				double y = (double)pl.D*(double)C; y -= (double)pl.C*(double)D; y = -1.0*y/s;
				double z = -1.0*((double)pl.B*y+(double)pl.D)/pl.C;
				pt = new Vecteur(0, (long)y, (long)z);
				return new Droite3D (dir, pt);	
			}
		}

		// Y = 0
		s = (double)A*(double)pl.C-(double)C*(double)pl.A;
		if (s != 0) {
			if (pl.C != 0) {
				double x = (double)pl.D*(double)C; x -= (double)D*(double)pl.C; x = x/s;
				double z = -1.0*((double)pl.A*x+(double)pl.D)/pl.C;
				pt = new Vecteur((long)x, 0, (long)z);
				return new Droite3D (dir, pt);	
			}
			if (pl.A != 0) {
				double z = (double)pl.D*(double)A; z -= (double)D*(double)pl.A; z = -1.0*z/s;
				double x = -1.0*((double)pl.C*z+(double)pl.D)/pl.A;
				pt = new Vecteur((long)x, 0, (long)z);
				return new Droite3D (dir, pt);	
			}
		}

		// Z = 0
		s = (double)pl.A*(double)B-(double)pl.B*(double)A;
		if (s != 0) {
			if (pl.A != 0) {
				double y = ((double)pl.D*(double)A); y-= (double)pl.A*(double)D; y = y/s;
				double x = -1.0*((double)pl.B*y + (double)pl.D)/pl.A;
				pt = new Vecteur((long)x, (long)y, 0);
				return new Droite3D (dir, pt);	
			}
			if (pl.B != 0) {
				double x = ((double)pl.D*(double)B); x-= (double)pl.B*(double)D; x = -1.0*x/s;
				double y = -1.0*((double)pl.A*x + (double)pl.D)/pl.B;
				pt = new Vecteur((long)x, (long)y, 0);
				return new Droite3D (dir, pt);	
			}
		}
		return null;
	}
	
	
	/**
	 *  Création d'un polyedre par défaut, calcul du centre, de la surface et du volume
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
}
