package appli.values.properties.build;

import model.Cata;
import model.Gabarit;
import appli.values.CataValuesException;
import appli.values.CataValuesProperty;

public class GabaritDetails extends CataValuesProperty {
	
	int position;
	
	public GabaritDetails (Gabarit gab, int pos) {
		super("Gabarit");
		position = pos;
	}

	public Object getPropertyValue(Cata data) {
		Gabarit v = data.structure.gabarits.get(position);
		return v;
	}
	
	public String setProperty (Cata data, Object val) throws CataValuesException {
		try {
			data.structure.gabarits.set(position, (Gabarit) val);
			return val.toString();
		} catch (NumberFormatException e) {
			throw new CataValuesException(e);
		}
	}

}
