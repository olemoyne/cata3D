package appli.values.properties.optimize;

import model.Cata;
import model.Poids;
import appli.values.CataValuesException;
import appli.values.CataValuesProperty;

public class PoidsTotal extends CataValuesProperty {
	
	public PoidsTotal () {
		super("Poids total");
	}

	public Object getPropertyValue(Cata data) {
		Poids v = data.mer.poidsTotal;
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
