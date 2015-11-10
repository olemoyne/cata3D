package appli.values.properties.optimize;

import model.Cata;
import model.math.Decimal;
import appli.values.CataValuesException;
import appli.values.CataValuesProperty;

public class Pilonnement extends CataValuesProperty {

	public Pilonnement () {
		super("Enfoncement du bateau");
	}

	public Object getPropertyValue(Cata data) {
		return data.mer.pilonnement;
	}
	
	public String setProperty (Cata data, Object val) throws CataValuesException {
		try {
			Decimal dec = (Decimal)val;
			if (Math.abs(dec.floatValue()) > 1)
				throw new CataValuesException("Le pilonnement doit etre comprise entre -1 et + 1 metres");
			data.mer.pilonnement = dec;
			data.recalcule();
			return dec.toString();
		} catch (NumberFormatException e) {
			throw new CataValuesException(e);
		}
	}
}
