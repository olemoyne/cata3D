package appli.values;

import java.util.ArrayList;


// Permet de d�finir une liste de caract�ristiques relatives au catamaran
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
