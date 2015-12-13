package appli.arbre;


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
	    new DesignTreeNode(this, "plans");
	}		

}
