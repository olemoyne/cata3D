package appli.values.updater;

import java.awt.GridLayout;

import model.Poutre;
import model.math.Decimal;
import model.math.Vecteur;

/** 
 * Permet de mettre à jour un vecteur
 * 
 * @author olemoyne
 *
 */
public class PoutreUpdater extends ObjectUpdater {

	DecimalField fldX, fldY, fldZ, 
				 longu, epai, haut;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4291603188855301380L;

	public PoutreUpdater () {
		super();
		
		addFields();
	}
	
	// Ajoute les champs de saisie
	private void addFields() {
		setLayout(new GridLayout(3, 2));

		fldX = new DecimalField("X Position : ", "m");
		add(fldX);

		epai = new DecimalField("Epaisseur : ", "m");
		add(epai);

		fldY = new DecimalField("Y Position : ", "m");
		add(fldY);

		haut = new DecimalField("Hauteur : ", "m");
		add(haut);

		fldZ = new DecimalField("Z Position : ", "m");
		add(fldZ);

		longu = new DecimalField("Longueur : ", "m");
		add(longu);

	}

	public void setProperty (Object val) {
		Poutre v = (Poutre)val;
		fldX.setField(v.depart.getDecX());
		fldY.setField(v.depart.getDecY());
		fldZ.setField(v.depart.getDecZ());
		longu.setField(v.longueur);
		haut.setField(v.hauteur);
		epai.setField(v.epaisseur);
	}
	
	public Object getData() {
		Decimal x = (Decimal) fldX.getValue();
		Decimal y = (Decimal) fldY.getValue();
		Decimal z = (Decimal) fldZ.getValue();
		Decimal l = (Decimal) longu.getValue();
		Decimal e = (Decimal) epai.getValue();
		Decimal h = (Decimal) haut.getValue();

		Poutre ret = new Poutre();
		ret.depart = new Vecteur (x, y, z);
		ret.epaisseur = e;
		ret.longueur = l;
		ret.hauteur = h;
		
		return ret;
	}
	
}
