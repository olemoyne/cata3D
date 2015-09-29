package appli.values.properties;

import model.math.Vecteur;
import appli.values.CataValuesException;
import appli.values.CataValuesProperty;

public class PatchPoint extends CataValuesProperty {
	int posX = 0;
	int posY = 0;
	
	public PatchPoint (int x, int y, Vecteur val) {
		posX = x;
		posY = y;
		
		value = val;
	}
	
	@Override
	public String getPropertyName() {
		StringBuilder sb = new StringBuilder("Point ");
		sb.append(posX);
		sb.append(", ");
		sb.append(posX);
		
		return sb.toString();
	}

	@Override
	public String getPropertyValue() {
		return ((Vecteur)value).toString();
	}

	@Override
	public Object validateProperty(String val) throws CataValuesException{
		try {
			value = new Vecteur(val);
			return value.toString();
		} catch (NumberFormatException e) {
			throw new CataValuesException(e);
		}
	}

}
