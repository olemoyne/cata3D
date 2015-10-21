package appli.values.properties;

import java.awt.Color;

import view.scene.PrintableScene;
import view.scene.PrintedMap;
import view.scene.PrintedPoint;
import appli.values.CataValuesProvider;
import model.Cata;
/**
 * Permet de gérer la mise à jour des données de patch
 * 
 * @author olemoyne
 *
 */
public class PatchValuesProvider extends CataValuesProvider {

	/**
	 * Intitialisation avec un beateau vide
	 */
	public PatchValuesProvider () {
		super();
		liste.add(new HorizontalPatchSize());
		liste.add(new VerticalPatchSize());

		for (int pos = 0; pos < 16; pos ++) {
			liste.add(new PatchPoint(pos / 4, pos % 4));
		}
	}

	public void setProperties(Cata bateau) {
		// 	Fait un RAZsur les donnes
		liste.clear();
		liste.add(new HorizontalPatchSize());
		liste.add(new VerticalPatchSize());

		// ajuste le nombre de points associés
		for (int x = 0; x < bateau.patch.x; x++) {
			for (int y = 0; y < bateau.patch.y; y++) {
				liste.add(new PatchPoint(x, y));
			}
		}
	}

	@Override
	public int getPropertyCount(Cata data) {
		return data.patch.x*data.patch.y+2;
	}

	/** 
	 * Permet d'afficher la scene correspondant à la vue définie
	 * 
	 */
	public PrintableScene getScene ( Cata bateau) {
		PrintableScene ret = super.getScene(bateau);

		// Affiche les points en jaune
		for (int y = 0; y < bateau.patch.y; y++) 
			for (int x = 0; x < bateau.patch.x; x++) 
				ret.add(new PrintedPoint(bateau.patch.points[x][y], "", Color.yellow));

		// Affiche la coque en gris
		ret.add(new PrintedMap (bateau.mapAffichage, "Coque", false, Color.lightGray));
					
		return ret;
	}


}
