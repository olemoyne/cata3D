package model.math;


// 2D : Z= 0
public class Droite2D {
	// Termes de l'équation y = ax+b
	Decimal a;
	Decimal b;
	
	public Droite2D (Vecteur A, Vecteur B){
		Vecteur d = (B.minus(A));
		a = d.getDecY().divide(d.getDecX());
		b = A.getDecY();
	}
	
	public Decimal donneCote (Vecteur v) {
		Decimal res = v.getDecX().multiply(a);
		res = res.minus(v.getDecY());
		res = res.add(b);
		
		return res;
	}

}
