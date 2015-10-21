package appli.values.properties;

import java.awt.Color;

import view.scene.PrintableScene;
import view.scene.PrintedFluidityMap;
import view.scene.PrintedMap;
import appli.values.CataValuesProvider;
import model.Cata;
/**
 * Permet de gérer la mise à jour des données de patch
 * 
 * @author olemoyne
 *
 */
public class FluiditeValuesProvider extends CataValuesProvider {

	/**
	 * Intitialisation avec un beateau vide
	 */
	public FluiditeValuesProvider () {
		super();
	}

	public void setProperties(Cata bateau) {
		// 	Fait un RAZsur les donnes
		liste.clear();
	}

	@Override
	public int getPropertyCount(Cata data) {
		return liste.size();
	}

	/** 
	 * Permet d'afficher la scene correspondant à la vue définie
	 * 
	 */
	public PrintableScene getScene ( Cata bateau) {
		PrintableScene ret = super.getScene(bateau);

		// Affiche la coque en gris
		ret.add(new PrintedMap (bateau.mer.coque, false, Color.lightGray));

		// Affiche la carène en bleu
		if (bateau.mer.carene != null)
			ret.add(new PrintedFluidityMap (bateau.mer.carene));
		
		return ret;
	}


}
