package view;

import com.jogamp.opengl.glu.GLU;

import math.Decimal;
import math.Vecteur;


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
		position = new Vecteur("0;0;10");
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

}
