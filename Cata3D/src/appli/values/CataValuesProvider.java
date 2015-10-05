package appli.values;

import java.util.ArrayList;

import view.scene.PrintableScene;
import model.Cata;

public class CataValuesProvider {

	protected ArrayList<CataValuesProperty> liste;

	
	public CataValuesProvider () {
		liste = new ArrayList<CataValuesProperty>();
	}

	
	public boolean areArrayButtonsNeeded() {
		return false;
	}

	public void setProperty(Cata data, int row, Object value) throws CataValuesException {
		
	}

	public String getProperty(Cata data, int row) {
		return "Not found";
	}

	public int getPropertyCount(Cata data) {
		return 0;
	}

	public void ajouter(Cata data, int pos) {
		
	}

	public void supprimer(Cata data, int pos){
		
	}


	public String getPropertyName(int row) {
		return "Not founds";
	}

	
	public PrintableScene getScene (Cata bateau ) {
		return new PrintableScene();
	}
}
