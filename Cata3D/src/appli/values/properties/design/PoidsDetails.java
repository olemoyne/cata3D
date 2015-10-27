package appli.values.properties.design;

import model.Cata;
import model.Poids;
import appli.values.CataValuesException;
import appli.values.CataValuesProperty;

public class PoidsDetails extends CataValuesProperty {
	
	int position;
	
	public PoidsDetails (Poids pds, int pos) {
		super("Poids appliqué");
		position = pos;
	}

	public Object getPropertyValue(Cata data) {
		Poids v = data.poids.get(position);
		return v;
	}
	
	public String setProperty (Cata data, Object val) throws CataValuesException {
		try {
			data.poids.set(position, (Poids) val);
			return val.toString();
		} catch (NumberFormatException e) {
			throw new CataValuesException(e);
		}
	}

}
