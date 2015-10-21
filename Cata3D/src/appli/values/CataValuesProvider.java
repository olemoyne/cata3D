package appli.values;

import java.util.ArrayList;

import view.scene.PrintableScene;
import model.Cata;

public class CataValuesProvider {

	protected ArrayList<CataValuesProperty> liste;

	
	public CataValuesProvider () {
		liste = new ArrayList<CataValuesProperty>();
	}

	/**
	 * Initialise les propriétés du tableau
	 * 
	 * @param bateau
	 */
	public void setProperties(Cata bateau) {
	
	}

	public boolean areArrayButtonsNeeded() {
		return false;
	}

	public void setProperty(Cata data, int row, Object value) throws CataValuesException {
		if (liste.size() == 0) return;

		// Identifie le type de données en fonction de la position
		CataValuesProperty prop = liste.get(row);
		prop.setProperty(data, value);
	}

	public boolean isUpdatable(Cata data, int row) throws CataValuesException {
		if (liste.size() == 0) return false;
		// Identifie le type de données en fonction de la position
		CataValuesProperty prop = liste.get(row);
		return prop.isUpdatable();
	}


	
	public Object getProperty(Cata data, int row) {
		if (liste.size() == 0) return null;
		CataValuesProperty prop = liste.get(row);
		return prop.getPropertyValue(data);
	}


	public int getPropertyCount(Cata data) {
		return 0;
	}

	public void ajouter(Cata data, int pos) {
		
	}

	public void supprimer(Cata data, int pos){
		
	}


	public String getPropertyName(int row) {
		if (liste.size() == 0) return "";

		CataValuesProperty prop = liste.get(row);
		return prop.getPropertyName();
	}
	
	public PrintableScene getScene (Cata bateau ) {
		return new PrintableScene();
	}
}
