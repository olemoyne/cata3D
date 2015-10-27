package appli.values.properties.design;

import model.Cata;
import model.math.Vecteur;
import appli.values.CataValuesException;
import appli.values.CataValuesProperty;

public class PatchPoint extends CataValuesProperty {
	int posX = 0;
	int posY = 0;
	
	public PatchPoint (int x, int y) {
		super("");
		
		posX = x;
		posY = y;
		
		StringBuilder sb = new StringBuilder("Point ");
		sb.append(posX);
		sb.append(", ");
		sb.append(posY);
		
		super.setName(sb.toString());
	}

	public Object getPropertyValue(Cata data) {
		Vecteur v = data.patch.points[posX][posY];
		return v;
	}
	
	public String setProperty (Cata data, Object value) throws CataValuesException {
		try {
			data.patch.points[posX][posY] = (Vecteur)value;
			data.recalculeMaps();
			return value.toString();
		} catch (NumberFormatException e) {
			throw new CataValuesException(e);
		}
	}

}
