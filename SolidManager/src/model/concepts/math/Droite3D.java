package model.concepts.math;




/**
 * Représentation d'une droite dans l'espace.
 * La droite est représentée par :
 *   . l'équation ax + by + c= 0
 *   . deux points dans l'espace
 *   
 * @author olemoyne
 *
 */
public class Droite3D {
	/**
	 *  Vecteur directeur de la droite
	 */
	protected Vecteur directeur;
	
	protected Decimal norme;
	/**
	 * Un point de la droite
	 */
	protected Vecteur point;
	

	/** Creation de la droite par définition d'un vecteur directeur et d'un point
	 * 
	 * @param dir
	 * @param pt
	 */
	public Droite3D(Vecteur dir, Vecteur pt) {
		directeur = dir;
		norme = dir.getNorme();
		point = pt;
	}


	public Vecteur getDirection() {
		return directeur;
	}
	
	public Vecteur getPoint() {
		return point;
	}


	/**
	 * Définit la valeur du parametre k pour un point donné de la droite
	 * 
	 * point + k*dir = v
	 * k = (v-point)/dir
	 * @param v
	 * @return
	 */
	public Decimal getPosition(Vecteur v) {
		Vecteur res = v.minus(point);
		// Définit le k en fonction des valeurs de dir
		if (directeur.getX()!=0) 
			return res.getDecX().divide(directeur.getDecX());
		if (directeur.getY()!=0) 
			return res.getDecY().divide(directeur.getDecY());
		if (directeur.getZ()!=0) 
			return res.getDecZ().divide(directeur.getDecZ());
		
		return null;
	}


	/**
	 * La doite contient-elle le point identifié par le vecteur
	 *   Pourquoi utiliser des décimaux ...
	 * 
	 * @param a
	 * @return
	 */
	public boolean contient(Vecteur a) {
		Double alfa = null;
		if (directeur.getX() != 0) {
			alfa = -1.0d*((double)point.getX())+ ((double)a.getX())/((double)directeur.getX());
//			alfa = this.point.getDecX().negate().add(a.getDecX()).divide(directeur.getDecX());
		} else {
			if (directeur.getY() != 0) {
				alfa = ((double)a.getY())- ((double)point.getY())/((double)directeur.getY());
//				alfa = a.getDecY().minus(point.getDecY()).divide(directeur.getDecY());
			} else {
				alfa = -1.0d*((double)point.getZ())+ ((double)a.getZ())/((double)directeur.getZ());
//				alfa = this.point.getDecZ().negate().add(a.getDecZ()).divide(directeur.getDecZ());
			}
		}
		Decimal dec = new Decimal(alfa);
		return this.point.add(this.directeur.multiply(dec)).equals(a);
	}
	
	
	/**
	 *  Donne le vecteur positionné à taille du point de fin de segment
	 *   
	 * @param debut
	 * @param fin
	 * @param taille
	 * @return
	 */
	public static Vecteur getReduction(Vecteur debut, Vecteur fin, Decimal taille) {
		Vecteur direction = fin.minus(debut).multiply(debut.distance(fin).inverse());
		Vecteur delta = debut.minus(fin);
		
		Segment arr = new Segment(debut, fin);
		
		//Decimal a = direction.getDecX().square().add(direction.getDecY().square()).add(direction.getZ().square());
		double a = (double)direction.getX()*(double)direction.getX()+
				(double)direction.getY()*(double)direction.getY()+
				(double)direction.getZ()*(double)direction.getZ();
		
		//Decimal b = Decimal.DEUX.multiply(direction.getX().multiply(delta.getX()).add(direction.getY().multiply(delta.getY())).add(direction.getZ().multiply(delta.getZ())));
		double b = 2.0*((double)direction.getX()*(double)delta.getX()+((double)direction.getY()*(double)delta.getY()))+((double)direction.getZ()*(double)delta.getZ());

		//Decimal c = delta.getX().square().add(delta.getY().square()).add(delta.getZ().square()).minus(taille.square());
		double c = (double)delta.getX()*(double)delta.getX()+((double)delta.getY()*(double)delta.getY())+((double)delta.getZ()*(double)delta.getZ())-((double)taille.square().longValue());
		
		double det = b*b-(4*a*c);
		
		if (det <0) return null;
		
		//t= -b/2a
		if (det==0) {
			Decimal n = new Decimal(-1*b);
			Decimal d = new Decimal(2*a);
			Decimal t = n.divide(d);
			return direction.multiply(t).add(debut);
		}
		
		Decimal dt = new Decimal(det);
		Decimal n = new Decimal(-1*b).minus(dt.racine());
		Decimal d = new Decimal(2*a);
		Decimal t1 = n.divide(d);
		Vecteur v1 = direction.multiply(t1).add(debut);
		if (arr.contient(v1)) return v1;

		dt = new Decimal(det);
		n = new Decimal(b).minus(dt.racine());
		d = new Decimal(2*a);
		Decimal t2 = n.divide(d);
		Vecteur v2 = direction.multiply(t2).add(debut);
		if (arr.contient(v2)) return v2;

		return null;
	}


