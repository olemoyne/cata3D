package appli.arbre.nodes;

import java.awt.Color;
import java.util.ArrayList;

import view.scene.PrintableScene;
import view.scene.PrintedArea;
import view.scene.PrintedBlock;
import view.scene.PrintedMap;
import appli.arbre.TreeNodeProperty;
import appli.values.CataValuesException;
import appli.values.updater.ObjectUpdater;
import model.Gabarit;
import model.Poutre;
import model.composants.PatchComposant;
import model.composants.PatchVide;
import model.math.Decimal;

public class PoutresTreeNode extends DesignTreeNode {

	PatchVide comp;
	/**
	 * 
	 */
	private static final long serialVersionUID = 4852166551916601414L;

	public PoutresTreeNode(DesignTreeNode up, PatchVide cmp) {
		super(up, "Poutres");
		comp = cmp;
	}
	
	/**
	 * Gestion du patch de données
	 */
	public ArrayList<TreeNodeProperty> getProperties() {
		
		ArrayList<TreeNodeProperty> ret = super.getProperties();
		int pos = 0;
		for (Poutre ptr : comp.structure.poutres) {
			ret.add(new TreeNodeProperty (Integer.toString(pos), ptr, true, ObjectUpdater.POUTRE) );
			pos ++;
		}
		return ret;
	}

	
	public void updateValue (String fld, Object value) {
		int pos = Integer.parseInt(fld);
		if (pos >= comp.structure.poutres.size()) return;
		Poutre the = comp.structure.poutres.get(pos);
		if (the == null) return;
		
		Poutre p = (Poutre)value;
		the.depart = p.depart;
		the.epaisseur = p.epaisseur;
		the.hauteur = p.hauteur;
		the.longueur = p.longueur;

		comp.recalcule();
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
		ret.add(new PrintedMap (cmp.mapAffichage, "Coque", false, Color.darkGray));					

		// Affiche les gabarits en rouge
		for (Gabarit gab : comp.structure.gabarits) {
			ret.add(new PrintedArea(gab.getArea(comp, Decimal.ZERO), gab.position.toString(), false, Color.GREEN));
		}
		
		// Affiche les poutres en jaune

		int pos = 0;
		for (Poutre ptr : comp.structure.poutres) {
			ret.add(new PrintedBlock(ptr.getStart(), ptr.getEnd(), Integer.toString(pos), false, Color.YELLOW));
			pos ++;
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
		Poutre n = (Poutre) treeNodeProperty.value;
		comp.structure.poutres.add(n);

		comp.recalcule();
	}

	/**
	 * Supprime un poids dans la liste
	 */
	
	public void removeProperty(TreeNodeProperty treeNodeProperty) throws CataValuesException{
		if (treeNodeProperty == null) return;
		int pos = Integer.parseInt(treeNodeProperty.nom);
		if (pos >= comp.structure.poutres.size()) return;
		Poutre the = comp.structure.poutres.get(pos);
		if (the == null) return;
				
		comp.structure.poutres.remove(the);
		comp.recalcule();
	}

	public TreeNodeProperty getNewProperty() {
		Poutre pds = new Poutre();
		int pos = comp.structure.poutres.size();
		return new TreeNodeProperty (Integer.toString(pos), pds, true, ObjectUpdater.POUTRE);
	}



}
