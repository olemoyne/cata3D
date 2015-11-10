package appli.values.properties.optimize;

import java.awt.Color;

import view.scene.PrintableScene;
import view.scene.PrintedArea;
import view.scene.PrintedMap;
import view.scene.PrintedPoint;
import appli.values.CataValuesProvider;
import model.Cata;
import model.math.Decimal;
import model.math.Vecteur;
/**
 * Permet de gï¿½rer la mise ï¿½ jour des donnï¿½es de patch
 * 
 * @author olemoyne
 *
 */
public class DeriveValuesProvider extends CataValuesProvider {

	/**
	 * Intitialisation avec un beateau vide
	 */
	public DeriveValuesProvider () {
		super();
		liste.add(new PositionDuMat());
		// Elements calculÃ©s
		liste.add(new SurfaceAntiDerive());
		liste.add(new CentreAntiDerive());
	}

	public void setProperties(Cata bateau) {
		// 	Fait un RAZsur les donnes
		liste.clear();

		liste.add(new PositionDuMat());
		// Elements calculÃ©s
		liste.add(new SurfaceAntiDerive());
		liste.add(new CentreAntiDerive());

	}


	/** 
	 * Permet d'afficher la scene correspondant ï¿½ la vue dï¿½finie
	 * 
	 */
	public PrintableScene getScene ( Cata bateau) {
		PrintableScene ret = super.getScene(bateau);

		// Affiche la coque en gris
		ret.add(new PrintedMap (bateau.mer.coque, "Coque", false, Color.darkGray));

		// Affiche la carï¿½ne en bleu
		if (bateau.mer.carene != null)
			ret.add(new PrintedMap (bateau.mer.carene, "Carene", false, Color.blue));

		// Affiche la mer en bleu ciel 
		
		// Affiche le centre anti dérive en rouge
		if (bateau.mer.centreAntiDerive!= null)
			ret.add(new PrintedPoint (bateau.mer.centreAntiDerive, "Centre antiDerive", Color.red));

		// Affiche la surface anti dérive en rouge
		if (bateau.mer.surfaceAntiDerive!= null)
			ret.add(new PrintedArea (bateau.mer.surfaceAntiDerive, "Surface antiDerive", false, Color.magenta));
		

		// Affiche la position du mat
		if (bateau.structure.positionDuMat!= null) {
			Vecteur v = new Vecteur (Decimal.ZERO, Decimal.ZERO, bateau.structure.positionDuMat);
			ret.add(new PrintedPoint (v, "Mature", Color.green));
		}
		return ret;
	}


}
