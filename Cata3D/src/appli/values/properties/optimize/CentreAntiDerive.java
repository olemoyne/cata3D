package appli.values.properties.optimize;

import model.Cata;
import model.math.Vecteur;
import appli.values.CataValuesException;
import appli.values.CataValuesProperty;

public class CentreAntiDerive extends CataValuesProperty {
	
	public CentreAntiDerive () {
		super("Centre de surface anti dérive");
	}

	public Object getPropertyValue(Cata data) {
		Vecteur v = data.mer.centreAntiDerive;
		return v;
	}
	
	public String setProperty (Cata data, Object val) throws CataValuesException {
		try {
			return val.toString();
		} catch (NumberFormatException e) {
			throw new CataValuesException(e);
		}
	}

	public boolean isUpdatable() {
		return false;
	}

}
