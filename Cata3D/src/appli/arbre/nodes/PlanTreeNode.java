package appli.arbre.nodes;

import javax.swing.tree.DefaultMutableTreeNode;

import model.composants.PatchComposant;
import model.composants.PatchVide;
import model.math.Plan3D;
import view.gabarits.GabaritScene;
import view.scene.PrintableScene;

public class PlanTreeNode extends DesignTreeNode {

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5121177429213459976L;

	public PlanTreeNode(DefaultMutableTreeNode up) {
		super(up, "Plans");
	}

	/** 
	 * Permet d'afficher la scene correspondant à la vue définie
	 * 
	 */
	public PrintableScene getScene () {
		PatchTreeNode father = (PatchTreeNode)this.parent;
		PatchComposant cmp = (PatchComposant)father.composant;
		PrintableScene ret = new GabaritScene((PatchVide)cmp, new Plan3D()); 
		return ret;
	}
	

}
