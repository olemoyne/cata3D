package appli.values.updater;

import model.math.Decimal;
import model.math.Vecteur;

/** 
 * Permet de mettre à jour un vecteur
 * 
 * @author olemoyne
 *
 */
public class VecteurUpdater extends ObjectUpdater {

	DecimalField fldX, fldY, fldZ;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4291603188855301380L;

	public VecteurUpdater () {
		super();	
		fldX = new DecimalField("X : ", "m");
		add(fldX);
		fldY = new DecimalField("Y : ", "m");
		add(fldY);
		fldZ = new DecimalField("Z : ", "m");
		add(fldZ);
	}
	
	public void setProperty (Object val) {
		Vecteur v = (Vecteur)val;
		fldX.setField(v.getDecX());
		fldY.setField(v.getDecY());
		fldZ.setField(v.getDecZ());
	}
	
	public Object getData() {
		Decimal x = (Decimal) fldX.getValue();
		Decimal y = (Decimal) fldY.getValue();
		Decimal z = (Decimal) fldZ.getValue();
		
		if ((x != null)&&(y != null)&&(y != null))
		   return new Vecteur(x, y, z);
		
		return null;
	}
	
}
