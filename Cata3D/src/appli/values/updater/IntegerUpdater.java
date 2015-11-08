package appli.values.updater;

import model.math.Decimal;

/** 
 * Permet de mettre � jour un vecteur
 * 
 * @author olemoyne
 *
 */
public class IntegerUpdater extends ObjectUpdater {

	DecimalField intVal;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4291603188855301380L;

	public IntegerUpdater () {
		super();	
		intVal = new DecimalField("Valeur : ", "");
		add(intVal);
	}
	
	public void setProperty (Object val) {
		Integer v = (Integer)val;
		intVal.setField(v);
	}
	
	public Object getData() {
		Decimal dec = (Decimal) intVal.getValue();
		int idec = Math.round(dec.floatValue());
		return new Decimal(idec);
	}
	
}
