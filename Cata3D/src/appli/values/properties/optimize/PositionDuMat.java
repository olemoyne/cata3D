package appli.values.properties.optimize;

import model.Cata;
import model.math.Decimal;
import appli.values.CataValuesException;
import appli.values.CataValuesProperty;

public class PositionDuMat extends CataValuesProperty {

	public PositionDuMat () {
		super("Position du mat");
	}

	public Object getPropertyValue(Cata data) {
		return data.structure.positionDuMat;
	}
	
	public String setProperty (Cata data, Object val) throws CataValuesException {
		try {
			Decimal dec = (Decimal)val;
			if (dec.floatValue() < 0 )
				throw new CataValuesException("La position du mat doit être positive");
			data.structure.positionDuMat = dec;
			data.recalculeFlottaison();
			return dec.toString();
		} catch (NumberFormatException e) {
			throw new CataValuesException(e);
		}
	}
}
