package appli.values.properties.optimize;

import java.awt.Color;

import view.scene.PrintableScene;
import view.scene.PrintedMap;
import view.scene.PrintedPoint;
import appli.values.CataValuesProvider;
import model.Cata;
import model.math.Decimal;
import model.math.Vecteur;
/**
 * Permet de g�rer la mise � jour des donn�es de patch
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
		// Elements calculés
		liste.add(new SurfaceAntiDerive());
		liste.add(new CentreAntiDerive());
	}

	public void setProperties(Cata bateau) {
		// 	Fait un RAZsur les donnes
		liste.clear();

		liste.add(new PositionDuMat());
		// Elements calculés
		liste.add(new SurfaceAntiDerive());
		liste.add(new CentreAntiDerive());

	}


	/** 
	 * Permet d'afficher la scene correspondant � la vue d�finie
	 * 
	 */
	public PrintableScene getScene ( Cata bateau) {
		PrintableScene ret = super.getScene(bateau);

		// Affiche la coque en gris
		ret.add(new PrintedMap (bateau.mer.coque, "Coque", false, Color.darkGray));

		// Affiche la car�ne en bleu
		if (bateau.mer.carene != null)
			ret.add(new PrintedMap (bateau.mer.carene, "Carene", false, Color.blue));

		// Affiche la mer en bleu ciel 
		
		// Affiche le poids total en rouge
		if (bateau.mer.CentreAntiDerive!= null)
			ret.add(new PrintedPoint (bateau.mer.CentreAntiDerive, "AntiDerive", Color.red));
		// Affiche la pouss�e en vert
		if (bateau.structure.positionDuMat!= null) {
			Vecteur v = new Vecteur (Decimal.ZERO, Decimal.ZERO, bateau.structure.positionDuMat);
			ret.add(new PrintedPoint (v, "Mature", Color.blue));
		}
		return ret;
	}


}
