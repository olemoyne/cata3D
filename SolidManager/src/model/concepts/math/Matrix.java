package model.concepts.math;

public class Matrix {

	/*************
	(0 1 2)
	(3 4 5)
	(6 7 8)
	**************/
	//La lecture se fait de gauche à droite et de haut en base
	public Decimal [] data;
	
	public Matrix () {
		data = new Decimal[9];
	}

	public Matrix (Vecteur a, Vecteur b, Vecteur c) {
		data = new Decimal[9];
		data[0] = a.getDecX();
		data[1] = b.getDecX();
		data[2] = c.getDecX();
		data[3] = a.getDecY();
		data[4] = b.getDecY();
		data[5] = c.getDecY();
		data[6] = a.getDecZ();
		data[7] = b.getDecZ();
		data[8] = c.getDecZ();
	}

	
	public Matrix inverse() {
		Matrix ret = new Matrix();
		
		Decimal det = getDeterminant().inverse();
		
		ret.data[0] = det.multiply(data[4].multiply(data[8]).minus(data[5].multiply(data[7])));
		ret.data[1] = det.multiply(data[2].multiply(data[7]).minus(data[1].multiply(data[8])));
		ret.data[2] = det.multiply(data[1].multiply(data[5]).minus(data[2].multiply(data[4])));
		ret.data[3] = det.multiply(data[5].multiply(data[6]).minus(data[3].multiply(data[8])));
		ret.data[4] = det.multiply(data[0].multiply(data[8]).minus(data[2].multiply(data[6])));
		ret.data[5] = det.multiply(data[2].multiply(data[3]).minus(data[0].multiply(data[5])));
		ret.data[6] = det.multiply(data[3].multiply(data[7]).minus(data[4].multiply(data[6])));
		ret.data[7] = det.multiply(data[1].multiply(data[6]).minus(data[0].multiply(data[7])));
		ret.data[8] = det.multiply(data[0].multiply(data[4]).minus(data[1].multiply(data[3])));
		
		return ret;
	}
	
	public Decimal getDeterminant () {
		Decimal det = data[0].multiply(data[4]).multiply(data[8]);
		det = det.add(data[3].multiply(data[7]).multiply(data[2]));
		det = det.add(data[6].multiply(data[1]).multiply(data[5]));
		det = det.minus(data[6].multiply(data[4]).multiply(data[2]));
		det = det.minus(data[0].multiply(data[7]).multiply(data[5]));
		det = det.minus(data[1].multiply(data[3]).multiply(data[8]));

		return det;
	}
	
	
	public Vecteur multiply(Vecteur v) {
		
		Decimal x = data[0].multiply(v.getDecX()).add(data[1].multiply(v.getDecY())).add(data[2].multiply(v.getDecZ()));
		Decimal y = data[3].multiply(v.getDecX()).add(data[4].multiply(v.getDecY())).add(data[5].multiply(v.getDecZ()));
		Decimal z = data[6].multiply(v.getDecX()).add(data[7].multiply(v.getDecY())).add(data[8].multiply(v.getDecZ()));
		
		return new Vecteur(x, y, z);
	}

	public static void main(String[] args) {
		
		try {
			Triangle tri = new Triangle(new Vecteur ("1;0;0"), new Vecteur ("1;1;1"), new Vecteur ("1;0;1"));
			Plan3D pl = tri.getPlan();
			Matrix m = pl.getMatrix();
			
			new Vecteur ("0;0;0");
			
		} catch (InvalidGeomAction e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
