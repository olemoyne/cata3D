package appli.values.properties;

import appli.values.CataValuesException;
import appli.values.CataValuesProperty;

public class VerticalPatchSize extends CataValuesProperty {

	public VerticalPatchSize (int val) {
		value = new Integer(val);
	}
	

	@Override
	public String getPropertyName() {
		return "Nombre de points verticaux";
	}

	@Override
	public String getPropertyValue() {
		return ((Integer)value).toString();
	}

	@Override
	public Object validateProperty(String val) throws CataValuesException{
		try {
			int v = Integer.parseInt(val);
			if (v-1%3 != 0) 
				throw new CataValuesException("Le nombre de points doit être compatible avec le patch");
			value = new Integer(v);
			return value.toString();
		} catch (NumberFormatException e) {
			throw new CataValuesException(e);
		}
	}

}
