package appli.values;

import model.Cata;

public interface CataValuesProvider {

	public CataValuesProperties getProperties (Cata boat);
	
	public void saveProperties (Cata boat);
	
	public boolean areArrayButtonsNeeded();

	public CataValuesProperties ajouter(CataValuesProperties values, int pos);

	public CataValuesProperties supprimer(CataValuesProperties values, int pos);

	public void updateProperty (CataValuesProperty prop);	
}
