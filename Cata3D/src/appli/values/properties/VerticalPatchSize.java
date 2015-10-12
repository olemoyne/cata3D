package appli.values.properties;

import model.Cata;
import appli.values.CataValuesException;
import appli.values.CataValuesProperty;

public class VerticalPatchSize extends CataValuesProperty {

	public VerticalPatchSize () {
		super("Nombre de points verticaux");
	}
	

	public Object getPropertyValue(Cata data) {
		return new Integer(data.patch.y);
	}
	
	public String setProperty (Cata data, Object val) throws CataValuesException {
		try {
			int v = ((Integer)val).intValue();
			if ((v-1)%3 != 0) 
				throw new CataValuesException("Le nombre de points doit être compatible avec le patch");
			data.recalculePatch(data.patch.x, v);
			return Integer.toString(v);
		} catch (NumberFormatException e) {
			throw new CataValuesException(e);
		}
	}

}
