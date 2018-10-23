package appli.arbre.nodes;

import java.util.ArrayList;

import javax.swing.tree.DefaultMutableTreeNode;

import view.scene.PrintableScene;
import appli.arbre.TreeNodeProperty;
import appli.values.CataValuesException;
import appli.values.updater.ObjectUpdater;
import model.Cata;
import model.CataScene;
import model.Compartiment;
import model.Habitacle;

public class HabitabilityTreeNode extends DesignTreeNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = 156467930396700730L;

	private Cata bateau;
	private Habitacle inside; 

	public HabitabilityTreeNode(DefaultMutableTreeNode up, Cata boat, String name) {
		super(up, name);
		bateau = boat;
		inside = bateau.habitacle;
	}

	/**
	 * Gestion du patch de données
	 */
	public ArrayList<TreeNodeProperty> getProperties() {
		ArrayList<TreeNodeProperty> ret = super.getProperties();
		
		if (bateau == null) return ret;

		if (bateau.habitacle == null) bateau.habitacle = new Habitacle();
		inside = bateau.habitacle;

		for (Compartiment ptr : inside.compartiments) {
			ret.add(new TreeNodeProperty (ptr.getNom(), ptr, true, ObjectUpdater.COMPARTIMENT) );
		}
		return ret;
	}
	

	
	public void updateValue (String fld, Object value) {
	
		Compartiment toUpdate= null;
		for (Compartiment comp : inside.compartiments) {
			if (comp.getNom().equals(fld)) toUpdate = comp; 
		}

		if (toUpdate == null) return;
				

		Compartiment p = (Compartiment)value;
		toUpdate.setData(p.getNom(), p.getPosition(), p.getTaille(), p.getCouleur());
		
		bateau.recalcule();
	}


	/** 
	 * Permet d'afficher la scene correspondant à la vue définie
	 * 
	 */
	public PrintableScene getScene () {
		PrintableScene  ret = CataScene.getHabitationScene(bateau);
		
		return ret;

	}

	public boolean requireButtons() {
		return true;
	}

	/** 
	 * Ajoute un poids dans la liste 
	 * **/
	public void addProperty(TreeNodeProperty treeNodeProperty) throws CataValuesException{
		Compartiment n = (Compartiment) treeNodeProperty.value;
		inside.compartiments.add(n);

		bateau.recalcule();
	}

	/**
	 * Supprime un poids dans la liste
	 */
	
	public void removeProperty(TreeNodeProperty treeNodeProperty) throws CataValuesException{
		if (treeNodeProperty == null) return;
		
		Compartiment toRemove= null;
		for (Compartiment comp : inside.compartiments) {
			if (comp.getNom().equals(treeNodeProperty.nom)) toRemove = comp; 
		}

		if (toRemove == null) return;
				
		inside.compartiments.remove(toRemove);
		bateau.recalcule();

	}

	public TreeNodeProperty getNewProperty() {
		Compartiment pds = new Compartiment();
		return new TreeNodeProperty ("Nouveau", pds, true, ObjectUpdater.COMPARTIMENT);
	}


}
