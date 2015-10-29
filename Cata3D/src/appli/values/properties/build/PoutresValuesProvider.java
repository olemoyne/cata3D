package appli.values.properties.build;

import java.awt.Color;

import view.scene.PrintableScene;
import view.scene.PrintedArea;
import view.scene.PrintedBlock;
import view.scene.PrintedMap;
import appli.values.CataValuesProperty;
import appli.values.CataValuesProvider;
import model.Cata;
import model.Gabarit;
import model.Poutre;
import model.Structure;
import model.math.Decimal;
/**
 * Permet de g�rer la mise � jour des donn�es de patch
 * 
 * @author olemoyne
 *
 */
public class PoutresValuesProvider extends CataValuesProvider {

	/**
	 * Intitialisation avec un beateau vide
	 */
	public PoutresValuesProvider () {
		super();
	}

	public void setProperties(Cata bateau) {
		// 	Fait un RAZsur les donnes
		liste.clear();
		
		if (bateau.structure == null) bateau.structure =  new Structure();
		
		//Positionne les gabarits sur le bateau
		for (int x = 0; x < bateau.structure.poutres.size(); x++) {
			liste.add(new PoutreDetails(bateau.structure.poutres.get(x), x));
		}
	}
	
	/** 
	 * Permet d'afficher la scene correspondant � la vue d�finie
	 * 
	 */
	public PrintableScene getScene ( Cata bateau) {
		PrintableScene ret = super.getScene(bateau);

		// Affiche la coque en gris fonc�
		ret.add(new PrintedMap (bateau.mapAffichage, "Coque", false, Color.darkGray));
					
		// Affiche chaque gabarit
		for (Gabarit gab : bateau.structure.gabarits) {
			ret.add(new PrintedArea(gab.getArea(bateau, Decimal.ZERO), "Bagarit "+gab.position.toString(), false,  Color.lightGray));
		}

		// Affiche chaque gabarit
		for (Poutre gab : bateau.structure.poutres) {
			ret.add(new PrintedBlock(gab.depart, gab.getEnd(), "Poutre", false,  Color.yellow));
		}

		return ret;
	}

	/**
	 * 	Les boutons sont n�cessaires
	 */
	public boolean areArrayButtonsNeeded() {
		return true;
	}

	/** Ajoute un poids **/
	public void ajouter(Cata data, int pos) {
		Poutre gab = new Poutre();
		data.structure.poutres.add(gab);
		this.liste.add(new PoutreDetails(gab, data.structure.poutres.size()- 1));
	}

	public void supprimer(Cata data, int pos){
		// D�cale les positions
		data.structure.poutres.remove(pos);
		this.liste.remove(pos);
		// d�cale les positions
		int position = 0;
		for (CataValuesProperty prp : liste) {
			PoutreDetails pd = (PoutreDetails)prp;
			pd.position = position;
			position ++;
		}
	}



}
