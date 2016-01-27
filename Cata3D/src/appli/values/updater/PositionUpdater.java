package appli.values.updater;

import java.awt.GridLayout;

import javax.swing.JLabel;

import model.Position;
import model.math.Decimal;
import model.math.Vecteur;


/** 
 * Permet de mettre � jour un vecteur
 * 
 * @author olemoyne
 *
 */
public class PositionUpdater extends ObjectUpdater {

	DecimalField posX, posY, posZ;
	DecimalField rotX, rotY, rotZ;

	/**
	 * 
	 */
	private static final long serialVersionUID = 4291603188855301380L;

	public PositionUpdater () {
		super();	
		setLayout(new GridLayout(4, 2));
		JLabel l = new JLabel("Position");
		add(l);
		l = new JLabel("Rotation");
		add(l);
		
		posX = new DecimalField("X : ", "m");
		add(posX);
		rotX = new DecimalField("X : ", "deg.");
		add(rotX);

		posY = new DecimalField("Y : ", "m");
		add(posY);
		rotY = new DecimalField("Y : ", "deg.");
		add(rotY);

		posZ = new DecimalField("Z : ", "m");
		add(posZ);
		rotZ = new DecimalField("Z : ", "deg.");
		add(rotZ);
	}
	
	public void setOptions(Object[] opts) {
	}
	
	public void setProperty (Object val) {
		Position v = (Position)val;

		posX.setField(v.position.getDecX());
		posY.setField(v.position.getDecY());
		posZ.setField(v.position.getDecZ());
		
		rotX.setField(v.rotation.getDecX());
		rotY.setField(v.rotation.getDecY());
		rotZ.setField(v.rotation.getDecZ());
	}
	
	public Object getData() {
		Vecteur pos = new Vecteur ((Decimal)posX.getValue(), (Decimal)posY.getValue(), (Decimal)posZ.getValue());
		Vecteur rot = new Vecteur ((Decimal)rotX.getValue(), (Decimal)rotY.getValue(), (Decimal)rotZ.getValue());
		return new Position(pos, rot);
	}
	
}
