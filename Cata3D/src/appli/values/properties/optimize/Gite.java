package appli.values.properties.optimize;

import model.Cata;
import model.math.Decimal;
import appli.values.CataValuesException;
import appli.values.CataValuesProperty;

public class Gite extends CataValuesProperty {

	public Gite () {
		super("Degrés de gite");
	}

	public Object getPropertyValue(Cata data) {
		return data.mer.gite;
	}
	
	public String setProperty (Cata data, Object val) throws CataValuesException {
		try {
			Decimal dec = (Decimal)val;
			if (Math.abs(dec.floatValue()) > 180)
				throw new CataValuesException("La gite doit etre comprise entre -180 et + 180");
			data.mer.gite = dec;
			data.recalculeFlottaison();
			return dec.toString();
		} catch (NumberFormatException e) {
			throw new CataValuesException(e);
		}
	}
}
