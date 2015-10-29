package appli.values.properties.build;

import java.awt.Color;

import view.scene.PrintableScene;
import view.scene.PrintedArea;
import view.scene.PrintedMap;
import appli.values.CataValuesProperty;
import appli.values.CataValuesProvider;
import model.Cata;
import model.Gabarit;
import model.Structure;
import model.math.Decimal;
/**
 * Permet de g�rer la mise � jour des donn�es de patch
 * 
 * @author olemoyne
 *
 */
public class GabaritsValuesProvider extends CataValuesProvider {

	/**
	 * Intitialisation avec un beateau vide
	 */
	public GabaritsValuesProvider () {
		super();
		liste.add(new EpaisseurBardage());
	}

	public void setProperties(Cata bateau) {
		// 	Fait un RAZsur les donnes
		liste.clear();
		liste.add(new EpaisseurBardage());
		
		if (bateau.structure == null) bateau.structure =  new Structure();
		
		//Positionne les gabarits sur le bateau
		for (int x = 0; x < bateau.structure.gabarits.size(); x++) {
			liste.add(new GabaritDetails(bateau.structure.gabarits.get(x), x));
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
			ret.add(new PrintedArea(gab.getArea(bateau, Decimal.ZERO), "Bagarit "+gab.position.toString(), true,  Color.yellow));
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
		Gabarit gab = new Gabarit();
		data.structure.gabarits.add(gab);
		this.liste.add(new GabaritDetails(gab, data.structure.gabarits.size()- 1));
	}

	public void supprimer(Cata data, int pos){
		// D�cale les positions
		data.structure.gabarits.remove(pos);
		this.liste.remove(pos);
		// d�cale les positions
		int position = 0;
		for (CataValuesProperty prp : liste) {
			GabaritDetails pd = (GabaritDetails)prp;
			pd.position = position;
			position ++;
		}
	}



}