	public void setData(Vecteur d, Vecteur p) {
		directeur = d;
		norme = d.getNorme();
		this.point = p;
	}

	
	public boolean contient (Segment seg) {
		Vecteur segDir = seg.getB().minus(seg.getA());
		if (!this.directeur.estColineaire(segDir)) return false;
		if (this.contient (seg.getA())) return true;
		return false;
	}

	/**
	 * Identifie l'intersection entre l'arrete et la droite;
	 * 
	 * @param drt
	 * @return
	 */
	public Vecteur getIntersection(Segment seg) {
		Vecteur myDir = seg.getB().minus(seg.getA());
		Vecteur myStart = seg.getA();
		
		Vecteur hisDir = getDirection();
		Vecteur hisStart = getPoint();
		
		
		if (myDir.estColineaire(hisDir)) return null;
		
		// Z = 0
		Decimal n= null;
		Decimal s = new Decimal((double)myDir.getY()*(double)hisDir.getX()-((double)hisDir.getY()*((double)myDir.getX())));
		if (!s.isZero()) {
			n = new Decimal( (double)hisStart.getY()*(double)myDir.getX()+(double)myStart.getX()*(double)myDir.getY()-(double)hisStart.getX()*(double)myDir.getY()-(double)myStart.getY()*(double)myDir.getX());
		} else {
			// X = 0
			//s = (myDir.getZ().multiply(hisDir.getY()).minus(hisDir.getZ().multiply(myDir.getY())));
			s = new Decimal((double)myDir.getZ()*(double)hisDir.getY()-(double)hisDir.getZ()*(double)myDir.getY());
			if (!s.isZero()) {
				n = new Decimal((double)hisStart.getZ()*(double)myDir.getY()+(double)myStart.getY()*(double)myDir.getZ()-(double)hisStart.getY()*(double)myDir.getZ()-(double)myStart.getZ()*(double)myDir.getY()); 
			} else { 
				s = new Decimal ((double)myDir.getX()*(double)hisDir.getZ()-(double)hisDir.getX()*(double)myDir.getZ());
				if (s.isZero()) return null;
				n = new Decimal((double)hisStart.getX()*(double)myDir.getZ()+(double)myStart.getZ()*(double)myDir.getX()-(double)hisStart.getZ()*(double)myDir.getX()-(double)myStart.getX()*(double)myDir.getZ());
			}
		}
//		if (n == null) return null;

		// Calcule le point d'impact
		Vecteur v = getPoint(n.divide(s));
		// Vérifie que inter est bien entre A et B
		// La distance entre A et V < celle entre A et B
		Decimal da = v.distance(seg.getA());
		Decimal db = v.distance(seg.getB());
		Decimal taille = seg.getB().distance(seg.getA());
		if (taille.compareTo(da.add(db)) >= 0) { //.doubleValue() >= d.doubleValue()) {
				return v;
		}
		return null;
	}
	
	public String toString () {
		StringBuilder sb = new StringBuilder();
		sb.append("Dir : ");
		sb.append(this.directeur.toString());
		sb.append(" - Point : ");
		sb.append(this.point.toString());
		return sb.toString();
	}
	
	/**
	 * Test d'intersection
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Droite3D drt = new Droite3D(new Vecteur("0;1;0"), new Vecteur("0.034;0;0"));
		Segment seg = new Segment(new Vecteur("0.0335;0.0242;0"), new Vecteur("0.0341;0.0203;0"));
		Vecteur v = drt.getIntersection(seg);
		
		System.out.println(v);
	}


	public Vecteur getPoint(Decimal dist) {
		Decimal k = dist; //.divide(norme);
		Decimal x = this.directeur.getDecX().multiply(k).add(this.point.getDecX());
		Decimal y = directeur.getDecY().multiply(k).add(point.getDecY());
		Decimal z = directeur.getDecZ().multiply(k).add(point.getDecZ());
		
		Vecteur v = new Vecteur(x, y, z);
		return v;
	}

}
