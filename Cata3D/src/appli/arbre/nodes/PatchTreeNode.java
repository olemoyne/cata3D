package appli.arbre.nodes;


import java.awt.Color;

import view.scene.PrintableScene;
import view.scene.PrintedForce;
import view.scene.PrintedMap;
import model.composants.PatchComposant;

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
