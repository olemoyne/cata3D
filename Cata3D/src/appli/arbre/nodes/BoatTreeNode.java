package appli.arbre.nodes;

import java.util.ArrayList;

import javax.swing.tree.DefaultMutableTreeNode;

import model.Cata;
import model.composants.Composant;
import view.scene.PrintableObject;
import view.scene.PrintableScene;
import appli.arbre.TreeNodeProperty;
import appli.values.updater.ObjectUpdater;

public class BoatTreeNode extends DesignTreeNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = 177421273266625358L;
	public Cata bateau;
	
	public BoatTreeNode(DefaultMutableTreeNode up, Cata boat, String name) {
		super(up, name);
		bateau = boat;
	}

	
	/**
	 * Gestion du patch de données
	 */
	public ArrayList<TreeNodeProperty> getProperties() {
		ArrayList<TreeNodeProperty> ret = super.getProperties();
		
		if (bateau == null) return ret;
		
		if (bateau.mer != null) {
			ret.add(new TreeNodeProperty ("Poids total", bateau.mer.poidsTotal, false, ObjectUpdater.POIDS) );
			ret.add(new TreeNodeProperty ("Pouss�e", bateau.mer.pousseeArchimede, false, ObjectUpdater.POIDS) );
		}
		
		return ret;
	}

	
	/** 
	 * Permet d'afficher la scene correspondant à la vue définie
	 * 
	 */
	public PrintableScene getScene () {
		PrintableScene ret = new PrintableScene();

		if (bateau == null) return ret;
		
		// Affiche chaque composant
		for (Composant cmp : this.bateau.composants) {
			for (PrintableObject obj : cmp.getSceneObjects(cmp.situation.getTransformation(null))) {
				ret.add(obj);
			}
		}
		return ret;
	}


	public String[] getActionList() {
		String[] strs =  {"Ajoute composant", "Supprime composant"};
		return strs;
	}

	public int getNodeLevel() {
		return LEVEL_CATA_DESIGN;
	}

}
