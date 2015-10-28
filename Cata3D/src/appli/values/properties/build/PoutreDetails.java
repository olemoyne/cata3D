package appli.values.properties.build;

import model.Cata;
import model.Poutre;
import appli.values.CataValuesException;
import appli.values.CataValuesProperty;

public class PoutreDetails extends CataValuesProperty {
	
	int position;
	
	public PoutreDetails (Poutre gab, int pos) {
		super("Poutre");
		position = pos;
	}

	public Object getPropertyValue(Cata data) {
		Poutre v = data.structure.poutres.get(position);
		return v;
	}
	
	public String setProperty (Cata data, Object val) throws CataValuesException {
		try {
			data.structure.poutres.set(position, (Poutre) val);
			return val.toString();
		} catch (NumberFormatException e) {
			throw new CataValuesException(e);
		}
	}

}
