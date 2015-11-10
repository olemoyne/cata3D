package appli.values.properties.build;

import model.Cata;
import model.math.Decimal;
import appli.values.CataValuesException;
import appli.values.CataValuesProperty;

public class EpaisseurBardage extends CataValuesProperty {

	public EpaisseurBardage () {
		super("Epaisseur de bardage");
	}

	public Object getPropertyValue(Cata data) {
		return data.structure.epaisseurDeBardage;
	}
	
	public String setProperty (Cata data, Object val) throws CataValuesException {
		try {
			Decimal dec = (Decimal)val;			
			data.structure.epaisseurDeBardage = dec;
			data.recalcule();
			return dec.toString();
		} catch (NumberFormatException e) {
			throw new CataValuesException(e);
		}
	}
}
