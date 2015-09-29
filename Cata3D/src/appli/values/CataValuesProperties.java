package appli.values;

import java.util.ArrayList;


// Permet de définir une liste de caractéristiques relatives au catamaran
public class CataValuesProperties {
	
	
	public String[] header;

	public Class<?>[] types;

	public ArrayList<CataValuesProperty> properties;
	
	/**
	 * Creation des properties
	 */
	public CataValuesProperties () {
		properties = new ArrayList<CataValuesProperty> ();
	}

}
