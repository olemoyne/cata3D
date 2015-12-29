package appli.arbre;

import javax.swing.tree.DefaultMutableTreeNode;

import model.Cata;

public class BoatTreeNode extends DesignTreeNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = 177421273266625358L;
	public Cata bateau;
	
	public BoatTreeNode(DefaultMutableTreeNode up, Cata boat, String name) {
		super(up, name);
	}

}
