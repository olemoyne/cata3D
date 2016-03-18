package appli.arbre.nodes;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.tree.DefaultMutableTreeNode;

import appli.arbre.TreeNodeProperty;
import appli.values.CataValuesException;
import appli.values.updater.ObjectUpdater;
import model.Poids;
import model.Position;
import model.composants.Composant;
import model.composants.PatchComposant;
import view.scene.PrintableScene;
import view.scene.PrintedForce;
import view.scene.PrintedMap;

/**
 * Liste des poids soumis 
 * 
 * @author lemoyne
 *
 */
public class PoidsTreeNode extends DesignTreeNode {

//	 ArrayList<Poids> poids;
	 Composant comp;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -871482094598531184L;

	public PoidsTreeNode(DefaultMutableTreeNode up, Composant cmp) {
		super(up, "Poids");
		
		comp = cmp;
	}
	
	/**
	 * Gestion du patch de données
	 */
	public ArrayList<TreeNodeProperty> getProperties() {
		
		ArrayList<TreeNodeProperty> ret = super.getProperties();
		for (Poids pds : comp.poids) {
			ret.add(new TreeNodeProperty (pds.nom, pds, true, ObjectUpdater.POIDS) );			
		}
		return ret;
	}

	
	public void updateValue (String fld, Object value) {
		Poids the = null;
		for (Poids pds : comp.poids) if (pds.nom.equals(fld)) the = pds;
		if (the == null) return;
		
		Poids p = (Poids)value;
		the.force= p.force;
		the.position = p.position;
		the.nom = p.nom;
	}

	/** 
	 * Permet d'afficher la scene correspondant à la vue définie
	 * 
	 */
	public PrintableScene getScene () {
		PatchTreeNode father = (PatchTreeNode)this.parent;
		PatchComposant cmp = (PatchComposant)father.composant;
		PrintableScene ret = super.getScene(); 

		// Affiche la coque en gris
		ret.add(new PrintedMap (cmp.mapAffichage, "Coque", true, Color.darkGray, new Position()));					

		// Affiche les poids en rouge
		for (Poids pds : comp.poids) {
			ret.add(new PrintedForce(pds, Color.RED));
		}
		return ret;
	}
	
	public boolean requireButtons() {
		return true;
	}

	/** 
	 * Ajoute un poids dans la liste 
	 * **/
	public void addProperty(TreeNodeProperty treeNodeProperty) throws CataValuesException{
		Poids n = (Poids) treeNodeProperty.value;
		comp.poids.add(n);

		comp.recalcule();
	}

	/**
	 * Supprime un poids dans la liste
	 */
	
	public void removeProperty(TreeNodeProperty treeNodeProperty) throws CataValuesException{
		if (treeNodeProperty == null) return;
		Poids the = null;
		for (Poids pds : comp.poids) if (pds.nom.equals(treeNodeProperty.nom)) the = pds;
		if (the == null) return;
		
		comp.poids.remove(the);
		comp.recalcule();
	}

	public TreeNodeProperty getNewProperty() {
		Poids pds = new Poids();
		return new TreeNodeProperty (pds.nom, pds, true, ObjectUpdater.POIDS);
	}

}
