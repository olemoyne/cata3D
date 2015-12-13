package appli.arbre;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.tree.DefaultMutableTreeNode;

import appli.values.CataValuesException;
import appli.values.updater.ObjectUpdater;
import model.Poids;
import model.composants.Composant;
import model.composants.PatchComposant;
import model.math.Decimal;
import model.math.Vecteur;
import view.scene.PrintableScene;
import view.scene.PrintedForce;
import view.scene.PrintedMap;
import view.scene.PrintedPoint;

/**
 * Liste des poids soumis 
 * 
 * @author lemoyne
 *
 */
public class PoidsTreeNode extends DesignTreeNode {

	 ArrayList<Poids> poids;
	 Composant comp;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -871482094598531184L;

	public PoidsTreeNode(DefaultMutableTreeNode up, Composant cmp) {
		super(up, "Poids");
		
		comp = cmp;
		poids = cmp.poids;
	}
	
	/**
	 * Gestion du patch de données
	 */
	public ArrayList<TreeNodeProperty> getProperties() {
		
		ArrayList<TreeNodeProperty> ret = super.getProperties();
		for (Poids pds : poids) {
			ret.add(new TreeNodeProperty (pds.nom, pds, true, ObjectUpdater.POIDS) );			
		}
		return ret;
	}

	
	public void updateValue (String fld, Object value) {
		Poids the = null;
		for (Poids pds : poids) if (pds.nom.equals(fld)) the = pds;
		if (the == null) return;
		
		Poids p = (Poids)value;
		the.force= p.force;
		the.position = p.position;
		
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

		// Affiche les poids en rouge
		for (Poids pds : poids) {
			ret.add(new PrintedForce(pds, Color.RED));
		}

		return ret;
	}
	
	public boolean requireButtons() {
		return true;
	}
	
	public void addProperty(TreeNodeProperty treeNodeProperty) throws CataValuesException{
		// TODO Ajoute un poids
		
	}

	public void removeProperty(TreeNodeProperty treeNodeProperty) throws CataValuesException{
		// TODO Supprime le poids
		
	}

}
