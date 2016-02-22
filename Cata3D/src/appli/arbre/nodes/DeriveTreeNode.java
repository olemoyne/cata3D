package appli.arbre.nodes;

import javax.swing.tree.DefaultMutableTreeNode;

import model.Cata;

public class DeriveTreeNode extends DesignTreeNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1673478056209937230L;
	private Cata bateau;

	public DeriveTreeNode(DefaultMutableTreeNode up, Cata boat, String name) {
		super(up, name);
		bateau = boat;
	}

	


	
}
