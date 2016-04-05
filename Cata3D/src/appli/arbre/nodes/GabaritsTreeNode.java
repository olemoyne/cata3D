package appli.arbre.nodes;

import java.awt.Color;
import java.util.ArrayList;

import view.scene.PrintableScene;
import view.scene.PrintedArea;
import view.scene.PrintedMap;
import appli.arbre.TreeNodeProperty;
import appli.values.CataValuesException;
import appli.values.updater.ObjectUpdater;
import model.Gabarit;
import model.Position;
import model.composants.PatchComposant;
import model.composants.PatchVide;

public class GabaritsTreeNode extends DesignTreeNode {

	PatchVide comp;
	/**
	 * 
	 */
	private static final long serialVersionUID = 4852166551916601414L;

	public GabaritsTreeNode(DesignTreeNode up, PatchVide cmp) {
		super(up, "Gabarits");
		comp = cmp;
	}
	
	/**
	 * Gestion du patch de données
	 */
	public ArrayList<TreeNodeProperty> getProperties() {
		
		ArrayList<TreeNodeProperty> ret = super.getProperties();
		for (Gabarit gab : comp.structure.gabarits) {
			ret.add(new TreeNodeProperty (gab.position.toString(), gab, true, ObjectUpdater.GABARIT) );			
		}
		return ret;
	}

	
	public void updateValue (String fld, Object value) {
		Gabarit the = null;
		for (Gabarit pds : comp.structure.gabarits) if (pds.position.toString().equals(fld)) the = pds;
		if (the == null) return;
		
		Gabarit p = (Gabarit)value;
		the.epaisseur = p.epaisseur;
		the.position = p.position;
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
		ret.add(new PrintedMap (cmp.mapAffichage, "Coque", false, Color.darkGray, new Position()));					

		// Affiche les poids en rouge
		for (Gabarit gab : comp.structure.gabarits) {
			ret.add(new PrintedArea(gab.getArea(comp, gab.position, null, false), gab.position.toString(), false, false, Color.GREEN, new Position()));
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
		Gabarit n = (Gabarit) treeNodeProperty.value;
		comp.structure.gabarits.add(n);

		comp.recalcule();
	}

	/**
	 * Supprime un poids dans la liste
	 */
	
	public void removeProperty(TreeNodeProperty treeNodeProperty) throws CataValuesException{
		if (treeNodeProperty == null) return;
		Gabarit the = null;
		for (Gabarit pds : comp.structure.gabarits) if (pds.position.toString().equals(treeNodeProperty.nom)) the = pds;
		if (the == null) return;
		
		comp.structure.gabarits.remove(the);
		comp.recalcule();
	}

	public TreeNodeProperty getNewProperty() {
		Gabarit pds = new Gabarit();
		return new TreeNodeProperty (pds.position.toString(), pds, true, ObjectUpdater.GABARIT);
	}



}
