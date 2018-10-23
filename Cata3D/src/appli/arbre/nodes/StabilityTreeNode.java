package appli.arbre.nodes;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.tree.DefaultMutableTreeNode;

import view.scene.PrintableScene;
import view.scene.PrintedForce;
import appli.arbre.TreeNodeProperty;
import appli.values.updater.ObjectUpdater;
import model.Cata;
import model.CataScene;

public class StabilityTreeNode extends DesignTreeNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = 156467930396700730L;

	private Cata bateau;

	public StabilityTreeNode(DefaultMutableTreeNode up, Cata boat, String name) {
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
		PrintableScene  ret = CataScene.getNavigationScene(bateau, true);
		// Affiche le poids total et la pouss�e r�sultante
		if (bateau.mer.poidsTotal != null) {
			ret.add(new PrintedForce(bateau.mer.poidsTotal, Color.RED));
		}

		if (bateau.mer.pousseeArchimede != null) {
			ret.add(new PrintedForce(bateau.mer.pousseeArchimede, Color.BLUE));
		}
		
		return ret;

	}

	

}
