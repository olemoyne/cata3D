package appli.values.properties.build;

import view.scene.GabaritScene;
import view.scene.PrintableScene;
import appli.values.CataValuesProvider;
import model.Cata;
/**
 * Permet de g�rer la mise � jour des donn�es de patch
 * 
 * @author olemoyne
 *
 */
public class PlansValuesProvider extends CataValuesProvider {

	/**
	 * Intitialisation avec un beateau vide
	 */
	public PlansValuesProvider () {
		super();
	}

	public void setProperties(Cata bateau) {
		// 	Fait un RAZsur les donnes
		liste.clear();
	}
	
	/** 
	 * Permet d'afficher la scene correspondant � la vue d�finie
	 * 
	 */
	public PrintableScene getScene ( Cata bateau) {
		GabaritScene ret = new GabaritScene(bateau);
		return ret;
	}

	/**
	 * 	Les boutons sont n�cessaires
	 */
	public boolean areArrayButtonsNeeded() {
		return false;
	}

}
