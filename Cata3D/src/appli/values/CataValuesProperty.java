package appli.values;

import model.Cata;

public class CataValuesProperty {

	private String name;
	
	public CataValuesProperty (String str) {
		name = str;
	}
	
	public String getPropertyName() {
		return name;
	}
	
	public Object getPropertyValue(Cata data) {
		return "Not defined";
	}
	
	public String setProperty (Cata data, Object val) throws CataValuesException {
		throw new CataValuesException("Fonction non surchargée");
	}

	public void setName(String string) {
		name = string;
	}
	
	public boolean isUpdatable() {
		return true;
	}
	
}
