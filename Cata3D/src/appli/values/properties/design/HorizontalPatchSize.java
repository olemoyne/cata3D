package appli.values.properties.design;

import model.Cata;
import appli.values.CataValuesException;
import appli.values.CataValuesProperty;

public class HorizontalPatchSize extends CataValuesProperty {

	public HorizontalPatchSize () {
		super("Nombre de points horizontaux");
	}

	public Object getPropertyValue(Cata data) {
		return new Integer(data.patch.x);
	}
	
	public String setProperty (Cata data, Object val) throws CataValuesException {
		try {
			int v = ((Integer)val).intValue();
			if ((v-1)%3 != 0) 
				throw new CataValuesException("Le nombre de points doit être compatible avec le patch : "+v);
			data.setPatch(v, data.patch.y);
			return Integer.toString(v);
		} catch (NumberFormatException e) {
			throw new CataValuesException(e);
		}
	}
}
