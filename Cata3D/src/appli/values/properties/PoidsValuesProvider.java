package appli.values.properties;

import java.awt.Color;

import view.scene.PrintableScene;
import view.scene.PrintedForce;
import view.scene.PrintedMap;
import appli.values.CataValuesProperty;
import appli.values.CataValuesProvider;
import model.Cata;
import model.Poids;
/**
 * Permet de gérer la mise à jour des données de patch
 * 
 * @author olemoyne
 *
 */
public class PoidsValuesProvider extends CataValuesProvider {

	/**
	 * Intitialisation avec un beateau vide
	 */
	public PoidsValuesProvider () {
		super();

	}

	public void setProperties(Cata bateau) {
		// 	Fait un RAZsur les donnes
		liste.clear();

		// ajuste le nombre de points associés
		int pos = 0;
		for (Poids pds : bateau.poids) {
			liste.add(new PoidsDetails(pds, pos));
			pos ++;
		}
	}

	@Override
	public int getPropertyCount(Cata data) {
		return data.poids.size();
	}

	/** 
	 * Permet d'afficher la scene correspondant à la vue définie
	 * 
	 */
	public PrintableScene getScene ( Cata bateau) {
		PrintableScene ret = super.getScene(bateau);

		// Affiche les poids en rouge
		for (Poids pds : bateau.poids) 
				ret.add(new PrintedForce(pds, Color.red));

		// Affiche la coque en gris
		ret.add(new PrintedMap (bateau.mapAffichage, false, Color.lightGray));
					
		return ret;
	}

	/**
	 * 	Les boutons sont nécessaires
	 */
	public boolean areArrayButtonsNeeded() {
		return true;
	}

	/** Ajoute un poids **/
	public void ajouter(Cata data, int pos) {
		Poids pds = new Poids();
		data.poids.add(pds);
		this.liste.add(new PoidsDetails(pds, data.poids.size()- 1));
	}

	public void supprimer(Cata data, int pos){
		// Décale les positions
		data.poids.remove(pos);
		this.liste.remove(pos);
		// décale les positions
		int position = 0;
		for (CataValuesProperty prp : liste) {
			PoidsDetails pd = (PoidsDetails)prp;
			pd.position = position;
			position ++;
		}
	}



}
