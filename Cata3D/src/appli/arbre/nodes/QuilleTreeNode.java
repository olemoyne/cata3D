package appli.arbre.nodes;

import java.awt.Color;
import java.util.ArrayList;

import view.scene.PrintableScene;
import view.scene.PrintedMap;
import appli.arbre.TreeNodeProperty;
import appli.values.updater.ObjectUpdater;
import model.Position;
import model.Quille;
import model.composants.PatchComposant;
import model.composants.PatchVide;
import model.math.Decimal;

public class QuilleTreeNode extends DesignTreeNode {

	PatchVide comp;
	/**
	 * 
	 */
	private static final long serialVersionUID = 4852166551916601414L;

	public QuilleTreeNode(DesignTreeNode up, PatchVide cmp) {
		super(up, "Quille");
		comp = cmp;
	}
	
	/**
	 * Gestion du patch de données
	 */
	public ArrayList<TreeNodeProperty> getProperties() {
		
		ArrayList<TreeNodeProperty> ret = super.getProperties();

		if (comp != null) {
			if (comp.structure.quille == null) comp.structure.quille = new Quille(); 
			ret.add(new TreeNodeProperty ("Epaisseur de quille", comp.structure.quille.epaisseur, false, ObjectUpdater.DECIMAL) );
			ret.add(new TreeNodeProperty ("Profondeur de quille", comp.structure.quille.profondeur, false, ObjectUpdater.DECIMAL) );
		}	
		return ret;
	}

	
	public void updateValue (String fld, Object value) {
		super.updateValue(fld, value);

		if (fld.equals("Epaisseur de quille")) comp.structure.quille.epaisseur = (Decimal)value;
		if (fld.equals("Profondeur de quille")) comp.structure.quille.profondeur = (Decimal)value;
	}

	/** 
	 * TODO : Permet d'afficher la scene correspondant à la vue définie
	 * 
	 */
	public PrintableScene getScene () {
		PatchTreeNode father = (PatchTreeNode)this.parent;
		PatchComposant cmp = (PatchComposant)father.composant;
		PrintableScene ret = super.getScene(); 

		// Affiche la coque en quadrillage
		ret.add(new PrintedMap (cmp.mapAffichage, "Coque", true, Color.darkGray, new Position()));					

		return ret;
	}
	
	public boolean requireButtons() {
		return false;
	}

}
