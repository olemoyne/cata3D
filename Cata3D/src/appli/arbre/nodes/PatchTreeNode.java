package appli.arbre.nodes;


import java.awt.Color;
import java.util.ArrayList;

import appli.arbre.TreeNodeProperty;
import appli.values.updater.ObjectUpdater;
import view.scene.PrintableScene;
import view.scene.PrintedForce;
import view.scene.PrintedMap;
import model.composants.PatchComposant;
import model.math.Decimal;

public class PatchTreeNode extends ComposantTreeNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7587228627542488036L;


	public PatchTreeNode(DesignTreeNode up, String name, PatchComposant cmp) {
		super(up, name, cmp);
		// Ajoute les TreeNode correspondant au composant
	    new PatchPropertyTreeNode(this, cmp.patch);
	    //Patches de design de la coque
	    new PoidsTreeNode(this, cmp);
	    // gestion des plans de construction
	    new PlanTreeNode(this);
	}		
	
	
	public ArrayList<TreeNodeProperty> getProperties() {
		PatchComposant p = (PatchComposant)composant;
		
		ArrayList<TreeNodeProperty> ret = super.getProperties();
		ret.add(new TreeNodeProperty ("Précision", p.precision, true, ObjectUpdater.INTEGER) );
		return ret;
	}
	
	
	
	public void updateValue (String fld, Object value) {
		super.updateValue(fld, value);

		PatchComposant pv = (PatchComposant)composant;

		
		if (fld.equals("Précision")) {
			Decimal dec = (Decimal)value;
			pv.precision = (int)Math.round(dec.doubleValue());
		}
	}


	
	/** 
	 * Permet d'afficher la scene correspondant à la vue définie
	 * 
	 */
	public PrintableScene getScene () {
		PrintableScene ret = super.getScene(); 

		// Affiche la coque en gris
		ret.add(new PrintedMap (composant.mapAffichage, "Coque", false, Color.darkGray));					
		
		ret.add(new PrintedForce(this.composant.gravite, Color.RED));

		return ret;
	}
	



}
