package appli.arbre;

import java.util.ArrayList;

import javax.swing.tree.DefaultMutableTreeNode;

public class DesignTreeNode extends DefaultMutableTreeNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = 815607850685164482L;
	
	private String nomAffiche;

	public DesignTreeNode (DesignTreeNode up, String name) {
		super();
		nomAffiche = name;
		if (up != null) up.add(this);
	}
	
	public String toString(){
		return nomAffiche;
	}
	
	public ArrayList<TreeNodeProperty> getProperties() {
		return null;
	}
}
