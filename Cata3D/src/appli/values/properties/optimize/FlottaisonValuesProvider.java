package appli.values.properties.optimize;

import java.awt.Color;

import view.scene.PrintableScene;
import view.scene.PrintedForce;
import view.scene.PrintedMap;
import appli.values.CataValuesProvider;
import model.Cata;
/**
 * Permet de g�rer la mise � jour des donn�es de patch
 * 
 * @author olemoyne
 *
 */
public class FlottaisonValuesProvider extends CataValuesProvider {

	/**
	 * Intitialisation avec un beateau vide
	 */
	public FlottaisonValuesProvider () {
		super();
		liste.add(new Gite());
		liste.add(new Pilonnement());
		liste.add(new Tangage());
		liste.add(new DensiteSurfaciqueCoque());
		
		// Affichage des valeurs calcul�es
		liste.add(new PoidsDeCoque());
		liste.add(new PoidsTotal());
		liste.add(new PousseeDeCarene());
	}

	public void setProperties(Cata bateau) {
		// 	Fait un RAZsur les donnes
		liste.clear();

		liste.add(new Gite());
		liste.add(new Pilonnement());
		liste.add(new Tangage());
		liste.add(new DensiteSurfaciqueCoque());
		liste.add(new PoidsDeCoque());
		liste.add(new PoidsTotal());
		liste.add(new PousseeDeCarene());

	}

	@Override
	public int getPropertyCount(Cata data) {
		return liste.size();
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
			ret.add(new PrintedMap (bateau.mer.carene, "Car�ne", false, Color.blue));

		// Affiche la mer en bleu ciel 
		
		// Affiche le poids total en rouge
		if (bateau.mer.poidsTotal!= null)
			ret.add(new PrintedForce (bateau.mer.poidsTotal, Color.red));
		// Affiche la pouss�e en vert
		if (bateau.mer.poidsTotal!= null)
			ret.add(new PrintedForce  (bateau.mer.pousseeArchimede, Color.blue));
		
		return ret;
	}


}
