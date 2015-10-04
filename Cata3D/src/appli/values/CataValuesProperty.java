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
	
	public String getPropertyValue(Cata data) {
		return "Not defined";
	}
	
	public String setProperty (Cata data, String val) throws CataValuesException {
		return "Empty";
	}

	public void setName(String string) {
		name = string;
	}
	
}
