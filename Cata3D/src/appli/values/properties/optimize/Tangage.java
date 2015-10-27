package appli.values.properties.optimize;

import model.Cata;
import model.math.Decimal;
import appli.values.CataValuesException;
import appli.values.CataValuesProperty;

public class Tangage extends CataValuesProperty {

	public Tangage () {
		super("Degrés de tangage");
	}

	public Object getPropertyValue(Cata data) {
		return data.mer.tangage;
	}
	
	public String setProperty (Cata data, Object val) throws CataValuesException {
		try {
			Decimal dec = (Decimal)val;
			if (Math.abs(dec.floatValue()) > 20)
				throw new CataValuesException("Le tanagage doit etre comprise entre -20 et + 20 degrés");
			data.mer.tangage = dec;
			data.recalculeFlottaison();
			return dec.toString();
		} catch (NumberFormatException e) {
			throw new CataValuesException(e);
		}
	}
}
