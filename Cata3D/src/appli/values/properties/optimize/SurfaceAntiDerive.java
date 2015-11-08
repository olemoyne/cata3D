package appli.values.properties.optimize;

import model.Cata;
import model.math.Decimal;
import appli.values.CataValuesException;
import appli.values.CataValuesProperty;

public class SurfaceAntiDerive extends CataValuesProperty {
	
	public SurfaceAntiDerive () {
		super("Surface anti derive");
	}

	public Object getPropertyValue(Cata data) {
		Decimal v = data.mer.SurfaceAntiDerive;
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
