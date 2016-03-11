package view.view3D.GL3;

public class Matrix {
	
	public float[] data;
	
	public Matrix(){
		data = new float[16];
	}

	public Matrix(float[] d){
		data = d;
	}

	
	// Normalize a vec3
	public void normalize() {

		float mag = (float) Math.sqrt(data[0] * data[0] + data[1] * data[1] + data[2] * data[2]);
	
		data[0] /= mag;
		data[1] /= mag;
		data[2] /= mag;
	}

	
	// res = a cross b;
	public Matrix crossProduct(Matrix b) {

		Matrix res = new Matrix(); 

		res.data[0] = (data[1] * b.data[2]) - (b.data[1] * data[2]);
		res.data[1] = data[2] * b.data[0] - b.data[2] * data[0];
		res.data[2] = data[0] * b.data[1] - b.data[0] * data[1];
		
		return res;
	}


	// Defines a transformation matrix mat with a translation
	static public Matrix setTranslationMatrix(float x, float y, float z) {

		Matrix res = getIdentityMatrix(4);
		res.data[12] = x;
		res.data[13] = y;
		res.data[14] = z;
		
		return res;
	}

	// sets the square matrix mat to the identity matrix,
	// size refers to the number of rows (or columns)
	static Matrix getIdentityMatrix(int size) {

		Matrix mat = new Matrix();
		// fill matrix with 0s
		for (int i = 0; i < size * size; ++i)
			mat.data[i] = 0.0f;
	
		// fill diagonal with 1s
		for (int i = 0; i < size; ++i)
			mat.data[i + i * size] = 1.0f;
		
		return mat;
	}

	
	public Matrix multMatrix(Matrix b) {
		Matrix res = new Matrix();

		for (int i = 0; i < 4; ++i) {
			for (int j = 0; j < 4; ++j) {
				res.data[j * 4 + i] = 0.0f;
				for (int k = 0; k < 4; ++k) {
					res.data[j * 4 + i] += data[k * 4 + i] * b.data[j * 4 + k];
				}
			}
		}
		return res;
	}


}
