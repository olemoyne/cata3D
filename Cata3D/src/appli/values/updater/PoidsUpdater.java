package appli.values.updater;

import model.Poids;
import model.math.Decimal;
import model.math.Vecteur;

/** 
 * Permet de mettre à jour un vecteur
 * 
 * @author olemoyne
 *
 */
public class PoidsUpdater extends ObjectUpdater {

	DecimalField fldX, fldY, fldZ, force;
	StringField nFld;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4291603188855301380L;

	public PoidsUpdater () {
		super();
		
		addFields();
	}
	
	// Ajoute les champs de saisie
	private void addFields() {
		nFld = new StringField("Nom : ");
		add(nFld);

		fldX = new DecimalField("X : ", "m");
		add(fldX);

		fldY = new DecimalField("Y : ", "m");
		add(fldY);
		fldZ = new DecimalField("Z : ", "m");
		add(fldZ);
		force = new DecimalField("Poids : ", "kg");
		add(force);
	}

	public void setProperty (Object val) {
		Poids v = (Poids)val;
		nFld.setField(v.nom);
		fldX.setField(v.position.getDecX());
		fldY.setField(v.position.getDecY());
		fldZ.setField(v.position.getDecZ());
		force.setField(v.force);
	}
	
	public Object getData() {
		String s = (String)nFld.getValue();
		Decimal x = (Decimal) fldX.getValue();
		Decimal y = (Decimal) fldY.getValue();
		Decimal z = (Decimal) fldZ.getValue();
		Decimal f = (Decimal) force.getValue();
		
		if ((x != null)&&(y != null)&&(y != null))
		   return new Poids(s, new Vecteur(x, y, z), f);
		
		return null;
	}
	
}
