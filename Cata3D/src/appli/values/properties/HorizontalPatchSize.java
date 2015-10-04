package appli.values.properties;

import model.Cata;
import appli.values.CataValuesException;
import appli.values.CataValuesProperty;

public class HorizontalPatchSize extends CataValuesProperty {

	public HorizontalPatchSize () {
		super("Nombre de points horizontaux");
	}

	public String getPropertyValue(Cata data) {
		return Integer.toString(data.patch.x);
	}
	
	public String setProperty (Cata data, String val) throws CataValuesException {
		try {
			int v = Integer.parseInt(val);
			if (v-1%3 != 0) 
				throw new CataValuesException("Le nombre de points doit être compatible avec le patch");
			data.recalculePatch(v, data.patch.y);
			return Integer.toString(v);
		} catch (NumberFormatException e) {
			throw new CataValuesException(e);
		}
	}
}
