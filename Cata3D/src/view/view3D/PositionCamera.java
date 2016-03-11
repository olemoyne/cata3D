package view.view3D;

import view.view3D.GL3.Matrix;

import com.jogamp.opengl.glu.GLU;

import model.math.Decimal;
import model.math.Vecteur;


public class PositionCamera {
	
	private Vecteur position;
	private Vecteur direction;
	private Vecteur haut;
	
    private boolean updated;
    
	private long step;

	public PositionCamera(String inc) {
		
		step = new Decimal(inc).multiply(Vecteur.METER).longValue();
		
		this.reset();
	}

	public boolean isUpdated() {
		return updated;
	}

	public void setPosition (Decimal i, Decimal j, Decimal k) {
		position = new Vecteur (i, j , k);
		updated = true;
	}

	public void setDirection (Decimal i, Decimal j, Decimal k) {
		direction= new Vecteur (i, j , k);
		updated = true;
	}

	public void setHaut (Decimal i, Decimal j, Decimal k) {
		haut= new Vecteur (i, j , k);
		updated = true;
	}


	public Vecteur getDirection() {
		return direction;
	}


	public Vecteur getHaut() {
		return haut;
	}


	public Vecteur getPosition() {
		return position;
	}
	
	public boolean equals(PositionCamera autre) {
		if (!position.equals(autre.position)) return false;
		if (!direction.equals(autre.direction)) return false;
		if (!haut.equals(autre.haut)) return false;
		return true;
	}


	public void lookAt(GLU glu) {
        glu.gluLookAt(position.getDecX().doubleValue(), position.getDecY().doubleValue(), position.getDecZ().doubleValue(),
        		direction.getDecX().doubleValue(), direction.getDecY().doubleValue(), direction.getDecZ().doubleValue(), 
        		haut.getDecX().doubleValue(), haut.getDecY().doubleValue(), haut.getDecZ().doubleValue());
        updated = false;
	}


	public void moveUp() {		position = new Vecteur (position.getX(), position.getY()+step, position.getZ()); updated = true;}
	public void moveDown() {	position = new Vecteur (position.getX(), position.getY()-step, position.getZ()); updated = true;}
	public void moveRight() {	position = new Vecteur (position.getX()+step, position.getY(), position.getZ()); ; updated = true;}
	public void moveLeft() {	position = new Vecteur (position.getX()-step, position.getY(), position.getZ()); ; updated = true;}
	public void moveInto() {	position = new Vecteur (position.getX(), position.getY(), position.getZ()+step); ; updated = true;}
	public void moveOut() {		position = new Vecteur (position.getX(), position.getY(), position.getZ()-step); ; updated = true;}

	public void lookUp() {		direction = new Vecteur (direction.getX(), direction.getY()+step, direction.getZ()); updated = true;}
	public void lookDown() {	direction = new Vecteur (direction.getX(), direction.getY()-step, direction.getZ()); updated = true;}
	public void lookRight() {	direction = new Vecteur (direction.getX()+step, direction.getY(), direction.getZ()); ; updated = true;}
	public void lookLeft() {	direction = new Vecteur (direction.getX()-step, direction.getY(), direction.getZ()); ; updated = true;}
	public void lookInto() {	direction = new Vecteur (direction.getX(), direction.getY(), direction.getZ()+step); ; updated = true;}
	public void lookOut() {		direction = new Vecteur (direction.getX(), direction.getY(), direction.getZ()-step); ; updated = true;}


	public void reset() {
		position = new Vecteur("0;0;4");
		direction = new Vecteur("0;0;0");
		haut = new Vecteur("0;1;0");
		updated = true;
	}
	
	
	public String toString() {
		StringBuilder sb = new StringBuilder ("Position = ");
		sb.append(position.toString());
		sb.append (" Direction = ");
		sb.append(direction.toString());
		return sb.toString();
	}

	public Matrix getMatrix() {

		Matrix viewMatrix = new Matrix();
		
		Matrix up = new Matrix(getHaut().getFloats());
		Matrix dir = new Matrix(getDirection().minus(getPosition()).getFloats());
		
		dir.normalize();

		Matrix right = dir.crossProduct(up);
		right.normalize();

		up = right.crossProduct(dir);
		up.normalize();

		viewMatrix.data[0] = right.data[0];
		viewMatrix.data[4] = right.data[1];
		viewMatrix.data[8] = right.data[2];
		viewMatrix.data[12] = 0.0f;

		viewMatrix.data[1] = up.data[0];
		viewMatrix.data[5] = up.data[1];
		viewMatrix.data[9] = up.data[2];
		viewMatrix.data[13] = 0.0f;

		viewMatrix.data[2] = -dir.data[0];
		viewMatrix.data[6] = -dir.data[1];
		viewMatrix.data[10] = -dir.data[2];
		viewMatrix.data[14] = 0.0f;

		viewMatrix.data[3] = 0.0f;
		viewMatrix.data[7] = 0.0f;
		viewMatrix.data[11] = 0.0f;
		viewMatrix.data[15] = 1.0f;

		float[] pos = getPosition().getFloats();
		Matrix aux = Matrix.setTranslationMatrix(-pos[0], -pos[1], -pos[2]);

		viewMatrix = viewMatrix.multMatrix(aux);
		return viewMatrix;
	}

}
