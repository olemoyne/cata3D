package appli.values.properties;

import model.Cata;
import model.math.Vecteur;
import appli.values.CataValuesException;
import appli.values.CataValuesProperty;

public class PatchPoint extends CataValuesProperty {
	int posX = 0;
	int posY = 0;
	
	public PatchPoint (int x, int y) {
		super("");
		
		StringBuilder sb = new StringBuilder("Point ");
		sb.append(posX);
		sb.append(", ");
		sb.append(posX);
		
		super.setName(sb.toString());

		posX = x;
		posY = y;
	}

	public String getPropertyValue(Cata data) {
		Vecteur v = data.patch.points[posX][posY];
		return v.toString();
	}
	
	public String setProperty (Cata data, String val) throws CataValuesException {
		try {
			Vecteur value = new Vecteur(val);
			return value.toString();
		} catch (NumberFormatException e) {
			throw new CataValuesException(e);
		}
	}

}
