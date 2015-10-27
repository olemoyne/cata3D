package appli.values.properties.optimize;

import model.Cata;
import model.Poids;
import appli.values.CataValuesException;
import appli.values.CataValuesProperty;

public class PoidsDeCoque extends CataValuesProperty {
	
	public PoidsDeCoque () {
		super("Poids de la coque");
	}

	public Object getPropertyValue(Cata data) {
		Poids v = data.mer.poidsDeLaCoque;
		return v;
	}
	
	public String setProperty (Cata data, Object val) throws CataValuesException {
		try {
			return val.toString();
		} catch (NumberFormatException e) {
			throw new CataValuesException(e);
		}
	}

	public boolean isUpdatable() {
		return false;
	}

}
