package appli.values.updater;

import model.math.Decimal;

/** 
 * Permet de mettre à jour un vecteur
 * 
 * @author olemoyne
 *
 */
public class DecimalUpdater extends ObjectUpdater {

	DecimalField fld;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4291603188855301380L;

	public DecimalUpdater () {
		super();	
		fld = new DecimalField("Valeur : ", "");
		add(fld);
	}
	
	public void setProperty (Object val) {
		Decimal v = (Decimal)val;
		fld.setField(v);
	}
	
	public Object getData() {
		return (Decimal) fld.getValue();
	}
	
}
