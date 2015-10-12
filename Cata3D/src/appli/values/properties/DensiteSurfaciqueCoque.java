package appli.values.properties;

import model.Cata;
import model.math.Decimal;
import appli.values.CataValuesException;
import appli.values.CataValuesProperty;

public class DensiteSurfaciqueCoque extends CataValuesProperty {

	public DensiteSurfaciqueCoque () {
		super("Densite surfacique de la coque");
	}

	public Object getPropertyValue(Cata data) {
		return data.mer.densiteSurfaciqueCoque;
	}
	
	public String setProperty (Cata data, Object val) throws CataValuesException {
		try {
			Decimal dec = (Decimal)val;
			data.mer.densiteSurfaciqueCoque = dec;
			data.recalculeFlottaison();
			return dec.toString();
		} catch (NumberFormatException e) {
			throw new CataValuesException(e);
		}
	}
}
